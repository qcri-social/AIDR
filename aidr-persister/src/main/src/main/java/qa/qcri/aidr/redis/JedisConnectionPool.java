package qa.qcri.aidr.redis;


import qa.qcri.aidr.utils.Config;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 *
 * @author Imran
 */
public class JedisConnectionPool  {

    static JedisPool jedisPool;

    public Jedis getJedisConnection() throws Exception {		// koushik: removed static
        try {
            if (jedisPool == null) {
                JedisPoolConfig config = new JedisPoolConfig();
                config.maxActive = 1000;
                config.maxIdle = 10;
                config.minIdle = 1;
                config.maxWait = 30000;
                jedisPool = new JedisPool(config, Config.REDIS_HOST, Config.REDIS_PORT);
                
            }
            return jedisPool.getResource();
        } catch (Exception e) {
            System.out.println("Could not establish Redis connection. Is the Redis running?");
            throw e;
        }
    }

    public void close(Jedis resource) {		// koushik: removed static, added code to increase robustness
        //jedisPool.returnResource(resource);
        
        if (jedisPool != null) {
			try {
				if (null != resource) {
					jedisPool.returnResource(resource);
				}
			} catch (JedisConnectionException e) {
				jedisPool.returnBrokenResource(resource);
			} finally {
				if (null != resource) 
					jedisPool.returnResource(resource);
			}
		}
    }
}
