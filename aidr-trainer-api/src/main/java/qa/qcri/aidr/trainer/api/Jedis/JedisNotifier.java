package qa.qcri.aidr.trainer.api.Jedis;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 10/2/13
 * Time: 7:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class JedisNotifier {
    protected static Logger logger = Logger.getLogger(JedisNotifier.class);
   
    Jedis jedis;

    public JedisNotifier() {
        try {
            jedis = JedisDataStore.getJedisConnection();
            //System.out.println("Obtained jedis connection: " + jedis);
            logger.info("Redis connection established");
        } catch (Exception e) {
        	logger.error("failed to create jedis connection", e);
        }
    }

    public void notifyToJedis(String itemJosn){

        // String itemJosn ="'[{\"labelID\":3,\"attributeID\":15}]'";
        try {
            //  jedis.r
            logger.info("input data : " + itemJosn);
            //System.out.println("input data : " + itemJosn);
            jedis.rpush("training_sample_info_stream", itemJosn);
            JedisDataStore.close(jedis);
            //outputCount++;
        } catch (Exception e) {
            logger.error("Error when serializing output document.", e);
        }
    }

}