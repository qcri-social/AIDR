package qa.qcri.aidr.trainer.api.Jedis;

//import org.apache.log4j.Logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import qa.qcri.aidr.common.logging.ErrorLog;
import redis.clients.jedis.Jedis;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 10/2/13
 * Time: 7:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class JedisNotifier {
    protected static Logger logger = LoggerFactory.getLogger(JedisNotifier.class);
    private static ErrorLog elog = new ErrorLog();
    
    Jedis jedis;

    public JedisNotifier() {
        try {
            jedis = JedisDataStore.getJedisConnection();
        } catch (Exception e) {
        	logger.error("failed to create jedis connection");
            logger.error(elog.toStringException(e));
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
            logger.error("Error when serializing output document.");
            logger.error(elog.toStringException(e));
        }
    }

}