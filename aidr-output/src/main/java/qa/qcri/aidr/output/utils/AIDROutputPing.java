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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static qa.qcri.aidr.output.utils.ConfigProperties.getProperty;

import org.apache.log4j.Logger;

import qa.qcri.aidr.common.code.ResponseWrapperNEW;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;


@Path("/manage")
public class AIDROutputPing {

	public static JedisConnectionObject jedisConn;		// we need only a single instance of JedisConnectionObject running in background
	private static String host = "localhost";
	private static int port = 1978; 

	private static HashMap<String, Method>APIHashMap = null;

	// Debugging
	private static Logger logger = Logger.getLogger(AIDROutputPing.class);

	public AIDROutputPing() {
		this(host, port);
	}

	public AIDROutputPing(final String host, final int port) {

		//		AIDROutputConfig configuration = new AIDROutputConfig();
		//		HashMap<String, String> configParams = configuration.getConfigProperties();
		AIDROutputPing.host = getProperty("host");
		AIDROutputPing.port = Integer.parseInt(getProperty("port"));
		/*
		if (configParams.get("logger").equalsIgnoreCase("log4j")) {
			// For now: set up a simple configuration that logs on the console
			// PropertyConfigurator.configure("log4j.properties");      
			// BasicConfigurator.configure();    // initialize log4j logging
		}
		if (configParams.get("logger").equalsIgnoreCase("slf4j")) {
			System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");	// set logging level for slf4j
		}
		 */
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
			logger.error("Error! Couldn't establish connection to REDIS!");
			logger.error(e);
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

	static String consumerKeyStr = "****";
	static String consumerSecretStr = "****";
	static String accessTokenStr = "****";
	static String accessTokenSecretStr = "****";


	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/username")
	public Response getTwitterUserData(TwitterUserLookUp userList) {
		String[] userNameList = null;
		long[] userIdList = null;
		if (userList != null) {
			if (userList.getUserNames() != null && !userList.getUserNames().isEmpty()) {
				try {
					userNameList = new String[userList.getUserNames().size()];
					int i = 0;
					for (String user: userList.getUserNames()) {
						userNameList[i] = user;
						//System.out.println("Going to fetch twitter IDs for the following set of screen names: " + userNameList[i]);
						++i;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (userList.getUserIds() != null && !userList.getUserIds().isEmpty()) {
				try {
					userIdList = new long[userList.getUserIds().size()];
					int i = 0;
					for (Long id: userList.getUserIds()) {
						userIdList[i] = id.longValue();
						//System.out.println("Going to fetch twitter IDs for the following set of screen IDs: " + userIdList[i]);
						++i;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		List<User> dataList = new ArrayList<User>();
		dataList.addAll(getUserDataFromScreenName(userNameList));
		dataList.addAll(getUserDataFromTwitterID(userIdList));
		
		if (!dataList.isEmpty()) {
			return Response.ok(dataList).build();
		}
		else {
			ResponseWrapperNEW response = new qa.qcri.aidr.common.code.ResponseWrapperNEW();
			response.setReturnCode("FETCH_TWITTER_ERROR");
			response.setDeveloperMessage("Error in twitter user data lookup");
			response.setUserMessages("Twitter user data lookup unsuccessful");
			return Response.ok(response).build();
		}

	}

	private List<User> getUserDataFromScreenName(String[] userNameList)	{		
		if (userNameList != null) {
			//System.out.println("input array size = " + userNameList.length);
			try {
				Twitter twitter = new TwitterFactory().getInstance();

				twitter.setOAuthConsumer(consumerKeyStr, consumerSecretStr);
				AccessToken accessToken = new AccessToken(accessTokenStr, accessTokenSecretStr);

				twitter.setOAuthAccessToken(accessToken);
				final int batchSize = 100;
				String[] batchList = new String[Math.min(userNameList.length, batchSize)];
				ResponseList<User> list = null; 
				for (int i = 0; i < userNameList.length;i = i + batchSize) {
					//System.out.println("i = " + i + ", user: " + userNameList[i] + ", size: " + Math.min(userNameList.length, batchSize));
					System.arraycopy(userNameList, i, batchList, 0, Math.min(userNameList.length, batchSize));
					ResponseList<User> tempList = twitter.lookupUsers(batchList);
					if (null == list) {
						list = tempList;
					} else {
						list.addAll(tempList);
					}
					//System.out.println("done lookup : " + i);
				}
				System.out.println("Successfully looked up in Twitter by screen name: " + (list != null ? list.size() : "null"));
				return list;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new ArrayList<User>();
	}

	private List<User> getUserDataFromTwitterID(long[] userIdList)	{		
		if (userIdList != null) {
			//System.out.println("input array size = " + userIdList.length);
			try {
				Twitter twitter = new TwitterFactory().getInstance();

				twitter.setOAuthConsumer(consumerKeyStr, consumerSecretStr);
				AccessToken accessToken = new AccessToken(accessTokenStr, accessTokenSecretStr);

				twitter.setOAuthAccessToken(accessToken);
				final int batchSize = 100;
				long[] batchList = new long[Math.min(userIdList.length, batchSize)];
				ResponseList<User> list = null; 
				for (int i = 0; i < userIdList.length;i = i + batchSize) {
					//System.out.println("i = " + i + ", user: " + userIdList[i] + ", size: " + Math.min(userIdList.length, batchSize));
					System.arraycopy(userIdList, i, batchList, 0, Math.min(userIdList.length, batchSize));
					ResponseList<User> tempList = twitter.lookupUsers(batchList);
					if (null == list) {
						list = tempList;
					} else {
						list.addAll(tempList);
					}
					//System.out.println("done lookup : " + i);
				}
				System.out.println("Successfully looked up in Twitter by ID: " + (list != null ? list.size() : "null"));
				return list;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new ArrayList<User>();
	}
}
