/*
 * Class for implementing the REDIS subscriber for an Asynchronous REST service. 
 * The Async subscriber subscribes to REDIS for a given collection code and spawns a new thread that 
 * opens an async REST channel and continues to push data to it from REDIS until the client is closed. 
 * 
 *   It uses Glassfish specific jersey Async data type "ChunkedOutput" - useful for 
 *   sending messages in "typed" chunks. Useful for long running processes,that need to generate
 *   partial responses at a time.
 */

package qa.qcri.aidr.output.stream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;

import org.apache.log4j.Logger;
import org.glassfish.jersey.server.ChunkedOutput;

import qa.qcri.aidr.output.utils.JedisConnectionObject;
import qa.qcri.aidr.output.utils.JsonDataFormatter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;


public class AsyncStreamRedisSubscriber extends JedisPubSub implements AsyncListener, Runnable {
	//////////////////////////////////////////////////////////////////////////////////////////////////
	// The inner class that handles both Asynchronous Servlet Thread and Redis Threaded Subscription
	//////////////////////////////////////////////////////////////////////////////////////////////////
	// Redis/Jedis related
	private String channel = null;
	private String callbackName = null;

	// Constants
	public static final int REDIS_CALLBACK_TIMEOUT = 30 * 60 * 1000;	// in ms
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

	private ArrayList<ChunkedOutput<String>> writerList = null;

	// Debugging
	private static Logger logger = Logger.getLogger(AsyncStreamRedisSubscriber.class);

	public AsyncStreamRedisSubscriber(final Jedis jedis, final ChunkedOutput<String> responseWriter,
			ArrayList<ChunkedOutput<String>> writerList,
			final SubscriptionDataObject subData) throws IOException {
		this.channel = subData.redisChannel;
		this.callbackName = subData.callbackName;
		this.responseWriter = responseWriter;
		this.writerList = writerList;

		this.subData = new SubscriptionDataObject();
		this.subData.set(subData);

		this.setRunFlag(true);		
		if (subData.duration != null) {
			subscriptionDuration = parseTime(subData.duration);
		} else {
			subscriptionDuration = SUBSCRIPTION_MAX_DURATION;
		}
		//System.out.println("rate=" + subData.rate + ", duration=" + subData.duration + ", callbackName=" + subData.callbackName);
		logger.info("Client requested subscription for duration = " + subscriptionDuration);
		if (subData.rate > 0) {
			messageRate = subData.rate;			// specified as messages/min (NOTE: upper-bound)
			sleepTime = Math.max(0, Math.round(60 * 1000 / messageRate));		// time to sleep between sends (in msecs)
		} else {
			sleepTime = DEFAULT_SLEEP_TIME;		// use default value
		}
		messageList = new ArrayList<String>(DEFAULT_COUNT);
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
		try {
			if (messageList != null) {
				synchronized(messageList) {
					if (messageList.size() < DEFAULT_COUNT) messageList.add(message);
				}
			}
		} catch (Exception e) {
			logger.error("Error in onPMessage channel : " + channel + " message : " + message);
		}
	}

	@Override
	public void onPMessage(String pattern, String channel, String message) {
		try {
			if (messageList != null) {
				synchronized(messageList) {
					if (messageList.size() < DEFAULT_COUNT) messageList.add(message);
				}
			}
		} catch (Exception e) {
			logger.error("Error in onPMessage pattern : " + pattern + " channel : " + channel + " message : " + message);
		}
	}

	@Override
	public void onPSubscribe(String pattern, int subscribedChannels) {
		subData.isSubscribed = true;
		logger.info("Started pattern subscription for pattern: " + pattern);
	}

	@Override
	public void onPUnsubscribe(String pattern, int subscribedChannels) {
		subData.isSubscribed = false;
		logger.info("Unsubscribed from pattern subscription: " + pattern);
	}

	@Override
	public void onSubscribe(String channel, int subscribedChannels) {
		subData.isSubscribed = true;
		logger.info("Started channel subscription for " + channel);
	}

	@Override
	public void onUnsubscribe(String channel, int subscribedChannels) {
		subData.isSubscribed = false;
		logger.info("Unsubscribed from channel " + channel);
	}

	// Stop subscription of this subscribed thread and return resources to the JEDIS thread pool
	public void stopSubscription(final JedisConnectionObject jedisConn, final SubscriptionDataObject subData) {
		if (this.isSubscribed()) {
			if (!subData.patternSubscriptionFlag) { 
				this.unsubscribe();				
			}
			else {
				this.punsubscribe();
			}
		}
		subData.jedisConn.returnJedis(subData.subscriberJedis);
		logger.info("Subscription ended for Channel=" + subData.redisChannel);
	}


	///////////////////////////////////
	// Now to implement Async methods
	///////////////////////////////////
	public boolean isThreadTimeout(long startTime) {
		// No timeout if subscriptionDuration < 0
		if ((subscriptionDuration > 0) && (new Date().getTime() - startTime) > subscriptionDuration) {
			logger.info("Exceeded Thread timeout = " + subscriptionDuration + "msec");
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
		logger.info(initMsg.toString());
	
		logger.info(channel + ": Async Thread ready to send messsages to client");
		long messageCount = 0;
		while (getRunFlag() && !isThreadTimeout(startTime)) {
			// Here we poll a non blocking resource for updates
			if (messageList != null && !messageList.isEmpty()) {
				// There are updates, send these to the waiting client
				if (!error && !timeout) {
					// Send updates response as JSON
					JsonDataFormatter taggerOutput = null;
					StringBuilder jsonDataList = null;
					List<String> localCopy = null; 
					synchronized (messageList) {
						localCopy = new ArrayList<String>(messageList.size());
						localCopy.addAll(messageList);
					}
					taggerOutput = new JsonDataFormatter(callbackName);	// Tagger specific JSONP output formatter
					jsonDataList = taggerOutput.createStreamingList(localCopy, localCopy.size(), subData.rejectNullFlag);

					int count = taggerOutput.getMessageCount();
					try {
						//logger.info("[run] Formatted jsonDataList: " + jsonDataList.toString());
						if (!responseWriter.isClosed()) {
							if (count > 0) {
								// data present to send
								responseWriter.write(jsonDataList.toString());
								responseWriter.write("\n\n");
								messageCount += count;
								//logger.info(channel + ": sending to client message #" + messageCount);
							}
						}
						else {
							logger.warn(channel + ": No responseWriter available!");
							break;
						}
					} catch (Exception e) {
						setRunFlag(false);
						logger.error(channel + ": Error in write attempt - possible client disconnect");
					} 
					if (count != 0)	{								// we did not just send an empty JSONP message
						lastAccessedTime = new Date().getTime();	// approx. time when message last received from REDIS
					}
					// Reset the messageList buffer and cleanup
					jsonDataList = null;
					synchronized(messageList) {
						messageList.clear();	// remove the sent message from list
					}
					localCopy.clear();
					// Now sleep for a short time before going for next message - easy to read on screen
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
						logger.warn("Error in sleep.");
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
					logger.error(channel + ": Exceeded REDIS timeout for a message to appear on channel = " + REDIS_CALLBACK_TIMEOUT + "msec");
					setRunFlag(false);
				}	
				else {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						logger.warn("error in sleep.");
						// TODO Auto-generated catch block
					}
				}
			}
			// check if the client is up - indirectly through whether the write succeeded or failed
			if (responseWriter.isClosed()) {
				logger.info(channel + ": Client side error - possible client disconnect..." + new Date());
				setRunFlag(false);
			}
		}	// end-while

		logger.info(channel + ": total sent message count = " + messageCount);
		// clean-up and exit thread
		if (!error && !timeout) {
			if (messageList != null) {
				messageList.clear();
				messageList = null;
			}
			if (!responseWriter.isClosed()) {
				try {
					responseWriter.close();
					writerList.remove(responseWriter);
				} catch (IOException e) {
					logger.error(channel + ": Error attempting closing ChunkedOutput.");
				}
			}
			try {
				stopSubscription(subData.jedisConn, subData);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error(channel + ": Attempting clean-up. Exception occurred attempting stopSubscription: " + e.toString());
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
		logger.error(channel + ": An error occured while executing task for client ");
	}

	@Override
	public void onTimeout(AsyncEvent event) throws IOException {
		setRunFlag(false);
		timeout = true;
		logger.warn(channel + ": Timed out while executing task for client");
	}

	@Override
	public void onStartAsync(AsyncEvent event) throws IOException {
		logger.info(channel + ": Async thread started...");
	}

	@Override
	public void onComplete(AsyncEvent event) throws IOException {
		logger.info(channel + ": Async thread complete...");
	}
}