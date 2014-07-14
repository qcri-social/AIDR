package qa.qcri.aidr.output.utils;

import java.util.List;
import java.util.ListIterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	public StringBuilder createList(List<String> bufferedMessages, int messageCount, boolean rejectNullFlag) {
		// Now, build the jsonp object to be sent - data in reverse chronological order.
		// The entire collection of json objects are wrapped with a single callback function.
		StringBuilder jsonDataList = new StringBuilder();

		count = 0;		
		if (callbackName != null) 
			jsonDataList.append(callbackName).append("([");
		else 
			jsonDataList.append("[");

		ListIterator<String> itr = bufferedMessages.listIterator(bufferedMessages.size());  // Must be in synchronized block
		while (itr.hasPrevious() && count < messageCount) {
			final String msg = itr.previous();
			final TaggerJsonOutputAdapter jsonOutput = new TaggerJsonOutputAdapter();
			final String jsonData = (msg != null) ? jsonOutput.buildJsonString(msg, rejectNullFlag) : null;
			//logger.info("[createList] json string: " + jsonData);
			if (jsonData != null) {
				jsonDataList.append(jsonData);
				++count;
				if ((count < messageCount) && itr.hasPrevious()) jsonDataList.append(",");		// otherwise, this was the last message to append
			}
		}
		if (count == 0) {
			// send empty jsonp object
			jsonDataList.append(callbackName != null ? new String("{}])") : new String("{}]"));
		} 
		else {
			// there are json objects to send
			//jsonDataList.deleteCharAt(jsonDataList.lastIndexOf(","));		// delete the extra "," at the end of the json string
			if (callbackName != null) 
				jsonDataList.append("])");
			else 
				jsonDataList.append("]");
		}

		return jsonDataList;
	}

	public StringBuilder createRateLimitedList(List<String> bufferedMessages, final SimpleRateLimiter channelSelector, int messageCount, boolean rejectNullFlag) {
		// Now, build the jsonp object to be sent - data in reverse chronological order.
		// The entire collection of json objects are wrapped with a single callback function.
		StringBuilder jsonDataList = null; 
		boolean attempt = true;
		while (attempt) {
			boolean existsFlag = false;
			jsonDataList = new StringBuilder();
			count = 0;		
			if (callbackName != null)  
				jsonDataList.append(callbackName).append("([");
			else 
				jsonDataList.append("[");

			ListIterator<String> itr = bufferedMessages.listIterator(bufferedMessages.size());  
			while (itr.hasPrevious() && count < messageCount) {
				final String msg = itr.previous();
				final TaggerJsonOutputAdapter jsonOutput = new TaggerJsonOutputAdapter();
				final String jsonData = (msg != null) ? jsonOutput.buildJsonString(msg, rejectNullFlag) : null;

				//logger.info("[createRateLimitedList] channel: " + jsonOutput.getCrisisCode() + ", freq = " + channelSelector.getValue(jsonOutput.getCrisisCode()) + ", rate limited = " + channelSelector.isRateLimited(jsonOutput.getCrisisCode()));
				//logger.info("[createRateLimitedList] json string: " + jsonData);

				existsFlag = (jsonData != null) ? true : false;
				if (jsonData != null && !channelSelector.isRateLimited(jsonOutput.getCrisisCode())) {
					jsonDataList.append(jsonData);
					channelSelector.increment(jsonOutput.getCrisisCode());

					//logger.info("[createRateLimitedList] Added tweet to send list, freq = " + channelSelector.getValue(jsonOutput.getCrisisCode()));
					++count;
					if (count < messageCount && itr.hasPrevious()) jsonDataList.append(",");		// otherwise, this was the last message to append
				}
			}
			if (count == 0) { 
				//if (existsFlag || (!channelSelector.existsNotRateLimitedKey() && !bufferedMessages.isEmpty())) {
				if (existsFlag && channelSelector.existsNoRateLimitedKey()) {
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
				if (callbackName != null) 
					jsonDataList.append("])");
				else 
					jsonDataList.append("]");
			}
		}
		return jsonDataList;
	}

}
