/** 
 * @author Koushik Sinha
 * Last modified: 06/01/2014
 * 
 * The ChannelBufferManager class implements the following channel buffer management functionalities 
 * for the /getLast REST API: 
 * 		a) Subscribe to all CHANNEL_PREFIX_STRING channels in REDIS using pattern-based subscription.
 * 		b) Monitor the REDIS pubsub system for new or removed channels.
 * 		c) If new channel found, then create a new ChannelBuffer for it.
 * 		d) If no messages received on an existing channel for a certain duration then delete the channel.
 * 		e) Return an ArrayList<String> of messages for a specific channel and messageCount value. 
 *
 */

package qa.qcri.aidr.output.getdata;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Future;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.common.redis.LoadShedder;
import qa.qcri.aidr.output.utils.AIDROutputConfig;
import qa.qcri.aidr.output.utils.JedisConnectionObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.apache.log4j.Logger;


public class ChannelBufferManager {

	public static final int NO_DATA_TIMEOUT = 48 * 60 * 60 * 1000;		// when to delete a channel buffer
	public static final int CHECK_INTERVAL = NO_DATA_TIMEOUT;
	public static final int CHECK_CHANNEL_PUBLIC_INTERVAL = 5 * 60 * 1000;

	private static int PERSISTER_LOAD_LIMIT;
	private static int PERSISTER_LOAD_CHECK_INTERVAL_MINUTES;

	private static Logger logger = Logger.getLogger(ChannelBufferManager.class);
	private static ErrorLog elog = new ErrorLog();

	// Thread related
	private static ExecutorService executorServicePool = null;
	private volatile static boolean shutdownFlag = false;

	// Redis connection related
	public static String redisHost = "localhost";	// Current assumption: REDIS running on same m/c
	public static int redisPort = 6379;	

	// Jedis related
	public static JedisConnectionObject jedisConn = null;		// we need only a single instance of JedisConnectionObject running in background
	public static Jedis subscriberJedis = null;
	public static RedisSubscriber aidrSubscriber = null;

	// Runtime related
	private static boolean isConnected = false;
	private static boolean isSubscribed =false;
	private volatile static long lastCheckedTime = 0; 
	private volatile static long lastPublicFlagCheckedTime = 0;
	private static int bufferSize = -1;

	// Channel Buffering Algorithm related
	public static final String CHANNEL_PREFIX_STRING = "aidr_predict.";
	public static ConcurrentHashMap<String, ChannelBuffer> subscribedChannels;

	// DB access related
	//private static DatabaseInterface dbController = null;
	private static String managerMainUrl = "http://localhost:8080/AIDRFetchManager";

	private static ConcurrentHashMap<String, LoadShedder> redisLoadShedder = null;

	//////////////////////////////////////////
	// ********* Method definitions *********
	//////////////////////////////////////////

	// Constructor
	public ChannelBufferManager() {}

	public void initiateChannelBufferManager(final String channelRegEx) {
		AIDROutputConfig configuration = new AIDROutputConfig();
		HashMap<String, String> configParams = configuration.getConfigProperties();

		redisLoadShedder = new ConcurrentHashMap<String, LoadShedder>(20);

		redisHost = configParams.get("host");
		redisPort = Integer.parseInt(configParams.get("port"));
		PERSISTER_LOAD_CHECK_INTERVAL_MINUTES = Integer.parseInt(configParams.get("PERSISTER_LOAD_CHECK_INTERVAL"));
		PERSISTER_LOAD_LIMIT = Integer.parseInt(configParams.get("PERSISTER_LOAD_LIMIT"));

		managerMainUrl = configParams.get("managerUrl");
		logger.info("Initializing channel buffer manager.");
		System.out.println("[ChannelBufferManager] Initializing channel buffer manager.");

		bufferSize = -1;
		executorServicePool = Executors.newCachedThreadPool();	//Executors.newFixedThreadPool(10);		// max number of threads
		logger.info("Created thread pool: " + executorServicePool);

		jedisConn = new JedisConnectionObject(redisHost, redisPort);
		try {
			subscriberJedis = jedisConn.getJedisResource();
			if (subscriberJedis != null) isConnected = true;
		} catch (JedisConnectionException e) {
			subscriberJedis = null;
			isConnected = false;
			logger.error("Fatal error! Couldn't establish connection to REDIS!");
			logger.error(elog.toStringException(e));
			e.printStackTrace();
		}
		if (isConnected) {
			aidrSubscriber = new RedisSubscriber();
			jedisConn.setJedisSubscription(subscriberJedis, true);		// we will be using pattern-based subscription
			logger.info("Created new Jedis connection: " + subscriberJedis);
			try {
				subscribeToChannel(channelRegEx);
				isSubscribed = true;
				aidrSubscriber.setChannelName(channelRegEx);
				logger.info("Created pattern subscription");
			} catch (Exception e) {
				isSubscribed = false;
				logger.error("Fatal exception occurred attempting subscription: " + e.toString());
				logger.error(elog.toStringException(e));
			}
			if (isSubscribed) {
				subscribedChannels = new ConcurrentHashMap<String,ChannelBuffer>(20);
				logger.debug("Created HashMap");
				loadBuffersFromDisk();
			}
		}

	}
	
	public ExecutorService getExecutorServicePool() {
		return executorServicePool;
	}
	
	public void initiateChannelBufferManager(final int bufferSize, final String channelRegEx) {
		initiateChannelBufferManager(channelRegEx);					// call default constructor
		this.bufferSize = bufferSize;		// set buffer size
	}

	public void manageChannelBuffersWrapper(final String subscriptionPattern, final String channelName, 
													final String receivedMessage) {
		manageChannelBuffers(subscriptionPattern, channelName, receivedMessage);
	}

	// Does all the essential work:
	// 1. Searches received message to see if channel name present.
	// 2. If channel present then simply adds receivedMessage to that channel.
	// 3. Else, first calls createChannelBuffer() and then executes step (2).
	// 4. Deletes channelName and channel buffer if channelName not seen for TIMEOUT duration.
	public void manageChannelBuffers(final String subscriptionPattern, final String channelName, 
			final String receivedMessage) {
		if (null == channelName) {
			logger.error("Something terribly wrong! Fatal error in: " + channelName);
			//System.exit(1);
		}
		if (isChannelPresent(channelName)) {
			// Add to appropriate circular buffer
			addMessageToChannelBuffer(channelName, receivedMessage);
		}
		else {
			//First create a new circular buffer and then add to that buffer
			createChannelQueue(channelName);
			addMessageToChannelBuffer(channelName, receivedMessage);
			logger.info("Created new channel: " + channelName);
			System.out.println("[manageChannelBuffers] Created new channel: " + channelName);
		}
		long currentTime = new Date().getTime();

		// Periodically check if channel's isPubliclyListed flag has changed
		if (currentTime - lastPublicFlagCheckedTime > CHECK_CHANNEL_PUBLIC_INTERVAL) {
			logger.info("Invoked by message on channel: " + channelName + ". Periodic check for publiclyListed flag of channels");
			Map<String, Boolean> statusFlags = getAllRunningCollections();	
			logger.info("Retrieved list from aidr-manager, size = " + statusFlags.size());
			if (statusFlags != null) {
				try {
					for (String cName: statusFlags.keySet()) {
						if (subscribedChannels.containsKey(CHANNEL_PREFIX_STRING+cName)) {
							ChannelBuffer cb = subscribedChannels.get(CHANNEL_PREFIX_STRING+cName);
							cb.setPubliclyListed(statusFlags.get(cName));
							logger.info("For channel: " + cb.getChannelName() + ", isChannelPublic = " + cb.getPubliclyListed() + ", msg count = " + cb.getCurrentMsgCount() + ", last msg timestamp = " + new Date(cb.getLastAddTime()));
						}
					}
					statusFlags.clear();
				} catch (Exception e) {
					logger.error(elog.toStringException(e));
				}
			}
			lastPublicFlagCheckedTime = new Date().getTime();

		}

		// Periodically check if any channel is down - if so, delete
		if (currentTime - lastCheckedTime > CHECK_INTERVAL) {
			logger.info("Invoked by message on channel: " + channelName + ". Periodic check for inactive channels - delete if any.");
			for (String channel: subscribedChannels.keySet()) {
				ChannelBuffer temp = subscribedChannels.get(channel);
				if ((currentTime - temp.getLastAddTime()) > NO_DATA_TIMEOUT) {
					logger.info("Deleting inactive channel = " + channelName);
					logger.info("Stat on deleted channel " + channelName + ": msg count = " + temp.getCurrentMsgCount() + ", last msg timestamp = " + new Date(temp.getLastAddTime()));
					deleteChannelBuffer(temp.getChannelName());
				}
			}
			lastCheckedTime = new Date().getTime();
		}
	}

	public void addMessageToChannelBuffer(final String channelName, final String msg) {
		try {
			subscribedChannels.get(channelName).addMessage(msg);
		} catch (Exception e) {
			logger.error("Unable to add message to buffer for channel: " + channelName);
		}
	}

	public List<String> getLastMessages(String channelName, int msgCount) {
		if (isChannelPresent(channelName)) {
			ChannelBuffer cb = subscribedChannels.get(channelName);
			// Note: Ideally, we should have used the method call cb.getMessages(msgCount)
			// However, we get all messages in buffer since we do not know how many will 
			// eventually be valid, due to rejectNullFlag setting in caller. The filtering
			// to send msgCount number of messages will happen in the caller. 
			try {
				return cb != null ? cb.getMessages(Math.min(2 * msgCount, ChannelBuffer.MAX_FETCH_SIZE)) : null;
			} catch (Exception e) {
				logger.error("Failed to retrieve messages from buffer for channel: " + channelName);
			}
		}
		return null;
	}

	// Returns true if channelName present in list of channels
	// TODO: define the appropriate collections data structure - HashMap, HashSet, ArrayList? 
	public boolean isChannelPresent(String channelName) {
		try {
			return (subscribedChannels != null) ? subscribedChannels.containsKey(channelName) : false;
		} catch (Exception e) {
			logger.error("Unable to check if channel present: " + channelName);
			return false;
		}
	}

	// channelName = fully qualified channel name as present in REDIS pubsub system
	public void createChannelQueue(final String channelName) {
		try {
			ChannelBuffer cb = new ChannelBuffer(channelName);
			if (bufferSize <= 0)
				cb.createChannelBuffer();				// use default buffer size
			else
				cb.createChannelBuffer(bufferSize);		// use specified buffer size
			subscribedChannels.put(channelName, cb);
			cb.setPubliclyListed(getChannelPublicStatus(channelName));
			logger.info("Created channel buffer for channel: " + channelName + ", public = " + cb.getPubliclyListed());
		} catch (Exception e) {
			logger.error("Unable to create buffer for channel: " + channelName);
		}
	}


	public void deleteChannelBuffer(final String channelName) {
		try {
			ChannelBuffer cb = subscribedChannels.get(channelName);
			cb.deleteBuffer();
			subscribedChannels.remove(channelName);
			logger.info("Deleted channel buffer: " + channelName);
		} catch (Exception e) {
			logger.error("Unable to delete buffer for channel: " + channelName);
		}
	}

	public void deleteAllChannelBuffers() {
		try {
			if (subscribedChannels != null) {
				logger.info("Deleting buffers for currently subscribed list of channels: " + subscribedChannels.keySet());
				for (String channel: subscribedChannels.keySet()) {
					subscribedChannels.get(channel).deleteBuffer();
					subscribedChannels.remove(channel);
				}
				subscribedChannels.clear();
			}
		} catch (Exception e) {
			logger.error("Unable to delete all channel buffers");
		}
	}

	/** 
	 * @return A set of fully qualified channel names, null if none found
	 */
	public Set<String> getActiveChannelsList() {
		try {
			final Set<String> channelSet = (subscribedChannels != null) ? subscribedChannels.keySet() : null;
			return channelSet.isEmpty() ? null : channelSet;
		} catch (Exception e) {
			logger.error("Unable to fetch list of active channels");
			return null;
		}
	}

	/** 
	 * @return A set of only the channel codes - stripped of CHANNEL_PREFIX_STRING, null if none found
	 */
	public Set<String> getActiveChannelCodes() {
		try {
			Set<String> channelCodeSet = new HashSet<String>();
			final Set<String> tempSet = (subscribedChannels != null) ? subscribedChannels.keySet() : null;
			if (tempSet != null) {
				for (String s:tempSet) {
					channelCodeSet.add(s.substring(CHANNEL_PREFIX_STRING.length()));
				}
				return channelCodeSet.isEmpty() ? null : channelCodeSet;
			}
		} catch (Exception e) {
			logger.error("Unable to get active channel codes");
		}
		return null;
	}

	/**
	 * Calls the manager's public collection REST API.
	 * @return publiclyListed value of given channel name
	 */
	@SuppressWarnings("unchecked")
	public Boolean getChannelPublicStatus(String channelName) {
		String channelCode = parseChannelName(channelName);
		Response clientResponse = null;
		Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
		try {
			WebTarget webResource = client.target(managerMainUrl 
					+ "/public/collection/getChannelPublicFlagStatus?channelCode=" + channelCode);

			clientResponse = webResource.request(MediaType.APPLICATION_JSON).get();
			Map<String, Boolean> collectionMap = new HashMap<String, Boolean>();

			if (clientResponse.getStatus() == 200) {
				//convert JSON string to Map
				collectionMap = clientResponse.readEntity(Map.class);
				logger.info("Channel info received from manager: " + collectionMap);
				if (collectionMap != null) {
					return collectionMap.get(channelCode);
				}
			} else {
				logger.warn("Couldn't contact AIDRFetchManager for publiclyListed status, channel: " + channelName);
			}
		} catch (Exception e) {
			logger.error("Error in querying manager for running collections: " + clientResponse);
			logger.error(elog.toStringException(e));
		}
		return true;		// Question: should default be true or false?
	}


	/**
	 * Calls the manager's public collection REST API.
	 * @return Map<String, Boolean> containing publiclyListed value for each channel code
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Boolean> getAllRunningCollections() {
		Map<String, Boolean> collectionList = null;
		Response clientResponse = null;
		Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
		try {
			WebTarget webResource = client.target(managerMainUrl 
					+ "/public/collection/getPublicFlagStatus");

			clientResponse = webResource.request(MediaType.APPLICATION_JSON).get();
			Map<String, Boolean> collectionMap = new HashMap<String, Boolean>();
			logger.info("Response from manager: " + clientResponse);
			//convert JSON string to Map
			if (clientResponse.getStatus() == 200) {
				collectionMap = clientResponse.readEntity(Map.class);
				logger.info("Received from manager: " + collectionMap);
				return collectionMap;
			} else {
				logger.warn("Couldn't contact AIDRFetchManager for publiclyListed status of running collections");
			}
		} catch (Exception e) {
			logger.error("Error in querying manager for running collections: " + clientResponse);
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	/**
	 * @return List of latest tweets seen on all active channels, one tweet/channel, null if none found
	 */
	public List<String> getLatestFromAllChannels(final int msgCount) {
		List<String>dataSet = new ArrayList<String>();

		final int EXTRA = 3;		
		try {
			if (subscribedChannels != null) {
				for (ChannelBuffer cb: subscribedChannels.values()) {
					if (cb.getPubliclyListed()) {
						//logger.info("Looking at buffer: " + cb.getChannelName());
						//long startTime = System.currentTimeMillis();
						List<String> fetchedList = cb.getMessages(msgCount+EXTRA);
						//logger.info("Total time taken to retrieve from channel " + cb.getChannelName() + " = " + (System.currentTimeMillis() - startTime));
						if (fetchedList != null) {
							dataSet.addAll(fetchedList);		
							//logger.info("Channel: " + cb.getChannelName() + ", fetched size = " + fetchedList.size());
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("Unable to get list of latest messages across channels");
		}
		return (dataSet.isEmpty() ? null : dataSet);
	}

	public String parseChannelName(String channelName) {
		try {
			String[] strs = channelName.split(CHANNEL_PREFIX_STRING);
			return (strs != null) ? strs[1] : channelName;
		} catch (Exception e) {
			logger.error("Failed to parse channel name for channel: " + channelName);
		}
		return null;
	}


	private void subscribeToChannel(final String channelRegEx) throws Exception {
		Future redisThread = executorServicePool.submit(new Runnable() {
			public void run() {
				Thread.currentThread().setName("ChannelBufferManager Redis subscription Thread");
				logger.info("New thread <" +  Thread.currentThread().getName() + "> created for subscribing to redis channel: " + channelRegEx);
				try {
					// Execute the blocking REDIS subscription call
					subscriberJedis.psubscribe(aidrSubscriber, channelRegEx);
				} catch (JedisConnectionException e) {
					logger.error("AIDR Predict Channel pSubscribing failed for channel = " + channelRegEx);
					System.out.println("[subscribeToChannel] AIDR Predict Channel pSubscribing failed for channel = " + channelRegEx);
					e.printStackTrace();
					stopSubscription();
					Thread.currentThread().interrupt();
				} finally {
					try {
						stopSubscription();
					} catch (Exception e) {
						logger.error(channelRegEx  + ": Exception occurred attempting stopSubscription: " + e.toString());
						logger.error(elog.toStringException(e));
					}
				}
				Thread.currentThread().interrupt();
				logger.info("Exiting thread: " + Thread.currentThread().getName());
			}
		});
	}

	private synchronized void stopSubscription() {
		logger.info("Stopsubscription attempt for channel: " + aidrSubscriber.getChannelName());
		try {
			if (aidrSubscriber != null && aidrSubscriber.getSubscribedChannels() > 0) {
				aidrSubscriber.punsubscribe();				
			}
		} catch (JedisConnectionException e) {
			logger.error("Connection to REDIS seems to be lost!");
			logger.error(elog.toStringException(e));
		}
		try {
			if (jedisConn != null && aidrSubscriber != null) { 
				jedisConn.returnJedis(subscriberJedis);
				subscriberJedis = null;
				logger.info("Stopsubscription completed...");
				System.out.println("[stopSubscription] Stopsubscription completed...");
			}
		} catch (Exception e) {
			logger.error("Failed to return Jedis resource");
			logger.error(elog.toStringException(e));
		}
		logger.info("isShutDown initiated = " + shutdownFlag);
		if (!shutdownFlag) {
			attemptResubscription();
		}

	}

	private void attemptResubscription() {
		int attempts = 0;
		boolean isSetup = false;
		final int MAX_RECONNECT_ATTEMPTS = 10;
		while (!isSetup && attempts < MAX_RECONNECT_ATTEMPTS && !shutdownFlag) {
			try {
				Thread.sleep(60000);
				logger.info("Attempting to resubscribe to REDIS, with jedisConn = " + jedisConn);
				if (jedisConn != null) {
					isSetup = setupRedisConnection(CHANNEL_PREFIX_STRING+"*");
				} else {
					jedisConn = new JedisConnectionObject(redisHost, redisPort);
					isSetup = setupRedisConnection(CHANNEL_PREFIX_STRING+"*");	
				}
			} catch (Exception e) {
				isSubscribed = false;
				isSetup = false;
				logger.error("Fatal exception occurred attempting subscription: " + e.toString());
				logger.error(elog.toStringException(e));
				++attempts;
			}
		}
	}

	private boolean setupRedisConnection(final String channelRegEx) {
		try {
			isConnected = false;
			if (null == subscriberJedis) subscriberJedis = jedisConn.getJedisResource();
			if (subscriberJedis != null) isConnected = true;
		} catch (JedisConnectionException e) {
			subscriberJedis = null;
			isConnected = false;
			logger.error("Fatal error! Couldn't establish connection to REDIS!");
			logger.error(elog.toStringException(e));
		}
		if (isConnected) {
			aidrSubscriber = new RedisSubscriber();
			jedisConn.setJedisSubscription(subscriberJedis, true);		// we will be using pattern-based subscription
			logger.info("Created new Jedis connection: " + subscriberJedis);
			try {
				subscribeToChannel(channelRegEx);
				isSubscribed = true;
				aidrSubscriber.setChannelName(channelRegEx);
				logger.info("Resubscribed with pattern subscription: " + channelRegEx);
				return true;
			} catch (Exception e) {
				isSubscribed = false;
				logger.error("Fatal exception occurred attempting subscription: " + e.toString());
				logger.error(elog.toStringException(e));
			}
		}
		return false;
	}


	public void close() {
		shutdownFlag = true;
		dumpBuffersToDisk();
		stopSubscription();
		deleteAllChannelBuffers();
		shutdownAndAwaitTermination();
		logger.info("All done, fetch service has been shutdown...");
		System.out.println("[close] All done, fetch service has been shutdown...");
	}

	/**
	 * Dumps all buffered data to disk - one file per collection
	 */
	private void dumpBuffersToDisk() {
		// TODO:
	}

	/**
	 * On restart loads all dumped channel data from disk
	 * Requires creation of channelBuffers where not present
	 */
	private void loadBuffersFromDisk() {
		// TODO: 
	}

	// cleanup all threads 
	void shutdownAndAwaitTermination() {
		int attempts = 0;
		executorServicePool.shutdown(); // Disable new tasks from being submitted
		while (!executorServicePool.isTerminated() && attempts < 3) {
			try {
				// Wait a while for existing tasks to terminate
				if (!executorServicePool.awaitTermination(5, TimeUnit.SECONDS)) {
					executorServicePool.shutdownNow();                         // Cancel currently executing tasks
					// Wait a while for tasks to respond to being cancelled
					if (!executorServicePool.awaitTermination(5, TimeUnit.SECONDS))
						logger.error("Executor Thread Pool did not terminate");
				} else {
					logger.info("All tasks completed post service shutdown");
				}
			} catch (InterruptedException e) {
				// (Re-)Cancel if current thread also interrupted
				executorServicePool.shutdownNow();
				// Preserve interrupt status
				Thread.currentThread().interrupt();
			} finally {
				executorServicePool.shutdownNow();
			}
			++attempts;
			if (!executorServicePool.isTerminated()) {
				logger.warn("Warning! Some threads not shutdown still. Trying again, attempt = " + attempts);
			}
		}
	}

	////////////////////////////////////////////////////
	private class RedisSubscriber extends JedisPubSub {

		private String channelName = null;

		public void setChannelName(String channelName) {
			this.channelName = channelName;
		}

		public String getChannelName() {
			return this.channelName;
		}

		@Override
		public void onMessage(String channel, String message) {}

		@Override
		public void onPMessage(String pattern, String channel, String message) {
			try {
				if (!redisLoadShedder.containsKey(channel)) {
					redisLoadShedder.put(channel, new LoadShedder(PERSISTER_LOAD_LIMIT, PERSISTER_LOAD_CHECK_INTERVAL_MINUTES, true));
					logger.info("Created new redis load shedder for channel: " + channel);
				}
				if (redisLoadShedder.get(channel).canProcess(channel)) {
					manageChannelBuffersWrapper(pattern, channel, message);
				} 
			} catch (Exception e) {
				logger.error("Exception occurred, redisLoadShedder = " + redisLoadShedder + ", channel status: " + redisLoadShedder.containsKey(channel));
				logger.error(elog.toStringException(e));
			}
		}

		@Override
		public void onSubscribe(String channel, int subscribedChannels) {
			logger.info("Subscribed to channel:" + channel);
		}

		@Override
		public void onUnsubscribe(String channel, int subscribedChannels) {
			logger.info("Unsubscribed from channel:" + channel);
		}

		@Override
		public void onPUnsubscribe(String pattern, int subscribedChannels) {
			logger.info("Unsubscribed from channel pattern:" + pattern + ", shutdownFlag = " + shutdownFlag);
		}

		@Override
		public void onPSubscribe(String pattern, int subscribedChannels) {
			logger.info("Subscribed to channel pattern:" + pattern);
		}
	}

}
