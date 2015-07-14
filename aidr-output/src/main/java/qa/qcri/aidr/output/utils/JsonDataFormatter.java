/**
 * Provides methods to create formatted JSON output data for the REST APIs  
 */

package qa.qcri.aidr.output.utils;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.JsonObject;

public class JsonDataFormatter {

	private static Logger logger = Logger.getLogger(JsonDataFormatter.class);
	String callbackName = null;
	int count = 0;
	public JsonDataFormatter(String callbackName) {
		//BasicConfigurator.configure();		// configuration for log4j logging
		//System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");		// set logging level for slf4j
		this.callbackName = callbackName;
		count = 0;
	}

	public int getMessageCount() {
		return count;
	}

	private StringBuilder setPrefix(boolean isArray) {
		StringBuilder jsonDataList = new StringBuilder();
		if (callbackName != null) 
			jsonDataList.append(callbackName).append("(");
		if (isArray) 
			jsonDataList.append("[");
		
		return jsonDataList;
	}

	private StringBuilder setSuffix(StringBuilder jsonDataList, boolean isArray) {
		if (isArray) 
				jsonDataList.append("]");
		if (callbackName != null) 
				jsonDataList.append(")");
		
		return jsonDataList;
	}

	public StringBuilder createList(List<String> bufferedMessages, int messageCount, boolean rejectNullFlag) {
		// Now, build the jsonp object to be sent - data in reverse chronological order.
		// The entire collection of json objects are wrapped with a single callback function.
		StringBuilder jsonDataList = new StringBuilder();

		count = 0;		
		jsonDataList.append(setPrefix(true));

		if (bufferedMessages != null) {
			ListIterator<String> itr = bufferedMessages.listIterator(bufferedMessages.size());  // Must be in synchronized block
			while (itr.hasPrevious() && count < messageCount) {
				final String msg = itr.previous();
				final TaggerJsonOutputAdapter jsonOutput = new TaggerJsonOutputAdapter();
				final String jsonData = (msg != null) ? jsonOutput.buildJsonString(msg, rejectNullFlag) : null;
				//logger.info("json string: " + jsonData);
				if (jsonData != null) {
					jsonDataList.append(jsonData);
					++count;
					if ((count < messageCount) && itr.hasPrevious()) jsonDataList.append(",");		// otherwise, this was the last message to append
				}
			}
		}
		if (count == 0) {
			// send empty jsonp object
			jsonDataList.append(callbackName != null ? new String("{}])") : new String("{}]"));
		} 
		else {
			// there are json objects to send
			//jsonDataList.deleteCharAt(jsonDataList.lastIndexOf(","));		// delete the extra "," at the end of the json string
			jsonDataList = setSuffix(jsonDataList, true);		
		}

		return jsonDataList;
	}

	public StringBuilder createRateLimitedList(List<String> bufferedMessages, final SimpleFairScheduler channelSelector, int messageCount, boolean rejectNullFlag) {
		// Now, build the jsonp object to be sent - data in reverse chronological order.
		// The entire collection of json objects are wrapped with a single callback function.
		StringBuilder jsonDataList = null; 
		boolean attempt = true;
		while (attempt) {
			boolean existsFlag = false;
			jsonDataList = new StringBuilder();
			jsonDataList.append(setPrefix(true));
			count = 0;		

			if (bufferedMessages != null) {
				ListIterator<String> itr = bufferedMessages.listIterator(bufferedMessages.size());  
				while (itr.hasPrevious() && count < messageCount) {
					final String msg = itr.previous();
					final TaggerJsonOutputAdapter jsonOutput = new TaggerJsonOutputAdapter();
					final String jsonData = (msg != null) ? jsonOutput.buildJsonString(msg, rejectNullFlag) : null;

					//logger.info("channel: " + jsonOutput.getCrisisCode() + ", freq = " + channelSelector.getValue(jsonOutput.getCrisisCode()) + ", rate limited = " + channelSelector.isRateLimited(jsonOutput.getCrisisCode()));
					//logger.info(jsonOutput.getCrisisCode() + ": json string = " + jsonData);

					existsFlag = (jsonData != null) ? true : false;
					if (jsonData != null && !channelSelector.isRateLimited(jsonOutput.getCrisisCode())) {
						jsonDataList.append(jsonData);
						channelSelector.increment(jsonOutput.getCrisisCode());

						//logger.info("Added tweet to send list, freq = " + channelSelector.getValue(jsonOutput.getCrisisCode()));
						++count;
						if (count < messageCount && itr.hasPrevious()) jsonDataList.append(",");		// otherwise, this was the last message to append
					}
				}
			}
			if (count == 0) { 
				//if (existsFlag || (!channelSelector.existsNotRateLimitedKey() && !bufferedMessages.isEmpty())) {
				if (existsFlag && !channelSelector.existsNotRateLimitedKey()) {
					// reset all rate limits
					channelSelector.initializeAll();
					//logger.info("Reset rate limits - again attempting, attempt = " + attempt);
				} else {
					attempt = false;		// no point retrying
					// send empty jsonp object
					//logger.info("Giving up on attempting - no data! attempt = " + attempt);
					jsonDataList.append(callbackName != null ? new String("{}])") : new String("{}]"));
				}
			} 
			else {
				// there are json objects to send
				//jsonDataList.deleteCharAt(jsonDataList.lastIndexOf(","));		// delete the extra "," at the end of the json string
				attempt = false;
				//logger.info("Done attempting: sending data, attempt = " + attempt);
				jsonDataList = setSuffix(jsonDataList, true);
			}
		}
		return jsonDataList;
	}

	public StringBuilder createStreamingList(List<String> bufferedMessages, int messageCount, boolean rejectNullFlag) {
		// Now, build the jsonp object to be sent - data in reverse chronological order.
		// The entire collection of json objects are wrapped with a single callback function.
		StringBuilder jsonDataList = new StringBuilder();
		jsonDataList.append(setPrefix(false));
		count = 0;		

		if (bufferedMessages != null) {
			ListIterator<String> itr = bufferedMessages.listIterator(bufferedMessages.size());  // Must be in synchronized block
			while (itr.hasPrevious() && count < messageCount) {
				final String msg = itr.previous();
				final TaggerJsonOutputAdapter jsonOutput = new TaggerJsonOutputAdapter();
				final String jsonData = (msg != null) ? jsonOutput.buildJsonString(msg, rejectNullFlag) : null;
				//logger.info("json string: " + jsonData);
				if (jsonData != null) {
					try {
						JsonObject jsonMessage = new JsonObject();
						jsonMessage.addProperty("document_type", "Twitter");
						jsonMessage.addProperty("document", jsonData);
						jsonDataList.append(jsonMessage);
						++count;
					} catch (Exception e) {
						logger.error("Error in creating json streaming output object");
					}
				}
			}
		}
		if (count == 0) {
			// send empty jsonp object
			jsonDataList.append(callbackName != null ? new String("{})") : new String("{}"));
		} 
		else {
			// there are json objects to send
			//jsonDataList.deleteCharAt(jsonDataList.lastIndexOf(","));		// delete the extra "," at the end of the json string
			jsonDataList = setSuffix(jsonDataList, false);
		}

		return jsonDataList;
	}


}
