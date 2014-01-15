package qa.qcri.aidr.output.getdata;

import java.util.List;
import java.util.ListIterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;

public class JsonDataFormatter {

	private static Logger logger = LoggerFactory.getLogger(JsonDataFormatter.class);
	String callbackName = null;
	int count = 0;
	public JsonDataFormatter(String callbackName) {
		//BasicConfigurator.configure();		// configuration for log4j logging
		System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");		// set logging level for slf4j
		this.callbackName = callbackName;
		count = 0;
	}

	public int getMessageCount() {
		return count;
	}
	
	public StringBuilder createList(List<String> bufferedMessages, int messageCount) {
		// Now, build the jsonp object to be sent - data in reverse chronological order.
		// Update from previous version: the entire collection of json objects are wrapped 
		// with a single callback function.
		synchronized (bufferedMessages) {
			count = 0;
			StringBuilder jsonDataList = new StringBuilder();		
			if (callbackName != null) 
				jsonDataList.append(callbackName).append("([");
			else 
				jsonDataList.append("[");

			ListIterator<String> itr = bufferedMessages.listIterator(bufferedMessages.size());  // Must be in synchronized block
			while (itr.hasPrevious() && count < messageCount) {
				String msg = itr.previous();
				TaggerJsonOutputAdapter jsonOutput = new TaggerJsonOutputAdapter();
				String jsonData = (msg != null) ? jsonOutput.buildJsonString(msg) : null;
				if (jsonData != null) {
					jsonDataList.append(jsonData).append(",");
					++count;
					//logger.debug("[createList] Will send JSON data: " + jsonData);
				}
				jsonOutput = null;
				jsonData = null;
				msg = null;

			}
			if (count == 0) {
				// send empty jsonp object
				jsonDataList.append(callbackName != null ? new String("{}])") : new String("{}"));
			} 
			else {
				// there are json objects to send
				jsonDataList.deleteCharAt(jsonDataList.lastIndexOf(","));		// delete the extra "," at the end of the json string
				if (callbackName != null) 
					jsonDataList.append("])");
				else 
					jsonDataList.append("]");
			}
			return jsonDataList;
		}
	}
}
