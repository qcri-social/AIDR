package qa.qcri.aidr.output.stream;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.ChunkedOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import qa.qcri.aidr.output.utils.AIDROutputConfig;
import qa.qcri.aidr.output.utils.JedisConnectionObject;
import qa.qcri.aidr.output.stream.RedisSubscriber;
import redis.clients.jedis.Jedis;

@Path("/crisis/stream/")
public class AsyncStream implements ServletContextListener {

	// Related to channel buffer management
	private static final String CHANNEL_REG_EX = "aidr_predict.*";
	private static final String CHANNEL_PREFIX_CODE = "aidr_predict.";

	private static final boolean rejectNullFlag = true;		

	private static final String redisHost = "localhost";		// Current assumption: REDIS running on same m/c
	private static final int redisPort = 6379;					

	// Jedis related
	private static JedisConnectionObject jedisConn;

	// Related to Async Thread management
	private static ExecutorService executorService = null;
	private static ArrayList<ChunkedOutput<String>> writerList = null;

	// Debugging
	private static Logger logger = LoggerFactory.getLogger(AsyncStream.class);

	/////////////////////////////////////////////////////////////////////////////

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		AIDROutputConfig configuration = new AIDROutputConfig();
		HashMap<String, String> configParams = configuration.getConfigProperties();

		if (configParams.get("logger").equalsIgnoreCase("log4j")) {
			// For now: set up a simple configuration that logs on the console
			// PropertyConfigurator.configure("log4j.properties");      
			// BasicConfigurator.configure();    // initialize log4j logging
		}
		if (configParams.get("logger").equalsIgnoreCase("slf4j")) {
			System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");	// set logging level for slf4j
		}

		// Now initialize shared jedis connection object and thread pool object
		jedisConn = new JedisConnectionObject(redisHost, redisPort);
		executorService = Executors.newCachedThreadPool();
		writerList = new ArrayList<ChunkedOutput<String>>();
		logger.info("Context Initialized, executorService: " + executorService);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		for (ChunkedOutput<String> w: writerList) {
			if (w != null && !w.isClosed()) {
				try {
					w.close();
				} catch (IOException e) {
					logger.info("Error trying to close ChunkedOutput writer");
				} finally {
					try {
						w.close();
					} catch (IOException e) {
						logger.info("Error trying to close ChunkedOutput writer");
					}
				}
			}
		}
		writerList.clear();
		logger.info("[contextDestroyed] executorService: " + executorService);
		if (executorService != null) shutdownAndAwaitTermination(executorService);
		logger.info("Context destroyed");
	}


	private boolean isPattern(String channelName) {
		// We consider only the wildcards * and ?
		if (channelName.contains("*") || channelName.contains("?")) {
			return true;
		}
		else {
			return false;
		}
	}

	public String setFullyQualifiedChannelName(final String channelPrefixCode, final String channelCode) {
		if (channelCode.startsWith(channelPrefixCode)) {
			return channelCode;                        // already fully qualified name
		}
		else {
			String channelName = channelPrefixCode.concat(channelCode);
			return channelName;
		}
	}


	// Create a subscription to specified REDIS channel: spawn a new thread
	private void subscribeToChannel(final ExecutorService exec, final RedisSubscriber sub, final SubscriptionDataObject subData) throws NullPointerException, RejectedExecutionException {
		logger.info("[subscribeToChannel] executorServicePool = " + exec);
		try {
			exec.execute(new Runnable() {
				public void run() {
					try {
						logger.info("[subscribeToChannel] patternSubscriptionFlag = " + subData.patternSubscriptionFlag);
						if (!subData.patternSubscriptionFlag) { 
							logger.info("[subscribeToChannel] Attempting subscription for " + redisHost + ":" + redisPort + "/" + subData.redisChannel);
							subData.subscriberJedis.subscribe(sub, subData.redisChannel);
						} 
						else {
							logger.info("[subscribeToChannel] Attempting pSubscription for " + redisHost + ":" + redisPort + "/" + subData.redisChannel);
							subData.subscriberJedis.psubscribe(sub, subData.redisChannel);
						}
					} catch (Exception e) {
						logger.error("[subscribeToChannel] AIDR Predict Channel Subscribing failed");
						sub.setRunFlag(false);			// force any orphaned subscriber thread to exit
						sub.stopSubscription(jedisConn, subData);
					} finally {
						try {
							sub.stopSubscription(jedisConn, subData);
						} catch (Exception e) {
							logger.error("[subscribeToChannel] Exception occurred attempting stopSubscription: " + e.toString());
							e.printStackTrace();
							System.exit(1);
						}
					}
				}
			});
		} catch (RejectedExecutionException|NullPointerException e) {
			logger.info("[doGet] Fatal error executing async thread! Terminating.");
		}
	}

	/**
	 * 
	 * @param callbackName  JSONP callback name
	 * @param count  number of buffered messages to fetch
	 * @return returns the 'count' number of buffered messages from requested channel as jsonp data 
	 * @throws IOException 
	 */
	@GET
	@Path("/channel/{crisisCode}")
	@Produces(MediaType.APPLICATION_JSON)
	public ChunkedOutput<String> streamChunkedResponse(
			@PathParam("crisisCode") String channelCode,
			@QueryParam("callback") final String callbackName,
			@DefaultValue("-1") @QueryParam("rate") Float rate,
			@DefaultValue("0") @QueryParam("duration") String duration) throws IOException {

		final ChunkedOutput<String> responseWriter = new ChunkedOutput<String>(String.class);
		writerList.add(responseWriter);

		if (channelCode != null) {
			// TODO: Handle client refresh of web-page in same session				
			if (jedisConn != null) {
				Jedis subscriberJedis = jedisConn.getJedisResource();

				// Get callback function name, if any
				String channel = setFullyQualifiedChannelName(CHANNEL_PREFIX_CODE, channelCode);

				SubscriptionDataObject subscriptionData = new SubscriptionDataObject();
				subscriptionData.rejectNullFlag = rejectNullFlag;
				subscriptionData.jedisConn = jedisConn;
				subscriptionData.subscriberJedis = subscriberJedis;
				subscriptionData.redisChannel = channel;
				subscriptionData.patternSubscriptionFlag = isPattern(channelCode);
				subscriptionData.callbackName = callbackName;
				subscriptionData.rate = rate;
				subscriptionData.duration = duration;

				RedisSubscriber aidrSubscriber = new RedisSubscriber(subscriberJedis, responseWriter, subscriptionData);
				try {
					logger.info("subscriberJedis = " + subscriberJedis + ", aidrSubscriber = " + aidrSubscriber);
					if (null == executorService) {
						executorService = Executors.newCachedThreadPool();                // max number of threads
					}
					subscribeToChannel(executorService, aidrSubscriber, subscriptionData);
					jedisConn.setJedisSubscription(subscriberJedis, subscriptionData.patternSubscriptionFlag);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error("[streamChunkedResponse] Fatal exception occurred attempting subscription: " + e.toString());
					e.printStackTrace();
					System.exit(1);
				}
				logger.info("[streamChunkedResponse] Attempting async response thread");
				try {
					executorService.execute(aidrSubscriber);	
				} catch (RejectedExecutionException|NullPointerException e) {
					logger.info("[doGet] Fatal error executing async thread! Terminating.");
				}
			}
		} 
		else {
			// No crisisCode provided...
			StringBuilder errorMessageString = new StringBuilder();
			if (callbackName != null) {
				errorMessageString.append(callbackName).append("(");
			}
			errorMessageString.append("{\"crisisCode\":\"null\"");
			errorMessageString.append("\"streaming status\":\"error\"}");
			if (callbackName != null) {
				errorMessageString.append(")");
			}
			responseWriter.write(errorMessageString.toString());
		}
		logger.info("[streamChunkedResponse] Reached end of function");

		return responseWriter;
	}

	// cleanup all threads 
	void shutdownAndAwaitTermination(ExecutorService threadPool) {
		logger.info("threadPool: " + threadPool);
		threadPool.shutdown(); // Disable new tasks from being submitted
		try {
			// Wait a while for existing tasks to terminate
			if (!threadPool.awaitTermination(5, TimeUnit.SECONDS)) {
				threadPool.shutdownNow();                         // Cancel currently executing tasks
				// Wait a while for tasks to respond to being cancelled
				if (!threadPool.awaitTermination(5, TimeUnit.SECONDS))
					logger.error("[shutdownAndAwaitTermination] Pool did not terminate");
			}
		} catch (InterruptedException ie) {
			// (Re-)Cancel if current thread also interrupted
			threadPool.shutdownNow();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
	}
}
