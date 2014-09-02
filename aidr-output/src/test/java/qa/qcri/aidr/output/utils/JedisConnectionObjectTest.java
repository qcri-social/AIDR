package qa.qcri.aidr.output.utils;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import redis.clients.jedis.Jedis;

public class JedisConnectionObjectTest {
	//
	static JedisConnectionObject jedisConnectionObject;
	Jedis jedis;
	//
	@BeforeClass
	public static void setUp() throws Exception {
		jedisConnectionObject = new JedisConnectionObject();
	}
	//
	@Test
	public void getJedisResourceTest() {
		jedis = jedisConnectionObject.getJedisResource();
		//
		assertNotNull(jedis);
	}
	//
	@Test
	public void returnJedisTest() {
		if(jedis == null)
			jedis = jedisConnectionObject.getJedisResource();
		jedisConnectionObject.returnJedis(jedis);
		//
		assertNotNull(jedisConnectionObject.getJedisPool());
	}
	//
	@Test
	public void isPoolSetupTest() {
		boolean actual = jedisConnectionObject.isPoolSetup();
		//
		assertEquals(true, actual);
	}
	//
	@Test
	public void isConnectionSetupTest() {
		Jedis jedis = jedisConnectionObject.getJedisResource();
		boolean actual = jedisConnectionObject.isConnectionSetup();
		//
		assertEquals(true, actual);
	}
}
