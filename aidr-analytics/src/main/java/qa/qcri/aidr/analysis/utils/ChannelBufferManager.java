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

package qa.qcri.aidr.analysis.utils;



import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import qa.qcri.aidr.analysis.stat.ConfDataMapRecord;
import qa.qcri.aidr.analysis.stat.MapRecord;
import qa.qcri.aidr.analysis.stat.TagDataMapRecord;
import qa.qcri.aidr.common.code.Configurator;
import qa.qcri.aidr.common.filter.ClassifiedFilteredTweet;
import qa.qcri.aidr.common.filter.NominalLabel;
import qa.qcri.aidr.common.redis.LoadShedder;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class ChannelBufferManager {
	//private static OutputConfigurator configProperties = OutputConfigurator.getInstance();
	public static final int NO_DATA_TIMEOUT = 48 * 60 * 60 * 1000;		// when to delete a channel buffer
	public static final int CHECK_INTERVAL = NO_DATA_TIMEOUT;
	public static final int CHECK_CHANNEL_PUBLIC_INTERVAL = 5 * 60 * 1000;

	private static int PERSISTER_LOAD_LIMIT;
	private static int PERSISTER_LOAD_CHECK_INTERVAL_MINUTES;

	private static Logger logger = Logger.getLogger(ChannelBufferManager.class);

	// Thread related
	private static ExecutorService executorServicePool = null;
	private volatile static boolean shutdownFlag = false;

	// Redis connection related
	public static String redisHost; //= configProperties.getProperty(OutputConfigurationProperty.REDIS_HOST);
	public static int redisPort; 	//= Integer.valueOf(configProperties.getProperty(OutputConfigurationProperty.REDIS_PORT));

	// Jedis related
	public static JedisConnectionObject jedisConn = null;		// we need only a single instance of JedisConnectionObject running in background
	public static Jedis subscriberJedis = null;
	public static RedisSubscriber aidrSubscriber = null;

	// Runtime related
	private static boolean isConnected = false;
	private static boolean isSubscribed =false;

	private static int bufferSize = -1;

	// Channel Buffering Algorithm related
	public static String CHANNEL_PREFIX_STRING; //= configProperties.getProperty(OutputConfigurationProperty.TAGGER_CHANNEL_BASENAME)+".";
	public static Set<String> subscribedChannels;

	private static ConcurrentHashMap<String, LoadShedder> redisLoadShedder = null;

	private static ConcurrentHashMap<CounterKey, Object> tagDataMap = null;
	private static ConcurrentHashMap<CounterKey, Object> confDataMap = null;
	private static ConcurrentHashMap<String, Long> channelMap = null;
	private List<Long> granularityList = null;
	private long lastTagDataCheckedTime = 0;
	private long lastConfDataCheckedTime = 0;
	//////////////////////////////////////////
	// ********* Method definitions *********
	//////////////////////////////////////////

	// Constructor
	public ChannelBufferManager() {}

	public void initiateChannelBufferManager(final String channelRegEx) {
		Configurator configurator = OutputConfigurator.getInstance();
		redisLoadShedder = new ConcurrentHashMap<String, LoadShedder>();
		redisHost = configurator.getProperty(OutputConfigurationProperty.REDIS_HOST);
		redisPort = Integer.parseInt(configurator.getProperty(OutputConfigurationProperty.REDIS_PORT));
		CHANNEL_PREFIX_STRING = configurator.getProperty(OutputConfigurationProperty.TAGGER_CHANNEL_BASENAME)+".";
		PERSISTER_LOAD_CHECK_INTERVAL_MINUTES = Integer.parseInt(configurator.getProperty(OutputConfigurationProperty.PERSISTER_LOAD_CHECK_INTERVAL_MINUTES));
		PERSISTER_LOAD_LIMIT = Integer.parseInt(configurator.getProperty(OutputConfigurationProperty.PERSISTER_LOAD_LIMIT));

		AnalyticsConfigurator analyticsConfigurator = AnalyticsConfigurator.getInstance();
		granularityList = analyticsConfigurator .getGranularities();

		tagDataMap = new ConcurrentHashMap<CounterKey, Object>();
		confDataMap = new ConcurrentHashMap<CounterKey, Object>();
		channelMap = new ConcurrentHashMap<String, Long>();

		logger.info("Initializing channel buffer manager.");
		System.out.println("[ChannelBufferManager] Initializing channel buffer manager with values: <" + redisHost + ", " + redisPort 
				+ ", " + PERSISTER_LOAD_CHECK_INTERVAL_MINUTES + ", " + PERSISTER_LOAD_LIMIT + ">");

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
			logger.error("Fatal error! Couldn't establish connection to REDIS!", e);
			AnalyticsErrorLog.sendErrorMail("Redis", e.getMessage());
		}
		if (isConnected) {
			aidrSubscriber = new RedisSubscriber();
			jedisConn.setJedisSubscription(subscriberJedis, true);		// we will be using pattern-based subscription
			logger.info("Created new Jedis connection: " + subscriberJedis);
			try {
				subscribeToChannel(channelRegEx);
				isSubscribed = true;
				aidrSubscriber.setChannelName(channelRegEx);
				logger.info("Created pattern subscription for pattern: " + channelRegEx);
			} catch (Exception e) {
				isSubscribed = false;
				logger.error("Fatal exception occurred attempting subscription: " + e.toString(), e);
				AnalyticsErrorLog.sendErrorMail("Redis", e.getMessage());
			}
			if (isSubscribed) {
				subscribedChannels = new HashSet<String>();
			}
		}

	}

	public static ConcurrentHashMap<CounterKey, Object> getConfDataMap() {
		return confDataMap;
	}

	public static void setConfDataMap(ConcurrentHashMap<CounterKey, Object> confDataMap) {
		ChannelBufferManager.confDataMap = confDataMap;
	}

	public static ConcurrentHashMap<String, Long> getChannelMap() {
		return channelMap;
	}

	public static void setChannelMap(ConcurrentHashMap<String, Long> channelMap) {
		ChannelBufferManager.channelMap = channelMap;
	}

	public static ConcurrentHashMap<CounterKey, Object> getTagDataMap() {
		return tagDataMap;
	}

	public static void setTagDataMap(ConcurrentHashMap<CounterKey, Object> tagDataMap) {
		ChannelBufferManager.tagDataMap = tagDataMap;
	}

	public ExecutorService getExecutorServicePool() {
		return executorServicePool;
	}

	public void initiateChannelBufferManager(final int bufferSize, final String channelRegEx) {
		initiateChannelBufferManager(channelRegEx);					// call default constructor
		this.bufferSize = bufferSize;		// set buffer size
	}


	/**
	 * This method is the 'producer' - producing statistics data to be written
	 * to the respective tables
	 * 
	 * @param subscriptionPattern
	 *            REDIS pattern that Jedis is subscribed to
	 * @param channelName
	 *  		  Name of the channel on which message received from REDIS
	 * @param receivedMessage
	 *            Received message for the given channelName
	 */

	public void manageChannelBuffers(final String subscriptionPattern, final String channelName, final String receivedMessage) {
		if (null == channelName) {
			logger.error("Something terribly wrong! Fatal error in: " + channelName);
		} else {
			if (!isChannelPresent(channelName)) {
				System.out.println("New collection/channel found: " + channelName);
				subscribedChannels.add(channelName);
			}
			try {
				ClassifiedFilteredTweet classifiedTweet = new ClassifiedFilteredTweet().deserialize(receivedMessage);
				if (classifiedTweet != null && classifiedTweet.getNominalLabels() != null && !classifiedTweet.getNominalLabels().isEmpty()) {
					channelMap.putIfAbsent(classifiedTweet.getCrisisCode(), System.currentTimeMillis());
					//System.out.println("Found a valid classified tweet for collection: " + classifiedTweet.getCrisisCode() + ", tweet = " + receivedMessage);
					for (NominalLabel nb : classifiedTweet.getNominalLabels()) {
						if (nb.attribute_code != null && nb.label_code != null) {
							CounterKey tagDataKey = new CounterKey(classifiedTweet.getCrisisCode(), nb.attribute_code, nb.label_code);
							CounterKey confDataKey = new ConfCounterKey(classifiedTweet.getCrisisCode(), nb.attribute_code, nb.label_code, getBinNumber(nb.confidence));

							if (tagDataMap.containsKey(tagDataKey)) {
								TagDataMapRecord t = (TagDataMapRecord) tagDataMap.get(tagDataKey);
								t.incrementAllCounts();
								tagDataMap.put(tagDataKey, t);
								//System.out.println("Updated Tag map entry with key: " + tagDataKey.toString() + " value = " + tagDataMap.get(tagDataKey).toString());
							} else {
								TagDataMapRecord t = new TagDataMapRecord(granularityList);
								tagDataMap.put(tagDataKey, t);
								
								//logger.info("New Tag map entry with key: " + tagDataKey.toString() + " value = " + tagDataMap.get(tagDataKey).toString());
								//System.out.println("New Tag map entry with key: " + tagDataKey.toString() + " value = " + tagDataMap.get(tagDataKey).toString());
							}
							if (confDataMap.containsKey(confDataKey)) {
								ConfDataMapRecord f = (ConfDataMapRecord) confDataMap.get(confDataKey);
								f.incrementAllCounts();
								confDataMap.put(confDataKey, f);
								//System.out.println("Updated Conf map entry with key: " + confDataKey.toString() + " value = " + confDataMap.get(confDataKey).toString());
							} else {
								ConfDataMapRecord t = new ConfDataMapRecord(granularityList);
								confDataMap.put(confDataKey, t);
								//logger.info("[manageChannelBuffersWrapper] New Conf map entry with key: " + confDataKey + " value = " + confDataMap.get(confDataKey));
								//System.out.println("New Conf map entry with key: " + confDataKey.toString() + " value = " + confDataMap.get(confDataKey).toString());
							}
						}
					}
				}
			} catch (Exception e) {
				logger.error("Exception", e);
			}
		}
		// Periodically check if any channel is down - if so, delete all
		// in-memory data for that channel
		lastTagDataCheckedTime = periodicInactiveChannelCheck(lastTagDataCheckedTime, tagDataMap);
		lastConfDataCheckedTime = periodicInactiveChannelCheck(lastConfDataCheckedTime, confDataMap);
	}

	private long periodicInactiveChannelCheck(long lastCheckedTime, ConcurrentHashMap<CounterKey, Object> dataMap) {
		long currentTime = System.currentTimeMillis();
		if (currentTime - lastCheckedTime > ChannelBufferManager.CHECK_INTERVAL) {
			for (String key : channelMap.keySet()) {
				if ((currentTime - channelMap.get(key)) > ChannelBufferManager.NO_DATA_TIMEOUT) {
					logger.info("Attempt deleting data for inactive channel = " + key);
					int deleteCount = deleteMapRecordsForCollection(key, dataMap);
					logger.info("Deleted records count for inactive channel <" + key + "> is = " + deleteCount);
				}
			}
		}
		return currentTime;
	}

	private int deleteMapRecordsForCollection(final String deleteCollectionCode, ConcurrentHashMap<CounterKey, Object> dataMap) {
		int count = 0;
		Iterator<CounterKey> itr = dataMap.keySet().iterator();
		while (itr.hasNext()) {
			CounterKey keyVal = itr.next();
			MapRecord data = (MapRecord) dataMap.get(keyVal);
			if (keyVal.getCrisisCode().equals(deleteCollectionCode) && data != null && data.isCountZeroForAllGranularity()) {
				synchronized (dataMap) {		// prevent modification while deletion attempt
					dataMap.remove(keyVal);
					++count;
				}
			}
		}
		return count;
	}

	// Returns true if channelName present in list of channels
	// TODO: define the appropriate collections data structure - HashMap, HashSet, ArrayList? 
	public boolean isChannelPresent(String channelName) {
		try {
			//logger.info("Checking channelName: " + channelName + ", result = " + subscribedChannels.containsKey(channelName) + ", with message count = " + subscribedChannels.get(channelName).getCurrentMsgCount());
			return (subscribedChannels != null) ? subscribedChannels.contains(channelName) : false;
		} catch (Exception e) {
			logger.error(Thread.currentThread().getName() + ":: Unable to check if channel present: " + channelName, e);
			return false;
		}
	}



	/** 
	 * @return A set of fully qualified channel names, null if none found
	 */
	public Set<String> getActiveChannelsList() {
		try {
			Set<String> channelSet = (subscribedChannels != null && !subscribedChannels.isEmpty()) ? subscribedChannels : null;
			return channelSet;
		} catch (Exception e) {
			logger.error("Unable to fetch list of active channels", e);
			return null;
		}
	}


	@SuppressWarnings("unused")
	private void subscribeToChannel(final String channelRegEx) throws Exception {
		Future<?> redisThread = executorServicePool.submit(new Runnable() {
			public void run() {
				Thread.currentThread().setName("ChannelBufferManager Redis subscription Thread");
				logger.info("New thread <" +  Thread.currentThread().getName() + "> created for subscribing to redis channel: " + channelRegEx);
				System.out.println("New thread <" +  Thread.currentThread().getName() + "> created for subscribing to redis channel: " + channelRegEx);
				try {
					// Execute the blocking REDIS subscription call
					subscriberJedis.psubscribe(aidrSubscriber, channelRegEx);
				} catch (JedisConnectionException e) {
					logger.error("AIDR Predict Channel pSubscribing failed for channel = " + channelRegEx, e);
					stopSubscription();
					Thread.currentThread().interrupt();
				} 
				Thread.currentThread().interrupt();
				logger.info("Exiting thread: " + Thread.currentThread().getName());
			}
		});
	}

	private void stopSubscription() {
		try {
			if (aidrSubscriber != null && aidrSubscriber.getSubscribedChannels() > 0) {
				logger.info("Stopsubscription attempt for channel: " + aidrSubscriber.getChannelName());
				aidrSubscriber.punsubscribe();	
				logger.info("Unsubscribed from channel pattern: " + CHANNEL_PREFIX_STRING);
			}
		} catch (JedisConnectionException e) {
			logger.error("Connection to REDIS seems to be lost!", e);
		}
		try {
			if (jedisConn != null && aidrSubscriber != null && subscriberJedis != null) { 
				jedisConn.returnJedis(subscriberJedis);
				subscriberJedis = null;
				logger.info("Stopsubscription completed...");
				System.out.println("[stopSubscription] Stopsubscription completed...");
			}
		} catch (Exception e) {
			logger.error("Failed to return Jedis resource", e);
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
			logger.error("Fatal error! Couldn't establish connection to REDIS!", e);
			AnalyticsErrorLog.sendErrorMail("Redis", e.getMessage());
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
				AnalyticsErrorLog.sendErrorMail("Redis", e.getMessage());
			}
		}
		return false;
	}


	public void close() {
		shutdownFlag = true;
		stopSubscription();
		shutdownAndAwaitTermination();
		logger.info("All done, fetch service has been shutdown...");
		System.out.println("[close] All done, fetch service has been shutdown...");
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
						logger.warn("Executor Thread Pool did not terminate");
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


	private String getBinNumber(float confidence) {
		int bin = 0;
		for (int i = 10; i <= 100; i += 10) {
			if (i > (confidence * 100)) {
				return Integer.toString(bin);
			}
			++bin;
		}
		return Integer.toString(bin - 1);
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
					manageChannelBuffers(pattern, channel, message);
					System.out.println("Done putting message");
				} 			
			} catch (Exception e) {
				logger.error("Exception occurred, redisLoadShedder = " + redisLoadShedder + ", channel status: " + redisLoadShedder.containsKey(channel), e);
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
