/**
 * This code makes a HTTP request to get the last 'n' JSONP data 
 * from a REDIS DB to a client using a servlet. After sending the data, it 
 * closes the connection. The data from the REDIS channels are buffered in 
 * the background by a long running buffering system - started at servlet startup
 * The jsonp messages are returned in an ArrayList data structure, in reverse chronological order.
 * 
 * The code accepts i) channel name or, ii) fully qualified channel name. However, wildcard '*' for
 * pattern based subscription are NOT allowed.
 * 
 * @author Koushik Sinha
 * Last modified: 14/01/2014
 *
 * Dependencies:  servlets 3+, jedis-2.2.1, gson-2.2.4, commons-pool-1.6, slf4j-1.7.5, JAX-RS 2.0, jersey 2.0+
 * 	
 * Hints for testing:
 * 		1. Tune the socket timeout parameter in JedisPool(...) call if connecting over a slow network
 *  	2. Tune REDIS_CALLBACK_TIMEOUT, in case the rate of publication is very slow
 *  	3. Tune the number of threads in ExecutorService 	 
 *
 * Deployment steps: 
 * 		1. [Required] Set redisHost and redisPort in code, as per your REDIS setup/location
 * 		2. [Optional] Tune time-out and other parameters, if necessary
 * 		3. [Required]Compile and package as WAR file
 * 		4. [Required] Deploy as WAR file in glassfish 3.1.2
 * 		5. [Optional] Setup ssh tunneling (e.g. command: ssh tunneling:: ssh -f -L 1978:localhost:6379 scd1.qcri.org -N)
 * 		6. Issue getLast request from client
 *
 *
 * Invocation:	host:port/context-root/rest/crisis/fetch/channel/{crisisCode}?callback={callback}&count={count} 
 * ============	
 * Channel name based examples: 
 *  1. http://localhost:8080/AIDROutput/rest/crisis/fetch/channel/clex_20131201?count=50
 *  2. http://localhost:8080/AIDROutput/rest/crisis/fetch/channel/clex_20131201?callback=JSONP
 *  3. http://localhost:8080/AIDROutput/rest/crisis/fetch/channel/clex_20131201?callback=JSONP&count=50
 *  
 * Fully qualified channel name based examples: 
 *  1. http://localhost:8080/AIDROutput/rest/crisis/fetch/channel/aidr_predict.clex_20131201?count=50
 *  2. http://localhost:8080/AIDROutput/rest/crisis/fetch/channel/aidr_predict.clex_20131201?callback=func
 *  3. http://localhost:8080/AIDROutput/rest/crisis/fetch/channel/aidr_predict.clex_20131201?callback=func&count=50
 * 
 * Apart from the above valid paths one can use:
 * 	1. http://localhost:8080/AIDROutput/rest/crisis/fetch/channels/list     => returns list of active channels
 * 	2. http://localhost:8080/AIDROutput/rest/crisis/fetch/channels/latest	=> returns the latest tweet data from  across all channels
 *  
 *  Parameter explanations:
 *  	1. crisisCode [mandatory]: the REDIS channel to which to subscribe
 *  	2. callback [optional]: name of the callback function for JSONP data
 *  	3. count [optional]: the specified number of messages that have been buffered by the service. If unspecified
 *  		or <= 0 or larger than the MAX_MESSAGES_COUNT, the default number of messages are returned  
 */

package qa.qcri.aidr.output.getdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


//import org.apache.log4j.BasicConfigurator;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;



import org.apache.log4j.Logger;

import com.google.gson.JsonObject;

import qa.qcri.aidr.output.filter.ClassifiedFilteredTweet;
import qa.qcri.aidr.output.filter.FilterQueryMatcher;
import qa.qcri.aidr.output.filter.JsonQueryList;
import qa.qcri.aidr.output.filter.NominalLabel;
import qa.qcri.aidr.output.utils.AIDROutputConfig;
import qa.qcri.aidr.output.utils.JsonDataFormatter;
import qa.qcri.aidr.output.utils.SimpleRateLimiter;
import qa.qcri.aidr.output.filter.DeserializeFilters;
import qa.qcri.aidr.output.utils.ErrorLog;


@Path("/crisis/fetch/")
public class GetBufferedAIDRData implements ServletContextListener {

	// Debugging
	private static Logger logger = Logger.getLogger(GetBufferedAIDRData.class.getName());
	private static ErrorLog elog = new ErrorLog();

	// Related to channel buffer management
	private static final String CHANNEL_REG_EX = "aidr_predict.*";
	private static final String CHANNEL_PREFIX_STRING = "aidr_predict.";
	private static final int MAX_MESSAGES_COUNT = 1000;
	private static final int DEFAULT_COUNT = 50;		// default number of messages to fetch
	private static final String DEFAULT_COUNT_STR = "50";

	private static SimpleRateLimiter channelSelector = null;		// select a channel to display
	private volatile static StringBuffer lastSentLatestTweet = null;
	private volatile static long lastSentTimeGetLatestData = 0;
	private static final int CACHE_TIME_INTERVAL = 0;		// cache time for getLatest
	private volatile static AtomicInteger inRequests = null;
	private final int MAX_IN_REQUESTS = 1000;

	private volatile static ChannelBufferManager masterCBManager = null; 			// managing buffers for each publishing channel
	private static final boolean rejectNullFlag = true;


	/////////////////////////////////////////////////////////////////////////////

	/**
	 * 
	 * @return Returns list of active channels
	 */
	@GET
	@Path("/channels/list")
	@Produces("text/html")
	public Response getActiveChannelsList() {

		Set<String> channelList = masterCBManager.getActiveChannelsList();
		StringBuilder htmlMessageString = new StringBuilder();

		// Build HTML doc to return
		htmlMessageString.append("<!DOCTYPE html>");
		htmlMessageString.append("<html>");
		htmlMessageString.append("<head><title>REDIS PUBSUB Channel Data Output Service</title></head>");
		htmlMessageString.append("<body>");
		htmlMessageString.append("<p><big>Available active channels: </big></p>");
		htmlMessageString.append("<ul>"); 
		if (channelList != null) {
			Iterator<String> itr = channelList.iterator();
			while (itr.hasNext()) {
				htmlMessageString.append("<li>" + itr.next().substring(CHANNEL_PREFIX_STRING.length()) + "</li>");
			}
		}
		htmlMessageString.append("</body></html>");
		if (channelList != null) channelList.clear();
		channelList = null;

		return Response.ok(htmlMessageString.toString()).build();
	}

	/**
	 * 
	 * @param callbackName  JSONP callback name
	 * @param count number of messages to fetch
	 * @param confidence minimum confidence threshold across all classifiers of a tweet
	 * @return the latest tweet data as a jsonp object from across all active channels
	 * subject to maximum confidence across all classifiers of a tweet >= confidence
	 */
	@GET
	@Path("/channels/latest")
	@Produces("application/json")
	public Response getLatestBufferedAIDRData(@QueryParam("callback") String callbackName,
			@DefaultValue("1") @QueryParam("count") String count,
			@DefaultValue("0.7") @QueryParam("confidence") float confidence,
			@DefaultValue("true") @QueryParam("balanced_sampling") boolean balanced_sampling) {

		//System.out.println("[getLatestBufferedAIDRData] request received");
		//return Response.ok(new String("[{}]")).build();		// NO-OP test

		// Cache data - performance reasons
		if ((inRequests.incrementAndGet() > MAX_IN_REQUESTS) 
				|| ((System.currentTimeMillis() - lastSentTimeGetLatestData) < CACHE_TIME_INTERVAL)) {

			if (inRequests.get() > MAX_IN_REQUESTS) {
				logger.warn("WARNING! We already have max number of readers : " + inRequests.get());
			} else {
				logger.warn("Not sufficient time elapsed between successive requests");
			}
			if (lastSentLatestTweet != null && lastSentLatestTweet.length() > 0) {
				logger.warn("Sending cached data to requester: " + lastSentLatestTweet);
				inRequests.decrementAndGet();
				return Response.ok(lastSentLatestTweet.toString()).build();
			} else {
				logger.warn("Returning null JSON as there is no data in buffers");
				inRequests.decrementAndGet();
				return returnEmptyJson(callbackName);		// NO-OP condition, since nothing to send
			}
		}

		// Otherwise, go the full way...
		//logger.info("Attempting to fetch from buffered data, for requester = " + inRequests.get());
		//long startTime = System.currentTimeMillis();
		ChannelBufferManager cbManager = new ChannelBufferManager();

		if (null != cbManager.jedisConn && cbManager.jedisConn.isPoolSetup()) {		// Jedis pool is ready
			// Get the last count number of messages for channel=channelCode
			final int messageCount = Integer.parseInt(count);		// number of latest messages across all channels to return
			//long t = System.currentTimeMillis();
			List<String> bufferedMessages = cbManager.getLatestFromAllChannels(messageCount);
			//logger.info("Total time taken to retrieve from buffers = " + (System.currentTimeMillis() - t));
			
			// Added code for filteredMessages as per new feature: pivotal #67373070
			List<String> filteredMessages = null;			

			if (bufferedMessages != null) {
				//logger.info("Buffered messages list size = " + bufferedMessages.size());
				Map<Long, String> sortedFilteredMessages = new TreeMap<Long, String>();
				//long t0 = System.currentTimeMillis();
				for (String tweet: bufferedMessages) {
					//long t1 = System.currentTimeMillis();
					ClassifiedFilteredTweet classifiedTweet = new ClassifiedFilteredTweet().deserialize(tweet);
					//logger.info("Time taken to deserialize a tweet = " + (System.currentTimeMillis() - t1));
					if (classifiedTweet != null && classifiedTweet.getMaxConfidence() >= confidence
							&& classifiedTweet.getCreatedAt() != null) {
						sortedFilteredMessages.put(classifiedTweet.getCreatedAt().getTime(), tweet);		// note: we may override duplicate key-values but that is acceptable in our use-case
						channelSelector.initializeNew(classifiedTweet.getCrisisCode());	
						//logger.info("Added tweet from channel " + classifiedTweet.getCrisisCode() + ", confidence: " + classifiedTweet.getMaxConfidence());
					}
				}
				//logger.info("Total time taken to deserialize all tweets = " + (System.currentTimeMillis() - t0));
				
				// Now sort the dataSet in ascending order of timestamp
				//long t3 = System.currentTimeMillis();
				//QuickSort sorter = new QuickSort();
				//sorter.sort(filteredMessages);
				
				filteredMessages = new ArrayList<String>(sortedFilteredMessages.values());
				sortedFilteredMessages.clear();
				//logger.info("Finished sorting the list in time = " + (System.currentTimeMillis() - t3));
			}

			final JsonDataFormatter taggerOutput = new JsonDataFormatter(callbackName);	// Tagger specific JSONP output formatter
			StringBuilder jsonDataList = null;
			//long t4 = System.currentTimeMillis();
			if (!balanced_sampling) {
				jsonDataList = taggerOutput.createList(filteredMessages, messageCount, rejectNullFlag);
			} else {
				//logger.info("Going for Rate Limited, buffer size = " + filteredMessages.size());
				jsonDataList = taggerOutput.createRateLimitedList(filteredMessages, channelSelector, messageCount, rejectNullFlag);
			}
			int sendCount = taggerOutput.getMessageCount();
			//logger.info("Total time taken to create send List = " + (System.currentTimeMillis() - t4));

			if (0 == sendCount) {
				// Nothing to send = so send the last sent data again!
				if (lastSentLatestTweet != null && lastSentLatestTweet.length() > 0) { 
					jsonDataList.replace(0, jsonDataList.length(), lastSentLatestTweet.toString());
					sendCount = 1;
					//logger.warn("[getLatestBufferedAIDRData] Warning, sending cached last sent data: " + lastSentLatestTweet);
				}
			} else {
				// Note: we risk thread-unsafe operation here		
				lastSentLatestTweet = new StringBuffer(jsonDataList.length());
				lastSentLatestTweet.append(jsonDataList);
			}
			lastSentTimeGetLatestData = System.currentTimeMillis();

			//logger.info("send count = " + sendCount);
			//System.out.println("[getLatestBufferedAIDRData] send count = " + sendCount);
			//System.out.println("[getLatestBufferedAIDRData] sent data: " + jsonDataList);

			// Finally, send the retrieved list to client and close connection
			inRequests.decrementAndGet();
			//logger.info("Time taken to process request: " + (System.currentTimeMillis() - startTime));
			return Response.ok(jsonDataList.toString()).build();
		}
		inRequests.decrementAndGet();
		logger.error("Error in jedis connection. Bailing out...");
		System.err.println("[getLatestBufferedAIDRData] Error in jedis connection. Bailing out...");
		return returnEmptyJson(callbackName);
	}


	/**
	 * 
	 * @param callbackName  JSONP callback name
	 * @param count  number of buffered messages to fetch
	 * @return returns the 'count' number of buffered messages from requested channel as jsonp data 
	 */
	@GET
	@Path("/channel/{crisisCode}")
	@Produces({"application/json"})
	public Response getBufferedAIDRData(@PathParam("crisisCode") String channelCode,
			@QueryParam("callback") String callbackName,
			@DefaultValue(DEFAULT_COUNT_STR) @QueryParam("count") String count) {

		System.out.println("[getBufferedAIDRData] request received");
		ChannelBufferManager cbManager = new ChannelBufferManager();
		if (null != cbManager.jedisConn && cbManager.jedisConn.isPoolSetup()) {
			boolean error = false;
			// Parse the HTTP GET request and generating results for output
			// Set the response MIME type of the response message
			if (null == channelCode) {
				error = true;
			}
			if (!error && channelCode.contains("*")) {
				// Got a wildcard fetch request - fetch from all channels
				return getLatestBufferedAIDRData(callbackName, count, (float) 0.0, false);
			}
			if (channelCode != null && channelCode.contains("?")) { 
				error = true;
			}
			if (error)
			{	
				logger.warn("Error in requested channel name: " + channelCode);
				return Response.ok(new String("[{}]")).build();
			}
			else {
				// Form fully qualified channelName and get other parameter values, if any
				String channelName = null;
				if (channelCode.startsWith(CHANNEL_PREFIX_STRING) || channelCode.contains(".")) {
					channelName = channelCode;		// fully qualified channel name provided
				}
				else {
					channelName = CHANNEL_PREFIX_STRING.concat(channelCode);	// fully qualified channel name - same as REDIS channel
				}
				if (isChannelPresent(channelName)) {
					int msgCount = Integer.parseInt(count);
					int messageCount = DEFAULT_COUNT;
					if (msgCount > 0) {
						messageCount = Math.min(msgCount, MAX_MESSAGES_COUNT);
					}
					// Get the last messageCount messages for channel=channelCode
					List<String> bufferedMessages = cbManager.getLastMessages(channelName, messageCount);
					StringBuilder jsonDataList = null;
					int sendCount = 0;
					if (bufferedMessages != null) {
						final JsonDataFormatter taggerOutput = new JsonDataFormatter(callbackName);	// Tagger specific JSONP output formatter
						jsonDataList = taggerOutput.createList(bufferedMessages, messageCount, rejectNullFlag);
						sendCount = taggerOutput.getMessageCount();
					}

					System.out.println(channelCode + " : sending jsonp data, count = " + sendCount);
					if (jsonDataList != null) {
					return Response.ok(jsonDataList.toString()).build(); 
					} else {
						return returnEmptyJson(callbackName);
					}
				}
				else {
					return returnEmptyJson(callbackName);
				}
			}
		}
		logger.error(channelCode + ": error in jedis connection. Bailing out...");
		return returnEmptyJson(callbackName);
	}

	@OPTIONS
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/channel/filter/{crisisCode}")
	public Response getBufferedAIDRDataPostFilter(@PathParam("crisisCode") String channelCode,
			@QueryParam("callback") String callbackName,
			@DefaultValue(DEFAULT_COUNT_STR) @QueryParam("count") String count) {
		return Response.ok()
				.allow("POST", "GET", "PUT", "UPDATE", "OPTIONS", "HEAD")
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS, HEAD")
				.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
				.build();
	}


	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/channel/filter/{crisisCode}")
	public Response getBufferedAIDRDataPostFilter(String queryString, @PathParam("crisisCode") String channelCode,
			@QueryParam("callback") String callbackName,
			@DefaultValue(DEFAULT_COUNT_STR) @QueryParam("count") String count) {

		logger.info("Request received for :" + channelCode);
		logger.debug(channelCode + ": received json string: " + queryString);
		DeserializeFilters des = new DeserializeFilters();
		JsonQueryList queryList = des.deserializeConstraints(queryString);

		if (queryList != null) {
			logger.info(channelCode + ": received POST list = " + queryList.toString());
		} else {
			logger.info(channelCode + ": received POST list = " + queryList);
			//queryList = new JsonQueryList();
		}
		ChannelBufferManager cbManager = new ChannelBufferManager();
		if (null != cbManager.jedisConn && cbManager.jedisConn.isPoolSetup()) {
			boolean error = false;
			// Parse the HTTP GET request and generating results for output
			// Set the response MIME type of the response message
			if (null == channelCode) {
				error = true;
			}
			if (!error && (channelCode.contains("?") || channelCode.contains("*"))) { 
				error = true;
			}
			if (!error)
			{	
				// Form fully qualified channelName and get other parameter values, if any
				String channelName = null;
				if (channelCode.startsWith(CHANNEL_PREFIX_STRING) || channelCode.contains(".")) {
					channelName = channelCode;		// fully qualified channel name provided
				}
				else {
					channelName = CHANNEL_PREFIX_STRING.concat(channelCode);	// fully qualified channel name - same as REDIS channel
				}
				if (isChannelPresent(channelName)) {
					//logger.info("Going for channel data fetch: " + channelName);
					int msgCount = Integer.parseInt(count);
					int messageCount = DEFAULT_COUNT;
					if (msgCount > 0) {
						messageCount = Math.min(msgCount, MAX_MESSAGES_COUNT);
					}
					// Get the last messageCount messages for channel=channelCode
					List<String> bufferedMessages = cbManager.getLastMessages(channelName, messageCount);
					if (bufferedMessages != null) {
						//logger.info("Fetched unfiltered message List: " + bufferedMessages.size());
						// Now filter the retrieved bufferedMessages list
						FilterQueryMatcher tweetFilter = new FilterQueryMatcher();
						if (queryList != null) tweetFilter.queryList.setConstraints(queryList);
						tweetFilter.buildMatcherArray();

						// Now to serially filter each tweet in the bufferedMessages list
						List<String> filteredMessages = new ArrayList<String>();
						if (null == queryList || queryList.getConstraints().isEmpty()) {
							//|| (queryList.getConstraints().get(0).queryType != QueryType.classifier_query
							//&& queryList.getConstraints().get(0).queryType != QueryType.date_query)) {
							// default behavior - no filtering if no POST payload
							logger.info(channelCode + ": no filtering...");
							filteredMessages.addAll(bufferedMessages);
						} else {
							for (String tweet: bufferedMessages) {
								ClassifiedFilteredTweet classifiedTweet = new ClassifiedFilteredTweet().deserialize(tweet);
								if (classifiedTweet != null && tweetFilter.getMatcherResult(classifiedTweet)) {
									//logger.info(channelCode + ": adding tweet to filteredMessages ["); 
									filteredMessages.add(tweet);
								}
							}
							logger.debug(channelCode + ": fetched bufferedMessages size = " + bufferedMessages.size());
							logger.debug(channelCode + ": Final filteredMessages size = " + filteredMessages.size());
						}
						// Finally the usual stuff - format tweets for tagger specific output
						final JsonDataFormatter taggerOutput = new JsonDataFormatter(callbackName);	// Tagger specific JSONP output formatter
						final StringBuilder jsonDataList = taggerOutput.createList(filteredMessages, messageCount, rejectNullFlag);
						final int sendCount = taggerOutput.getMessageCount();
						logger.info(channelCode + ": sending jsonp data, count = " + sendCount);

						filteredMessages.clear();
						filteredMessages = null;

						logger.debug(channelCode + ": sending jsonp data, count = " + sendCount);
						return Response.ok(jsonDataList.toString())
								.allow("POST", "GET", "PUT", "UPDATE", "OPTIONS", "HEAD")
								.header("Access-Control-Allow-Origin", "*")
								.header("Access-Control-Allow-Credentials", "true")
								.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS, HEAD")
								.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
								.build();
					} else {
						logger.warn("No data found for channel: " + channelName);
						return returnEmptyJson(callbackName);
					}
				} else {
					logger.warn("Channel name doesn't exist: " + channelName);
					return returnEmptyJson(callbackName);
				}
			}
			else {
				logger.warn("Error in user supplied channel: " + channelCode);
				return returnEmptyJson(callbackName);
			}
		}
		logger.error(channelCode + ": error in jedis connection. Bailing out...");
		return returnEmptyJson(callbackName);
	}

	private Response returnEmptyJson(final String callbackName) {
		if (callbackName != null) {
			StringBuilder respStr = new StringBuilder();
			respStr.append(callbackName).append("([{}])");
			return Response.ok(respStr.toString())
					.allow("POST", "GET", "PUT", "UPDATE", "OPTIONS", "HEAD")
					.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Credentials", "true")
					.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS, HEAD")
					.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
					.build();
		} else {
			return Response.ok(new String("[{}]"))
					.allow("POST", "GET", "PUT", "UPDATE", "OPTIONS", "HEAD")
					.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Credentials", "true")
					.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS, HEAD")
					.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
					.build();
		}
	}

	/**
	 * 
	 * @param channel fully qualified channel name
	 * @return true if present, false if not
	 */
	public boolean isChannelPresent(String channel) {
		Set<String> channelList = masterCBManager.getActiveChannelsList();
		if (channelList != null) {
			//System.out.println("[isChannelPresent] channels: " + channelList);
			return channelList.contains(channel);
		}
		return false;
	}

	@GET
	@Path("/channel/error/{crisisCode}")
	@Produces({"text/html"})
	public Response onErrorResponse() {
		Set<String> channelList = masterCBManager.getActiveChannelsList();
		StringBuilder htmlMessageString = new StringBuilder();

		// Build HTML doc to return
		htmlMessageString.append("<!DOCTYPE html>");
		htmlMessageString.append("<html>");
		htmlMessageString.append("<head><title>REDIS PUBSUB Channel Data Output Service</title></head>");
		htmlMessageString.append("<body>");
		htmlMessageString.append("<h1>Can not initiate REDIS channel subscription!</h1>");
		htmlMessageString.append("<p><big>Available active channels: </big></p>");
		htmlMessageString.append("<ul>"); 
		if (channelList != null) {
			Iterator<String> itr = channelList.iterator();
			while (itr.hasNext()) {
				htmlMessageString.append("<li>" + itr.next().substring(CHANNEL_PREFIX_STRING.length()) + "</li>");
			}
		}
		htmlMessageString.append("</body></html>");
		if (channelList != null) channelList.clear();
		channelList = null;

		return Response.ok(htmlMessageString.toString()).build();
	}

	@GET
	@Path("/manage/restart/{passcode}")
	@Produces("application/json")
	public Response restartFetchService(@PathParam("passcode") String passcode) {
		logger.info("[restartFetchService] request received");
		if (passcode.equals("sysadmin2013")) {
			if (masterCBManager != null) {
				masterCBManager.close();
			}
			//cbManager = new ChannelBufferManager(CHANNEL_REG_EX);
			masterCBManager = new ChannelBufferManager();
			masterCBManager.initiateChannelBufferManager(CHANNEL_REG_EX);
			logger.info("aidr-output fetch service restarted...");
			final String statusStr = "{\"aidr-output fetch service\":\"RESTARTED\"}";
			return Response.ok(statusStr).build();
		}
		return Response.ok(new String("{\"password\":\"invalid\"}")).build();
	}

	@POST
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/channel/test/{crisisCode}")
	public Response testPost(String testString, @PathParam("crisisCode") String channelCode) {
		logger.info("request received :" + channelCode + ", received string: " + testString);
		return Response.ok(new String("{\"test\":\"passed\"}")).build();
	}

	@GET
	@Path("/channel/test/jsonmap")
	public Response testJsonMap() {
		long startTime = System.currentTimeMillis();
		Map<String, Boolean> map = masterCBManager.getAllRunningCollections();
		System.out.println("Time to retrieve map from manager: " + (System.currentTimeMillis() - startTime));
		System.out.println("Received MAP: ");
		for (String cName: map.keySet()) {
			System.out.println(cName + ": " + map.get(cName));
		}
		return Response.ok(new Long(System.currentTimeMillis() - startTime).toString()).build();
	}

	@GET
	@Path("/channel/test/channelpublic")
	public Response testChannelPublicFlag(@QueryParam("channelCode") String channelCode) {
		long startTime = System.currentTimeMillis();
		Boolean status = masterCBManager.getChannelPublicStatus(CHANNEL_PREFIX_STRING+channelCode);
		System.out.println("Time to retrieve publiclyListed status from manager: " + (System.currentTimeMillis() - startTime));
		System.out.println(channelCode + ": " + status);

		return Response.ok(new Long(System.currentTimeMillis() - startTime).toString()).build();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		masterCBManager.close();
		logger.info("Context destroyed");
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		inRequests = new AtomicInteger(0);
		AIDROutputConfig configuration = new AIDROutputConfig();
		HashMap<String, String> configParams = configuration.getConfigProperties();
		logger.info("Logger = " + configParams.get("logger"));

		// Most important action - setup channel buffering thread
		if (null == masterCBManager) {
			logger.info("Initializing channel buffer manager");
			System.out.println("[contextInitialized] Initializing channel buffer manager");
			//cbManager = new ChannelBufferManager(CHANNEL_REG_EX);
			masterCBManager = new ChannelBufferManager();
			masterCBManager.initiateChannelBufferManager(CHANNEL_REG_EX);
			logger.info("Done initializing channel buffer manager");
			System.out.println("[contextInitialized] Done initializing channel buffer manager");
		}
		channelSelector = new SimpleRateLimiter();
		logger.info("Context Initialized");
	}
}
