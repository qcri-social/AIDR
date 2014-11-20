/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.persister.collction;

import org.apache.log4j.Logger;

import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.redis.JedisConnectionPool;
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
    Jedis subscriberJedis = null;
    CollectionSubscriber subscriber = null;
    String channel;
    String collectionCode;

    private JedisConnectionPool connObject = null;

    public RedisCollectionPersister(String fileName, String channel, String collectionCode) throws InterruptedException {
        this.fileName = fileName + collectionCode;
        this.channel = channel;
        this.collectionCode = collectionCode;
        connObject = new JedisConnectionPool();
        t = new Thread(this, this.fileName);
        suspendFlag = true;
        try {
            subscriberJedis = connObject.getJedisConnection();
            subscriber = new CollectionSubscriber(fileName, channel, collectionCode);
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

    public void startMe() {
        t.start(); 
    }

    public void run() {
        try {
            while (suspendFlag) {
                try {
                    logger.info("Started persisting data to [" + fileName + "] for collection [" +  collectionCode + "]");
                    logger.info("Listening on channel [" + channel +"]");
                    subscriberJedis.psubscribe(subscriber, channel);
                    logger.info("Stopped persisting data to [" + fileName + "] for collection [" + collectionCode + "]");
                    Thread.sleep(200);
                } finally {
                    if (subscriber != null && subscriber.isSubscribed()) {
                        subscriber.punsubscribe(channel);
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
            subscriber.punsubscribe(channel);
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
