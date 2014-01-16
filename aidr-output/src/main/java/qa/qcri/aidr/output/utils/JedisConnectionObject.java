package qa.qcri.aidr.output.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class JedisConnectionObject {
	// Jedis related
	public static JedisPoolConfig poolConfig = null;
	public static JedisPool pool = null;
	public Jedis subscriberJedis = null;

	private String redisHost = null;
	private int redisPort = 6379;

	// Logger setup
	private static Logger logger = LoggerFactory.getLogger(JedisConnectionObject.class);

	public JedisConnectionObject(final String host, final int port) {
		redisHost = host;
		redisPort = port;
		// For now: set up a simple configuration that logs on the console
		//PropertyConfigurator.configure("log4j.properties");		// where to place the properties file?
		//BasicConfigurator.configure();							// initialize log4j logging
		System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");		// set logging level for slf4j
	}

	public JedisConnectionObject() {
		this("localhost", 6379);
	}
	
	public JedisConnectionObject(final int port) {
		this("localhost", port);
	}
	
	// Initialize JEDIS parameters and get connection resource from JEDIS pool
	public boolean connectToRedis() {
		if (null == poolConfig) {
			poolConfig = new JedisPoolConfig();
			poolConfig.setMaxActive(100);
			poolConfig.setMaxIdle(50);
			poolConfig.setMinIdle(5);
			poolConfig.setTestWhileIdle(true);
			poolConfig.setTestOnBorrow(true);
			poolConfig.setTestOnReturn(true);
			poolConfig.numTestsPerEvictionRun = 10;
			poolConfig.timeBetweenEvictionRunsMillis = 60000;
			poolConfig.maxWait = 3000;
			poolConfig.whenExhaustedAction = org.apache.commons.pool.impl.GenericKeyedObjectPool.WHEN_EXHAUSTED_GROW;
			logger.debug("[connectToRedis] New Jedis poolConfig: " + poolConfig);
		} else {
			logger.debug("[connectToRedis] Reusing existing Jedis poolConfig: " + poolConfig);
		}
		if (null == pool) {
			pool = new JedisPool(poolConfig, redisHost, redisPort, 10000);
			logger.debug("[connectToRedis] New Jedis pool: " + pool);
		} else {
			logger.debug("[connectToRedis] Reusing existing Jedis pool: " + pool);
		}
		subscriberJedis = pool.getResource();
		if (subscriberJedis != null) {
			return true;
		}
		return false;
	}

	public boolean initRedisConnection() { 
		subscriberJedis = pool.getResource();
		if (subscriberJedis != null) {
			logger.info("[initRedisConnection] Obtained Jedis object from pool");
			return true;
		}
		return false;
	}
	
	public void returnJedis() {
		try {
			if (null != subscriberJedis) 
				pool.returnResource(subscriberJedis);
			logger.info("[returnJedis] Pool resource returned");
		} catch (JedisConnectionException e) {
			logger.error("[returnJedis] JedisConnectionException occurred...");
			pool.returnBrokenResource(subscriberJedis);
			subscriberJedis = null;
		} finally {
			if (null != subscriberJedis) 
				pool.returnResource(subscriberJedis);
		}
	}
	
	public Jedis getJedis() {
		return subscriberJedis;
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
	
	public void finalize() {
		pool.destroy();
		poolConfig = null;
	}
}
