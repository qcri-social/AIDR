
package qa.qcri.aidr.persister.collction;

import org.apache.log4j.Logger;

import qa.qcri.aidr.redis.JedisConnectionPool;
import qa.qcri.aidr.utils.PersisterErrorHandler;
import redis.clients.jedis.Jedis;

/**
 * Creates a new thread per collection to listen to REDIS and persis twetes being streamed from REDIS by collector. 
 * @author Imran
 */
public class RedisCollectionPersister implements Runnable {

    private static Logger logger = Logger.getLogger(RedisCollectionPersister.class.getName());
    String fileName;
    Thread t;
    boolean suspendFlag;
    Jedis subscriberJedis = null;
    CollectionSubscriber subscriber = null;
    String channel;
    String collectionCode;

    private JedisConnectionPool connObject = null;

    public RedisCollectionPersister(String fileName, String channel, String collectionCode, boolean saveMediaEnabled) throws InterruptedException {
        this.fileName = fileName + collectionCode;
        this.channel = channel;
        this.collectionCode = collectionCode;
        connObject = new JedisConnectionPool();
        t = new Thread(this, this.fileName);
        suspendFlag = true;
        try {
            subscriberJedis = connObject.getJedisConnection();
            subscriber = new CollectionSubscriber(fileName, channel, collectionCode, saveMediaEnabled);
        } catch (Exception e) {
            logger.error(collectionCode + ": Error in subscribing to Redis");
            PersisterErrorHandler.sendErrorMail(e.getLocalizedMessage(), "Error in subscribing to Redis for collection: "+collectionCode);
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
