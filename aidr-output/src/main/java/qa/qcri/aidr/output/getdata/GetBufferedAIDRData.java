/**
 * This code makes a HTTP request to get the last 'n' JSONP data 
 * from a REDIS DB to a client using a servlet. After sending the data, it 
 * closes the connection. The data from the REDIS channels is buffered in 
 * the background by a continuous running buffering system - started at servlet startup
 * The jsonp messages are returned in an ArrayList data structure, in reverse chronological order.
 * 
 * The code accepts i) channel name or, ii) fully qualified channel name. However, wildcard '*' for
 * pattern based subscription are NOT allowed.
 * 
 * @author Koushik Sinha
 * Last modified: 06/01/2014
 *
 * Dependencies:  servlets 3+, jedis-2.2.1, gson-2.2.4, commons-pool-1.6, slf4j-1.7.5
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
 * Invocations: 
 * ============	
 * Channel name based examples: 
 *  1. http://localhost:8080/aidr-output/getLast?crisisCode=clex_20131201&count=50
 *  2. http://localhost:8080/aidr-output/getLast?crisisCode=clex_20131201&callback=func
 *  3. http://localhost:8080/aidr-output/getLast?crisisCode=clex_20131201&callback=func&count=50
 *  
 * Fully qualified channel name based examples: 
 *  1. http://localhost:8080/aidr-output/getLast?crisisCode=aidr_predict.clex_20131201&count=50
 *  2. http://localhost:8080/aidr-output/getLast?crisisCode=aidr_predict.clex_20131201&callback=func
 *  3. http://localhost:8080/aidr-output/getLast?crisisCode=aidr_predict.clex_20131201&callback=func&count=50
 * 
 *  
 *  Parameter explanations:
 *  	1. crisisCode [mandatory]: the REDIS channel to which to subscribe
 *  	2. callback [optional]: name of the callback function for JSONP data
 *  	3. count [optional]: the specified number of messages that have been buffered by the service. If unspecified
 *  		or <= 0 or larger than the MAX_MESSAGES_COUNT, the default number of messages are returned  
 */

package qa.qcri.aidr.output.getdata;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


//import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@SuppressWarnings("serial")
@WebServlet(value = "/getLast", asyncSupported = true)
public class GetBufferedAIDRData extends HttpServlet {

	// Debugging
	private static Logger logger = LoggerFactory.getLogger(GetBufferedAIDRData.class);

	// Related to channel buffer management
	private static final String CHANNEL_REG_EX = "aidr_predict.*";
	private static final String CHANNEL_PREFIX_STRING = "aidr_predict.";
	private static final int MAX_MESSAGES_COUNT = 1000;
	private static final int DEFAULT_COUNT = 50;		// default number of messages to fetch
	private int messageCount = DEFAULT_COUNT;			// number of messages to fetch
	private ChannelBufferManager cbManager; 			// managing buffers for each publishing channel

	boolean error = false;
	/////////////////////////////////////////////////////////////////////////////

	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		// For now: set up a simple configuration that logs on the console
		//PropertyConfigurator.configure("log4j.properties");		// where to place the properties file?
		//BasicConfigurator.configure();
		System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");		// set logging level for slf4j
		logger.info("[init] In servlet init...");

		// Most important action - setup channel buffering thread
		this.cbManager = new ChannelBufferManager(CHANNEL_REG_EX);
		logger.info("[init] Created cbManager: " + cbManager);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		error = false;
		logger.info("[doGet] Received a request");
		// Parse the HTTP GET request and generating results for output
		// Set the response MIME type of the response message
		if (request.getParameter("crisisCode") == null) {
			error = true;
		}
		String channelCode = request.getParameter("crisisCode");
		if (channelCode.contains("*") || channelCode.contains("?")) {	// || !cbManager.getActiveChannelCodes().contains(channelCode)) {
			error = true;			// Error - regular expression based retrieval not supported
		}
		if (error)
		{
			showErrorMessage(response);
		}
		else {
			// Form fully qualified channelName and get other parameter values, if any
			String channelName = null;
			if (channelCode.startsWith(CHANNEL_PREFIX_STRING)) {
				channelName = channelCode;		// fully qualified channel name provided
			}
			else {
				channelName = CHANNEL_PREFIX_STRING.concat(channelCode);	// fully qualified channel name - same as REDIS channel
			}
			
			String callbackName = request.getParameter("callback");
			if (request.getParameter("count") != null) {
				int msgCount = Integer.parseInt(request.getParameter("count"));
				if (msgCount > 0) {
					messageCount = Math.min(msgCount, MAX_MESSAGES_COUNT);
				}
			}
			// Get the last messageCount messages for channel=channelCode
			List<String> bufferedMessages = new ArrayList<String>();
			bufferedMessages.addAll(this.cbManager.getLastMessages(channelName, messageCount) != null 
					? this.cbManager.getLastMessages(channelName, messageCount) : new ArrayList<String>());
			
			JsonDataFormatter taggerOutput = new JsonDataFormatter(callbackName);	// Tagger specific JSONP output formatter
			StringBuilder jsonDataList = taggerOutput.createList(bufferedMessages, messageCount);
			int count = taggerOutput.getMessageCount();
			
			// Now send the retrieved list to client and close connection
			writeJsonData(response, jsonDataList, count);
		
			// Now reset the messageList buffer and close connection
			bufferedMessages.clear();
			bufferedMessages = null;
			
			//jsonDataList.clear;			// uncomment if using wrapping per json object
			jsonDataList = null;
			response.getWriter().close();
		} 
		logger.info("[doGet] Reached end-of-function...");
	}

	public void writeJsonData(HttpServletResponse response, StringBuilder jsonDataList, int count) {
		logger.info("[writeJsonData] Sending actual message count = " + count);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter responseWriter = null;
		try {
			responseWriter = response.getWriter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("[writeJsonData] Error initializing PrintWriter", e);
			e.printStackTrace();
		}
		if (jsonDataList.length() > 0) { 
			responseWriter.println(jsonDataList);		// change made at home
			responseWriter.flush();
		}
		logger.info("[writeJsonData] Sent jsonP data set");
		// check if the write succeeded
		if (responseWriter.checkError()) {
			logger.error("[writeJsonData] Client side error - possible client disconnect...");
		}							
	}
	
	public void showErrorMessage(HttpServletResponse response) throws IOException {
		// No crisisCode provided...
		PrintWriter out = response.getWriter();
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");

			// Allocate a output writer to write the response message into the network socket
			out.println("<!DOCTYPE html>");
			out.println("<html>");
			out.println("<head><title>REDIS PUBSUB Channel Data Output Service</title></head>");
			out.println("<body>");
			out.println("<h1>Invalid/No CrisisCode Provided! </h1>");
			out.println("<h2>Can not initiate REDIS channel subscription!</h2>");
			out.println("<p><big>Available active channels: </big></p>");
			out.println("<ul>");
			Set<String> channelList = this.cbManager.getActiveChannelsList(); 
			if (channelList != null) {
				Iterator<String> itr = channelList.iterator();
				while (itr.hasNext()) {
					out.println("<li>" + itr.next().substring(CHANNEL_PREFIX_STRING.length()) + "</li>");
				}
			}
			out.println("</body></html>");
			if (channelList != null) channelList.clear();
			channelList = null;
		} finally {
			out.flush();
			out.close();  // Always close the output writer
		}
	} 


	// cleanup when servlet is destroyed (e.g., server shutdown)
	public void destroy() {
		cbManager.finalize();
		super.destroy();
	}
}
