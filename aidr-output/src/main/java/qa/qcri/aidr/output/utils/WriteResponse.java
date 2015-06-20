/** 
 * Handles the actual writing of data to the client. If keepAlive
 * flag is set then the responsibility of closing the write is
 * on the caller.
 */

package qa.qcri.aidr.output.utils;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

@Deprecated
public class WriteResponse {
	public final static String DEFAULT_MIME_TYPE = "application/json";
	public HttpServletResponse response;
	public boolean keepAlive;
	
	// public PrintWriter responseWriter = null;
	public PrintStream writerHandle = null;		
	private static Logger logger = Logger.getLogger(WriteResponse.class);

	public WriteResponse(HttpServletResponse response, boolean keepAlive) {
		BasicConfigurator.configure();		// configuration for log4j logging
		this.response = response;
		this.keepAlive = keepAlive;	
	}
	
	public boolean initWriter(String mimeType) {
		if (mimeType.equals("application/json")) {
			response.setContentType("application/json");
		}
		if (mimeType.equals("text/html")) {
			response.setContentType("text/html");
		}
		response.setCharacterEncoding("UTF-8");
		
		try {
			writerHandle = new PrintStream(response.getOutputStream(), true, "UTF-8");
		} catch (IOException e) {
			logger.error("Error initializing PrintWriter", e);
			logger.error(e);
			return false;
		}
		return true;
	}
	
	public boolean checkError() {
		return writerHandle.checkError();	
	}
	
	public void close() {
		writerHandle.flush();
		writerHandle.close();
	}
	
	public void writeJsonData(StringBuilder jsonDataList, int count) {
		if (jsonDataList.length() > 0) { 
			writerHandle.println(jsonDataList);		
			writerHandle.flush();
		}
		if (!keepAlive)
			writerHandle.close();
	}

	public void writeHtmlData(StringBuilder htmlDataList, int count) {
		// Allocate a output writer to write the response message into the network socket
		if (htmlDataList.length() > 0) { 
			writerHandle.println(htmlDataList);		
			writerHandle.flush();
		}
		if (!keepAlive)
			writerHandle.close();
	}


	protected void writeErrorMessage(Set<String> channelList, String CHANNEL_PREFIX_STRING) {
		try {
			// Allocate a output writer to write the response message into the network socket
			writerHandle.println("<!DOCTYPE html>");
			writerHandle.println("<html>");
			writerHandle.println("<head><title>REDIS PUBSUB Channel Data Output Service</title></head>");
			writerHandle.println("<body>");
			writerHandle.println("<h1>Invalid/No CrisisCode Provided! </h1>");
			writerHandle.println("<h2>Can not initiate REDIS channel subscription!</h2>");
			writerHandle.println("<p><big>Available active channels: </big></p>");
			writerHandle.println("<ul>"); 
			if (channelList != null) {
				Iterator<String> itr = channelList.iterator();
				while (itr.hasNext()) {
					writerHandle.println("<li>" + itr.next().substring(CHANNEL_PREFIX_STRING.length()) + "</li>");
				}
			}
			writerHandle.println("</body></html>");
		} finally {
			writerHandle.flush();
			writerHandle.close();  // Always close the output writer
		}
	}
}
