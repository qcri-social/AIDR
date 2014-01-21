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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import qa.qcri.aidr.output.utils.JedisConnectionObject;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;

//import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChannelBufferManager {

	private static final int NO_DATA_TIMEOUT = 5 * 60 * 1000;		// when to delete a channel buffer
	private static final int CHECK_INTERVAL = NO_DATA_TIMEOUT;

	private static Logger logger = LoggerFactory.getLogger(ChannelBufferManager.class);

	// Thread related
	private static ExecutorService executorServicePool;

	// Redis connection related
	public static final String redisHost = "localhost";	// Current assumption: REDIS running on same m/c
	public static final int redisPort = 6379;	

	// Jedis related
	public static JedisConnectionObject jedisConn;
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
	//////////////////////////////////////////
	// ********* Method definitions *********
	//////////////////////////////////////////

	// Constructor
	public ChannelBufferManager(final String channelRegEx) {
		//BasicConfigurator.configure();			// setup logging
		System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");		// set logging level for slf4j

		logger.info("[ChannelBufferManager] Initializing channel buffer manager.");
		bufferSize = -1;
		executorServicePool = Executors.newFixedThreadPool(200);		// max number of threads
		jedisConn = new JedisConnectionObject(redisHost, redisPort);
		try {
			isConnected = jedisConn.connectToRedis();
			subscriberJedis = jedisConn.getJedis();
		} catch (JedisConnectionException e) {
			logger.error("Fatal error! Couldn't establish connection to REDIS!");
			e.printStackTrace();
			System.exit(1);
		}
		if (isConnected) {
			aidrSubscriber = new RedisSubscriber();
			logger.info("[ChannelBufferManager] Created new Jedis connection: " + aidrSubscriber);
			try {
				subscribeToChannel(channelRegEx);
				//this.channelRegEx = channelRegEx;
				isSubscribed = true;
				logger.info("[ChannelBufferManager] Created pattern subscription");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("[ChannelBufferManager] Fatal exception occurred attempting subscription: " + e.toString());
				e.printStackTrace();
				System.exit(1);
			}
			if (isSubscribed) {
				ChannelBufferManager.subscribedChannels = new ConcurrentHashMap<String,ChannelBuffer>();
				logger.debug("[ChannelBufferManager] Created HashMap");
			}
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
			System.exit(1);
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
					logger.debug("[manageChannelBuffers] Deleting inactive channel = " + channelName);
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
		//logger.info("[getLastMessages] fetching count = " + msgCount);
		if (isChannelPresent(channelName)) {
			ChannelBuffer cb = ChannelBufferManager.subscribedChannels.get(channelName);
			return cb != null ? cb.getMessages(msgCount) : null;
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
	}

	public void deleteChannelBuffer(final String channelName) {
		if (isChannelPresent(channelName)) {
			ChannelBuffer cb = ChannelBufferManager.subscribedChannels.get(channelName);
			cb.deleteBuffer();
			ChannelBufferManager.subscribedChannels.remove(channelName);
			logger.debug("[deleteChannelBuffer] Deleted channel buffer: " + channelName);
		}
	}

	public void deleteAllChannelBuffers() {
		for (String channelId: ChannelBufferManager.subscribedChannels.keySet()) {
			ChannelBufferManager.subscribedChannels.get(channelId).deleteBuffer();
			ChannelBufferManager.subscribedChannels.remove(channelId);
		}
		ChannelBufferManager.subscribedChannels.clear();
	}

	/** 
	 * @return A set of fully qualified channel names, null if none found
	 */
	public Set<String> getActiveChannelsList() {
		Set<String> channelSet = new HashSet<String>();
		channelSet.addAll(ChannelBufferManager.subscribedChannels.keySet().isEmpty() 
				? new HashSet<String>() : ChannelBufferManager.subscribedChannels.keySet());
		return channelSet.isEmpty() ? null : channelSet;
	}

	/** 
	 * @return A set of only the channel codes - stripped of CHANNEL_PREFIX_STRING, null if none found
	 */
	public Set<String> getActiveChannelCodes() {
		Set<String> channelCodeSet = new HashSet<String>();
		final Set<String> tempSet = getActiveChannelsList();
		for (String s:tempSet) {
			channelCodeSet.add(s.substring(CHANNEL_PREFIX_STRING.length()));
		}
		tempSet.clear();
		return channelCodeSet.isEmpty() ? null : channelCodeSet;
	}

	/**
	 * @return List of latest tweets seen on all active channels, one tweet/channel, null if none found
	 */
	public List<String> getLatestFromAllChannels() {
		TreeMap<Long, String>dataSet = new TreeMap<Long, String>();

		List<ChannelBuffer>cbList = new ArrayList<ChannelBuffer>();
		cbList.addAll(ChannelBufferManager.subscribedChannels.values());
		Iterator<ChannelBuffer>it = cbList.iterator();
		while (it.hasNext()) {
			final ChannelBuffer temp = it.next();
			final List<String> tempList = temp.getLIFOMessages(1);
			if (!tempList.isEmpty())
				dataSet.put(temp.getLastAddTime(), tempList.get(0));
			tempList.clear();
		}
		List<String> msgList = null; 
		if (!dataSet.isEmpty()) {
			msgList = new ArrayList<String>();
			msgList.addAll(dataSet.descendingMap().values());
			dataSet.clear();
			dataSet = null;
		}
		return msgList;
	}


	private void subscribeToChannel(final String channelRegEx) throws Exception {
		executorServicePool.submit(new Runnable() {
			public void run() {
				try {
					// Execute the blocking REDIS subscription call
					subscriberJedis.psubscribe(aidrSubscriber, channelRegEx);
				} catch (Exception e) {
					logger.error("[subscribeToChannel] AIDR Predict Channel Subscribing failed");
					stopSubscription();
				} finally {
					try {
						stopSubscription();
					} catch (Exception e) {
						logger.error("[subscribeToChannel::finally] Exception occurred attempting stopSubscription: " + e.toString());
						e.printStackTrace();
						System.exit(1);
					}
				}
			}
		}); 
	}

	private void stopSubscription() {
		if (aidrSubscriber.getSubscribedChannels() > 0) {
			aidrSubscriber.punsubscribe();				
		}
		jedisConn.returnJedis();
	}

	public void finalize() {
		logger.info("[finalize] Taking down all channel buffers and threads");
		stopSubscription();
		jedisConn.finalize();
		jedisConn = null;
		deleteAllChannelBuffers();
		executorServicePool.shutdown(); // Disable new tasks from being submitted
		try {
			super.finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			// TODO Auto-generated method stub
			logger.info("[onSubscribe] Subscribed to channel:" + channel);
		}

		@Override
		public void onUnsubscribe(String channel, int subscribedChannels) {
			// TODO Auto-generated method stub
			logger.info("[onUnsubscribe] Unsubscribed from channel:" + channel);
		}

		@Override
		public void onPUnsubscribe(String pattern, int subscribedChannels) {
			// TODO Auto-generated method stub
			logger.info("[onPSubscribe] Unsubscribed from channel pattern:" + pattern);
		}

		@Override
		public void onPSubscribe(String pattern, int subscribedChannels) {
			// TODO Auto-generated method stub
			logger.info("[onPSubscribe] Subscribed to channel pattern:" + pattern);
		}
	}
}
