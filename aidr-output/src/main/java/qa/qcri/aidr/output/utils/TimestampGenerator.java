package qa.qcri.aidr.output.utils;

import java.util.Date;


/**
 * 
 * @author koushik
 * Generates timestamp field for tweets received from Redis
 */
public class TimestampGenerator {
	
	public TimestampGenerator() {}
	
	public String insertTimestamp(String tweet) {
		StringBuilder timestampedTweet = new StringBuilder();
		StringBuilder dateStr = new StringBuilder();
		dateStr.append("\"timestamp\":").append(new Date().toString()).append(", ");
		
		timestampedTweet.append(tweet);
		timestampedTweet.insert(timestampedTweet.indexOf("{")+1, dateStr);
		
		return timestampedTweet.toString();
	}
	
}
