/**
 * Managing JEDIS pool and allocating/deallocating JEDIS resources for connection to REDIS. 
 * 
 * @author Imran, Koushik
 */
package qa.qcri.aidr.redis;

import org.apache.log4j.Logger;

import qa.qcri.aidr.utils.PersisterConfigurationProperty;
import qa.qcri.aidr.utils.PersisterConfigurator;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;


public class JedisConnectionPool  {
	
	private static Logger logger = Logger.getLogger(JedisConnectionPool.class.getName());
	
    static JedisPool jedisPool;
    
    public JedisConnectionPool() {
    	jedisPool = null;
    }

    public synchronized Jedis getJedisConnection() {		// koushik: removed static
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
				
                jedisPool = new JedisPool(config, PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.REDIS_HOST), Integer.parseInt(PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.REDIS_PORT)));
                
            }
            return jedisPool.getResource();
        } catch (JedisConnectionException e) {
            logger.error("Could not establish Redis connection. Is the Redis running?");
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
