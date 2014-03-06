package qa.qcri.aidr.output.stream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;

import org.glassfish.jersey.server.ChunkedOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import qa.qcri.aidr.output.stream.RedisSubscriber;
import qa.qcri.aidr.output.stream.SubscriptionDataObject;
import qa.qcri.aidr.output.utils.JedisConnectionObject;
import qa.qcri.aidr.output.utils.JsonDataFormatter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;


public class RedisSubscriber extends JedisPubSub implements AsyncListener, Runnable {
	//////////////////////////////////////////////////////////////////////////////////////////////////
	// The inner class that handles both Asynchronous Servlet Thread and Redis Threaded Subscription
	//////////////////////////////////////////////////////////////////////////////////////////////////
	// Redis/Jedis related
	private String channel = null;
	private String callbackName = null;

	// Constants
	public static final int REDIS_CALLBACK_TIMEOUT = 5 * 60 * 1000;	// in ms
	public static final int SUBSCRIPTION_MAX_DURATION = -1;			//default = no expiry

	public final int DEFAULT_COUNT = 1;

	// Async execution related 
	private final ChunkedOutput<String> responseWriter;
	private SubscriptionDataObject subData = null;
	private boolean runFlag = true;
	private boolean error = false;
	private boolean timeout = false;
	private long subscriptionDuration = SUBSCRIPTION_MAX_DURATION;

	// rate control related 
	private static final int DEFAULT_SLEEP_TIME = 0;		// in msec
	private float messageRate = -1;							// default: <= 0 implies no rate control
	private int sleepTime = DEFAULT_SLEEP_TIME;

	// Share data structure between Jedis and Async threads
	private List<String> messageList = Collections.synchronizedList(new ArrayList<String>());

	// Debugging
	private static Logger logger = LoggerFactory.getLogger(RedisSubscriber.class);

	public RedisSubscriber(final Jedis jedis, final ChunkedOutput<String> responseWriter, 
			final SubscriptionDataObject subData) throws IOException {
		this.channel = subData.redisChannel;
		this.callbackName = subData.callbackName;
		this.responseWriter = responseWriter;

		this.subData = new SubscriptionDataObject();
		this.subData.set(subData);
	
		this.setRunFlag(true);		
		if (subData.duration != null) {
			subscriptionDuration = parseTime(subData.duration);
		} else {
			subscriptionDuration = SUBSCRIPTION_MAX_DURATION;
		}
		//System.out.println("rate=" + subData.rate + ", duration=" + subData.duration + ", callbackName=" + subData.callbackName);
		//logger.info("Client requested subscription for duration = " + subscriptionDuration);
		if (subData.rate > 0) {
			messageRate = subData.rate;			// specified as messages/min (NOTE: upper-bound)
			sleepTime = Math.max(0, Math.round(60 * 1000 / messageRate));		// time to sleep between sends (in msecs)
		} else {
			sleepTime = DEFAULT_SLEEP_TIME;		// use default value
		}
	}

	private long parseTime(String timeString) {
		long duration = 0;
		final int maxDuration = SUBSCRIPTION_MAX_DURATION > 0 ? SUBSCRIPTION_MAX_DURATION : Integer.MAX_VALUE;
		
		float value = Float.parseFloat(timeString.substring(0, timeString.length()-1));
		if (value > 0) {
			String suffix = timeString.substring(timeString.length() - 1, timeString.length());
			if (suffix.equalsIgnoreCase("s"))
				duration = Math.min(maxDuration, Math.round(value * 1000));
			if (suffix.equalsIgnoreCase("m"))
				duration = Math.min(maxDuration, Math.round(value * 1000 * 60));
			if (suffix.equalsIgnoreCase("h"))
				duration = Math.min(maxDuration, Math.round(value * 1000 * 60 * 60));
			if (suffix.equalsIgnoreCase("d"))
				duration = Math.min(maxDuration, Math.round(value * 1000 * 60 * 60 * 24));
		}
		return duration;
	}

	@Override
	public void onMessage(String channel, String message) {
		synchronized (messageList) {
			if (messageList.size() < DEFAULT_COUNT) messageList.add(message);
			messageList.notifyAll();
		}
	}

	@Override
	public void onPMessage(String pattern, String channel, String message) {
		synchronized (messageList) {
			if (messageList.size() <  DEFAULT_COUNT) messageList.add(message);
			messageList.notifyAll();
		}
	}

	@Override
	public void onPSubscribe(String pattern, int subscribedChannels) {
		subData.isSubscribed = true;
		//logger.info("[onPSubscribe] Started pattern subscription...");
	}

	@Override
	public void onPUnsubscribe(String pattern, int subscribedChannels) {
		subData.isSubscribed = false;
		//logger.info("[onPUnsubscribe] Unsubscribed from pattern subscription...");
	}

	@Override
	public void onSubscribe(String channel, int subscribedChannels) {
		subData.isSubscribed = true;
		//logger.info("[onSubscribe] Started channel subscription...");
	}

	@Override
	public void onUnsubscribe(String channel, int subscribedChannels) {
		subData.isSubscribed = false;
		//logger.info("[onUnsubscribe] Unusbscribed from channel " + channel);
	}

	// Stop subscription of this subscribed thread and return resources to the JEDIS thread pool
	public synchronized void stopSubscription(final JedisConnectionObject jedisConn, final SubscriptionDataObject subData) {
		if (this.isSubscribed()) {
			if (!subData.patternSubscriptionFlag) { 
				this.unsubscribe();				
			}
			else {
				this.punsubscribe();
			}
		}
		subData.jedisConn.returnJedis(subData.subscriberJedis);
		this.notifyAll();
		logger.info("[stopSubscription] Subscription ended for Channel=" + subData.redisChannel);
	}


	///////////////////////////////////
	// Now to implement Async methods
	///////////////////////////////////
	public boolean isThreadTimeout(long startTime) {
		// No timeout if subscriptionDuration < 0
		if ((subscriptionDuration > 0) && (new Date().getTime() - startTime) > subscriptionDuration) {
			logger.info("[isThreadTimeout] Exceeded Thread timeout = " + subscriptionDuration + "msec");
			return true;
		}
		return false;
	}

	public void run() {
		// Time-out related local variables
		long startTime = new Date().getTime();			// start time of the thread execution
		long lastAccessedTime = startTime; 

		setRunFlag(true);
		StringBuilder initMsg = new StringBuilder();
		initMsg.append("{channel:").append(this.channel).append(", subscription: SUCCESS, streaming: STARTING}");
		try {
			responseWriter.write(initMsg.toString());
			responseWriter.write("\n\n\n");
		} catch (IOException e1) {
			logger.info("Error in writing Response to client");
			setRunFlag(false);
		}
		while (getRunFlag() && !isThreadTimeout(startTime)) {
			// Here we poll a non blocking resource for updates
			if (messageList != null && !messageList.isEmpty()) {
				// There are updates, send these to the waiting client
				if (!error && !timeout) {
					// Send updates response as JSON
					synchronized (messageList) {
						JsonDataFormatter taggerOutput = new JsonDataFormatter(callbackName);	// Tagger specific JSONP output formatter
						StringBuilder jsonDataList = taggerOutput.createList(messageList, messageList.size(), subData.rejectNullFlag);
						int count = taggerOutput.getMessageCount();
						try {
							//logger.info("[run] Formatted jsonDataList: " + jsonDataList.toString());
							if (!responseWriter.isClosed()) {
								responseWriter.write(jsonDataList.toString());
								responseWriter.write("\n\n");
								//logger.info("[run] sent jsonp data, count = " + count);
							}
							else {
								logger.info("Possible client disconnect...");
								messageList.notifyAll();
								break;
							}
						} catch (Exception e) {
							logger.info("Error in write attempt - possible client disconnect");
							setRunFlag(false);
						} 
						if (count != 0)									// we did not just send an empty JSONP message
							lastAccessedTime = new Date().getTime();	// approx. time when message last received from REDIS

						// Reset the messageList buffer and cleanup
						jsonDataList = null;
						messageList.clear();	// remove the sent message from list
						messageList.notifyAll();
					}
					// Now sleep for a short time before going for next message - easy to read on screen
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
				}
				else {
					setRunFlag(false);
				}
			}
			else {
				// messageList is empty --> no message received 
				// from REDIS. Wait for some more time before giving up.
				long currentTime = new Date().getTime();
				long elapsed = currentTime - lastAccessedTime;
				if (elapsed > REDIS_CALLBACK_TIMEOUT) {
					logger.error("[run] exceeded REDIS timeout = " + REDIS_CALLBACK_TIMEOUT + "msec");
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
			if (responseWriter.isClosed()) {
				logger.info("[run] Client side error - possible client disconnect..." + new Date());
				setRunFlag(false);
			}
		}	// end-while

		// clean-up and exit thread
		if (!error && !timeout) {
			if (messageList != null) {
				messageList.clear();
				messageList = null;
			}
			if (!responseWriter.isClosed()) {
				try {
					responseWriter.close();
				} catch (IOException e) {
					logger.error("[run] Error attempting closing ChunkedOutput.");
				}
			}
			try {
				stopSubscription(subData.jedisConn, subData);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("[run] Attempting clean-up. Exception occurred attempting stopSubscription: " + e.toString());
				e.printStackTrace();
			}
		}
	}

	public void setRunFlag(final boolean val) {
		runFlag = val;
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
	public void onComplete(AsyncEvent event) throws IOException {
		//logger.info("[run] Async thread complete...");
	}
}