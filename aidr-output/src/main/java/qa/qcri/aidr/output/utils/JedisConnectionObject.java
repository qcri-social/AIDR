package qa.qcri.aidr.output.utils;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;


import org.apache.log4j.Logger;
import qa.qcri.aidr.common.logging.ErrorLog;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class JedisConnectionObject {
	// Connection management related
	public static ConcurrentHashMap<Jedis, Boolean> allotedJedis;	// active Jedis resources and their subscription type

	// Jedis related
	private static JedisPoolConfig poolConfig = null;	// only one JedisPoolConfig per servlet instance
	private static JedisPool pool = null;				// only one JedisPool per servlet instance
	private Jedis subscriberJedis = null;				// one for each GET request

	private static String redisHost = null;
	private static int redisPort = 6379;

	private static boolean poolSetup = false;
	private boolean connectionSetup = false;

	// Logger setup
	private static Logger logger = Logger.getLogger(JedisConnectionObject.class);
	private static ErrorLog elog = new ErrorLog();
	/**
	 * 
	 * @param host hostname on which REDIS resides
	 * @param port port number to use for establishing connection
	 */
	public JedisConnectionObject(final String host, final int port) {
		// For now: set up a simple configuration that logs on the console
		//PropertyConfigurator.configure("log4j.properties");		// where to place the properties file?
		//BasicConfigurator.configure();							// initialize log4j logging
		//System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");		// set logging level for slf4j

		allotedJedis = new ConcurrentHashMap<Jedis, Boolean>();
		redisHost = host;
		redisPort = port;
		//synchronized (allotedJedis) 
		{
			if (null == JedisConnectionObject.poolConfig) {
				JedisConnectionObject.poolConfig = new JedisPoolConfig();
				JedisConnectionObject.poolConfig.setMaxTotal(200);
				JedisConnectionObject.poolConfig.setMaxIdle(50);
				JedisConnectionObject.poolConfig.setMinIdle(10);
				JedisConnectionObject.poolConfig.setMaxWaitMillis(3000);
				//JedisConnectionObject.poolConfig.setMaxActive(200);
				//poolConfig.setTestWhileIdle(true);
				//poolConfig.setTestOnBorrow(true);
				//poolConfig.setTestOnReturn(true);
				//poolConfig.numTestsPerEvictionRun = 10;
				//poolConfig.timeBetweenEvictionRunsMillis = 3000;
				//JedisConnectionObject.poolConfig.whenExhaustedAction = org.apache.commons.pool.impl.GenericKeyedObjectPool.WHEN_EXHAUSTED_GROW;
			
				logger.info("New Jedis poolConfig: " + JedisConnectionObject.poolConfig);
			} else {
				logger.info("Reusing existing Jedis poolConfig: " + JedisConnectionObject.poolConfig);
			}
			if (null == JedisConnectionObject.pool) {
				try {
					JedisConnectionObject.pool = new JedisPool(JedisConnectionObject.poolConfig, redisHost, redisPort, 30000);
					JedisConnectionObject.poolSetup = true;
					logger.info("New Jedis pool: " + pool);
				} catch (Exception e) {
					logger.error("Fatal error! Could not initialize Jedis Pool!");
					logger.error(elog.toStringException(e));
					JedisConnectionObject.poolConfig = null;
					JedisConnectionObject.pool = null;
					JedisConnectionObject.poolSetup = false;
				}
			} else {
				JedisConnectionObject.poolSetup = true;
				logger.info("Reusing existing Jedis pool: " + JedisConnectionObject.pool);
			}
			//allotedJedis.notifyAll();
		}
	}

	/**
	 * Connects to a REDIS DB on default port 6379 of localhost
	 */
	public JedisConnectionObject() {
		this("localhost", 6379);
	}

	/**
	 * Connects to a REDIS DB on lcoalhost with specified port
	 * @param port port number to use for connecting to REDIS DB
	 */
	public JedisConnectionObject(final int port) {
		this("localhost", port);
	}

	/**
	 *  Get a connection resource from JEDIS pool
	 * @return
	 */
	public Jedis getJedisResource() {
		Jedis subscriberJedis = null;
		if (isPoolSetup()) {
			try {
				subscriberJedis = JedisConnectionObject.pool.getResource();
				connectionSetup = true;
			} catch (Exception e) {
				subscriberJedis = null;
				connectionSetup = false;
				logger.error("Fatal error! Could not get a resource from the pool.");
				logger.error(elog.toStringException(e));
			}
			if (subscriberJedis != null) {
				allotedJedis.put(subscriberJedis, false);		// initially nothing assigned
				logger.info("Allocating jedis resource to caller: " + subscriberJedis);
				return subscriberJedis;
			}
		}
		return null;
	}

	/**
	 * @param jedis Jedis connection resource
	 * @param patternFlag subscription type associated with the Jedis connection 
	 * resource: true = pattern subscription, false = single channel subscription 
	 */
	public void setJedisSubscription(Jedis jedis, boolean patternFlag) {
		if (jedis != null) {
			allotedJedis.put(jedis, patternFlag);
		}
	}

	/**
	 * @param jedis Jedis connection resource
	 * @return subscription type associated with the Jedis connection 
	 * resource: true = pattern subscription, false = single channel subscription
	 */
	public boolean getJedisSubscription(Jedis jedis) {
		if (jedis != null) {
			return allotedJedis.get(jedis);
		}
		return false;
	}

	/**
	 * Returns to the Jedis pool a specific instance of allocated Jedis connection resource
	 * @param jedisInstance a Jedis instance borrowed from the Jedis Pool
	 */
	public void returnJedis(Jedis jedisInstance) {
		if (JedisConnectionObject.pool != null) {
			try {
				if (null != jedisInstance && jedisInstance.isConnected()) {
					if (allotedJedis != null) allotedJedis.remove(jedisInstance);
					logger.info("[returnJedis] Returned jedis resource: " + jedisInstance);
					jedisInstance.close();
					jedisInstance = null;
					//JedisConnectionObject.pool.returnResource(jedisInstance);
					connectionSetup = false;
				}
			} catch (JedisConnectionException e) {
				logger.error("JedisConnectionException occurred...");
				logger.error(elog.toStringException(e));
				if (jedisInstance != null && jedisInstance.isConnected()) jedisInstance.close();
				jedisInstance = null;
				//JedisConnectionObject.pool.returnBrokenResource(jedisInstance);
				connectionSetup = false;
			} 
		}
	}

	/**
	 * 
	 * @return active Jedis pool object
	 */
	public JedisPool getJedisPool() {
		return JedisConnectionObject.pool;
	}

	/**
	 * 
	 * @return active Jedis poolconfig object
	 */
	public JedisPoolConfig getJedisPoolConfig() {
		return JedisConnectionObject.poolConfig;
	}

	/**
	 * 
	 * @return Redis hostname being used
	 */
	public String getRedisHost() {
		return redisHost;
	}

	/**
	 * 
	 * @return port number being used to connect to Redis
	 */
	public int getRedisPort() {
		return redisPort;
	}

	/**
	 * Returns the state of Jedis pool for this JedisConnectionObject instance
	 * @return true is Jedis pool has been established, false otherwise
	 */
	public boolean isPoolSetup() {
		return JedisConnectionObject.poolSetup;
	}

	/**
	 * Returns the state of Jedis connection for this JedisConnectionObject instance
	 * @return true is Jedis connection has been established, false otherwise
	 */
	public boolean isConnectionSetup() {
		return connectionSetup;
	}

	/**
	 * Closes all open jedis connections and destroys the Jedis pool. 
	 * Warning! Not thread safe! Use with care!
	 */
	public void closeAll() {
		if (allotedJedis != null) {
			Iterator<Jedis>itr = allotedJedis.keySet().iterator();
			while (itr.hasNext()) {
				//stopSubscription(this.aidrSubscriber, this.subscriberJedis);
				Jedis j = itr.next();
				returnJedis(j);
			}
			allotedJedis.clear();
			allotedJedis = null;
		}
		if (JedisConnectionObject.pool != null && JedisConnectionObject.poolConfig != null) {
			JedisConnectionObject.pool.destroy();
			JedisConnectionObject.pool = null;
			JedisConnectionObject.poolConfig = null;
			logger.info("Pool destroyed");
		}
		JedisConnectionObject.poolSetup = false;
	}
}
