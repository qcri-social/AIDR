package qa.qcri.aidr.collector.collectors;

import javax.json.JsonObject;

import qa.qcri.aidr.collector.java7.Predicate;
import qa.qcri.aidr.common.redis.LoadShedder;

public class ShedderFilter implements Predicate<JsonObject> {
	
	private String channel;
	private LoadShedder delegate;
	
	public ShedderFilter(String channel, LoadShedder shedder){
		this.channel = channel;
		this.delegate = shedder;
	}

	@Override
	public boolean test(JsonObject t) {
		return delegate.canProcess(channel);
	}

	@Override
	public String getFilterName() {
		return this.getClass().getSimpleName();
	}

}
