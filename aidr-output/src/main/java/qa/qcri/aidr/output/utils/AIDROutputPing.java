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

import java.util.HashMap;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

//import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import qa.qcri.aidr.output.getdata.GetBufferedAIDRData;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;


@Path("/manage")
public class AIDROutputPing {

	public static JedisConnectionObject jedisConn;		// we need only a single instance of JedisConnectionObject running in background
	private static String host = "localhost";
	private static int port = 1978; 

	private static HashMap<String, Method>APIHashMap = null;

	// Debugging
	private static Logger logger = LoggerFactory.getLogger(AIDROutputPing.class);

	public AIDROutputPing() {
		this(host, port);
	}

	public AIDROutputPing(final String host, final int port) {
		
		AIDROutputConfig configuration = new AIDROutputConfig();
		HashMap<String, String> configParams = configuration.getConfigProperties();
		AIDROutputPing.host = configParams.get("host");
		AIDROutputPing.port = Integer.parseInt(configParams.get("port"));
		if (configParams.get("logger").equalsIgnoreCase("log4j")) {
			// For now: set up a simple configuration that logs on the console
			// PropertyConfigurator.configure("log4j.properties");      
			// BasicConfigurator.configure();    // initialize log4j logging
		}
		if (configParams.get("logger").equalsIgnoreCase("slf4j")) {
			System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");	// set logging level for slf4j
		}
		
		APIHashMap = new HashMap<String, Method>();

		// Register available REST APIs
		// TODO: Add code to register streaming API
		try {
			APIHashMap.put("fetch", qa.qcri.aidr.output.getdata.GetBufferedAIDRData.class.getMethod("getLatestBufferedAIDRData", 
					String.class , String.class));
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
						apiResult = APIHashMap.get(s).invoke(t, "JSONP", "1").toString();
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
			logger.error("Error! Couldn't establish connection to REDIS!");
			e.printStackTrace();
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
