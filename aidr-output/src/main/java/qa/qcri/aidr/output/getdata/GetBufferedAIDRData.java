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
 * Dependencies:  servlets 3+, jedis-2.2.1, gson-2.2.4, commons-pool-1.6, slf4j-1.7.5, JAX-RS 1.x, Jersey 1.8+
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
 * Invocation:	host:port/context-path/channel/{crisisCode}?callback={callback}&count={count} 
 * ============	
 * Channel name based examples: 
 *  1. http://localhost:8080/aidr-output/crisis/fetch/channel/clex_20131201?count=50
 *  2. http://localhost:8080/aidr-output/crisis/fetch/channel/clex_20131201?callback=JSONP
 *  3. http://localhost:8080/aidr-output/crisis/fetch/channel/clex_20131201?callback=JSONP&count=50
 *  
 * Fully qualified channel name based examples: 
 *  1. http://localhost:8080/aidr-output/crisis/fetch/channel/aidr_predict.clex_20131201?count=50
 *  2. http://localhost:8080/aidr-output/crisis/fetch/channel/aidr_predict.clex_20131201?callback=func
 *  3. http://localhost:8080/aidr-output/crisis/fetch/channel/aidr_predict.clex_20131201?callback=func&count=50
 * 
 * Apart from the above valid paths one can use:
 * 	1. http://localhost:8080/aidr-output/crisis/fetch/channels/list     => returns list of active channels
 * 	2. http://localhost:8080/aidr-output/crisis/fetch/channels/latest	=> returns the latest tweet data from  across all channels
 *  
 *  Parameter explanations:
 *  	1. crisisCode [mandatory]: the REDIS channel to which to subscribe
 *  	2. callback [optional]: name of the callback function for JSONP data
 *  	3. count [optional]: the specified number of messages that have been buffered by the service. If unspecified
 *  		or <= 0 or larger than the MAX_MESSAGES_COUNT, the default number of messages are returned  
 */

package qa.qcri.aidr.output.getdata;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.sun.jersey.spi.resource.Singleton;


//import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import qa.qcri.aidr.output.utils.JsonDataFormatter;

@Path("/")
@Singleton
public class GetBufferedAIDRData implements ServletContextListener {

	// Debugging
	private static Logger logger = LoggerFactory.getLogger(GetBufferedAIDRData.class);

	// Related to channel buffer management
	private static final String CHANNEL_REG_EX = "aidr_predict.*";
	private static final String CHANNEL_PREFIX_STRING = "aidr_predict.";
	private static final int MAX_MESSAGES_COUNT = 1000;
	private static final int DEFAULT_COUNT = 50;		// default number of messages to fetch
	private static final String DEFAULT_COUNT_STR = "50";
	private int messageCount = DEFAULT_COUNT;			// number of messages to fetch

	private static ChannelBufferManager cbManager; 			// managing buffers for each publishing channel
	boolean error = false;
	private final boolean rejectNullFlag = true;
	/////////////////////////////////////////////////////////////////////////////
	@POST
	@Path("/{crisisCode}")
	@Produces({"application/json", "text/html"})
	public Response handlePost(@PathParam("crisisCode") String channelCode,
			@QueryParam("callbackName") String callbackName,
			@DefaultValue(DEFAULT_COUNT_STR) @QueryParam("count") String count) {
		return Response.ok(getBufferedAIDRData(channelCode, callbackName, count)).build();
	}

	/**
	 * 
	 * @return Returns list of active channels
	 */
	@GET
	@Path("/channels/list")
	@Produces("text/html")
	public Response getActiveChannelsList() {

		Set<String> channelList = cbManager.getActiveChannelsList();
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
	 * @return the latest tweet data as a jsonp object from across all active channels
	 */
	@GET
	@Path("/channels/latest")
	@Produces("application/json")
	public Response getLatestBufferedAIDRData(@QueryParam("callback") String callbackName,
			@DefaultValue("1") @QueryParam("count") String count) {

		// Get the last count number of messages for channel=channelCode
		List<String> bufferedMessages = new ArrayList<String>();
		messageCount = Integer.parseInt(count);		// number of latest messages across all channels to return
		for (int i = 0;i < messageCount;i++) {
			List<String> temp = cbManager.getLatestFromAllChannels();
			bufferedMessages.addAll(temp != null ? temp : new ArrayList<String>());
			temp.clear();
			temp = null;
		}
		JsonDataFormatter taggerOutput = new JsonDataFormatter(callbackName);	// Tagger specific JSONP output formatter
		StringBuilder jsonDataList = taggerOutput.createList(bufferedMessages.subList(0, messageCount), messageCount, rejectNullFlag);
		int sendCount = taggerOutput.getMessageCount();

		// Reset the messageList buffer and return
		bufferedMessages.clear();
		bufferedMessages = null;

		// Finally, send the retrieved list to client and close connection
		logger.info("[doGet] Going to send json data, count = " + sendCount);
		return Response.ok(jsonDataList.toString()).build();
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

		error = false;

		// Parse the HTTP GET request and generating results for output
		// Set the response MIME type of the response message
		if (channelCode == null) {
			error = true;
		}
		if (channelCode.contains("*") || channelCode.contains("?")) {	// || !cbManager.getActiveChannelCodes().contains(channelCode)) {
			error = true;			// Error - regular expression based retrieval not supported
		}
		if (error)
		{	
			Set<String> channelList = cbManager.getActiveChannelsList();
			StringBuilder htmlMessageString = new StringBuilder();

			// Build HTML doc to return
			htmlMessageString.append("<!DOCTYPE html>");
			htmlMessageString.append("<html>");
			htmlMessageString.append("<head><title>REDIS PUBSUB Channel Data Output Service</title></head>");
			htmlMessageString.append("<body>");
			htmlMessageString.append("<h1>Invalid/No CrisisCode Provided! </h1>");
			htmlMessageString.append("<h2>Can not initiate REDIS channel subscription!</h2>");
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
				if (msgCount > 0) {
					messageCount = Math.min(msgCount, MAX_MESSAGES_COUNT);
				}
				// Get the last messageCount messages for channel=channelCode
				List<String> bufferedMessages = new ArrayList<String>();
				List<String> temp = cbManager.getLastMessages(channelName, messageCount);
				bufferedMessages.addAll(temp != null ? temp : new ArrayList<String>());

				JsonDataFormatter taggerOutput = new JsonDataFormatter(callbackName);	// Tagger specific JSONP output formatter
				StringBuilder jsonDataList = taggerOutput.createList(bufferedMessages, messageCount, rejectNullFlag);
				int sendCount = taggerOutput.getMessageCount();

				// Cleanup, send the retrieved list to client and close connection
				if (null != temp) { 
					temp.clear();
					temp = null;
				}
				bufferedMessages.clear();
				bufferedMessages = null;
				
				logger.info("[doGet] Sending jsonp data, count = " + sendCount);
				return Response.ok(jsonDataList.toString()).build();
			}
			else {
				if (callbackName != null) {
					StringBuilder respStr = new StringBuilder();
					respStr.append(callbackName).append("([{}])");
					return Response.ok(respStr.toString()).build();
				} else
					return Response.ok(new String("[{}]")).build();
			}
		}
	}

	/**
	 * 
	 * @param channel fully qualified channel name
	 * @return true if present, false if not
	 */
	public boolean isChannelPresent(String channel) {
		Set<String> channelList = cbManager.getActiveChannelsList();
		if (channelList != null) {
			return channelList.contains(channel);
		}
		return false;
	}

	@GET
	@Path("/channel/error/{crisisCode}")
	@Produces({"text/html"})
	public Response onErrorResponse() {
		Set<String> channelList = cbManager.getActiveChannelsList();
		StringBuilder htmlMessageString = new StringBuilder();

		// Build HTML doc to return
		htmlMessageString.append("<!DOCTYPE html>");
		htmlMessageString.append("<html>");
		htmlMessageString.append("<head><title>REDIS PUBSUB Channel Data Output Service</title></head>");
		htmlMessageString.append("<body>");
		htmlMessageString.append("<h1>Invalid/No CrisisCode Provided! </h1>");
		htmlMessageString.append("<h2>Can not initiate REDIS channel subscription!</h2>");
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


	// cleanup when servlet is destroyed (e.g., server shutdown)
	public void finalize() throws Throwable {
		//cbManager.finalize();
		super.finalize();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		cbManager.finalize();
		logger.info("Context destroyed");
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// For now: set up a simple configuration that logs on the console
		//PropertyConfigurator.configure("log4j.properties");		// where to place the properties file?
		//BasicConfigurator.configure();							// basic configuration for log4j logging
		System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");		// set logging level for slf4j

		// Most important action - setup channel buffering thread
		cbManager = new ChannelBufferManager(CHANNEL_REG_EX);
		logger.info("Context Initialized");
	}
}
