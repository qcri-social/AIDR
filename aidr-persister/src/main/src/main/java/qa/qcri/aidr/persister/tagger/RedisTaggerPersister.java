/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.persister.tagger;

import qa.qcri.aidr.persister.collector.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import qa.qcri.aidr.utils.Config;
import qa.qcri.aidr.redis.JedisConnectionPool;
import qa.qcri.aidr.utils.GenericCache;
import redis.clients.jedis.Jedis;

/**
 *
 * @author Imran
 */
public class RedisTaggerPersister implements Runnable {

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
			// TODO Auto-generated catch block
			e.printStackTrace();
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
					System.out.println("Started collecting Tagger data to -> " + fileName);
					System.out.println("Channel to Listen  to: " + Config.TAGGER_CHANNEL + collectionCode);
					subscriberJedis.psubscribe(subscriber, Config.TAGGER_CHANNEL + collectionCode);
					System.out.println("Stopped collecting data -> " + fileName);
					Thread.sleep(200);
				} finally {
					if (subscriber != null && subscriber.isSubscribed()) {
						subscriber.punsubscribe(Config.TAGGER_CHANNEL + collectionCode);
					}
				}
			}
		} catch (InterruptedException e) {
			System.out.println(collectionCode + " interrupted.");
		}
		System.out.println(collectionCode + " exiting.");
	}

	public void suspendMe() {
		suspendFlag = false;

		if (subscriber != null && subscriber.isSubscribed()) {
			// Bug fix: should unsubscribe from Config.TAGGER_CHANNEL + collectionCode!
			//subscriber.punsubscribe(Config.FETCHER_CHANNEL+ "."+collectionCode);
			subscriber.punsubscribe(Config.TAGGER_CHANNEL + collectionCode);
		}
		try {
			connObject.close(subscriberJedis);		// return jedis resource to JedisPool
			Thread.sleep(200);
		} catch (InterruptedException ex) {
			Logger.getLogger(RedisTaggerPersister.class.getName()).log(Level.SEVERE, null, ex);
		}
		//t.stop();		// koushik: replaced by t.join()
		try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("[InterruptedException] Tagger Persister Thread join interrupted");
		}
	}

	synchronized void myresume() {
		suspendFlag = false;
		notify();
	}
}
