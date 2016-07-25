package qa.qcri.aidr.trainer.api.Jedis;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import qa.qcri.aidr.trainer.api.util.TrainerConfigurationProperty;
import qa.qcri.aidr.trainer.api.util.TrainerConfigurator;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/30/13
 * Time: 1:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class JedisDataStore {
	private static TrainerConfigurator configProperties = TrainerConfigurator.getInstance();
    static JedisPool jedisPool;
    
    private static Logger logger = Logger.getLogger(JedisDataStore.class);
    
    /* REDIS */
    public static Jedis getJedisConnection() throws Exception {
        try {
        	configProperties.getProperty(TrainerConfigurationProperty.REDIS_HOST);
            if (jedisPool == null) {
                jedisPool = new JedisPool(new JedisPoolConfig(), configProperties.getProperty(TrainerConfigurationProperty.REDIS_HOST), Integer.valueOf(configProperties.getProperty(TrainerConfigurationProperty.REDIS_PORT)));

            }
            return jedisPool.getResource();
        } catch (Exception e) {
            logger.error("Could not establish Redis connection. Is the Redis server running?",e);
            throw e;
        }
    }

    public static void close(Jedis resource) {
        jedisPool.returnResource(resource);
    }


}
