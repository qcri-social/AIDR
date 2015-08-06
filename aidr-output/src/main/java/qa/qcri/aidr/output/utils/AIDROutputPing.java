/**
 * @author Koushik Sinha
 * Last modified: 26/01/2014
 * 
 * Provides a method to test whether:
 * 		i) Connection to REDIS is available
 * 		ii) Services are returning non-null results
 * 
 *  Invocation:	host:port/context-root/rest/manage/ping?callback={callback}
 *  
 *  Example: 
 *  	1. localhost:8080/AIDROutput/rest/manage/ping
 *  	2. localhost:8080/AIDROutput/rest/manage/ping?callback=JSONP
 *  
 */
package qa.qcri.aidr.output.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import qa.qcri.aidr.common.code.Configurator;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;


@Path("/manage")
public class AIDROutputPing {

	public static JedisConnectionObject jedisConn;		// we need only a single instance of JedisConnectionObject running in background
	private static String host = "localhost";
	private static int port = 6379; 

	private static HashMap<String, Method>APIHashMap = null;

	// Debugging
	private static Logger logger = Logger.getLogger(AIDROutputPing.class);

	public AIDROutputPing() {
		this(host, port);
	}

	public AIDROutputPing(final String host, final int port) {

		Configurator configurator = OutputConfigurator.getInstance();
		AIDROutputPing.host = configurator.getProperty(OutputConfigurationProperty.REDIS_HOST);
		AIDROutputPing.port = Integer.parseInt(configurator.getProperty(OutputConfigurationProperty.REDIS_PORT));
		APIHashMap = new HashMap<String, Method>();

		// Register available REST APIs
		// TODO: Add code to register streaming API
		try {
			APIHashMap.put("fetch", qa.qcri.aidr.output.getdata.GetBufferedAIDRData.class.getMethod("getLatestBufferedAIDRData", 
					String.class , String.class, float.class, boolean.class));
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			logger.error(e);
		}

		// Initialize connection to REDIS DB
		jedisConn = new JedisConnectionObject(AIDROutputPing.host, AIDROutputPing.port);
	}

	/**
	 * 
	 * @param apiList List of API names to test
	 * @return true if test passed for all APIs, false otherwise
	 * 
	 */
	private boolean testAIDROutputAPI(String... apiList) {
		String apiResult = null;
		for (String s: apiList) {
			if (APIHashMap.containsKey(s)) {
				try {
					if (s.equalsIgnoreCase("fetch")) {
						Class<?> c = Class.forName("qa.qcri.aidr.output.getdata.GetBufferedAIDRData");
						Object t = c.newInstance();
						apiResult = APIHashMap.get(s).invoke(t, "JSONP", "1", 0, true).toString();
						// TODO: Add code for deeper testing of returned result?
					}	

					if (s.equalsIgnoreCase("stream")) {
						// TODO - implement code here
					}
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException 
						| InstantiationException | ClassNotFoundException | JedisConnectionException e) {
					apiResult = null;
					logger.error("Error! API not working");
				}
				if (null == apiResult) {
					return false;		// something broken - API not working
				}
			}
		}
		return true;
	}

	/**
	 * 
	 * @param callbackName JSONP callback name (optional)
	 * @return A JSONP response indicating status of service availability	
	 * 
	 */
	@GET
	@Path("/ping")
	@Produces("application/json")
	public Response pingAIDROutput(@QueryParam("callback") String callbackName) {
		//logger.info("[pingAIDROutput] Ping request received...");
		// First ensure that AIDROutput is up and running
		String result = null;
		Jedis jedis = null;
		try {
			jedis = jedisConn.getJedisResource();
			if (jedis != null) {
				result = jedis.ping();
				jedisConn.returnJedis(jedis);
			}
		} catch (JedisConnectionException e) {
			logger.error("Error! Couldn't establish connection to REDIS!", e);
		}
		StringBuilder jsonpRes = new StringBuilder();
		if (callbackName != null) jsonpRes.append(callbackName).append("(");

		if (null != result && result.equalsIgnoreCase("pong")) {
			// REDIS connection is working - now to test if APIs are workings
			boolean isAPIRunning = testAIDROutputAPI("fetch");
			if (isAPIRunning) { 
				String responseStr = "{\"application\":\"AIDROutput\", \"status\":\"RUNNING\"}";
				if (callbackName != null) {
					jsonpRes.append(responseStr).append(")");
				}
				else {
					jsonpRes.append(responseStr);
				}
				return Response.ok(jsonpRes.toString()).build();
			}
		}
		//logger.info("Unable to get response from REDIS DB - services may be down!");
		String responseStr = "{\"application\":\"AIDROutput\", \"status\":\"UNAVAILABLE\"}";
		if (callbackName != null) {
			jsonpRes.append(responseStr).append(")");
		}
		else {
			jsonpRes.append(responseStr);
		}
		return Response.ok(jsonpRes.toString()).build();
	}
}
