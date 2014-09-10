package qa.qcri.aidr.trainer.api.Jedis;

import org.apache.log4j.Logger;

import qa.qcri.aidr.common.logging.ErrorLog;
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
    String REDIS_HOST="localhost";
    static JedisPool jedisPool;
    
    private static Logger logger = Logger.getLogger(JedisDataStore.class);
    private static ErrorLog elog = new ErrorLog();
    
    /* REDIS */
    public static Jedis getJedisConnection() throws Exception {
        try {
            if (jedisPool == null) {

                jedisPool = new JedisPool(new JedisPoolConfig(), "localhost",6379);

            }
            return jedisPool.getResource();
        } catch (Exception e) {
            logger.error("Could not establish Redis connection. Is the Redis server running?");
            logger.error(elog.toStringException(e));
            throw e;
        }
    }

    public static void close(Jedis resource) {
        jedisPool.returnResource(resource);
    }


}
