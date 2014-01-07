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
import java.util.List;
import java.util.ListIterator;

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
	private static final int MAX_MESSAGES_COUNT = 100;
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
		if (channelCode.indexOf("*") > 0) {
			error = true;			// Error - regular expression based retrieval not supported
		}
		if (error)
		{
			// No crisisCode provided...
			PrintWriter out = response.getWriter();
			try {
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/html");

				// Allocate a output writer to write the response message into the network socket
				out.println("<!DOCTYPE html>");
				out.println("<html>");
				out.println("<head><title>Redis HTTP Get Data App</title></head>");
				out.println("<body>");
				out.println("<h1>Invalid/No CrisisCode Provided! </h1>");
				out.println("<h>Can not initiate REDIS channel subscription!</h>");
				out.println("</body></html>");
			} finally {
				out.flush();
				out.close();  // Always close the output writer
			}
		} 
		else {
			// Form fully qualified channelName and get other parameter values, if any
			String channelName = null;
			if (channelCode.startsWith(CHANNEL_PREFIX_STRING)) {
				channelName = channelCode;		// fully qualified channel name provided
			}
			else {
				channelName = CHANNEL_PREFIX_STRING.concat(channelCode);
			}
			String callbackName = request.getParameter("callback");
			if (request.getParameter("count") != null) {
				int msgCount = Integer.parseInt(request.getParameter("count"));
				if (msgCount > 0) {
					messageCount = Math.min(msgCount, MAX_MESSAGES_COUNT);
				}
			}
			// Finally, get the last messageCount messages for channel=channelCode
			logger.info("[doGet] cbManager: " + this.cbManager + "::" + cbManager);
			List<String> bufferedMessages = new ArrayList<String>();
			bufferedMessages.addAll(this.cbManager.getLastMessages(channelName, messageCount) != null 
									? this.cbManager.getLastMessages(channelName, messageCount) : new ArrayList<String>());

			// Now send the retrieved list to client and close connection
			int count = 0;
			List<String>jsonDataList = new ArrayList<String>();
			ListIterator<String> itr = bufferedMessages.listIterator(bufferedMessages.size());  // Must be in synchronized block
			while (itr.hasPrevious() && count < messageCount) {
				String msg = itr.previous();
				StringBuilder jsonpMsg = null;
				if (callbackName != null) { 
					jsonpMsg = new StringBuilder(callbackName);
					jsonpMsg.append("(").append(msg).append(")");
				} 
				else {
					jsonpMsg = new StringBuilder(msg);
				}
				Gson jsonObject = new GsonBuilder().serializeNulls()		//.disableHtmlEscaping()
						.serializeSpecialFloatingPointValues().setPrettyPrinting()
						.create();
				final String jsonData = jsonObject.toJson(msg != null ? jsonpMsg.toString() : new String("{}"));
				jsonDataList.add(jsonData);
				logger.info("[doGet] Will send JSON data: " + jsonData);
				jsonObject = null;
				msg = null;
				++count;
			}
			if (count == 0 || jsonDataList.isEmpty()) {
				// both conditions should be satisfied ONLY simultaneously
				jsonDataList.add(callbackName != null ? callbackName.concat(new String("({})")) : new String("{}"));
			}
			logger.info("[doGet] Sending actual message count = " + count);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter responseWriter = null;
			try {
				responseWriter = response.getWriter();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error("[doGet] Error initializing PrintWriter", e);
				e.printStackTrace();
			}

			responseWriter.println(jsonDataList);
			responseWriter.flush();
			logger.info("[doGet] Sent jsonP data set");
			// check if the write succeeded
			if (responseWriter.checkError()) {
				logger.info("[doGet] Client side error - possible client disconnect...");
			}							
			// Now reset the messageList buffer and close connection
			bufferedMessages.clear();
			jsonDataList.clear();
			jsonDataList = null;
			responseWriter.close();
		} 
		logger.info("[doGet] Reached end-of-function...");
	}

	// cleanup when servlet is destroyed (e.g., server shutdown)
	public void destroy() {
		cbManager.finalize();
		super.destroy();
	}
}
