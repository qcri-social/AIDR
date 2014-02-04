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

	// Logger setup
	private static Logger logger = LoggerFactory.getLogger(JedisConnectionObject.class);

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
				logger.info("[connectToRedis] New Jedis poolConfig: " + poolConfig);
			} else {
				logger.info("[connectToRedis] Reusing existing Jedis poolConfig: " + poolConfig);
			}
			if (null == pool) {
				pool = new JedisPool(poolConfig, redisHost, redisPort, 30000);
				poolSetup = true;
				logger.info("[connectToRedis] New Jedis pool: " + pool);
			} else {
				logger.info("[connectToRedis] Reusing existing Jedis pool: " + pool);
			}
		}
	}

	public JedisConnectionObject() {
		this("localhost", 6379);
	}

	public JedisConnectionObject(final int port) {
		this("localhost", port);
	}

	// Get connection resource from JEDIS pool
	public synchronized Jedis getJedisResource() {
		if (pool != null) {
			Jedis subscriberJedis = pool.getResource();
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
	 * Returns a specific instance of Jedis connection pool
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
	}

	public JedisPool getJedisPool() {
		return pool;
	}

	public JedisPoolConfig getJedisPoolConfig() {
		return poolConfig;
	}

	public String getRedisHost() {
		return redisHost;
	}

	public int getRedisPort() {
		return redisPort;
	}

	public boolean getPoolSetup() {
		return poolSetup;
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
