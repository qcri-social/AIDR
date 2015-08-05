package qa.qcri.aidr.collector.collectors;

import java.io.Closeable;
import java.io.IOException;
import java.util.logging.Logger;

import javax.json.JsonObject;

import org.apache.commons.lang3.text.translate.UnicodeEscaper;

import qa.qcri.aidr.collector.utils.CollectorConfigurator;
import qa.qcri.aidr.collector.utils.CollectorConfigurationProperty;
import qa.qcri.aidr.collector.utils.CollectorErrorLog;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * Jedis factory class to generate new Jedis instances.
 * Pulished JSON objects or text messages on Redis channels.
 */
public class JedisPublisher implements Closeable, Publisher {

	private static Logger logger = Logger.getLogger(JedisPublisher.class.getName());
	
	private static CollectorConfigurator configProperties=CollectorConfigurator.getInstance();
	private static UnicodeEscaper unicodeEscaper = UnicodeEscaper.above(127); 
	
	private static JedisPool jedisPool;
	static {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxIdle(40);
		poolConfig.setMinIdle(20);
		poolConfig.setMaxTotal(500);
		poolConfig.setTestOnBorrow(true);
		poolConfig.setTestOnReturn(true);
		poolConfig.setTestWhileIdle(true);
		poolConfig.setTimeBetweenEvictionRunsMillis(30000);
		jedisPool = new JedisPool(poolConfig, configProperties.getProperty(CollectorConfigurationProperty.REDIS_HOST), Integer.valueOf(configProperties.getProperty(CollectorConfigurationProperty.REDIS_PORT)), 0);
	}

	public static JedisPublisher newInstance() {
		try {
			Jedis jedis = jedisPool.getResource();
			logger.info("Allocated new jedis resource: " + jedis);
			return new JedisPublisher(jedis);
		} catch (JedisConnectionException e) {
			logger.severe("Could not establish Redis connection. Is Redis running?");
			CollectorErrorLog.sendErrorMail("Redis Connection", e.getMessage());
			throw e;
		}
	}
	
	private Jedis delegate;
	
	public Jedis getDelegate() {
		return delegate;
	}

	protected JedisPublisher(Jedis instance) {
		this.delegate = instance;
	}

	@Override
	public void close() throws IOException {
		logger.info("Returned jedis resource: " + delegate);
		jedisPool.returnResource(delegate);
	}

	@Override
	public void publish(String channel, JsonObject doc) {
		
		publish(channel, unicodeEscaper.translate(doc.toString()));
	}

	@Override
	public void publish(String channel, String message) {
		delegate.publish(channel, unicodeEscaper.translate(message));
	}
}
