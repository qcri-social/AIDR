package qa.qcri.aidr.collector.collectors;

import javax.json.JsonObject;

import qa.qcri.aidr.collector.utils.GenericCache;

public class StatusPublisher implements Publisher {

	private long counter = 0, threhold;
	private String cacheKey; 
	
	public StatusPublisher(String cacheKey, long threhold) {
		this.cacheKey = cacheKey;
		this.threhold = threhold;
	}

	@Override
	public void publish(String channel, JsonObject doc) {
		publish(channel, doc.getString("text"));
	}

	@Override
	public void publish(String channel, String message) {
		++counter;
		if (counter >= threhold) {
			GenericCache.getInstance().incrCounter(cacheKey, counter);
			GenericCache.getInstance().setLastDownloadedDoc(cacheKey, message);
			counter = 0;
		}
	}

}
