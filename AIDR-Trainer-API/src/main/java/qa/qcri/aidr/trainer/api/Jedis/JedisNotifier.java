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
    protected static Logger logger = Logger.getLogger("service");
    Jedis jedis;

    public JedisNotifier() {
        try {
            jedis = JedisDataStore.getJedisConnection();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void notifyToJedis(String itemJosn){

        // String itemJosn ="'[{\"labelID\":3,\"attributeID\":15}]'";
        try {
            //  jedis.r
            //logger.debug("input data : " + itemJosn);
            jedis.rpush("training_sample_info_stream", itemJosn);
            JedisDataStore.close(jedis);
            //outputCount++;
        } catch (Exception e) {
            logger.debug("Error when serializing output document.", e);
        }
    }

}