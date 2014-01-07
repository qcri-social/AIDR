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
 * Invocations: 
 * ============
 * Channel Name based examples:
 *  1. http://localhost:8080/aidr-output/stream?crisisCode=clex_20131201&callback=print&rate=10  
 *  2. http://localhost:8080/aidr-output/stream?crisisCode=clex_20131201&subscription_duration=1h 
 *  3. http://localhost:8080/aidr-output/stream?crisisCode=clex_20131201&subscription_duration=1h&callback=print
 *  4. http://localhost:8080/aidr-output/stream?crisisCode=clex_20131201&subscription_duration=1h&rate=15
 *  5. http://localhost:8080/aidr-output/stream?crisisCode=clex_20131201&subscription_duration=1h&callback=print&rate=10
 *  
 * Wildcard based examples:
 *  1. http://localhost:8080/aidr-output/stream?crisisCode=*&callback=print&rate=10 
 *  2. http://localhost:8080/aidr-output/stream?crisisCode=*&subscription_duration=1h 
 *  3. http://localhost:8080/aidr-output/stream?crisisCode=*&subscription_duration=1h&callback=print
 *  4. http://localhost:8080/aidr-output/stream?crisisCode=*&subscription_duration=1h&rate=15
 *  5. http://localhost:8080/aidr-output/stream?crisisCode=*&subscription_duration=1h&callback=print&rate=10
 *  
 * Fully qualified channel name examples:
 *  1. http://localhost:8080/aidr-output/stream?crisisCode=aidr_predict.clex_20131201&callback=print&rate=10 
 *  2. http://localhost:8080/aidr-output/stream?crisisCode=aidr_predict.clex_20131201&subscription_duration=1h 
 *  3. http://localhost:8080/aidr-output/stream?crisisCode=aidr_predict.clex_20131201&subscription_duration=1h&callback=print
 *  4. http://localhost:8080/aidr-output/stream?crisisCode=aidr_predict.clex_20131201&subscription_duration=1h&rate=15
 *  5. http://localhost:8080/aidr-output/stream?crisisCode=aidr_predict.clex_20131201&subscription_duration=1h&callback=print&rate=10
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
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@SuppressWarnings("serial")
@WebServlet(value = "/stream", asyncSupported = true)
public class RedisHTTPStreaming extends HttpServlet {

	// Time-out constants
	private static final int REDIS_CALLBACK_TIMEOUT = 2 * 60 * 1000;		// in ms
	private static final int SUBSCRIPTION_MAX_DURATION = 6 * 60 * 60 * 1000;			// in ms

	// Pertaining to JEDIS - establishing connection with a REDIS DB
	// Currently using ssh tunneling:: ssh -f -L 1978:localhost:6379 scd1.qcri.org -N
	// Channel(s) being used for testing:
	// 		a) aidr_predict.clex_20131201
	//		
	private static final String CHANNEL_PREFIX_CODE = "aidr_predict.";
	private boolean patternSubscriptionFlag;

	private String redisChannel = "aidr_predict.clex_20131201";		// channel to subscribe to		
	private static final String redisHost = "localhost";					// Current assumption: REDIS running on same m/c
	private static final int redisPort = 1978;					
	public static JedisPoolConfig poolConfig = null;
	public static JedisPool pool = null;
	public Jedis subscriberJedis = null;
	public RedisSubscriber aidrSubscriber = null;

	// Related to Async Thread management
	public ExecutorService executorServicePool;

	// Debugging
	private static Logger logger = LoggerFactory.getLogger(RedisHTTPStreaming.class);

	/////////////////////////////////////////////////////////////////////////////

	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		// For now: set up a simple configuration that logs on the console
		//PropertyConfigurator.configure("log4j.properties");		// where to place the properties file?
		//BasicConfigurator.configure();							// initialize log4j logging
		logger.info("[init] In servlet init...");
		initJedisPool();
		executorServicePool = Executors.newFixedThreadPool(200);		// max number of threads
	}

	// Initialize JEDIS parameters and thread pool
	public void initJedisPool() {
		if (null == poolConfig) {
			poolConfig = new JedisPoolConfig();
			poolConfig.setMaxActive(100);
			poolConfig.setMaxIdle(50);
			poolConfig.setMinIdle(5);
			poolConfig.setTestWhileIdle(true);
			poolConfig.setTestOnBorrow(true);
			poolConfig.setTestOnReturn(true);
			poolConfig.numTestsPerEvictionRun = 10;
			poolConfig.timeBetweenEvictionRunsMillis = 60000;
			poolConfig.maxWait = 3000;
			poolConfig.whenExhaustedAction = org.apache.commons.pool.impl.GenericKeyedObjectPool.WHEN_EXHAUSTED_GROW;
			logger.info("[connectToRedis] New Jedis poolConfig: " + poolConfig);
		} else {
			logger.info("[connectToRedis] Reusing existing Jedis poolConfig: " + poolConfig);
		}
		if (null == pool) {
			pool = new JedisPool(poolConfig, redisHost, redisPort, 10000);
			logger.info("[connectToRedis] New Jedis pool: " + pool);
		} else {
			logger.info("[connectToRedis] Reusing existing Jedis pool: " + pool);
		}
	}

	public boolean initRedisConnection() { 
		this.subscriberJedis = pool.getResource();
		if (this.subscriberJedis != null) {
			return true;
		}
		return false;
	}

	// Stop subscription of this subscribed thread and return resources to the JEDIS thread pool
	private void stopSubscription(final RedisSubscriber sub, final Jedis jedis) {
		System.out.println("[stopSubscription] aidrSubscriber = " + sub + ", jedis = " + jedis + "patternFlag = " + patternSubscriptionFlag);
		System.out.println(sub + "@[stopSubscription] Subscription count = " + sub.getSubscribedChannels());
		if (sub != null && sub.getSubscribedChannels() > 0) {
			if (!patternSubscriptionFlag) { 
				sub.unsubscribe();				
			}
			else {
				sub.punsubscribe();
			}
			System.out.println("[stopSubscription] unsubscribed " + sub + ", Subscription count = " + sub.getSubscribedChannels());
		}

		try {
			pool.returnResource(jedis);
			System.out.println(sub + "@[stopSubscription] Pool resource returned");
		} catch (JedisConnectionException e) {
			System.out.println(sub + "@[stopsubscription] JedisConnectionException occurred...");
			pool.returnBrokenResource(jedis);
		}
		logger.info(sub + "@[stopSubscription] Subscription ended for Channel=" + redisChannel);
	}


	// Create a subscription to specified REDIS channel: spawn a new thread
	private void subscribeToChannel(final RedisSubscriber sub, final Jedis jedis, String channel) throws Exception {
		redisChannel = channel;
		System.out.println("[subscribeToChannel] aidrSubscriber = " + sub + ", jedis = " + jedis + "patternFlag = " + patternSubscriptionFlag);

		executorServicePool.submit(new Runnable() {
			public void run() {
				try {
					if (!patternSubscriptionFlag) { 
						logger.info(sub + "@[subscribeToChannel] Attempting subscription for " + redisHost + ":" + redisPort + "/" + redisChannel);
						jedis.subscribe(sub, redisChannel);
						logger.info(sub + "@[subscribeToChannel] Out of subscription for Channel = " + redisChannel);
					} 
					else {
						logger.info(sub + "@[subscribeToChannel] Attempting pSubscription for " + redisHost + ":" + redisPort + "/" + redisChannel);
						jedis.psubscribe(sub, redisChannel);
						logger.info(sub + "@[subscribeToChannel] Out of pSubscription for Channel = " + redisChannel);
					}
				} catch (Exception e) {
					logger.info(sub + "@[subscribeToChannel] AIDR Predict Channel Subscribing failed");
					stopSubscription(sub, jedis);
				} finally {
					try {
						logger.info(sub + "@[subscribeToChannel::finally] Attempting stopSubscription...");
						stopSubscription(sub, jedis);
						logger.info(sub + "@[subscribeToChannel::finally] stopSubscription success!");
					} catch (Exception e) {
						System.out.println(sub + "@[subscribeToChannel::finally] Exception occurred attempting stopSubscription: " + e.toString());
						e.printStackTrace();
						System.exit(1);
					}
				}
			}
		}); 
	}


	public String setFullyQualifiedChannelName(final String channelPrefixCode, final String channelCode) {
		if (channelCode.startsWith(channelPrefixCode)) {
			return channelCode;			// already fully qualified name
		}
		String channelName = channelPrefixCode + channelCode;
		if (channelCode.indexOf("*") > 0) {
			patternSubscriptionFlag = true;
		}
		else {
			patternSubscriptionFlag = false;
		}
		return channelName;
	}


	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		if (request.getParameter("crisisCode") != null) {
			// TODO: Handle client refresh of webpage in same session				
			if (initRedisConnection()) {
				// Get callback function name, if any
				String channel = setFullyQualifiedChannelName(CHANNEL_PREFIX_CODE, request.getParameter("crisisCode"));
				String callbackName = request.getParameter("callback");

				// Now spawn asynchronous response.getWriter() - if coming from a different session
				//TODO: handling same sessions gracefully 
				// if (!isSameSession(request)) {}
				final AsyncContext asyncContext = request.startAsync(request, response);
				aidrSubscriber = new RedisSubscriber(asyncContext, subscriberJedis, channel, callbackName);
				System.out.println("[doGet] aidrSubscriber = " + aidrSubscriber);
				try {
					subscribeToChannel(aidrSubscriber, subscriberJedis, channel);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("[doGet] Fatal exception occurred attempting subscription: " + e.toString());
					e.printStackTrace();
					System.exit(1);
				}
				executorServicePool.execute(aidrSubscriber);	// alternatively, use: asyncContext.start(aidrSubscriber);
			}
		} 
		else {
			// No crisisCode provided...
			logger.info("[doGet] In parameter: crisisCode = null, stopSubscription=false");
			PrintWriter out = response.getWriter();
			try {
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/html");

				// Allocate a output writer to write the response message into the network socket
				out.println("<!DOCTYPE html>");
				out.println("<html>");
				out.println("<head><title>Redis HTTP Streaming App</title></head>");
				out.println("<body>");
				out.println("<h1>No CrisisCode Provided! </h1>");
				out.println("<h>Can not initiate REDIS channel subscription!</h>");
				out.println("</body></html>");
			} finally {
				out.flush();
				out.close();  // Always close the output writer
			}
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
					System.err.println("[shutdownAndAwaitTermination] Pool did not terminate");
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
			logger.info("[destroy] Attempting stopSubscription...");
			stopSubscription(this.aidrSubscriber, this.subscriberJedis);
			pool.destroy();
			logger.info("[destroy] stopSubscription success!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("[destroy] Exception occurred attempting stopSubscription: " + e.toString());
			e.printStackTrace();
		}
		shutdownAndAwaitTermination(executorServicePool);
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////
	// The inner class that handles both Asynchronous Servlet Thread and Redis Threaded Subscription
	//////////////////////////////////////////////////////////////////////////////////////////////////
	private class RedisSubscriber extends JedisPubSub implements Runnable, AsyncListener {

		// Redis/Jedis related
		private String channel = redisChannel;
		private String callbackName = null;
		private String messageObjectResponse = null;		// contains JSON message from REDIS
		private Jedis jedis;

		// Async execution related 
		private AsyncContext asyncContext;
		private boolean runFlag = true;
		private boolean error = false;
		private boolean timeout = false;
		private int subscriptionDuration = SUBSCRIPTION_MAX_DURATION;

		// rate control related 
		private static final int DEFAULT_SLEEP_TIME = 0;		// in msec
		private float messageRate = 0;							// default: <= 0 implies no rate control
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

			logger.info(this + "@[RedisSubscriber] Step 1: get subscription duration");
			if (request.getParameter("subscription_duration") != null) {
				subscriptionDuration = parseTime(request.getParameter("subscription_duration"));
			}

			logger.info(this + "@[RedisSubscriber] Step 2: get output rate, if any");

			if (request.getParameter("rate") != null) {
				messageRate = Float.parseFloat(request.getParameter("rate"));	// specified as messages/min (NOTE: upper-bound)
				if (messageRate > 0) {		// otherwise, use default rate
					sleepTime = Math.max(0, Math.round(60 * 1000 / messageRate));		// time to sleep between sends (in msecs)
				}
			}
			logger.info(this + "@[RedisSubscriber] Parameters received: crisisCode:" + this.channel
					+ ", subscription_duration = " + subscriptionDuration 
					+ ", callback = " + this.callbackName 
					+ ", rate = " + this.messageRate);

			// Set timeout period for async thread
			asyncContext.setTimeout(subscriptionDuration);		// negative --> no timeout

			logger.info(this +"@[RedisSubscriber] Step 3: add asyc listener and set timeout period.");

			// Listen for errors and timeouts			
			asyncContext.addListener(this);
		}

		private int parseTime(String timeString) {
			int duration = 0;
			float value = Float.parseFloat(timeString.substring(0, timeString.length()-1));
			if (value > 0) {
				String suffix = timeString.substring(timeString.length() - 1, timeString.length());
				switch(suffix) {
				case "s":
					duration = Math.min(SUBSCRIPTION_MAX_DURATION, Math.round(value * 1000));
					break;
				case "m":
					duration = Math.min(SUBSCRIPTION_MAX_DURATION, Math.round(value * 1000 * 60));
					break;
				case "h":
					duration = Math.min(SUBSCRIPTION_MAX_DURATION, Math.round(value * 1000 * 60 * 60));
					break;
				case "d":
					duration = Math.min(SUBSCRIPTION_MAX_DURATION, Math.round(value * 1000 * 60 * 60 * 24));
					break;
				}
			}
			return duration;
		}

		@Override
		public void onMessage(String channel, String message) {
			// onMessage() asynchronously receives messages from the JedisPubSub channel 
			// Note: no explicit synchronization required for immutable objects
			// reset for every triggered event of receiving a new message.
			if (callbackName != null) {
				messageObjectResponse = callbackName + "(" + message + ")"; // with specified callback - JSONP
			}
			else {
				messageObjectResponse = message; 	// without callback - pure JSON
			}
			messageList.add(messageObjectResponse);		

			// Also log message for debugging purpose
			logger.info(this + "@[onMessage] Received Redis message to be sent to client: " + messageObjectResponse);

			// Clean-up memory - required?
			channel = null;
			message = null;
			messageObjectResponse = null;
		}

		@Override
		public void onPMessage(String pattern, String channel, String message) {
			if (callbackName != null) {
				messageObjectResponse = callbackName + "(" + message + ")"; // with specified callback - JSONP
			}
			else {
				messageObjectResponse = message; 	// without callback - pure JSON
			}
			messageList.add(messageObjectResponse);		

			// Also log message for debugging purpose
			logger.info("[onPMessage] For pattern: " + pattern + "##channel = " + channel + ", Received Redis message: " + messageObjectResponse);

			// Clean-up memory - required?
			pattern = null;
			channel = null;
			message = null;
			messageObjectResponse = null;
		}

		@Override
		public void onPSubscribe(String pattern, int subscribedChannels) {
			logger.info("[onPSubscribe] Started pattern subscription...");
		}

		@Override
		public void onPUnsubscribe(String pattern, int subscribedChannels) {
			logger.info("[onPUnsubscribe] Unsubscribed from pattern subscription...");
		}

		@Override
		public void onSubscribe(String channel, int subscribedChannels) {
			logger.info("[onSubscribe] Started channel subscription...");
		}

		@Override
		public void onUnsubscribe(String channel, int subscribedChannels) {
			logger.info(this + "@[onUnsubscribe] Unusbscribed from channel " + channel);
		}

		///////////////////////////////////
		// Now to implement Async methods
		///////////////////////////////////
		public boolean isThreadTimeout(long startTime) {
			if ((subscriptionDuration > 0) && (new Date().getTime() - startTime) > subscriptionDuration) {
				System.out.println(this + "@[isThreadTimeout] Exceeded Thread timeout = " + subscriptionDuration + "msec");
				return true;
			}
			return false;
		}


		@Override
		public void run() {
			logger.info(this + "@[run] started async thread execution..., time = " + new Date());
			// Time-out related local variables
			long startTime = new Date().getTime();			// start time of the thread execution
			long currentTime = new Date().getTime(); 
			long lastAccessedTime = currentTime; 

			HttpServletResponse response = (HttpServletResponse) asyncContext.getResponse();
			response.setContentType("application/json");
			PrintWriter responseWriter = null;
			try {
				responseWriter = response.getWriter();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error(this + "@[run] Error initializing PrintWriter", e);
				e.printStackTrace();
				setRunFlag(false);
			}

			while (getRunFlag() && !isThreadTimeout(startTime)) {
				// Here we poll a non blocking resource for updates
				if (!messageList.isEmpty()) {
					// There are updates, send these to the waiting client
					logger.info(this + "@[run] Received message from REDIS");

					if (!error && !timeout) {
						// Send updates response as JSON
						synchronized (messageList) {
							Gson jsonObject = new GsonBuilder().serializeNulls()		//.disableHtmlEscaping()
									.serializeSpecialFloatingPointValues().setPrettyPrinting()
									.create();
							String jsonData = jsonObject.toJson(messageList.get(messageList.size()-1) != null 
									? messageList.get(messageList.size()-1) : new String("{}"));

							logger.info(this + "@[run] Sending JSON data: " + jsonData);
							responseWriter.println(jsonData);
							responseWriter.flush();

							jsonObject = null;
							jsonData = null;
							messageList.remove(messageList.size()-1);	// remove the sent message from list
							//messageObjectResponse = null;
						}	// end synchronized block
						lastAccessedTime = new Date().getTime();		// time when message last received from REDIS

						// Now sleep for a short time before going for next message - easy to read on screen
						try {
							Thread.sleep(sleepTime);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else {
						logger.info(this + "@[run] Not sending response because task timed-out or error'ed. error={}, timeout={}, run={}", new Object[] { error, timeout, getRunFlag() });
						setRunFlag(false);
					}
				}
				else {
					// messageList is empty --> no message received 
					// from REDIS. Wait for some more time before giving up.
					currentTime = new Date().getTime();
					long elapsed = currentTime - lastAccessedTime;
					if (elapsed > REDIS_CALLBACK_TIMEOUT) {
						System.out.println(this + "@[run::Timeout] Elapsed time = " + elapsed + ", exceeded REDIS timeout = " + REDIS_CALLBACK_TIMEOUT + "sec");
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
					logger.info(this + "@[run] Client side error - possible client disconnect..." + new Date());
					setRunFlag(false);
				}
			}	// end-while

			// clean-up and exit thread
			if (!error && !timeout) {
				messageList.clear();
				messageList = null;
				if (!responseWriter.checkError()) {
					responseWriter.close();
				}
				try {
					logger.info(this + "@[run] All done. Attempting stopSubscription... time = " + new Date());
					stopSubscription(this, this.jedis);
					logger.info(this + "@[run] All done. unSubscription success! ");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println(this + "@[run] Attempting clean-up. Exception occurred attempting stopSubscription: " + e.toString());
					e.printStackTrace();
				}
				logger.info(this + "@[run] Attempting async complete...");
				// Double check just to ensure graceful exit - Not sure if required!
				if (!error && !timeout) {
					logger.info(this + "@[run] Async complete...");
					asyncContext.complete();
				}
			}
		}

		public void setRunFlag(final boolean val) {
			runFlag = val;
			logger.info(this + "@[setRunFlag] flag = " + runFlag);
		}

		public boolean getRunFlag() {
			return runFlag;
		}

		@Override
		public void onError(AsyncEvent event) throws IOException {
			setRunFlag(false);
			error = true;
			logger.error(this + "@[onError] An error occured while executing task for client ");
		}

		@Override
		public void onTimeout(AsyncEvent event) throws IOException {
			setRunFlag(false);
			timeout = true;
			logger.warn(this + "@[onTimeout] Timed out while executing task for client");
		}

		@Override
		public void onStartAsync(AsyncEvent event) throws IOException {}

		@Override
		public void onComplete(AsyncEvent event) throws IOException {}
	}
}
