/**
 * This code creates a long pooling connection to stream JSONP data 
 * from a REDIS DB to a client using a servlet. The connection is
 * kept alive until one of the conditions occur:
 * 		1. The streaming connection duration expires (subscription_duration parameter value)
 * 		2. The REDIS DB connection times out (REDIS_CALLBACK_TIMEOUT constant)
 * 		3. Connection loss, e.g., client closes the connection 

 * The code accepts i) channel name, ii) fully qualified channel name and, iii) wildcard '*' for
 * pattern based subscription.
 * 
 * @author Koushik Sinha
 * Last modified: 02/01/2014
 *
 * Dependencies:  servlets 3+, jedis-2.2.1, gson-2.2.4, commons-pool-1.6, slf4j-1.7.5
 * 	
 * Hints for testing:
 * 		1. You can increase the test duration by adjusting the SUBSCRIPTION_MAX_DURATION. 
 *  	2. Tune REDIS_CALLBACK_TIMEOUT, in case the rate of publication is very slow
 *  	3. Tune the number of threads in ExecutorService
 *
 * Deployment steps: 
 * 		1. [Required] Set redisHost and redisPort in code, as per your REDIS setup/location
 * 		2. [Optional] Tune time-out and other parameters, if necessary
 * 		3. [Required]Compile and package as WAR file
 * 		4. [Required] Deploy as WAR file in glassfish 3.1.2
 * 		5. [Optional] Setup ssh tunneling (e.g. command: ssh tunneling:: ssh -f -L 1978:localhost:6379 scd1.qcri.org -N)
 * 		6. Issue stream request from client
 *
 *
 * Invocation: host:port/context-path/channel?crisisCode={crisisCode}&callback={callback}&rate={rate}&duration={duration}  
 * ============
 * Channel Name based examples:
 *  1. http://localhost:8080/aidr-output/crisis/stream/channel?crisisCode=clex_20131201&callback=print&rate=10  
 *  2. http://localhost:8080/aidr-output/crisis/stream/channel?crisisCode=clex_20131201&duration=1h 
 *  3. http://localhost:8080/aidr-output/crisis/stream/channel?crisisCode=clex_20131201&duration=1h&callback=print
 *  4. http://localhost:8080/aidr-output/crisis/stream/channel?crisisCode=clex_20131201&duration=1h&rate=15
 *  5. http://localhost:8080/aidr-output/crisis/stream/channel?crisisCode=clex_20131201&duration=1h&callback=print&rate=10
 *  
 * Wildcard based examples:
 *  1. http://localhost:8080/aidr-output/crisis/stream/channel?crisisCode=*&callback=print&rate=10 
 *  2. http://localhost:8080/aidr-output/crisis/stream/channel?crisisCode=*&duration=1h 
 *  3. http://localhost:8080/aidr-output/crisis/stream/channel?crisisCode=*&duration=1h&callback=print
 *  4. http://localhost:8080/aidr-output/crisis/stream/channel?crisisCode=*&duration=1h&rate=15
 *  5. http://localhost:8080/aidr-output/crisis/stream/channel?crisisCode=*&duration=1h&callback=print&rate=10
 *  
 * Fully qualified channel name examples:
 *  1. http://localhost:8080/aidr-output/crisis/stream/channel?crisisCode=aidr_predict.clex_20131201&callback=print&rate=10 
 *  2. http://localhost:8080/aidr-output/crisis/stream/channel?crisisCode=aidr_predict.clex_20131201&duration=1h 
 *  3. http://localhost:8080/aidr-output/crisis/stream/channel?crisisCode=aidr_predict.clex_20131201&duration=1h&callback=print
 *  4. http://localhost:8080/aidr-output/crisis/stream/channel?crisisCode=aidr_predict.clex_20131201&duration=1h&rate=15
 *  5. http://localhost:8080/aidr-output/crisis/stream/channel?crisisCode=aidr_predict.clex_20131201&duration=1h&callback=print&rate=10
 * 
 *  
 *  Parameter explanations:
 *  	1. crisisCode [mandatory]: the REDIS channel to which to subscribe
 *  	2. subscription_duration [optional]: time for which to subscribe (connection automatically closed after that). 
 *		   	The allowed suffixes are: s (for seconds), m (for minutes), h (for hours) and d (for days). The max subscription 
 *		   	duration is specified by the hard coded SUBSCRIPTION_MAX_DURATION value (default duration). 
 *  	3. callback [optional]: name of the callback function for JSONP data
 *  	4. rate [optional]: an upper bound on the rate at which to send messages to client, expressed as messages/min 
 *  	   	(a floating point number). If <= 0, then default rate is assumed.
 */

package qa.qcri.aidr.output.stream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;
import qa.qcri.aidr.output.utils.JedisConnectionObject;
import qa.qcri.aidr.output.utils.JsonDataFormatter;
import qa.qcri.aidr.output.utils.WriteResponse;

@SuppressWarnings("serial")
@WebServlet(value = "/channel", asyncSupported = true)
public class RedisHTTPStreaming extends HttpServlet {

	// Time-out constants
	private static final int REDIS_CALLBACK_TIMEOUT = 5 * 60 * 1000;                // in ms
	private static final int SUBSCRIPTION_MAX_DURATION = -1;                        //default = no expiry

	// Pertaining to JEDIS - establishing connection with a REDIS DB
	// Currently using ssh tunneling:: ssh -f -L 1978:localhost:6379 scd1.qcri.org -N
	// Channel(s) being used for testing:
	//                 a) aidr_predict.clex_20131201
	//                
	private static final String CHANNEL_PREFIX_CODE = "aidr_predict.";
	private boolean patternSubscriptionFlag;
	private final boolean rejectNullFlag = true;

	private String redisChannel = "aidr_predict.*";       			// channel to subscribe to                
	private static final String redisHost = "localhost";			// Current assumption: REDIS running on same m/c
	private static final int redisPort = 1978;                                        


	public static JedisConnectionObject jedisConn;
	public Jedis subscriberJedis = null;
	public RedisSubscriber aidrSubscriber = null;

	// Runtime related
	private boolean isConnected = false;
	private boolean isSubscribed =false;
	public static ConcurrentHashMap<Jedis, RedisSubscriber> subscriptions;
	// Related to Async Thread management
	public ExecutorService executorServicePool;

	// Debugging
	private static Logger logger = LoggerFactory.getLogger(RedisHTTPStreaming.class);

	/////////////////////////////////////////////////////////////////////////////

	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		// For now: set up a simple configuration that logs on the console
		//PropertyConfigurator.configure("log4j.properties");                // where to place the properties file?
		//BasicConfigurator.configure();                                                        // initialize log4j logging
		System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");                // set logging level for slf4j
		jedisConn = new JedisConnectionObject(redisHost, redisPort);
		executorServicePool = Executors.newFixedThreadPool(200);                // max number of threads
		subscriptions = new ConcurrentHashMap<Jedis, RedisSubscriber>();
	}

	public synchronized boolean initRedisConnection() { 
		try {
			subscriberJedis = jedisConn.getJedisResource();
		} catch (JedisConnectionException e) {
			logger.error("Fatal error! Couldn't establish connection to REDIS!");
			e.printStackTrace();
			System.exit(1);
		}
		if (subscriberJedis != null) {
			return true;
		}
		return false;
	}

	// Stop subscription of this subscribed thread and return resources to the JEDIS thread pool
	private synchronized void stopSubscription(RedisSubscriber sub, Jedis jedis) {
		if (sub != null && sub.getSubscribedChannels() > 0) {
			logger.info("[stopSubscription] sub = " + sub + ", jedis = " + jedis + ", flag = " + sub.patternFlag);
			if (!sub.patternFlag) { 
				sub.unsubscribe();                                
			}
			else {
				sub.punsubscribe();
			}
		}
		//sub = null;
		logger.info("[stopSubscription] subscribed channels count = " + sub.getSubscribedChannels());
		if (sub.getSubscribedChannels() == 0) {
			if (jedisConn != null) jedisConn.returnJedis(jedis);
			if (subscriptions != null) subscriptions.remove(jedis);
		}
		logger.info("[stopSubscription] Executed all steps...");
	}


	// Create a subscription to specified REDIS channel: spawn a new thread
	private void subscribeToChannel(final RedisSubscriber sub, final Jedis jedis, final String channel) throws Exception {
		redisChannel = channel;
		subscriptions.put(jedis, sub);
		executorServicePool.submit(new Runnable() {
			public void run() {
				try {
					//logger.info("[subscribeToChannel] sub = " + sub + ", jedis = " + jedis);
					if (!isPattern(channel)) { 
						logger.info("[subscribeToChannel] Attempting subscription for " + redisHost + ":" + redisPort + "/" + redisChannel);
						sub.patternFlag = false;
						jedis.subscribe(sub, redisChannel);
					} 
					else {
						logger.info("[subscribeToChannel] Attempting pSubscription for " + redisHost + ":" + redisPort + "/" + redisChannel);
						sub.patternFlag = true;
						jedis.psubscribe(sub, redisChannel);
					}
				} catch (Exception e) {
					logger.error("[subscribeToChannel] AIDR Predict Channel Subscribing failed, making one more attempt...");
					logger.error("sub = " + sub + ", jedis = " + jedis + ", flag = " + sub.patternFlag);
					stopSubscription(sub, jedis);
				} finally {
					try {
						stopSubscription(sub, jedis);
					} catch (Exception e) {
						logger.error("[subscribeToChannel] Exception occurred attempting stopSubscription: " + e.toString());
						e.printStackTrace();
					}
				}
			}
		}); 
	}

	private boolean isPattern(String channelName) {
		// We consider only the wildcards * and ?
		if (channelName.contains("*") || channelName.contains("?")) {
			patternSubscriptionFlag = true;
			return true;
		}
		else {
			patternSubscriptionFlag = false;
			return false;
		}
	}

	public String setFullyQualifiedChannelName(final String channelPrefixCode, final String channelCode) {
		if (isPattern(channelCode)) {
			patternSubscriptionFlag = true;
		} else {
			patternSubscriptionFlag = false;
		}
		if (channelCode.startsWith(channelPrefixCode)) {
			return channelCode;                        // already fully qualified name
		}
		else {
			String channelName = channelPrefixCode.concat(channelCode);
			return channelName;
		}
	}


	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		if (jedisConn.getPoolSetup()) {			// Jedis pool is ready to use
			if (request.getParameter("crisisCode") != null) {
				// TODO: Handle client refresh of web-page in same session                                
				if (initRedisConnection()) {
					// Get callback function name, if any
					String channel = setFullyQualifiedChannelName(CHANNEL_PREFIX_CODE, request.getParameter("crisisCode"));
					String callbackName = request.getParameter("callback");

					// Now spawn asynchronous response thread to handle streaming
					final AsyncContext asyncContext = request.startAsync(request, response);
					aidrSubscriber = new RedisSubscriber(asyncContext, subscriberJedis, channel, callbackName);
					try {
						subscribeToChannel(aidrSubscriber, subscriberJedis, channel);
						jedisConn.setJedisSubscription(subscriberJedis, patternSubscriptionFlag);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logger.error("[doGet] Fatal exception occurred attempting subscription: " + e.toString());
						e.printStackTrace();
					}
					executorServicePool.execute(aidrSubscriber);        // alternatively, use: asyncContext.start(aidrSubscriber);
				}
			} 
			else {
				// No crisisCode provided...
				WriteResponse responseWriter = new WriteResponse(response,false);
				responseWriter.initWriter("text/html");
				StringBuilder htmlMessageString = new StringBuilder();

				// Build HTML doc to return
				htmlMessageString.append("<!DOCTYPE html>");
				htmlMessageString.append("<html>");
				htmlMessageString.append("<head><title>REDIS PUBSUB Channel Data Output Service</title></head>");
				htmlMessageString.append("<body>");
				htmlMessageString.append("<h1>Invalid/No CrisisCode Provided! </h1>");
				htmlMessageString.append("<h2>Can not initiate REDIS channel subscription!</h2>");
				htmlMessageString.append("</body></html>");
				responseWriter.writeHtmlData(htmlMessageString, 0);
			}
		}
	}

	// cleanup all threads 
	void shutdownAndAwaitTermination(ExecutorService threadPool) {
		threadPool.shutdown(); // Disable new tasks from being submitted
		try {
			// Wait a while for existing tasks to terminate
			if (!threadPool.awaitTermination(1, TimeUnit.SECONDS)) {
				threadPool.shutdownNow();                         // Cancel currently executing tasks
				// Wait a while for tasks to respond to being cancelled
				if (!threadPool.awaitTermination(1, TimeUnit.SECONDS))
					logger.error("[shutdownAndAwaitTermination] Pool did not terminate");
			}
		} catch (InterruptedException ie) {
			// (Re-)Cancel if current thread also interrupted
			threadPool.shutdownNow();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
	}

	// cleanup when servlet is destroyed (e.g., server shutdown)
	public void destroy() {
		try {
			long startTime = new Date().getTime();
			while (subscriptions.size() > 0) {
				Iterator<Jedis>itr = subscriptions.keySet().iterator();
				while (itr.hasNext()) {
					Jedis j = itr.next();
					subscriptions.get(j).setRunFlag(false);		// signal thread to stop
					if (new Date().getTime() - startTime > 5000) {	// spin-loop, waiting for subscription threads to unsubscribe
						// forcibly relinquish subscription and Jedis resource
						if (subscriptions.get(j).patternFlag) {
							subscriptions.get(j).punsubscribe();
						} else {
							subscriptions.get(j).unsubscribe();
						}
						jedisConn.returnJedis(j);
						subscriptions.remove(j);
					}
				}
			}		
			//jedisConn.closeAll();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("[destroy] Exception occurred attempting stopSubscription: " + e.toString());
			e.printStackTrace();
		}
		shutdownAndAwaitTermination(executorServicePool);
		logger.info("[destroy] All done, shutdown streaming service...");
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////
	// The inner class that handles both Asynchronous Servlet Thread and Redis Threaded Subscription
	//////////////////////////////////////////////////////////////////////////////////////////////////
	private class RedisSubscriber extends JedisPubSub implements Runnable, AsyncListener {

		// Redis/Jedis related
		private String channel;
		private String callbackName = null;
		private Jedis jedis;
		private boolean patternFlag;

		// Async execution related 
		private AsyncContext asyncContext;
		private boolean runFlag = true;
		private boolean error = false;
		private boolean timeout = false;
		private long subscriptionDuration = SUBSCRIPTION_MAX_DURATION;

		// rate control related 
		private static final int DEFAULT_SLEEP_TIME = 0;                // in msec
		private float messageRate = 0;                                                        // default: <= 0 implies no rate control
		private int sleepTime = DEFAULT_SLEEP_TIME;

		// Share data structure between Jedis and Async threads
		private List<String> messageList = Collections.synchronizedList(new ArrayList<String>());

		public RedisSubscriber(AsyncContext asyncContext, Jedis jedis, String channel, String callbackName) throws IOException {
			this.channel = channel;
			this.callbackName = callbackName;
			this.asyncContext = asyncContext;
			this.jedis = jedis;
			this.setRunFlag(true);                

			HttpServletRequest request = (HttpServletRequest) asyncContext.getRequest();
			if (request.getParameter("duration") != null) {
				subscriptionDuration = parseTime(request.getParameter("duration"));
			}
			logger.info("Client subscribed for duration = " + subscriptionDuration);
			if (request.getParameter("rate") != null) {
				messageRate = Float.parseFloat(request.getParameter("rate"));        // specified as messages/min (NOTE: upper-bound)
				if (messageRate > 0) {                // otherwise, use default rate
					sleepTime = Math.max(0, Math.round(60 * 1000 / messageRate));                // time to sleep between sends (in msecs)
				}
			}
			// Set timeout period for async thread
			asyncContext.setTimeout(subscriptionDuration);                // negative --> no timeout
			// Listen for errors and timeouts                        
			asyncContext.addListener(this);
		}

		private long parseTime(String timeString) {
			long duration = 0;
			final int maxDuration = SUBSCRIPTION_MAX_DURATION > 0 ? SUBSCRIPTION_MAX_DURATION : Integer.MAX_VALUE;
			float value = Float.parseFloat(timeString.substring(0, timeString.length()-1));
			if (value > 0) {
				String suffix = timeString.substring(timeString.length() - 1, timeString.length());
				if (suffix.equalsIgnoreCase("s"))
					duration = Math.min(maxDuration, Math.round(value * 1000));
				if (suffix.equalsIgnoreCase("m"))
					duration = Math.min(maxDuration, Math.round(value * 1000 * 60));
				if (suffix.equalsIgnoreCase("h"))
					duration = Math.min(maxDuration, Math.round(value * 1000 * 60 * 60));
				if (suffix.equalsIgnoreCase("d"))
					duration = Math.min(maxDuration, Math.round(value * 1000 * 60 * 60 * 24));
			}
			return duration;
		}

		@Override
		public void onMessage(String channel, String message) {
			final int DEFAULT_COUNT = 1;
			synchronized (messageList) {
				if (messageList.size() < DEFAULT_COUNT) messageList.add(message);
			}
		}

		@Override
		public void onPMessage(String pattern, String channel, String message) {
			synchronized (messageList) {
				if (messageList.size() <  1) messageList.add(message);
			}
		}

		@Override
		public void onPSubscribe(String pattern, int subscribedChannels) {
			isSubscribed = true;
			patternFlag = true;
			logger.info("[onPSubscribe] Started pattern subscription: " + pattern);
		}

		@Override
		public void onPUnsubscribe(String pattern, int subscribedChannels) {
			isSubscribed = false;
			logger.info("[onPUnsubscribe] Unsubscribed from pattern subscription: " + pattern);
		}

		@Override
		public void onSubscribe(String channel, int subscribedChannels) {
			isSubscribed = true;
			patternFlag = false;
			logger.info("[onSubscribe] Started channel subscription: " + channel);
		}

		@Override
		public void onUnsubscribe(String channel, int subscribedChannels) {
			isSubscribed = false;
			logger.info("[onUnsubscribe] Unsubscribed from channel " + channel);
		}

		///////////////////////////////////
		// Now to implement Async methods
		///////////////////////////////////
		public boolean isThreadTimeout(long startTime) {
			if ((subscriptionDuration > 0) && (new Date().getTime() - startTime) > subscriptionDuration) {
				logger.info("[isThreadTimeout] Exceeded Thread timeout = " + subscriptionDuration + "msec");
				return true;
			}
			return false;
		}


		@Override
		public void run() {
			// Time-out related local variables
			long startTime = new Date().getTime();                        // start time of the thread execution
			long lastAccessedTime = startTime; 

			HttpServletResponse response = (HttpServletResponse) asyncContext.getResponse();
			response.setBufferSize(700);
			WriteResponse responseWriter = new WriteResponse(response,true);
			boolean isWriter = responseWriter.initWriter("application/json");
			setRunFlag(isWriter);
			while (getRunFlag() && !isThreadTimeout(startTime)) {
				// Here we poll a non blocking resource for updates
				if (messageList != null && !messageList.isEmpty()) {
					// There are updates, send these to the waiting client
					if (!error && !timeout) {
						// Send updates response as JSON
						List<String> latestMsg = null; 
						synchronized (messageList) {
							latestMsg = new ArrayList<String>();
							latestMsg.addAll(messageList);
						}
						JsonDataFormatter taggerOutput = new JsonDataFormatter(callbackName);        // Tagger specific JSONP output formatter
						StringBuilder jsonDataList = taggerOutput.createList(latestMsg, latestMsg.size(), rejectNullFlag);
						int count = taggerOutput.getMessageCount();
						responseWriter.writeJsonData(jsonDataList, count);
						responseWriter.writerHandle.println();
						responseWriter.writerHandle.flush();
						synchronized (messageList) {
							// Reset the messageList buffer and cleanup
							messageList.clear();        // remove the sent message from list
							latestMsg.clear();
							latestMsg = null;
							jsonDataList = null;
						}
						lastAccessedTime = new Date().getTime();                        // approx. time when message last received from REDIS
						// Now sleep for a short time before going for next message - easy to read on screen
						try {
							Thread.sleep(sleepTime);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							//e.printStackTrace();
						}
					}
					else {
						setRunFlag(false);
					}
				}
				else {
					// messageList is empty --> no message received 
					// from REDIS. Wait for some more time before giving up.
					long currentTime = new Date().getTime();
					long elapsed = currentTime - lastAccessedTime;
					if (elapsed > REDIS_CALLBACK_TIMEOUT) {
						logger.error("[run] exceeded REDIS timeout = " + REDIS_CALLBACK_TIMEOUT + "msec");
						setRunFlag(false);
					}        
					else {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				// check if the client is up - indirectly through whether the write succeeded or failed
				if (responseWriter.checkError()) {
					logger.info("[run] Client side error - possible client disconnect..." + new Date());
					setRunFlag(false);
				}
			}        // end-while

			// clean-up and exit thread
			if (!error && !timeout) {
				if (messageList != null) {
					messageList.clear();
					messageList = null;
				}
				if (!responseWriter.checkError()) {
					responseWriter.close();
				}
				try {
					//logger.info("[run] Attempting stopSubscription: sub = " + this + ", patternFlag = " + this.patternFlag);
					stopSubscription(this, this.jedis);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error("[run] Exception occurred attempting stopSubscription: " + e.toString());
					e.printStackTrace();
				}
				// Double check just to ensure graceful exit - Not sure if required!
				if (!error && !timeout) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {}
					asyncContext.complete();
				}
			}
		}

		public void setRunFlag(final boolean val) {
			runFlag = val;
		}

		public boolean getRunFlag() {
			return runFlag;
		}

		@Override
		public void onError(AsyncEvent event) throws IOException {
			setRunFlag(false);
			error = true;
			logger.error("[onError] An error occured while executing task for client ");
		}

		@Override
		public void onTimeout(AsyncEvent event) throws IOException {
			setRunFlag(false);
			timeout = true;
			logger.warn("[onTimeout] Timed out while executing task for client");
		}

		@Override
		public void onStartAsync(AsyncEvent event) throws IOException {}

		@Override
		public void onComplete(AsyncEvent event) throws IOException {
			logger.debug("[run] Async thread complete...");
		}
	}
}