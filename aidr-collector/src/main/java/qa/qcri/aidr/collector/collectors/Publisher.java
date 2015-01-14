package qa.qcri.aidr.collector.collectors;

import javax.json.JsonObject;

public interface Publisher {

	public abstract void publish(String channel, JsonObject doc);

	public abstract void publish(String channel, String message);

}