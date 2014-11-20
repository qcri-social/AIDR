package qa.qcri.aidr.predict.communication;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.concurrent.ConcurrentHashMap;






import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import qa.qcri.aidr.common.redis.LoadShedder;
import qa.qcri.aidr.predict.DataStore;

import qa.qcri.aidr.predict.common.Serializer;
import qa.qcri.aidr.predict.data.Document;
import qa.qcri.aidr.predict.data.DocumentJSONConverter;
import static qa.qcri.aidr.predict.common.ConfigProperties.getProperty;


/**
 * AidrFetcherJsonInputProcessor receives tweets in JSON format from the AIDR
 * fetch module through a redis pub/sub channel. After parsing the JSON, the
 * tweets are pushed to a queue for further processing.
 * 
 * @author jrogstadius
 */
public class AidrFetcherJsonInputProcessor implements Runnable {

	private static Logger logger = Logger.getLogger(AidrFetcherJsonInputProcessor.class);
	
	public static ConcurrentHashMap<String, LoadShedder> redisLoadShedder = null;
	
	public AidrFetcherJsonInputProcessor() {
		if (null == redisLoadShedder) {
			redisLoadShedder = new ConcurrentHashMap<String, LoadShedder>(20);
		}
	}
	public class FetcherResponseWrapper implements Serializable {

		private static final long serialVersionUID = 1L;
		private String crisisId;
		private String tweetJson;

		public FetcherResponseWrapper() {

		}

		public String getCrisisId() {
			return crisisId;
		}

		public void setCrisisId(String crisisId) {
			this.crisisId = crisisId;
		}

		public String getTweetJson() {
			return tweetJson;
		}

		public void setTweetJson(String tweetJson) {
			this.tweetJson = tweetJson;
		}
	}

	public class Subscriber extends JedisPubSub {

		byte[] outputQueueName;

		public Subscriber(String outputQueueName) {
			this.outputQueueName = outputQueueName.getBytes();
		}

		@Override
		public void onMessage(String channel, String jsonDoc) {
			if (!redisLoadShedder.containsKey(channel)) {
				redisLoadShedder.put(channel, new LoadShedder(Integer.parseInt(getProperty("persister_load_limit")), Integer.parseInt(getProperty("persister_load_check_interval_minutes")), true));
			}
			if (redisLoadShedder.get(channel).canProcess(channel)) {
				Document doc;
				try {
					doc = DocumentJSONConverter.parseDocument(jsonDoc);
					//TODO: This should be the source IP, but this class is meant to 
					//consume from a fetcher instance running on localhost
					//doc.setSourceIP(InetAddress.getLocalHost()); 
					enqueue(doc);
				} catch (Exception e) {

					//Bypass the entire processing pipeline for documents that have a crisis code 
					//which has not been defined in the database.
					//TODO: Extend to HTTP consumers
					try {
						JSONObject docObj = new JSONObject(jsonDoc);
						if (docObj.has("aidr")) {
							JSONObject aidrObj = (JSONObject)docObj.get("aidr");
							if (aidrObj.has("crisis_code")) {
								String crisisCode = aidrObj.getString("crisis_code");
								//log(LogLevel.WARNING, "Crisis code has not been defined: " + crisisCode);
								OutputMatcher.PushToRedisStream(crisisCode, jsonDoc);
							}
						}
					} catch (Exception e2) {
						logger.error("Exception when parsing document:", e);
						logger.error(jsonDoc);
					}
				}
			}
		}

		@Override
		public void onPMessage(String pattern, String channel, String jsonDoc) {
			onMessage(channel, jsonDoc);
		}

		private void enqueue(Document doc) {
			Jedis jedis = DataStore.getJedisConnection();

			try {
				jedis.rpush(outputQueueName, Serializer.serialize(doc));
			} catch (IOException e) {
				logger.error("Error when serializing input document.", e);
			} finally {
				DataStore.close(jedis);
			}
		}

		@Override
		public void onPSubscribe(String arg0, int arg1) {
		}

		@Override
		public void onPUnsubscribe(String arg0, int arg1) {
		}

		@Override
		public void onSubscribe(String arg0, int arg1) {
		}

		@Override
		public void onUnsubscribe(String arg0, int arg1) {
		}
	}

	public String inputQueueName;
	public String outputQueueName;

	public void run() {
		//this.setMaxLogWritesPerMinute(2);

		while (true) {
			if (Thread.interrupted())
				return;

			Jedis redis = null;

			try {
				redis = DataStore.getJedisConnection();

				Subscriber subscriber = new Subscriber(outputQueueName);
				redis.psubscribe(subscriber, inputQueueName);
				redisLoadShedder.put(inputQueueName, new LoadShedder(Integer.parseInt(getProperty("persister_load_limit")), Integer.parseInt(getProperty("persister_load_check_interval_minutes")), true));
				Thread.sleep(60000);
			} catch (Exception e) {
				logger.error("RedisInputProcessor", e);
			} finally {
				DataStore.close(redis);
			}
		}
	}
}
