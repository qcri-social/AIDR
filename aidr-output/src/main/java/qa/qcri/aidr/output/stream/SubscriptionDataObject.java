package qa.qcri.aidr.output.stream;

import qa.qcri.aidr.output.utils.JedisConnectionObject;
import redis.clients.jedis.Jedis;

public class SubscriptionDataObject {
	
	public boolean patternSubscriptionFlag;
	public String redisChannel = "aidr_predict.*";       			// channel to subscribe to
	
	public JedisConnectionObject jedisConn;
	public Jedis subscriberJedis;
	
	// Runtime related
	public boolean isConnected;
	public boolean isSubscribed;
	
	// Request related
	public String callbackName;
	public float rate;
	public String duration;
	
	// JSON Output Configuration related
	public boolean rejectNullFlag;
	
	public void set(SubscriptionDataObject s) {
		this.patternSubscriptionFlag = s.patternSubscriptionFlag;
		this.redisChannel = s.redisChannel;
		this.rejectNullFlag = s.rejectNullFlag;
		
		this.jedisConn = s.jedisConn;
		this.subscriberJedis = s.subscriberJedis;
		
		this.isConnected = s.isConnected;
		this.isSubscribed = s.isSubscribed;
		
		this.callbackName = s.callbackName;
		this.rate = s.rate;
		this.duration = s.duration;
	}
}
