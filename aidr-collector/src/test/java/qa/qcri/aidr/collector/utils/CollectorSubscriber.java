/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.collector.utils;

/**
 *
 * @author Kushal
 */
import java.util.Date;

import redis.clients.jedis.JedisPubSub;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CollectorSubscriber extends JedisPubSub {
	private static Long tweetCount=0L;
	private Date tweetTimestamp=null;
	private String tweetText = "";
	private boolean quiet;

	public Long getTweetCount() {
		return tweetCount;
	}

	public CollectorSubscriber(Boolean toQuiet){
		this.quiet = toQuiet;
	}
	
	@Override
	public void onMessage(String channel, String message) {
		tweetCount++;
		if(!quiet){
			getTweet(message);
			System.out.println("[" + tweetTimestamp.getTime()+ "]" 
					+ tweetText.substring(0, Math.min(40, tweetText.length())));
		}
	}
   
	@Override
	public void onPMessage(String pattern, String channel, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSubscribe(String channel, int subscribedChannels) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnsubscribe(String channel, int subscribedChannels) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPUnsubscribe(String pattern, int subscribedChannels) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPSubscribe(String pattern, int subscribedChannels) {
		// TODO Auto-generated method stub
		
	}
   
	private void getTweet(String line) {
		JsonParser parser = new JsonParser();
		JsonObject jsonObj = (JsonObject) parser.parse(line);
	
		if (jsonObj.get("timestamp_ms") != null) {
			tweetTimestamp = new Date(jsonObj.get("timestamp_ms").getAsLong());
		}
	
		if (jsonObj.get("text") != null) {
			tweetText= jsonObj.get("text").getAsString();
		} 
	}
}
