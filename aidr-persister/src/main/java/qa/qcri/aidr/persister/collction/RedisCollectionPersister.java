/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.persister.collction;

//import java.util.logging.Level;
//import java.util.logging.Logger;

import org.apache.log4j.Logger;
import qa.qcri.aidr.logging.ErrorLog;
import qa.qcri.aidr.redis.JedisConnectionPool;
import qa.qcri.aidr.utils.Config;
import redis.clients.jedis.Jedis;

/**
 *
 * @author Imran
 */
public class RedisCollectionPersister implements Runnable {

	private static Logger logger = Logger.getLogger(RedisCollectionPersister.class.getName());
	private static ErrorLog elog = new ErrorLog();
	
	String fileName;
	Thread t;
	boolean suspendFlag;
	Jedis subscriberJedis=null;
	CollectionSubscriber subscriber = null;
	String channel;
	String collectionCode;

	// koushik: make jedis thread-safe
	private JedisConnectionPool connObject = null;
	
	public RedisCollectionPersister(String fileName, String channel, String collectionCode) throws InterruptedException {
		this.fileName = fileName + collectionCode;
        this.channel = channel;
		this.collectionCode = collectionCode;
		// koushik: thread-safe jedis access code
		connObject = new JedisConnectionPool();
		
		t = new Thread(this, this.fileName);
		suspendFlag = true;
		//subscriberJedis = new Jedis("localhost");
		try {
			subscriberJedis = connObject.getJedisConnection();
			subscriber = new CollectionSubscriber(fileName, collectionCode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(collectionCode + ": Error in subscribing to Redis");
        	logger.error(elog.toStringException(e));
        	
			connObject.close(subscriberJedis);
			subscriberJedis = null;
			subscriber = null;
            throw new IllegalStateException(e.getMessage());
		}
	}

	public void startMe(){
		t.start(); // Start the thread
	}

	public void run() {
		try {
			while (suspendFlag) {
				// koushik: Added a finally block to gracefully unsubscribe
				try {
					logger.info(collectionCode + ": started collecting data to -> " + fileName);
					logger.info("Channel to Listen  to: " + Config.COLLECTION_CHANNEL + channel);
					subscriberJedis.psubscribe(subscriber, Config.COLLECTION_CHANNEL + channel);
					logger.info(collectionCode + ": Stopped collecting data -> " + fileName);
					Thread.sleep(200);
				} finally {
					if (subscriber != null && subscriber.isSubscribed()) {
						subscriber.punsubscribe(Config.COLLECTION_CHANNEL + channel);
						try {
							connObject.close(subscriberJedis);		// return jedis resource to JedisPool
							Thread.sleep(200);
						} catch (InterruptedException ex) {
							logger.warn(collectionCode + " error in closing Redis connection");
				        	logger.warn(elog.toStringException(ex));
						}
					}
				}

			}
		} catch (InterruptedException e) {
			logger.warn(collectionCode + " interrupted.");
		}
		logger.info(collectionCode + " exiting.");
	}

	public void suspendMe() {
		suspendFlag = false;

		if (subscriber != null && subscriber.isSubscribed()) {
			subscriber.punsubscribe(Config.COLLECTION_CHANNEL + channel);
		}
		try {
			t.join();
		} catch (InterruptedException e) {
			logger.warn(collectionCode + ": Collector Persister Thread join interrupted");
		}
	}

	synchronized void myresume() {
		suspendFlag = false;
		notify();
	}
}
