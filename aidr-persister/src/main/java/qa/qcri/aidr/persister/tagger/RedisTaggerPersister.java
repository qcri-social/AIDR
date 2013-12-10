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

    public RedisTaggerPersister(String fileName, String collectionCode) throws InterruptedException {
        this.fileName = fileName + collectionCode;
        this.collectionCode = collectionCode;
        t = new Thread(this, this.fileName);
        suspendFlag = true;
        subscriberJedis = new Jedis("localhost");
        subscriber = new TaggerSubscriber(fileName, collectionCode);
        
    }
    
    public void startMe(){
        t.start(); // Start the thread
    }
    

    public void run() {
        try {
            while (suspendFlag) {
                
                
                System.out.println("Started collecting Tagger data to -> " + fileName);
                System.out.println("Channel to Listen  to: " + Config.TAGGER_CHANNEL + collectionCode);
                subscriberJedis.psubscribe(subscriber, Config.TAGGER_CHANNEL + collectionCode);
                System.out.println("Stopped collecting data -> " + fileName);
                Thread.sleep(200);
            }
        } catch (InterruptedException e) {
            System.out.println(collectionCode + " interrupted.");
        }
        System.out.println(collectionCode + " exiting.");
    }

    public void suspendMe() {
        suspendFlag = false;
       
        if (subscriber != null)
        subscriber.punsubscribe(Config.FETCHER_CHANNEL+ "."+collectionCode);
        try {
            //JedisConnectionPool.close(subscriberJedis);
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            Logger.getLogger(RedisCollectorPersister.class.getName()).log(Level.SEVERE, null, ex);
        }
        t.stop();
    }

    synchronized void myresume() {
        suspendFlag = false;
        notify();
    }
}
