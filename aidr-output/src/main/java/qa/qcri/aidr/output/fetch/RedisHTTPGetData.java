
/**
 * This code creates a long pooling connection to get the last 'n' JSONP data 
 * from a REDIS DB to a client using a servlet. After sending the data, it 
 * closes the connection.
 * 
 * @author Koushik Sinha
 * Last modified: 02/01/2014
 *
 * Dependencies:  servlets 3+, jedis-2.2.1, gson-2.2.4, commons-pool-1.6, slf4j-1.7.5
 * 	
 * Hints for testing:
 * 		1. You can increase the test duration by adjusting the SUBSCRIPTION_MAX_DURATION. 
 *  	2. Adjust REDIS_CALLBACK_TIMEOUT, in case the rate of publication is very slow. 
 * 		 
 *
 * Deployment steps: 
 * 		1. [Required] Set redisHost and redisPort in code, as per your REDIS setup/location
 * 		2. [Optional] Tune time-out and other parameters, if necessary
 * 		3. [Required]Compile and package as WAR file
 * 		4. [Required] Deploy as WAR file in glassfish 3.1.2
 * 		5. [Optional] Setup ssh tunneling (e.g. command: ssh tunneling:: ssh -f -L 1978:localhost:6379 scd1.qcri.org -N)
 * 		6. Issue fetch or stream request from client
 *
 *
 * REST invocations: 
 * 	1. http://localhost:8080/aidr-output/fetch?crisisCode=aidr_predict.clex_20131201&count=50
 *  2. http://localhost:8080/aidr-output/fetch?crisisCode=aidr_predict.clex_20131201&callback=func
 *  3. http://localhost:8080/aidr-output/fetch?crisisCode=aidr_predict.clex_20131201&callback=func&count=50
 * 
 *  
 *  Parameter explanations:
 *  	1. crisisCode [mandatory]: the REDIS channel to which to subscribe
 *  	2. callback [optional]: name of the callback function for JSONP data
 *  	3. count [optional]: the specified number of messages that have been buffered by the service. 
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
import javax.servlet.http.HttpSession;


//import org.apache.log4j.BasicConfigurator;
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
	private static final int MAX_MESSAGES_COUNT = 1000;
	// Time-out constants
	private static final int REDIS_CALLBACK_TIMEOUT = 2 * 60 * 1000;		// in ms
	private static final int THREAD_TIMEOUT = 6 * 60 * 60 * 1000;			// in ms

	// Pertaining to JEDIS - establishing connection with a REDIS DB
	// Currently using ssh tunneling:: ssh -f -L 1978:localhost:6379 scd1.qcri.org -N
	// 2 channels being used for testing:
	// 		a) aidr_predict.clex_20131201
	
	private String redisChannel = "aidr_predict.clex_20131201";		// channel to subscribe to		
	private static String redisHost = "localhost";					// Current assumption: REDIS running on same m/c
	private static int redisPort = 1978;					
	public JedisPoolConfig poolConfig;
	public JedisPool pool;
	public Jedis subscriberJedis = null;
	public RedisSubscriber aidrSubscriber = null;
	public static int subscriberCount = 0;

	// Related to Async Thread managemant
	public ExecutorService executorServicePool;
	private static long lastAccessedTime = 0;
	private static long currentAccessTime = 0;

	// Debugging
	private static Logger logger = LoggerFactory.getLogger(RedisHTTPGetData.class);

	/////////////////////////////////////////////////////////////////////////////

	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		// For now: set up a simple configuration that logs on the console
		//PropertyConfigurator.configure("log4j.properties");		// where to place the properties file?
		//BasicConfigurator.configure();
		logger.info("[init] In servlet init...");

		executorServicePool = Executors.newFixedThreadPool(100);		// max 100 threads
		lastAccessedTime = new Date().getTime();		// servlet initialization time

		// Attempt setup of ssh tunneling, just to be sure
		// Turn on only on unix systems, if required
		/*
		try {
			Process p = Runtime.getRuntime().exec("ssh -f -L 1978:localhost:6379 scd1.qcri.org -N");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.info("[init] Unable to execute linux ssh tunneling command!");
			e.printStackTrace();
		}
		 */
	}

	private void initRedisSubscription(String host, int port) {
		redisHost = host;
		redisPort = port;

		// These are blind settings from the web samples
		// No idea in the absence of Jedis documentation

		poolConfig = new JedisPoolConfig();
		poolConfig.setMaxActive(50);
		poolConfig.setMaxIdle(10);
		poolConfig.setMinIdle(1);
		poolConfig.setTestWhileIdle(true);
		poolConfig.setTestOnBorrow(true);
		poolConfig.setTestOnReturn(true);
		poolConfig.numTestsPerEvictionRun = 10;
		poolConfig.timeBetweenEvictionRunsMillis = 60000;
		poolConfig.maxWait = 300;
		poolConfig.whenExhaustedAction = org.apache.commons.pool.impl.GenericKeyedObjectPool.WHEN_EXHAUSTED_GROW;
		pool = new JedisPool(poolConfig, redisHost, redisPort, 10);
		subscriberJedis = pool.getResource();

		++RedisHTTPGetData.subscriberCount;			// increment the number of opened subscription channels
		logger.info("[initRedisSubscription] Initialized empty subscriber, with subscriberJedis = " + subscriberJedis);
		logger.info("[initRedisSubscription] pool = " + pool);
		logger.info("[initRedisSubscription] poolConfig = " + poolConfig);
	}

	// Not used currently, for future, if required
	private void unSubscribeChannel(final RedisSubscriber sub) {
		System.out.println(sub + "@[unSubscribeChannel] subscriberCount = " + subscriberCount + ", Subscription count = " + sub.getSubscribedChannels());
		System.out.println("[unSubscribeChannel] aidrSubscriber = " + sub);
		if (sub != null && sub.getSubscribedChannels() > 0) 
		{
			sub.unsubscribe();				// aidrSubscriber.unsubscribe(redisChannel);
			--RedisHTTPGetData.subscriberCount;
			System.out.println(sub + "@[unSubscribeChannel] unsubscribed, Subscription count = " + sub.getSubscribedChannels());
		}
	}

	private void stopSubscription(final RedisSubscriber sub, final Jedis jedis) {
		System.out.println("[stopSubscription] aidrSubscriber = " + sub + ", jedis = " + jedis);
		System.out.println(sub + "@[stopSubscription] subscriberCount = " + subscriberCount + ", Subscription count = " + sub.getSubscribedChannels());

		if (sub != null && sub.getSubscribedChannels() > 0) {
			sub.unsubscribe();				// aidrSubscriber.unsubscribe(redisChannel);
			--RedisHTTPGetData.subscriberCount;
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



	private void subscribeToChannel(final RedisSubscriber sub, final Jedis jedis, String channel) throws Exception {
		redisChannel = channel;
		System.out.println("[subscribeToChannel] aidrSubscriber = " + sub + ", jedis = " + jedis);

		//new Thread(new Runnable() { 
		executorServicePool.submit(new Runnable() {
			public void run() {
				try {
					logger.info(sub + "@[subscribeToChannel] Attempting subscription for " + redisHost + ":" + redisPort + "/" + redisChannel);
					//subscriberJedis.subscribe(sub, redisChannel);
					jedis.subscribe(sub, redisChannel);
					logger.info(sub + "@[subscribeToChannel] Out of subscription for Channel = " + redisChannel);
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
		}); //.start();
	}

	// Not used currently - fur future, if required
	private boolean isSameSession(HttpServletRequest request) {

		HttpSession session = request.getSession(true);
		if (session.isNew()) {
			return false;
		}

		//final int SESSION_TIMEOUT_INTERVAL = session.getMaxInactiveInterval() * 100;		// returns time interval in sec 
		//logger.info("Session Timeout interval = " + SESSION_TIMEOUT_INTERVAL + " sec");

		currentAccessTime = new Date().getTime();		// time in msecs 
		long elapsed = (currentAccessTime - lastAccessedTime) * 1000;		// time in secs		
		logger.info("Current time = " + currentAccessTime + ", session last access time = " + lastAccessedTime + ", elapsed = " + elapsed);

		if ((elapsed) <= session.getMaxInactiveInterval()) {
			lastAccessedTime = currentAccessTime;
			return true;
		}
		return false;

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
			initRedisSubscription(redisHost, redisPort);

			// Get callback function name, if any
			String channel = request.getParameter("crisisCode");
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
			asyncContext.setTimeout(THREAD_TIMEOUT);		// negative --> no timeout
			executorServicePool.execute(aidrSubscriber);	// alternatively, use: asyncContext.start(aidrSubscriber);
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
		// TODO: later versions for more functionality
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
			if (callbackName != null) {
				messageObjectResponse = callbackName + "(" + message + ")"; // with specified callback - JSONP
			}
			else {
				messageObjectResponse = message; 			// without callback - pure JSON
			}
			if (messageList.size() < messageCount) {
				messageList.add(messageObjectResponse);
				logger.info("Added new message to messageList, new count = " + messageList.size());
			}
			lastAccessedTime = new Date().getTime();		// time when message last received from REDIS

			// Also log message for debugging purpose
			logger.info("[onMessage] Received Redis message: " + messageObjectResponse);
		}

		@Override
		public void onPMessage(String pattern, String channel, String message) {

		}

		@Override
		public void onPSubscribe(String pattern, int subscribedChannels) {}

		@Override
		public void onPUnsubscribe(String pattern, int subscribedChannels) {}

		@Override
		public void onSubscribe(String channel, int subscribedChannels) {}

		@Override
		public void onUnsubscribe(String channel, int subscribedChannels) {
			logger.info("[onUnsubscribe] We are in an interesting place...");
			//Thread.currentThread().interrupt();
		}

		///////////////////////////////////
		// Now to implement Async methods
		///////////////////////////////////
		public boolean isThreadTimeout(long startTime) {
			if ((new Date().getTime() - startTime) > THREAD_TIMEOUT) {
				System.out.println("Exceeded Thread timeout = " + THREAD_TIMEOUT + "msec");
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

			logger.info("[run] Started async thread...");
			while (getRunFlag() && !isThreadTimeout(startTime)) {
				// Here we poll a non blocking resource for updates
				//logger.info("[run] messageList.isEmpty = " + messageList.isEmpty());
				if (!messageList.isEmpty() && messageList.size() >= messageCount) { 
					// Messages received, send these to the waiting client
					logger.info(this + "@[run] Received message list from REDIS, count = " + messageList.size());
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
								final String jsonData = jsonObject.toJson(msg != null ? msg : new String("{}"));
								logger.info("Sending JSON data: " + jsonData);
								response.setContentLength(jsonData.length());

								responseWriter.println(jsonData);
								responseWriter.flush();

								jsonObject = null;
								msg = null;
								++count;
							}							
							// Now reset the messageList buffer
							messageList.clear();
						}	// end synchronized
						if (count == messageCount) {
							logger.info("run] Received all messages, exiting...magic number reached!!!");
							responseWriter.close();
							setRunFlag(false);								// done - exit async thread
						}
					}
					else {
						logger.info("Not sending response because task timed-out or error'ed. error={}, timeout={}, run={}", new Object[] { error, timeout, getRunFlag() });
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
						System.out.println("[run::Timeout] Elapsed time = " + elapsed + ", exceeded REDIS timeout = " + REDIS_CALLBACK_TIMEOUT + "sec");
						setRunFlag(false);
					}	
					else {
						try {
							Thread.sleep(10);
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