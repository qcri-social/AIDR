package qa.qcri.aidr.redis;


import org.apache.log4j.Logger;

import qa.qcri.aidr.logging.ErrorLog;
import qa.qcri.aidr.utils.Config;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

import static qa.qcri.aidr.utils.ConfigProperties.getProperty;

/**
 *
 * @author Imran
 */
public class JedisConnectionPool  {
	
	private static Logger logger = Logger.getLogger(JedisConnectionPool.class.getName());
	private static ErrorLog elog = new ErrorLog();
	
    static JedisPool jedisPool;
    
    public JedisConnectionPool() {
    	jedisPool = null;
    }

    public synchronized Jedis getJedisConnection() throws Exception {		// koushik: removed static
        try {
            if (jedisPool == null) {
                JedisPoolConfig config = new JedisPoolConfig();
                //config.maxActive = 1000;
                //config.maxIdle = 10;
                //config.minIdle = 1;
                config.setMaxTotal(1000);
				config.setMaxIdle(10);
				config.setMinIdle(1);
				config.setMaxWaitMillis(30000);
				
                jedisPool = new JedisPool(config, getProperty("REDIS_HOST"), Integer.parseInt(getProperty("REDIS_PORT")));
                
            }
            return jedisPool.getResource();
        } catch (Exception e) {
            logger.error("Could not establish Redis connection. Is the Redis running?");
            logger.error(elog.toStringException(e));
            throw e;
        }
    }

    public synchronized void close(Jedis resource) {		// koushik: removed static, added code to increase robustness
        //jedisPool.returnResource(resource);
        
        if (jedisPool != null) {
			try {
				if (resource != null) {
					jedisPool.returnResource(resource);
					resource = null;
				}
			} catch (JedisConnectionException e) {
				jedisPool.returnBrokenResource(resource);
				resource = null;
			} finally {
				if (resource != null) 
					jedisPool.returnResource(resource);
					resource = null;
			}
		}
    }
}
