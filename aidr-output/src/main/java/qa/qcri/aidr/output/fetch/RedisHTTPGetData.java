
/**
 * This code creates a long pooling connection to get 'n' JSONP data  
 * from a REDIS DB to a client using a servlet. After sending the data, it 
 * closes the connection. The difference between /getAll and /fetch is that
 * while /getAll get historical data, /fetch starts buffering from the time
 * the request is received by the servlet and destroyed after the response 
 * is sent. /getAll on the other hand, continues to buffer internally even
 * after the client connection is closed.
 * 
 * The code accepts i) channel name, ii) fully qualified channel name and, iii) wildcard '*' for
 * pattern based subscription.
 * 
 * @author Koushik Sinha
 * Last modified: 02/01/2014
 *
 * Dependencies:  servlets 3+, jedis-2.2.1, gson-2.2.4, commons-pool-1.6, slf4j-1.7.5
 * 	
 * Hints for testing:
 * 		1. Tune the socket timeout parameter in JedisPool(...) call if connecting over a slow network
 *  	2. Tune REDIS_CALLBACK_TIMEOUT, in case the rate of publication is very slow
 *  	3. Tune the number of threads in ExecutorService 
 * 		 
 *
 * Deployment steps: 
 * 		1. [Required] Set redisHost and redisPort in code, as per your REDIS setup/location
 * 		2. [Optional] Tune time-out and other parameters, if necessary
 * 		3. [Required]Compile and package as WAR file
 * 		4. [Required] Deploy as WAR file in glassfish 3.1.2
 * 		5. [Optional] Setup ssh tunneling (e.g. command: ssh tunneling:: ssh -f -L 1978:localhost:6379 scd1.qcri.org -N)
 * 		6. Issue fetch request from client
 *
 *
 * Invocation: host:port/context-path/channel?crisisCode={crisisCode}&callback={callback}&count={count}
 * ============
 * Channel name based examples:
 * 	1. http://localhost:8080/AIDROutput/crisis/getlist/channel?crisisCode=clex_20131201&count=50
 *  2. http://localhost:8080/AIDROutput/crisis/getlist/channel?crisisCode=clex_20131201&callback=func
 *  3. http://localhost:8080/AIDROutput/crisis/getlist/channel?crisisCode=clex_20131201&callback=func&count=50
 * 
 * Wildcard based examples: 
 *  1. http://localhost:8080/AIDROutput/crisis/getlist/channel?crisisCode=*&count=50
 *  2. http://localhost:8080/AIDROutput/crisis/getlist/channel?crisisCode=*&callback=func
 *  3. http://localhost:8080/AIDROutput/crisis/getlist/channel?crisisCode=*&callback=func&count=50
 *  
 * Fully qualified channel name based examples:
 *  1. http://localhost:8080/AIDROutput/crisis/getlist/channel?crisisCode=aidr_predict.clex_20131201&count=50
 *  2. http://localhost:8080/AIDROutput/crisis/getlist/channel?crisisCode=aidr_predict.clex_20131201&callback=func
 *  3. http://localhost:8080/AIDROutput/crisis/getlist/channel?crisisCode=aidr_predict.clex_20131201&callback=func&count=50
 *  
 *  
 *  Parameter explanations:
 *  	1. crisisCode [mandatory]: the REDIS channel to which to subscribe
 *  	2. callback [optional]: name of the callback function for JSONP data
 *  	3. count [optional]: the specified number of messages that have been buffered by the service. If unspecified
 *  		or <= 0 or larger than the MAX_MESSAGES_COUNT, the default number of messages are returned 
 */

package qa.qcri.aidr.output.fetch;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
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

import qa.qcri.aidr.output.utils.JedisConnectionObject;
import qa.qcri.aidr.output.utils.JsonDataFormatter;
import qa.qcri.aidr.output.utils.WriteResponse;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;


@SuppressWarnings("serial")
@WebServlet(value = "/channel", asyncSupported = true)
public class RedisHTTPGetData extends HttpServlet {

	// Message count constants
	private static final int MAX_MESSAGES_COUNT = 100;

	// Time-out constants
	private static final int REDIS_CALLBACK_TIMEOUT = 5 * 60 * 1000;		// in ms
	private static final int THREAD_TIMEOUT = 15 * 60 * 1000;				// in ms

	// Pertaining to JEDIS - establishing connection with a REDIS DB
	// Currently using ssh tunneling:: ssh -f -L 1978:localhost:6379 scd1.qcri.org -N
	// 2 channels being used for testing:
	// 		a) aidr_predict.clex_20131201

	private static final String CHANNEL_PREFIX_CODE = "aidr_predict.";
	private boolean patternSubscriptionFlag;
	private final boolean rejectNullFlag = false;

	private String redisChannel = "*";					// channel to subscribe to		
	private static String redisHost = "localhost";		// Current assumption: REDIS running on same m/c
	private static int redisPort = 6379;					

	public static JedisConnectionObject jedisConn;				// one JedisConnectionObject per connection
	public Jedis subscriberJedis = null;
	public RedisSubscriber aidrSubscriber = null;

	// Runtime related
	private boolean isConnected = false;
	private boolean isSubscribed =false;

	// Related to Async Thread management
	public ExecutorService executorServicePool;

	// Debugging
	private static Logger logger = LoggerFactory.getLogger(RedisHTTPGetData.class);

	/////////////////////////////////////////////////////////////////////////////

	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		// For now: set up a simple configuration that logs on the console
		//PropertyConfigurator.configure("log4j.properties");                // where to place the properties file?
		//BasicConfigurator.configure();                                                        // initialize log4j logging
		System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");                // set logging level for slf4j
		jedisConn = new JedisConnectionObject(redisHost, redisPort);
		executorServicePool = Executors.newFixedThreadPool(200);                // max number of threads
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
	private synchronized void stopSubscription(final RedisSubscriber sub, final Jedis jedis) {
		if (sub != null && sub.getSubscribedChannels() > 0) {
			logger.info("[stopSubscription] sub = " + sub + ", jedis = " + jedis + ", flag = " + sub.patternFlag);
			if (!sub.patternFlag) { 
				sub.unsubscribe();                                
			}
			else {
				sub.punsubscribe();
			}
		}
		if (jedisConn != null) jedisConn.returnJedis(jedis);
		logger.info("[stopSubscription] Subscription ended for Channel=" + sub.channel);
	}


	// Create a subscription to specified REDIS channel: spawn a new thread
	private void subscribeToChannel(final RedisSubscriber sub, final Jedis jedis, String channel) throws Exception {
		redisChannel = channel;
		executorServicePool.submit(new Runnable() {
			public void run() {
				try {
					if (!patternSubscriptionFlag) { 
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
					logger.error("[subscribeToChannel] AIDR Predict Channel Subscribing failed, attempting stopSubscription...");
					logger.error("sub = " + sub + ", jedis = " + jedis + ", flag = " + sub.patternFlag);
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

		// Parse the HTTP GET request and generating results for output
		// Set the response MIME type of the response message

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		if (request.getParameter("port") != null) {
			redisPort = Integer.parseInt(request.getParameter("port"));
		}

		if (request.getParameter("crisisCode") != null) {
			// TODO: Handle client refresh of webpage in same session
			//if (this.aidrSubscriber == null) {}				
			if (initRedisConnection()) {
				// Get callback function name, if any
				String channel = setFullyQualifiedChannelName(CHANNEL_PREFIX_CODE, request.getParameter("crisisCode"));
				String callbackName = request.getParameter("callback");
				redisChannel = channel;

				// Now spawn asynchronous response.getWriter() - if coming from a different session
				//TODO: handling same sessions gracefully 
				// if (!isSameSession(request)) {}
				final AsyncContext asyncContext = request.startAsync(request, response);
				aidrSubscriber = new RedisSubscriber(asyncContext, subscriberJedis, channel, callbackName);
				jedisConn.setJedisSubscription(subscriberJedis, patternSubscriptionFlag);
				try {
					subscribeToChannel(aidrSubscriber, subscriberJedis, channel);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error("[doGet] Fatal exception occurred attempting subscription: " + e.toString());
					e.printStackTrace();
					System.exit(1);
				}
				asyncContext.setTimeout(THREAD_TIMEOUT);		// negative --> no timeout
				executorServicePool.execute(aidrSubscriber);	// alternatively, use: asyncContext.start(aidrSubscriber);
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

		logger.info("[doGet] Reached end-of-function...");
	}

	// cleanup all threads 
	void shutdownAndAwaitTermination(ExecutorService threadPool) {
		threadPool.shutdown(); // Disable new tasks from being submitted
		try {
			// Wait a while for existing tasks to terminate
			if (!threadPool.awaitTermination(1, TimeUnit.SECONDS)) {
				threadPool.shutdownNow(); 			// Cancel currently executing tasks
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
			stopSubscription(this.aidrSubscriber, this.subscriberJedis);
			//jedisConn.closeAll();
			logger.info("[destroy] stopSubscription success!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("[destroy] Exception occurred attempting stopSubscription: " + e.toString());
			e.printStackTrace();
		}
		shutdownAndAwaitTermination(executorServicePool);
		logger.info("[destroy] All done, shutdown getlist service...");
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////
	// The inner class that handles both Asynchronous Servlet Thread and Redis Threaded Subscription
	//////////////////////////////////////////////////////////////////////////////////////////////////
	private class RedisSubscriber extends JedisPubSub implements Runnable, AsyncListener {

		// Redis/Jedis related
		private Jedis jedis;
		private boolean patternFlag;
		private String channel = redisChannel;

		private static final int DEFAULT_COUNT = 50;		// default number of messages to fetch
		private String callbackName = null;					// will contain the name of the callback function (JSONP)
		private int messageCount = DEFAULT_COUNT;			// number of messages to fetch
		private String messageObjectResponse = null;		// contains JSON message from REDIS

		// Async execution related 
		private AsyncContext asyncContext;
		private boolean runFlag = true;
		private boolean error = false;
		private boolean timeout = false;
		private long lastAccessedTime = new Date().getTime();

		// Share data structure between Jedis and Async threads
		private List<String> messageList = Collections.synchronizedList(new ArrayList<String>());


		public RedisSubscriber(AsyncContext asyncContext, Jedis jedis, String channel, String callbackName) throws IOException {
			this.channel = channel;
			this.callbackName = callbackName;
			this.asyncContext = asyncContext;
			this.jedis = jedis;
			this.setRunFlag(true);		

			// Listen for errors and timeouts
			asyncContext.addListener(this);
			HttpServletRequest request = (HttpServletRequest) asyncContext.getRequest();
			if (request.getParameter("count") != null) {
				int msgCount = Integer.parseInt(request.getParameter("count"));
				if (msgCount > 0) {
					messageCount = Math.min(msgCount, MAX_MESSAGES_COUNT);
				}
			}
		}

		@Override
		public void onMessage(String channel, String message) {
			// onMessage() asynchronously receives messages from the JedisPubSub channel 
			// Note: no explicit synchronization required for immutable objects
			if (messageList.size() < messageCount) {
				messageList.add(message);
			}
			lastAccessedTime = new Date().getTime();		// time when message last received from REDIS
		}

		@Override
		public void onPMessage(String pattern, String channel, String message) {

			if (messageList.size() < messageCount) {
				messageList.add(message);
			}
			lastAccessedTime = new Date().getTime();		// time when message last received from REDIS
		}


		@Override
		public void onPSubscribe(String pattern, int subscribedChannels) {
			patternFlag = true;
			logger.info("[onPSubscribe] Started pattern subscription...");
		}

		@Override
		public void onPUnsubscribe(String pattern, int subscribedChannels) {
			logger.info("[onPUnsubscribe] Unsubscribed from pattern subscription...");
		}

		@Override
		public void onSubscribe(String channel, int subscribedChannels) {
			patternFlag = false;
			logger.info("[onSubscribe] Started channel subscription...");
		}

		@Override
		public void onUnsubscribe(String channel, int subscribedChannels) {
			logger.info("[onUnsubscribe] We are in an interesting place...");
			//Thread.currentThread().interrupt();
		}

		///////////////////////////////////
		// Now to implement Async methods
		///////////////////////////////////
		public boolean isThreadTimeout(long startTime) {
			if ((THREAD_TIMEOUT > 0) && (new Date().getTime() - startTime) > THREAD_TIMEOUT) {
				logger.warn("Exceeded Thread timeout = " + THREAD_TIMEOUT + "msec");
				return true;
			}
			return false;
		}

		@Override
		public void run() {
			// Time-out related local variables
			long startTime = new Date().getTime();			// start time of the thread execution

			HttpServletResponse response = (HttpServletResponse) asyncContext.getResponse();
			WriteResponse responseWriter = new WriteResponse(response,true);
			boolean isWriter = responseWriter.initWriter("application/json");
			setRunFlag(isWriter);
			while (getRunFlag() && !isThreadTimeout(startTime)) {
				// Here we poll a non blocking resource for updates
				if (!messageList.isEmpty() && messageList.size() >= messageCount) { 
					// Messages received, send these to the waiting client
					if (!error && !timeout) {
						// Iterate over messageList and send each message individually as JSONP 
						synchronized(messageList) {
							final JsonDataFormatter taggerOutput = new JsonDataFormatter(callbackName);	// Tagger specific JSONP output formatter
							final StringBuilder jsonDataList = taggerOutput.createList(messageList, messageList.size(), rejectNullFlag);
							final int count = taggerOutput.getMessageCount();
							responseWriter.writeJsonData(jsonDataList, count);

							// Reset the messageList buffer and cleanup
							messageList.clear();
						}	// end synchronized
						responseWriter.close();
						setRunFlag(false);								// done - exit async thread
					}
					else {
						logger.error("Not sending response because task timed-out or error'ed. error={}, timeout={}, run={}", new Object[] { error, timeout, getRunFlag() });
						setRunFlag(false);
					}
				}
				else {
					// messageList.size < messageCount
					// Wait for some more time before giving up, 
					// in case REDIS connection is broken
					long currentTime = new Date().getTime();
					long elapsed = currentTime - lastAccessedTime;
					if (elapsed > REDIS_CALLBACK_TIMEOUT) {
						logger.error("[run::Timeout] Elapsed time = " + elapsed + ", exceeded REDIS timeout = " + REDIS_CALLBACK_TIMEOUT + "sec");
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
					logger.error("[run] Client side error - possible client disconnect..." + new Date());
					setRunFlag(false);
				}
			}	// end-while

			// clean-up and exit thread
			if (!error && !timeout) {
				messageList = null;
				if (!responseWriter.checkError()) {
					responseWriter.close();
				}
				try {
					stopSubscription(this, this.jedis);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error("[run] Attempting clean-up. Exception occurred attempting stopSubscription: " + e.toString());
					e.printStackTrace();
				}
				// Double check just to ensure graceful exit - Not sure if required!
				if (!error && !timeout) {
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
			logger.info("[run] Async thread complete...");
		}
	}
}