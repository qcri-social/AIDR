package qa.qcri.aidr.collector.redis;

import java.net.SocketException;
import org.apache.log4j.Logger;
import qa.qcri.aidr.collector.logging.ErrorLog;

import qa.qcri.aidr.collector.utils.Config;
import qa.qcri.aidr.collector.logging.Loggable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import static qa.qcri.aidr.collector.utils.ConfigProperties.getProperty;

/**
 *
 * @author Imran
 */
public class JedisConnectionPool extends Loggable {

    private static Logger logger = Logger.getLogger(JedisConnectionPool.class);
    private static ErrorLog elog = new ErrorLog();
    
    static JedisPool jedisPool;

    public static Jedis getJedisConnection() throws Exception {
        try {
            if (jedisPool == null) {
                JedisPoolConfig poolConfig = new JedisPoolConfig();
                poolConfig.setMaxIdle(40);
                poolConfig.setMinIdle(20);
                poolConfig.setMaxTotal(500);
                poolConfig.setTestOnBorrow(true);
                poolConfig.setTestOnReturn(true);
                poolConfig.setTestWhileIdle(true);
                poolConfig.setTimeBetweenEvictionRunsMillis(30000);
                jedisPool = new JedisPool(poolConfig, getProperty("REDIS_HOST"), 6379, 0);
            }
            Jedis jedis  = jedisPool.getResource();
            logger.info("Allocated new jedis resource: " + jedis);
            return jedis;
        } catch (Exception e) {
            logger.error("Could not establish Redis connection. Is Redis running?");
            logger.error(elog.toStringException(e));
            throw e;
        }
    }

    public static void close(Jedis resource) {
        logger.info("Returned jedis resource: " + resource);
    	jedisPool.returnResource(resource);
    }
}
