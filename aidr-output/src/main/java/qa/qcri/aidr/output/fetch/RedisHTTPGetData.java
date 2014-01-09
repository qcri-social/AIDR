
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
 * Invocations: 
 * ============
 * Channel name based examples:
 * 	1. http://localhost:8080/aidr-output/fetch?crisisCode=clex_20131201&count=50
 *  2. http://localhost:8080/aidr-output/fetch?crisisCode=clex_20131201&callback=func
 *  3. http://localhost:8080/aidr-output/fetch?crisisCode=clex_20131201&callback=func&count=50
 * 
 * Wildcard based examples: 
 *  1. http://localhost:8080/aidr-output/fetch?crisisCode=*&count=50
 *  2. http://localhost:8080/aidr-output/fetch?crisisCode=*&callback=func
 *  3. http://localhost:8080/aidr-output/fetch?crisisCode=*&callback=func&count=50
 *  
 * Fully qualified channel name based examples:
 *  1. http://localhost:8080/aidr-output/fetch?crisisCode=aidr_predict.clex_20131201&count=50
 *  2. http://localhost:8080/aidr-output/fetch?crisisCode=aidr_predict.clex_20131201&callback=func
 *  3. http://localhost:8080/aidr-output/fetch?crisisCode=aidr_predict.clex_20131201&callback=func&count=50
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

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@SuppressWarnings("serial")
@WebServlet(value = "/fetch", asyncSupported = true)
public class RedisHTTPGetData extends HttpServlet {

	// Message count constants
	private static final int MAX_MESSAGES_COUNT = 100;
	// Time-out constants
	private static final int REDIS_CALLBACK_TIMEOUT = 2 * 60 * 1000;		// in ms
	private static final int THREAD_TIMEOUT = 1 * 60 * 60 * 1000;			// in ms

	// Pertaining to JEDIS - establishing connection with a REDIS DB
	// Currently using ssh tunneling:: ssh -f -L 1978:localhost:6379 scd1.qcri.org -N
	// 2 channels being used for testing:
	// 		a) aidr_predict.clex_20131201

	private static final String CHANNEL_PREFIX_CODE = "aidr_predict.";
	private boolean patternSubscriptionFlag;

	private String redisChannel = "aidr_predict.clex_20131201";		// channel to subscribe to		
	private static String redisHost = "localhost";					// Current assumption: REDIS running on same m/c
	private static int redisPort = 6379;					
	public static JedisPoolConfig poolConfig;
	public static JedisPool pool;
	public Jedis subscriberJedis = null;
	public RedisSubscriber aidrSubscriber = null;

	// Related to Async Thread management
	public ExecutorService executorServicePool;

	// Debugging
	private static Logger logger = LoggerFactory.getLogger(RedisHTTPGetData.class);

	/////////////////////////////////////////////////////////////////////////////

	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		// For now: set up a simple configuration that logs on the console
		//PropertyConfigurator.configure("log4j.properties");		// where to place the properties file?
		//BasicConfigurator.configure();							// initialize log4j logging
		System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "DEBUG");		// set logging level for slf4j
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
			logger.debug("[connectToRedis] New Jedis poolConfig: " + poolConfig);
		} else {
			logger.debug("[connectToRedis] Reusing existing Jedis poolConfig: " + poolConfig);
		}
		if (null == pool) {
			pool = new JedisPool(poolConfig, redisHost, redisPort, 10000);
			logger.debug("[connectToRedis] New Jedis pool: " + pool);
		} else {
			logger.debug("[connectToRedis] Reusing existing Jedis pool: " + pool);
		}
	}

	public boolean initRedisConnection() { 
		this.subscriberJedis = pool.getResource();
		if (this.subscriberJedis != null) {
			return true;
		}
		return false;
	}

	private void stopSubscription(final RedisSubscriber sub, final Jedis jedis) {
		logger.debug("[stopSubscription] aidrSubscriber = " + sub + ", jedis = " + jedis + "patternFlag = " + this.patternSubscriptionFlag);
		logger.debug(sub + "@[stopSubscription] Subscription count = " + sub.getSubscribedChannels());

		if (sub != null && sub.getSubscribedChannels() > 0) {
			if (!this.patternSubscriptionFlag) { 
				sub.unsubscribe();				
			}
			else {
				sub.punsubscribe();
			}
			logger.info("[stopSubscription] unsubscribed " + sub + ", Subscription count = " + sub.getSubscribedChannels());
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
	
	private void subscribeToChannel(final RedisSubscriber sub, final Jedis jedis, String channel) throws Exception {
		redisChannel = channel;
		executorServicePool.submit(new Runnable() {
			public void run() {
				try {
					if (!patternSubscriptionFlag) { 
						logger.debug(sub + "@[subscribeToChannel] Attempting subscription for " + redisHost + ":" + redisPort + "/" + redisChannel);
						jedis.subscribe(sub, redisChannel);
						logger.info(sub + "@[subscribeToChannel] Out of subscription for Channel = " + redisChannel);
					} 
					else {
						logger.debug(sub + "@[subscribeToChannel] Attempting pSubscription for " + redisHost + ":" + redisPort + "/" + redisChannel);
						jedis.psubscribe(sub, redisChannel);
						logger.info(sub + "@[subscribeToChannel] Out of pSubscription for Channel = " + redisChannel);
					}
				} catch (Exception e) {
					logger.error(sub + "@[subscribeToChannel] AIDR Predict Channel Subscribing failed");
					stopSubscription(sub, jedis);
				} finally {
					try {
						logger.debug(sub + "@[subscribeToChannel::finally] Attempting stopSubscription...");
						stopSubscription(sub, jedis);
						logger.info(sub + "@[subscribeToChannel::finally] stopSubscription success!");
					} catch (Exception e) {
						logger.error(sub + "@[subscribeToChannel::finally] Exception occurred attempting stopSubscription: " + e.toString());
						e.printStackTrace();
						System.exit(1);
					}
				}
			}
		});
	}

	public String setFullyQualifiedChannelName(final String channelPrefixCode, final String channelCode) {
		if (isPattern(channelCode)) {
			patternSubscriptionFlag = true;
			return channelCode;			// already fully qualified name
		}
		//Otherwise concatenate to form the fully qualified channel name
		String channelName = channelPrefixCode.concat(channelCode);
		patternSubscriptionFlag = false;
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

				// Now spawn asynchronous response.getWriter() - if coming from a different session
				//TODO: handling same sessions gracefully 
				// if (!isSameSession(request)) {}
				final AsyncContext asyncContext = request.startAsync(request, response);
				aidrSubscriber = new RedisSubscriber(asyncContext, subscriberJedis, channel, callbackName);
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
			PrintWriter out = response.getWriter();
			try {
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/html");

				// Allocate a output writer to write the response message into the network socket
				out.println("<!DOCTYPE html>");
				out.println("<html>");
				out.println("<head><title>Redis HTTP Get Data App</title></head>");
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

	void shutdownAndAwaitTermination(ExecutorService pool) {
		pool.shutdown(); // Disable new tasks from being submitted
		try {
			// Wait a while for existing tasks to terminate
			if (!pool.awaitTermination(1, TimeUnit.SECONDS)) {
				pool.shutdownNow(); // Cancel currently executing tasks
				// Wait a while for tasks to respond to being cancelled
				if (!pool.awaitTermination(1, TimeUnit.SECONDS))
					logger.error("[shutdownAndAwaitTermination] Pool did not terminate");
					System.err.println("[shutdownAndAwaitTermination] Pool did not terminate");
			}
		} catch (InterruptedException ie) {
			// (Re-)Cancel if current thread also interrupted
			pool.shutdownNow();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
	}

	// cleanup when servlet is destroyed (e.g., server shutdown)
	public void destroy() {
		try {
			logger.debug("[destroy] Attempting stopSubscription...");
			stopSubscription(this.aidrSubscriber, this.subscriberJedis);
			pool.destroy();
			logger.info("[destroy] stopSubscription success!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("[destroy] Exception occurred attempting stopSubscription: " + e.toString());
			e.printStackTrace();
		}
		shutdownAndAwaitTermination(executorServicePool);
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////
	// The inner class that handles both Asynchronous Servlet Thread and Redis Threaded Subscription
	//////////////////////////////////////////////////////////////////////////////////////////////////
	private class RedisSubscriber extends JedisPubSub implements Runnable, AsyncListener {

		// Redis/Jedis related
		private Jedis jedis;
		private static final int DEFAULT_COUNT = 50;		// default number of messages to fetch
		private String channel = redisChannel;
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

			logger.info("[RedisSubscriber] Step 1: add asyc listener.");
			// Listen for errors and timeouts
			asyncContext.addListener(this);

			logger.info("[RedisSubscriber] Step 2: determine message count to be delivered");
			HttpServletRequest request = (HttpServletRequest) asyncContext.getRequest();
			if (request.getParameter("count") != null) {
				int msgCount = Integer.parseInt(request.getParameter("count"));
				if (msgCount > 0) {
					messageCount = Math.min(msgCount, MAX_MESSAGES_COUNT);
				}
			}
			logger.info(this + "@[RedisSubscriber] Parameters received: crisisCode:" + this.channel
					+ ", callback = " + this.callbackName 
					+ ", count = " + this.messageCount);
		}

		@Override
		public void onMessage(String channel, String message) {
			// onMessage() asynchronously receives messages from the JedisPubSub channel 
			// Note: no explicit synchronization required for immutable objects
			// reset for every triggered event of receiving a new message
			// Assign the messageObject response
			//String messageObjectResponse = null;
			if (callbackName != null) {
				messageObjectResponse = callbackName + "(" + message + ")"; // with specified callback - JSONP
			}
			else {
				messageObjectResponse = message; 			// without callback - pure JSON
			}
			if (messageList.size() < messageCount) {
				messageList.add(messageObjectResponse);
				logger.debug("[onMessage] Added new message to messageList, new count = " + messageList.size());
			}
			lastAccessedTime = new Date().getTime();		// time when message last received from REDIS

			// Also log message for debugging purpose
			logger.debug("[onMessage] Received Redis message: " + messageObjectResponse);
			channel = null;
			message = null;
			messageObjectResponse = null;
		}

		@Override
		public void onPMessage(String pattern, String channel, String message) {
			//String messageObjectResponse = null;
			if (callbackName != null) {
				messageObjectResponse = callbackName + "(" + message + ")"; // with specified callback - JSONP
			}
			else {
				messageObjectResponse = message; 			// without callback - pure JSON
			}
			if (messageList.size() < messageCount) {
				messageList.add(messageObjectResponse);
				logger.debug("[onPMessage] Added new message to messageList, new count = " + messageList.size());
			}
			lastAccessedTime = new Date().getTime();		// time when message last received from REDIS

			// Also log message for debugging purpose
			logger.debug("[onPMessage] For pattern: " + pattern + "##channel = " + channel + ", Received Redis message: " + messageObjectResponse);

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
			long currentTime = new Date().getTime(); 

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

			logger.debug("[run] Started async thread...");
			while (getRunFlag() && !isThreadTimeout(startTime)) {
				// Here we poll a non blocking resource for updates
				//logger.info("[run] messageList.isEmpty = " + messageList.isEmpty());
				if (!messageList.isEmpty() && messageList.size() >= messageCount) { 
					// Messages received, send these to the waiting client
					if (!error && !timeout) {
						// Iterate over messageList and send each message individually
						// Send updates response as JSON
						int count = 0;									// number of messages sent so far
						synchronized(messageList) {
							Iterator<String> i = messageList.iterator(); // Must be in synchronized block
							while (i.hasNext() && count < messageCount) {
								String msg = i.next();
								Gson jsonObject = new GsonBuilder().serializeNulls()		//.disableHtmlEscaping()
										.serializeSpecialFloatingPointValues().setPrettyPrinting()
										.create();
								String jsonData = jsonObject.toJson(msg != null ? msg : new String("{}"));
								logger.debug("[run] Sending JSON data: " + jsonData);

								responseWriter.println(jsonData);
								responseWriter.flush();

								jsonObject = null;
								jsonData = null;
								msg = null;
								++count;
							}							
							// Now reset the messageList buffer
							messageList.clear();
						}	// end synchronized
						if (count == messageCount) {
							logger.debug("run] Received all messages, exiting...magic number reached!!!");
							responseWriter.close();
							setRunFlag(false);								// done - exit async thread
						}
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
					currentTime = new Date().getTime();
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
					logger.error(this + "@[run] Client side error - possible client disconnect..." + new Date());
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
					logger.debug(this + "@[run] All done. Attempting stopSubscription... time = " + new Date());
					stopSubscription(this, this.jedis);
					logger.info(this + "@[run] All done. unSubscription success! ");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error(this + "@[run] Attempting clean-up. Exception occurred attempting stopSubscription: " + e.toString());
					e.printStackTrace();
				}
				logger.debug(this + "@[run] Attempting async complete...");
				// Double check just to ensure graceful exit - Not sure if required!
				if (!error && !timeout) {
					logger.info(this + "@[run] Async complete...");
					asyncContext.complete();
				}
			}
		}

		public void setRunFlag(final boolean val) {
			runFlag = val;
			logger.info("[setRunFlag] flag = " + runFlag);
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
		public void onComplete(AsyncEvent event) throws IOException {}
	}
}