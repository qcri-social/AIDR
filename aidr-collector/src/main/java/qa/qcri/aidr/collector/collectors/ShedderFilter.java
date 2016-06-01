package qa.qcri.aidr.collector.collectors;

import javax.json.JsonObject;

import qa.qcri.aidr.collector.java7.Predicate;
import qa.qcri.aidr.common.redis.LoadShedder;

/**
 * Controls tweets stream filtering based on load shedder.
 * 
 */
public class ShedderFilter implements Predicate<JsonObject> {
	
	private String channel;
	private LoadShedder delegate;
	
	public ShedderFilter(String channel, LoadShedder shedder){
		this.channel = channel;
		this.delegate = shedder;
	}

	@Override
	public boolean test(JsonObject t) {
		return delegate.canProcess();
	}

	@Override
	public String getFilterName() {
		return this.getClass().getSimpleName();
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public LoadShedder getDelegate() {
		return delegate;
	}

	public void setDelegate(LoadShedder delegate) {
		this.delegate = delegate;
	}
	
	public LoadShedder getDelegateForChannel(String channel) {
		return this.delegate;
	}

}
