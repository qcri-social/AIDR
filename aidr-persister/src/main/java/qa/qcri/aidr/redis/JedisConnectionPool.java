package qa.qcri.aidr.redis;


import qa.qcri.aidr.utils.Config;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 *
 * @author Imran
 */
public class JedisConnectionPool  {

    static JedisPool jedisPool;

    public static Jedis getJedisConnection() throws Exception {
        try {
            if (jedisPool == null) {
                JedisPoolConfig config = new JedisPoolConfig();
                config.maxActive = 1000;
                config.maxIdle = 10;
                config.minIdle = 1;
                config.maxWait = 30000;
                jedisPool = new JedisPool(config, Config.REDIS_HOST);
                
            }
            return jedisPool.getResource();
        } catch (Exception e) {
            System.out.println("Could not establish Redis connection. Is the Redis running?");
            throw e;
        }
    }

    public static void close(Jedis resource) {
        jedisPool.returnResource(resource);
    }
}
