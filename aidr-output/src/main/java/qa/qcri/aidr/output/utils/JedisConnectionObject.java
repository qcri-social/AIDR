package qa.qcri.aidr.output.utils;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import qa.qcri.aidr.output.getdata.ChannelBuffer;
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
	private static boolean connectionSetup = false;
	
	// Logger setup
	private static Logger logger = LoggerFactory.getLogger(JedisConnectionObject.class);

	/**
	 * 
	 * @param host hostname on which REDIS resides
	 * @param port port number to use for establishing connection
	 */
	public JedisConnectionObject(final String host, final int port) {
		// For now: set up a simple configuration that logs on the console
		//PropertyConfigurator.configure("log4j.properties");		// where to place the properties file?
		//BasicConfigurator.configure();							// initialize log4j logging
		System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");		// set logging level for slf4j

		allotedJedis = new ConcurrentHashMap<Jedis, Boolean>();
		redisHost = host;
		redisPort = port;
		synchronized (allotedJedis) {
			if (null == poolConfig) {
				poolConfig = new JedisPoolConfig();
				poolConfig.setMaxActive(200);
				poolConfig.setMaxIdle(50);
				poolConfig.setMinIdle(5);
				poolConfig.setTestWhileIdle(true);
				poolConfig.setTestOnBorrow(true);
				poolConfig.setTestOnReturn(true);
				poolConfig.numTestsPerEvictionRun = 10;
				poolConfig.timeBetweenEvictionRunsMillis = 60000;
				poolConfig.maxWait = 60000;
				poolConfig.whenExhaustedAction = org.apache.commons.pool.impl.GenericKeyedObjectPool.WHEN_EXHAUSTED_GROW;
				logger.debug("[connectToRedis] New Jedis poolConfig: " + poolConfig);
			} else {
				logger.debug("[connectToRedis] Reusing existing Jedis poolConfig: " + poolConfig);
			}
			if (null == pool) {
				try {
					pool = new JedisPool(poolConfig, redisHost, redisPort, 30000);
					poolSetup = true;
					logger.debug("[connectToRedis] New Jedis pool: " + pool);
				} catch (Exception e) {
					logger.debug("[connectToRedis] Fatal error! Could not initialize Jedis Pool!");
					poolConfig = null;
					pool = null;
					poolSetup = false;
				}
			} else {
				poolSetup = true;
				logger.debug("[connectToRedis] Reusing existing Jedis pool: " + pool);
			}
			allotedJedis.notifyAll();
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
	public synchronized Jedis getJedisResource() {
		Jedis subscriberJedis = null;
		if (isPoolSetup()) {
			try {
				subscriberJedis = pool.getResource();
				connectionSetup = true;
			} catch (Exception e) {
				subscriberJedis = null;
				connectionSetup = false;
				logger.error("[getJedisResource] Fatal error! Could not get a resource from the pool.");
			}
			if (subscriberJedis != null) {
				allotedJedis.put(subscriberJedis, false);		// initially nothing assigned
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
	public synchronized void returnJedis(Jedis jedisInstance) {
		if (pool != null) {
			try {
				if (null != jedisInstance) {
					if (allotedJedis != null) allotedJedis.remove(jedisInstance);
					pool.returnResource(jedisInstance);
				}
			} catch (JedisConnectionException e) {
				logger.error("[returnJedis] JedisConnectionException occurred...");
				pool.returnBrokenResource(jedisInstance);
			} finally {
				if (null != jedisInstance) 
					pool.returnResource(jedisInstance);
			}
		}
		this.notifyAll();
	}

	/**
	 * 
	 * @return active Jedis pool object
	 */
	public JedisPool getJedisPool() {
		return pool;
	}

	/**
	 * 
	 * @return active Jedis poolconfig object
	 */
	public JedisPoolConfig getJedisPoolConfig() {
		return poolConfig;
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
		return poolSetup;
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
		if (pool != null && poolConfig != null) {
			pool.destroy();
			pool = null;
			poolConfig = null;
			logger.info("[closeAll] Pool destroyed");
		}
		poolSetup = false;
	}
}
