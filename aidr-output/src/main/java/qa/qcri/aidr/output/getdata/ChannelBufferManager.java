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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
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
	private ExecutorService executorServicePool;

	// Redis connection related
	public static final String redisHost = "localhost";	// Current assumption: REDIS running on same m/c
	public static final int redisPort = 6379;	

	// Jedis related
	public static JedisPoolConfig poolConfig = null;
	public static JedisPool pool = null;
	public Jedis subscriberJedis = null;
	public RedisSubscriber aidrSubscriber = null;

	// Runtime related
	private boolean isConnected = false;
	private boolean isSubscribed =false;
	private long lastCheckedTime = 0; 
	private int bufferSize = -1;

	// Channel Buffering Algorithm related
	private final String CHANNEL_PREFIX_STRING = "aidr_predict.";
	public ConcurrentHashMap<String, ChannelBuffer> subscribedChannels;
	
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
		try {
			this.isConnected = connectToRedis();
		} catch (JedisConnectionException e) {
			logger.error("Fatal error! Couldn't establish connection to REDIS!");
			e.printStackTrace();
			System.exit(1);
		}
		if (this.isConnected) {
			this.aidrSubscriber = new RedisSubscriber();
			logger.debug("[ChannelBufferManager] Created new Jedis connection: " + aidrSubscriber);
			try {
				this.subscribeToChannel(channelRegEx);
				//this.channelRegEx = channelRegEx;
				this.isSubscribed = true;
				logger.debug("[ChannelBufferManager] Created pattern subscription");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("[ChannelBufferManager] Fatal exception occurred attempting subscription: " + e.toString());
				e.printStackTrace();
				System.exit(1);
			}
			if (this.isSubscribed) {
				this.subscribedChannels = new ConcurrentHashMap<String,ChannelBuffer>();
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
		if (this.isChannelPresent(channelName)) {
			// Add to appropriate circular buffer
			logger.debug("[manageChannelBuffers] Adding to existing channel = " + channelName);
			this.addMessageToChannelBuffer(channelName, receivedMessage);
		}
		else {
			//First create a new circular buffer and then add to that buffer
			logger.info("[manageChannelBuffers] Adding to new channel = " + channelName);
			this.createChannelBuffer(channelName);
			logger.debug("[manageChannelBuffers] Created new channel");
			this.addMessageToChannelBuffer(channelName, receivedMessage);
			logger.debug("[manageChannelBuffers] Added message to new channel");
		}
		// Periodically check if any channel is down - if so, delete
		long currentTime = new Date().getTime();
		if (currentTime - this.lastCheckedTime > CHECK_INTERVAL) {
			logger.info("[manageChannelBuffers] Periodic check for inactive channels - delete if any.");
			List<ChannelBuffer>cbList = new ArrayList<ChannelBuffer>();
			cbList.addAll(this.subscribedChannels.values());
			Iterator<ChannelBuffer>it = cbList.iterator();
			while (it.hasNext()) {
				ChannelBuffer temp = it.next();
				if ((currentTime - temp.getLastAddTime()) > NO_DATA_TIMEOUT) {
					logger.info("[manageChannelBuffers] Deleting inactive channel = " + channelName);
					deleteChannelBuffer(temp.getChannelName());
				}
			}
			this.lastCheckedTime = new Date().getTime();
			cbList.clear();
			cbList = null;
		}
	}

	public void addMessageToChannelBuffer(final String channelName, final String msg) {
		ChannelBuffer cb = this.subscribedChannels.get(channelName);
		cb.addMessage(msg);
		this.subscribedChannels.put(channelName, cb);
		logger.debug("[addMessageToChannelBuffer] Added new message to existing channel buffer");
	}

	public List<String> getLastMessages(String channelName, int msgCount) {
		if (isChannelPresent(channelName)) {
			ChannelBuffer cb = this.subscribedChannels.get(channelName);
			return cb != null ? cb.getMessages(msgCount) : null;
		}
		return null;
	}

	// Returns true if channelName present in list of channels
	// TODO: define the appropriate collections data structure - HashMap, HashSet, ArrayList? 
	public boolean isChannelPresent(String channelName) {
		return this.subscribedChannels.containsKey(channelName);
	}

	// channelName = fully qualified channel name as present in REDIS pubsub system
	public void createChannelBuffer(final String channelName) {
		if (!isChannelPresent(channelName)) {
		ChannelBuffer cb = new ChannelBuffer(channelName);
		if (bufferSize <= 0)
			cb.createChannelBuffer();				// use default buffer size
		else
			cb.createChannelBuffer(bufferSize);		// use specified buffer size
		this.subscribedChannels.put(channelName, cb);
		logger.debug("[createChannelBuffer] Created new channel buffer for channel: " + channelName);
		}
		else {
			logger.error("[createChannelBuffer] Trying to create an existing channel! Should never be here!");
		}
	}

	public void deleteChannelBuffer(final String channelName) {
		if (isChannelPresent(channelName)) {
			ChannelBuffer cb = this.subscribedChannels.get(channelName);
			cb.deleteBuffer();
			this.subscribedChannels.remove(channelName);
			logger.debug("[deleteChannelBuffer] Deleted channel buffer: " + channelName);
		}
	}

	public void deleteAllChannelBuffers() {
		this.subscribedChannels.clear();
	}
	
	/** 
	 * @return A set of fully qualified channel names
	 */
	public Set<String> getActiveChannelsList() {
		return this.subscribedChannels.keySet().isEmpty() ? null : this.subscribedChannels.keySet();
	}
	
	/** 
	 * @return A set of only the channel codes - stripped of CHANNEL_PREFIX_STRING
	 */
	public Set<String> getActiveChannelCodes() {
		Set<String> channelCodeSet = new HashSet<String>();
		Set<String> tempSet = getActiveChannelsList();
		for (String s:tempSet) {
			channelCodeSet.add(s.substring(CHANNEL_PREFIX_STRING.length()));
		}
		tempSet.clear();
		tempSet = null;
		return channelCodeSet.isEmpty() ? null : channelCodeSet;
	}
	
	
	// Initialize JEDIS parameters and get connection resource from JEDIS pool
	private boolean connectToRedis() {
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
		this.subscriberJedis = pool.getResource();
		if (this.subscriberJedis != null) {
			return true;
		}
		return false;
	}
	
	private void subscribeToChannel(final String channelRegEx) throws Exception {
		executorServicePool.submit(new Runnable() {
			public void run() {
				try {
					logger.debug("[subscribeToChannel] Attempting subscription for " + redisHost
							+ ":" + redisPort + "/" + channelRegEx);
					// Execute the blocking REDIS subscription call
					subscriberJedis.psubscribe(aidrSubscriber, channelRegEx);
					logger.info("[subscribeToChannel] Out of subscription for Channel = " + channelRegEx);
				} catch (Exception e) {
					logger.error("[subscribeToChannel] AIDR Predict Channel Subscribing failed");
					stopSubscription();
				} finally {
					try {
						logger.debug("[subscribeToChannel::finally] Attempting stopSubscription...");
						stopSubscription();
						logger.info("[subscribeToChannel::finally] stopSubscription success!");
					} catch (Exception e) {
						System.out.println("[subscribeToChannel::finally] Exception occurred attempting "
								+ "stopSubscription: " + e.toString());
						e.printStackTrace();
						System.exit(1);
					}
				}
			}
		}); 
	}

	private void stopSubscription() {
		logger.debug("[stopSubscription] aidrSubscriber = " + this.aidrSubscriber);
		logger.debug("[stopSubscription] Subscription count = " + this.aidrSubscriber.getSubscribedChannels());

		if (aidrSubscriber.getSubscribedChannels() > 0) {
			aidrSubscriber.punsubscribe();				
			logger.info("[stopSubscription] unsubscribed " + this.aidrSubscriber 
					+ ", Subscription count = " + this.aidrSubscriber.getSubscribedChannels());
		}

		try {
			if (null != subscriberJedis) 
				pool.returnResource(subscriberJedis);
			logger.info("[stopSubscription] Pool resource returned");
		} catch (JedisConnectionException e) {
			logger.error("[stopsubscription] JedisConnectionException occurred...");
			pool.returnBrokenResource(subscriberJedis);
			subscriberJedis = null;
		} finally {
			if (null != subscriberJedis) 
				pool.returnResource(subscriberJedis);
		}
		logger.info("[stopSubscription] Subscription ended...");
	}

	public void finalize() {
		logger.info("[finalize] Taking down all channel buffers and threads");
		this.stopSubscription();
		pool.destroy();
		poolConfig = null;
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
		public void onMessage(String channel, String message) {
			// TODO Auto-generated method stub
			logger.debug("[onMessage] Received message on channel:" + channel);
			channel = null;
			message = null;
		}

		@Override
		public void onPMessage(String pattern, String channel, String message) {
			// TODO Auto-generated method stub
			logger.debug("[onPMessage] Received message on channel:" + channel);
			manageChannelBuffers(pattern, channel, message);
			// free memory before next call
			pattern = null;
			message = null;
			channel = null;
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
