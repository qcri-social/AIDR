/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.persister.tagger;

import org.apache.log4j.Logger;

import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.persister.collector.*;
import qa.qcri.aidr.redis.JedisConnectionPool;
import qa.qcri.aidr.utils.GenericCache;
import qa.qcri.aidr.utils.PersisterConfigurationProperty;
import qa.qcri.aidr.utils.PersisterConfigurator;
import redis.clients.jedis.Jedis;

/**
 *
 * @author Imran
 */
public class RedisTaggerPersister implements Runnable {

	private static Logger logger = Logger.getLogger(RedisTaggerPersister.class.getName());
	private static ErrorLog elog = new ErrorLog();
	
	String fileName;
	Thread t;
	boolean suspendFlag;
	Jedis subscriberJedis=null;
	TaggerSubscriber subscriber = null;
	String collectionCode;
	
	// Added by koushik: make Jedis thread-safe
	private JedisConnectionPool connObject = null;
	
	public RedisTaggerPersister(String fileName, String collectionCode) throws InterruptedException {
		this.fileName = fileName + collectionCode;
		this.collectionCode = collectionCode;
		// koushik: thread-safe jedis access code
		connObject = new JedisConnectionPool();
		t = new Thread(this, this.fileName);
		suspendFlag = true;
		//subscriberJedis = new Jedis("localhost");
		try {
			subscriberJedis = connObject.getJedisConnection();
			subscriber = new TaggerSubscriber(fileName, collectionCode);
		} catch (Exception e) {
			logger.error(collectionCode + " error in subscribing to Redis");
        	logger.error(elog.toStringException(e));
        	
			connObject.close(subscriberJedis);
			subscriberJedis = null;
			subscriber = null;
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
					logger.info(collectionCode + ": Started collecting Tagger data to -> " + fileName);
					logger.info("Channel to Listen  to: " + PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.TAGGER_CHANNEL) + collectionCode);
					subscriberJedis.psubscribe(subscriber, PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.TAGGER_CHANNEL) + collectionCode);
					logger.info(collectionCode + ": Stopped collecting data -> " + fileName);
					Thread.sleep(200);
				} finally {
					if (subscriber != null && subscriber.isSubscribed()) {
						subscriber.punsubscribe(PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.TAGGER_CHANNEL) + collectionCode);
						try {
							connObject.close(subscriberJedis);		// return jedis resource to JedisPool
							Thread.sleep(200);
						} catch (InterruptedException ex) {
							logger.warn(collectionCode + ": Error in closing Redis connection");
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
			// Bug fix: should unsubscribe from getProperty("TAGGER_CHANNEL") + collectionCode!
			//subscriber.punsubscribe(Config.FETCHER_CHANNEL+ "."+collectionCode);
			subscriber.punsubscribe(PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.TAGGER_CHANNEL) + collectionCode);
		}
		/*try {
			connObject.close(subscriberJedis);		// return jedis resource to JedisPool
			Thread.sleep(200);
		} catch (InterruptedException ex) {
			Logger.getLogger(RedisTaggerPersister.class.getName()).log(Level.SEVERE, null, ex);
		}*/
		//t.stop();		// koushik: replaced by t.join()
		try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			logger.warn(collectionCode + ": Tagger Persister Thread join interrupted");
		}
	}

	synchronized void myresume() {
		suspendFlag = false;
		notify();
	}
}
