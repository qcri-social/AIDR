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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import qa.qcri.aidr.output.entity.AidrCollection;
import qa.qcri.aidr.output.filter.ClassifiedFilteredTweet;
import qa.qcri.aidr.output.utils.AIDROutputConfig;
import qa.qcri.aidr.output.utils.DatabaseController;
import qa.qcri.aidr.output.utils.DatabaseInterface;
import qa.qcri.aidr.output.utils.JedisConnectionObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

//import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChannelBufferManager {

	private static final int NO_DATA_TIMEOUT = 48 * 60 * 60 * 1000;		// when to delete a channel buffer
	private static final int CHECK_INTERVAL = NO_DATA_TIMEOUT;

	private static Logger logger = LoggerFactory.getLogger(ChannelBufferManager.class);

	// Thread related
	private static ExecutorService executorServicePool;

	// Redis connection related
	public static String redisHost = "localhost";	// Current assumption: REDIS running on same m/c
	public static int redisPort = 6379;	

	// Jedis related
	public static JedisConnectionObject jedisConn;		// we need only a single instance of JedisConnectionObject running in background
	public Jedis subscriberJedis = null;
	public RedisSubscriber aidrSubscriber = null;

	// Runtime related
	private boolean isConnected = false;
	private boolean isSubscribed =false;
	private long lastCheckedTime = 0; 
	private int bufferSize = -1;

	// Channel Buffering Algorithm related
	private final String CHANNEL_PREFIX_STRING = "aidr_predict.";
	public static ConcurrentHashMap<String, ChannelBuffer> subscribedChannels;

	// DB access related
	private static DatabaseInterface dbController;

	//////////////////////////////////////////
	// ********* Method definitions *********
	//////////////////////////////////////////

	// Constructor
	public ChannelBufferManager(final String channelRegEx) {
		AIDROutputConfig configuration = new AIDROutputConfig();
		HashMap<String, String> configParams = configuration.getConfigProperties();

		redisHost = configParams.get("host");
		redisPort = Integer.parseInt(configParams.get("port"));
		if (configParams.get("logger").equalsIgnoreCase("log4j")) {
			// For now: set up a simple configuration that logs on the console
			// PropertyConfigurator.configure("log4j.properties");      
			//BasicConfigurator.configure();    // initialize log4j logging
		}
		if (configParams.get("logger").equalsIgnoreCase("slf4j")) {
			System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");	// set logging level for slf4j
		}
		logger.info("[ChannelBufferManager] Initializing channel buffer manager.");

		bufferSize = -1;
		executorServicePool = Executors.newCachedThreadPool();	//Executors.newFixedThreadPool(10);		// max number of threads
		logger.info("Create thread pool: " + executorServicePool);

		jedisConn = new JedisConnectionObject(redisHost, redisPort);
		try {
			subscriberJedis = jedisConn.getJedisResource();
			if (subscriberJedis != null) isConnected = true;
		} catch (JedisConnectionException e) {
			subscriberJedis = null;
			isConnected = false;
			logger.error("Fatal error! Couldn't establish connection to REDIS!");
			e.printStackTrace();
			//System.exit(1);
		}
		if (isConnected) {
			aidrSubscriber = new RedisSubscriber();
			jedisConn.setJedisSubscription(subscriberJedis, true);		// we will be using pattern-based subscription
			logger.info("[ChannelBufferManager] Created new Jedis connection: " + aidrSubscriber);
			try {
				subscribeToChannel(channelRegEx);
				//this.channelRegEx = channelRegEx;
				isSubscribed = true;
				logger.info("[ChannelBufferManager] Created pattern subscription");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				isSubscribed = false;
				logger.error("[ChannelBufferManager] Fatal exception occurred attempting subscription: " + e.toString());
				e.printStackTrace();
				//System.exit(1);
			}
			if (isSubscribed) {
				ChannelBufferManager.subscribedChannels = new ConcurrentHashMap<String,ChannelBuffer>();
				logger.debug("[ChannelBufferManager] Created HashMap");
			}
		}
		
		try {
			dbController = new DatabaseController();
			logger.info("[ChannelBufferManager] Created dbController = " + dbController);
		} catch (Exception e) {
			logger.error("[ChannelBufferManager] Couldn't initiate DB access to aidr_fetch_manager");
			e.printStackTrace();
		}
		
	}

	public ChannelBufferManager(final int bufferSize, final String channelRegEx) {
		this(channelRegEx);					// call default constructor
		this.bufferSize = bufferSize;		// set buffer size
	}


	// Does all the essential work:
	// 1. Searches received message to see if channel name present.
	// 2. If channel present then simply adds receivedMessage to that channel.
	// 3. Else, first calls createChannelBuffer() and then executes step (2).
	// 4. Deletes channelName and channel buffer if channelName not seen for TIMEOUT duration.
	private void manageChannelBuffers(final String subscriptionPattern, 
			final String channelName, 
			final String receivedMessage) {
		if (null == channelName) {
			logger.error("[manageChannelBuffers] Something terribly wrong! Fatal error in: " + channelName);
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
			logger.info("[manageChannelBuffers] Created new channel: " + channelName);
		}
		// Periodically check if any channel is down - if so, delete
		long currentTime = new Date().getTime();
		if (currentTime - lastCheckedTime > CHECK_INTERVAL) {
			logger.info("[manageChannelBuffers] Periodic check for inactive channels - delete if any.");
			List<ChannelBuffer>cbList = new ArrayList<ChannelBuffer>();
			cbList.addAll(ChannelBufferManager.subscribedChannels.values());
			Iterator<ChannelBuffer>it = cbList.iterator();
			while (it.hasNext()) {
				ChannelBuffer temp = it.next();
				if ((currentTime - temp.getLastAddTime()) > NO_DATA_TIMEOUT) {
					logger.info("[manageChannelBuffers] Deleting inactive channel = " + channelName);
					deleteChannelBuffer(temp.getChannelName());
				}
			}
			lastCheckedTime = new Date().getTime();
			cbList.clear();
			cbList = null;
		}
	}

	public void addMessageToChannelBuffer(final String channelName, final String msg) {
		ChannelBuffer cb = ChannelBufferManager.subscribedChannels.get(channelName);
		cb.addMessage(msg);
		ChannelBufferManager.subscribedChannels.put(channelName, cb);
	}

	public List<String> getLastMessages(String channelName, int msgCount) {
		if (isChannelPresent(channelName)) {
			ChannelBuffer cb = ChannelBufferManager.subscribedChannels.get(channelName);
			// Note: Ideally, we should have used the method call cb.getMessages(msgCount)
			// However, we get all messages in buffer since we do not know how many will 
			// eventually be valid, due to rejectNullFlag setting in caller. The filtering
			// to send msgCount number of messages will happen in the caller. 
			return cb != null ? cb.getMessages(cb.getBufferSize()) : null;		
		}
		return null;
	}

	// Returns true if channelName present in list of channels
	// TODO: define the appropriate collections data structure - HashMap, HashSet, ArrayList? 
	public boolean isChannelPresent(String channelName) {
		return ChannelBufferManager.subscribedChannels.containsKey(channelName);
	}

	// channelName = fully qualified channel name as present in REDIS pubsub system
	public void createChannelQueue(final String channelName) {
		if (!isChannelPresent(channelName)) {
			ChannelBuffer cb = new ChannelBuffer(channelName);
			if (bufferSize <= 0)
				cb.createChannelBuffer();				// use default buffer size
			else
				cb.createChannelBuffer(bufferSize);		// use specified buffer size
			ChannelBufferManager.subscribedChannels.put(channelName, cb);
		}
		else {
			logger.error("[createChannelQueue] Trying to create an existing channel! Should never be here!");
		}
		//isChannelPublic(channelName);
	}

	public void deleteChannelBuffer(final String channelName) {
		if (isChannelPresent(channelName)) {
			ChannelBuffer cb = ChannelBufferManager.subscribedChannels.get(channelName);
			cb.deleteBuffer();
			ChannelBufferManager.subscribedChannels.remove(channelName);
			logger.info("[deleteChannelBuffer] Deleted channel buffer: " + channelName);
		}
	}

	public void deleteAllChannelBuffers() {
		if (ChannelBufferManager.subscribedChannels != null) {
			for (String channelId: ChannelBufferManager.subscribedChannels.keySet()) {
				ChannelBufferManager.subscribedChannels.get(channelId).deleteBuffer();
				ChannelBufferManager.subscribedChannels.remove(channelId);
			}
			ChannelBufferManager.subscribedChannels.clear();
		}
	}

	/** 
	 * @return A set of fully qualified channel names, null if none found
	 */
	public Set<String> getActiveChannelsList() {
		final Set<String> channelSet = new HashSet<String>();
		channelSet.addAll(ChannelBufferManager.subscribedChannels.keySet().isEmpty() 
				? new HashSet<String>() : ChannelBufferManager.subscribedChannels.keySet());
		return channelSet.isEmpty() ? null : channelSet;
	}

	/** 
	 * @return A set of only the channel codes - stripped of CHANNEL_PREFIX_STRING, null if none found
	 */
	public Set<String> getActiveChannelCodes() {
		final Set<String> channelCodeSet = new HashSet<String>();
		Set<String> tempSet = getActiveChannelsList();
		for (String s:tempSet) {
			channelCodeSet.add(s.substring(CHANNEL_PREFIX_STRING.length()));
		}
		tempSet.clear();
		tempSet = null;
		return channelCodeSet.isEmpty() ? null : channelCodeSet;
	}

	/**
	 * 
	 * @param channelName
	 * @return true if channel is publicly listed, false otherwise
	 */
	private boolean isChannelPublic(String channelName) {
		logger.info("[isChannelPublic] Received request for channel: " + channelName);
		//first strip off the prefix aidr_predict.
		String channelCode = channelName;
		if (channelName.startsWith(CHANNEL_PREFIX_STRING)) {
			String[] strs = channelName.split(CHANNEL_PREFIX_STRING);
			channelCode = (strs != null) ? strs[1] : channelName;
		}
		//logger.info("[isChannelPublic] Querying for: " + channelCode);
		Criterion criterion = Restrictions.eq("code", channelCode);
		AidrCollection collection = dbController.getByCriteria(criterion);
		if (collection != null) {
			logger.info("[isChannelPublic] channel: " + channelName + ", code = " + collection.getCode() + ", public = " + collection.getPubliclyListed());
			return collection.getPubliclyListed();
		} else {
			logger.info("[isChannelPublic] channel: " + channelName + ", fetched collection = " + collection);
		}
		return false;
	}


	/**
	 * @return List of latest tweets seen on all active channels, one tweet/channel, null if none found
	 */
	public List<String> getLatestFromAllChannels(final int msgCount) {
		final List<ChannelBuffer>cbList = new ArrayList<ChannelBuffer>();
		List<String>dataSet = new ArrayList<String>();

		cbList.addAll(ChannelBufferManager.subscribedChannels.values());
		int k = -1;
		for (ChannelBuffer temp: cbList) {
			//System.out.println("cbList size = " + cbList.size() + ", Channel buffer: " + temp.getChannelName());
			if (isChannelPublic(temp.getChannelName())) {
				final List<String> tempList = temp.getLIFOMessages(msgCount+4);		// reverse-chronologically ordered list
				if (!tempList.isEmpty()) {
					int channelFetchSize = Math.min(msgCount+4,tempList.size());
					for (int i = 0;i < channelFetchSize;i++) {
						//System.out.println("TWEET: " + tempList.get(i));
						ClassifiedFilteredTweet tweet = new ClassifiedFilteredTweet().deserialize(tempList.get(i));
						//System.out.println("Channel: " + tweet.getCrisisCode() + ", time: " + tweet.getCreatedAt() + ", conf=" + tweet.getMaxConfidence());
						long tweetTime = tweet.getCreatedAt().getTime();
						if (k < 0) {
							dataSet.add(k+1, tempList.get(i));
							++k;
						}
						else {
							ClassifiedFilteredTweet lastStoredTweet = new ClassifiedFilteredTweet()
							.deserialize(dataSet.get(k));
							// rule 1: if more recent, include
							if (lastStoredTweet.getCreatedAt().getTime() < tweetTime) {
								dataSet.add(k+1, tempList.get(i));
								++k;
								//System.out.println("Added using rule 1 from channel: " + tweet.getCrisisCode());
							}

							// rule 2: if not recent but from another channel, include
							if (lastStoredTweet.getCreatedAt().getTime() >= tweetTime
									&& !lastStoredTweet.getCrisisCode().equals(tweet.getCrisisCode())) {
								String tempTweet = dataSet.remove(k);
								dataSet.add(k, tempList.get(i));
								dataSet.add(k+1,tempTweet);
								++k;
								//System.out.println("Added using rule 2 from channel: " + tweet.getCrisisCode());
							}
						}
					}
				}
			}	// end if (isChannelPublic)
		}	// end for
		return dataSet;
	}

	public Date extractTweetTimestampField(String tweet) {
		String offsetString = "\"created_at\":\"";
		StringBuffer strBuff = new StringBuffer();
		strBuff.append(tweet.substring(tweet.indexOf(offsetString) + offsetString.length(), tweet.indexOf("\",", tweet.indexOf(offsetString))));

		// Tweet date format: Tue Feb 18 08:46:03 +0000 2014
		SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZ yyyy");
		formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		try {
			return formatter.parse(strBuff.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	private void subscribeToChannel(final String channelRegEx) throws Exception {
		executorServicePool.submit(new Runnable() {
			public void run() {
				try {
					// Execute the blocking REDIS subscription call
					subscriberJedis.psubscribe(aidrSubscriber, channelRegEx);
				} catch (Exception e) {
					logger.error("[subscribeToChannel] AIDR Predict Channel pSubscribing failed for channel = " + channelRegEx);
					stopSubscription();
				} finally {
					try {
						stopSubscription();
					} catch (Exception e) {
						logger.error("[subscribeToChannel] " + channelRegEx  + ": Exception occurred attempting stopSubscription: " + e.toString());
						e.printStackTrace();
					}
				}
			}
		}); 
	}

	private synchronized void stopSubscription() {
		try {
			if (aidrSubscriber != null && aidrSubscriber.getSubscribedChannels() > 0) {
				aidrSubscriber.punsubscribe();				
			}
		} catch (JedisConnectionException e) {
			logger.error("[stopSubscription] Connection to REDIS seems to be lost!");
		}
		if (jedisConn != null && aidrSubscriber != null && aidrSubscriber.getSubscribedChannels() == 0) 
			jedisConn.returnJedis(subscriberJedis);
		this.notifyAll();
	}

	public void close() {
		stopSubscription();
		//jedisConn.closeAll();
		deleteAllChannelBuffers();
		executorServicePool.shutdown(); // Disable new tasks from being submitted
		logger.info("[close] All done, shutdown fetch service...");
	}

	////////////////////////////////////////////////////
	private class RedisSubscriber extends JedisPubSub {

		@Override
		public void onMessage(String channel, String message) {}

		@Override
		public void onPMessage(String pattern, String channel, String message) {
			manageChannelBuffers(pattern, channel, message);
		}

		@Override
		public void onSubscribe(String channel, int subscribedChannels) {
			logger.info("[onSubscribe] Subscribed to channel:" + channel);
		}

		@Override
		public void onUnsubscribe(String channel, int subscribedChannels) {
			logger.info("[onUnsubscribe] Unsubscribed from channel:" + channel);
		}

		@Override
		public void onPUnsubscribe(String pattern, int subscribedChannels) {
			logger.info("[onPSubscribe] Unsubscribed from channel pattern:" + pattern);
		}

		@Override
		public void onPSubscribe(String pattern, int subscribedChannels) {
			logger.info("[onPSubscribe] Subscribed to channel pattern:" + pattern);
		}
	}

}
