/*
 * This code creates a long pooling connection to stream JSONP data 
 * from a REDIS DB to a client using a servlet. The connection is
 * kept alive until one of the conditions occur:
 * 		1. The streaming connection time-out occurs (THREAD_TIMEOUT constant)
 * 		2. The REDIS DB connection times out	(REDIS_CALLBACK_TIMEOUT constant)
 * 		3. Client closes the connection
 * 
 * 
 * @author Koushik Sinha
 * Last modified: 24/12/2013
 *
 * Dependencies:  jedis-2.1.0, gson-2.2.4, commons-pool-1.6, slf4j-1.7.5
 * 	
 * Instructions for running the code:
 * 		To test in a browser, use one of the first two strings mentioned below. For testing with the
 * 		the aidr_predict.clex_20131201 channel on the REDIS DB on scd1 server, the servlet should be 
 * 		configured to tunnel using the ssh tunneling command mentioned below. You can increase the
 * 		test duration by adjusting the THREAD_TIMEOUT. Sometimes, the REDIS_CALLBACK_TIMEOUT may
 * 		also need adjustment, in case the rate of publication is very slow. 
 * 		 
 *
 * Environment setup: 
 * 		1. Deploy as WAR file in glassfish 3.1.2
 * 		2. Setup ssh tunneling using the command: ssh tunneling:: ssh -f -L 1978:localhost:6379 scd1.qcri.org -N
 *
 *
 * REST invocations: 
 * 	1. http://localhost:8080/AsyncRedisServlet/getAll?crisisCode=aidr_predict.clex_20131201&callback=print
 *  2. http://localhost:8080/AsyncRedisServlet/getAll?crisisCode=aidr_predict.clex_20131201
 *  3. http://localhost:8080/AsyncRedisServlet/getAll?crisisCode=test_kou_channel&callback=print
 *  4. http://localhost:8080/AsyncRedisServlet/getAll?crisisCode=test_kou_channel
 *  5. http://localhost:8080/AsyncRedisServlet/getAll?crisisCode=aidr_predict.clex_20131201&callback=print&rate=3
 *  6. http://localhost:8080/AsyncRedisServlet/getAll?stopSubscription=true (deprecated - now using THREAD_TIMEOUT) 
 *  
 *  Parameter explanations:
 *  	1. crisisCode: the REDIS channel to which to subscribe
 *  	2. callback: name of the callback function for JSONP data
 *  	3. rate: an upper bound on the rate at which to send messages to client, expressed as messages/min (floating point number)
 */

/*
 * TODO: 
 * 		1. Remove debugging messages once testing is completed satisfactorily.
 * 		2. Figure out why using Jedis pool is creating dangling subscription 
 * 		   in REDIS that can only be stopped by rebooting the server.
 * 		3. Use annotations more extensively to improve coding style/readability.
 * 
 * 	Possible Problem areas:
 * 		1. Memory leak in long running or high rate streaming
 * 		2. Not graceful connection release (read item 2 in TODO above)
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
import javax.servlet.http.HttpSession;

import org.apache.log4j.BasicConfigurator;
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
	private static final int THREAD_TIMEOUT = 6 * 60 * 60 * 1000;			// in ms

	// Pertaining to JEDIS - establishing connection with a REDIS DB
	// Currently using ssh tunneling:: ssh -f -L 1978:localhost:6379 scd1.qcri.org -N
	// 2 channels being used for testing:
	// 		a) aidr_predict.clex_20131201
	//		b) test_kou_channel
	private static String redisChannel = "aidr_predict.clex_20131201";		// channel to subscribe to		
	private static String redisHost = "localhost";					// Current assumption: REDIS running on same m/c
	private static int redisPort = 6379;					
	public JedisPoolConfig poolConfig;
	public JedisPool pool;
	public Jedis subscriberJedis = null;
	public RedisSubscriber aidrSubscriber = null;
	public static int subscriberCount = 0;

	// Related to Async Thread management
	public ExecutorService executorServicePool;
	private static long lastAccessedTime = 0;
	private static long currentAccessTime = 0;

	// Debugging
	private static Logger logger = LoggerFactory.getLogger(RedisHTTPStreaming.class);

	/////////////////////////////////////////////////////////////////////////////

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		// For now: set up a simple configuration that logs on the console
		//PropertyConfigurator.configure("log4j.properties");		// where to place the properties file?
		BasicConfigurator.configure();
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

		++RedisHTTPStreaming.subscriberCount;			// increment the number of opened subscription channels
		logger.info("[initRedisSubscription] Initialized empty subscriber, with subscriberJedis = " + subscriberJedis);
		logger.info("[initRedisSubscription] pool = " + pool);
		logger.info("[initRedisSubscription] poolConfig = " + poolConfig);
	}

	// Currently unused - for future, if required
	private void unSubscribeChannel(final RedisSubscriber sub) {
		System.out.println(sub + "@[unSubscribeChannel] subscriberCount = " + subscriberCount + ", Subscription count = " + sub.getSubscribedChannels());
		System.out.println("[unSubscribeChannel] aidrSubscriber = " + sub);
		if (sub != null && sub.getSubscribedChannels() > 0) 
		{
			sub.unsubscribe();				// aidrSubscriber.unsubscribe(redisChannel);
			--RedisHTTPStreaming.subscriberCount;
			System.out.println(sub + "@[unSubscribeChannel] unsubscribed, Subscription count = " + sub.getSubscribedChannels());
		}
	}

	private void stopSubscription(final RedisSubscriber sub, final Jedis jedis) {
		System.out.println(sub + "@[stopSubscription] subscriberCount = " + subscriberCount + ", Subscription count = " + sub.getSubscribedChannels());
		System.out.println("[stopSubscription] aidrSubscriber = " + sub + ", jedis = " + jedis);
		if (sub != null && sub.getSubscribedChannels() > 0) {
			sub.unsubscribe();				// aidrSubscriber.unsubscribe(redisChannel);
			--RedisHTTPStreaming.subscriberCount;
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
					--RedisHTTPStreaming.subscriberCount;
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

	private boolean isSameSession(HttpServletRequest request) {

		HttpSession session = request.getSession(true);
		if (session.isNew()) {
			return false;
		}
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
		// response.setContentType("text/html");
		// response.setContentType("text/javascript");
		// response.setIntHeader("Refresh", 1);		// auto refresh web-page every 1 sec

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

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
			if (request.getParameter("stopSubscription") == null) {
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
			// Instead request to stop subscription - Not sure how to handle now with async threaded execution!
			if ((request.getParameter("stopSubscription") != null) && (request.getParameter("stopSubscription").equals("true"))) {
				try {
					logger.info("[doGet] In parameter: stopSubscription=true");
					//stopSubscription(aidrSubscriber);
					shutdownAndAwaitTermination(executorServicePool);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("[doGet] Exception occurred attempting stopSubscription in parameter=stopSubscription: " + e.toString());
					e.printStackTrace();
					System.exit(1);
				}
			}
		}
		logger.info("[doGet] Reached end-of-function...");
	}

	void shutdownAndAwaitTermination(ExecutorService threadPool) {
		threadPool.shutdown(); // Disable new tasks from being submitted
		try {
			// Wait a while for existing tasks to terminate
			if (!threadPool.awaitTermination(1, TimeUnit.SECONDS)) {
				threadPool.shutdownNow(); // Cancel currently executing tasks
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

		// rate control related 
		private static final int DEFAULT_SLEEP_TIME = 0;		// in msec
		private float messageRate = 0;							// default: <= 0 implies no rate control
		private int sleepTime = DEFAULT_SLEEP_TIME;

		// Share data structure between Jedis and Async threads
		// TODO: later versions for more functionality
		private List<String> messageList = Collections.synchronizedList(new ArrayList<String>());


		public RedisSubscriber(AsyncContext asyncContext, Jedis jedis, String channel, String callbackName) throws IOException {
			this.channel = channel;
			this.callbackName = callbackName;
			this.asyncContext = asyncContext;
			this.jedis = jedis;
			this.setRunFlag(true);		

			logger.info(this +"@[RedisSubscriber] Step 1: add asyc listener.");
			// Listen for errors and timeouts
			asyncContext.addListener(this);

			logger.info(this + "@[RedisSubscriber] Step 2: get output rate, if any");
			HttpServletRequest request = (HttpServletRequest) asyncContext.getRequest();
			if (request.getParameter("rate") != null) {
				messageRate = Float.parseFloat(request.getParameter("rate"));	// specified as messages/min (NOTE: upper-bound)
				if (messageRate > 0) {		// otherwise, use default rate
					sleepTime = Math.max(0, Math.round(60 * 1000 / messageRate));		// time to sleep between sends (in msecs)
				}
			}
			logger.info(this + "@[RedisSubscriber] Parameters received: crisisCode:" + this.channel
					+ ", callback = " + this.callbackName 
					+ ", count = " + this.messageRate);
			
		}

		@Override
		public void onMessage(String channel, String message) {
			// onMessage() asynchronously receives messages from the JedisPubSub channel 
			// Note: no explicit synchronization required for immutable objects
			// reset for every triggered event of receiving a new message.
			// Assign the messageObject response
			if (callbackName != null) {
				messageObjectResponse = callbackName + "(" + message + ")"; // with specified callback - JSONP
			}
			else {
				messageObjectResponse = message; 	// without callback - pure JSON
			}
			messageList.add(messageObjectResponse);		

			// Also log message for debugging purpose
			logger.info(this + "@[onMessage] Received Redis message to be sent to client: " + messageObjectResponse);
		}

		@Override
		public void onPMessage(String pattern, String channel, String message) {}

		@Override
		public void onPSubscribe(String pattern, int subscribedChannels) {}

		@Override
		public void onPUnsubscribe(String pattern, int subscribedChannels) {}

		@Override
		public void onSubscribe(String channel, int subscribedChannels) {}

		@Override
		public void onUnsubscribe(String channel, int subscribedChannels) {
			logger.info(this + "@[onUnsubscribe] We are in an interesting place...");
		}

		///////////////////////////////////
		// Now to implement Async methods
		///////////////////////////////////
		public boolean isThreadTimeout(long startTime) {
			if ((new Date().getTime() - startTime) > THREAD_TIMEOUT) {
				System.out.println(this + "@[isThreadTimeout] Exceeded Thread timeout = " + THREAD_TIMEOUT + "msec");
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
			//response.setHeader("Connection", "keep-alive");
			//response.setContentLength(messageObjectResponse == null ? 1024 : messageObjectResponse.length());		// dummy setting - required?
			PrintWriter responseWriter = null;
			try {
				responseWriter = response.getWriter();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error(this + "@[run] Error initializing PrintWriter", e);
				e.printStackTrace();
				setRunFlag(false);
			}
			/*
			ServletOutputStream responseStream = null;
			try {
				responseStream = response.getOutputStream();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				logger.info(this + "@[run] Error initializing output stream for sending data...");
				e1.printStackTrace();
				setRunFlag(false);		
			} 
			 */
			while (getRunFlag() && !isThreadTimeout(startTime)) {
				// Here we poll a non blocking resource for updates
				//System.out.println("[run] messageObjectResponse = " + messageObjectResponse);
				//if (messageObjectResponse != null) {
				if (!messageList.isEmpty()) {
					// There are updates, send these to the waiting client
					logger.info(this + "@[run] Received message from REDIS");

					if (!error && !timeout) {
						// Send updates response as JSON
						synchronized (messageList) {
							//final String jsonData = new Gson().toJson(messageObjectResponse != null ? messageObjectResponse : new String("{}"));
							Gson jsonObject = new GsonBuilder().serializeNulls()		//.disableHtmlEscaping()
									.serializeSpecialFloatingPointValues().setPrettyPrinting()
									.create();
							final String jsonData = jsonObject.toJson(messageList.get(messageList.size()-1) != null 
															? messageList.get(messageList.size()-1) : new String("{}"));
							//final String jsonData = new Gson().toJson(messageList.get(messageList.size()-1) != null 
							//						? messageList.get(messageList.size()-1) : new String("{}"));
							
							logger.info(this + "@[run] Sending JSON data: " + jsonData);
							response.setContentLength(jsonData.length());
							responseWriter.println(jsonData);
							responseWriter.flush();
							
							jsonObject = null;
							messageObjectResponse = null;
							messageList.remove(messageList.size()-1);	// remove the sent message from list	 
						}
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
					// messageObjectResponse = null --> no message received 
					// from REDIS. Wait for some more time before giving up.
					currentTime = new Date().getTime();
					long elapsed = currentTime - lastAccessedTime;
					if (elapsed > REDIS_CALLBACK_TIMEOUT) {
						System.out.println(this + "@[run::Timeout] Elapsed time = " + elapsed + ", exceeded REDIS timeout = " + REDIS_CALLBACK_TIMEOUT + "sec");
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

		// Currently unused - for future, if required
		public boolean isClientConnected(PrintWriter out) {
			// TODO: better way to detect client disconnects?
			if (out.checkError()) {
				return false;
			}
			return true;
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
