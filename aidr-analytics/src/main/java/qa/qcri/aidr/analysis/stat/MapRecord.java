package qa.qcri.aidr.analysis.stat;

import java.util.Map;

public abstract class MapRecord {
	public Map<Long, Integer> count = null;
	public long lastUpdateTime = 0;
	
	public abstract int getCount(Long g);
	public abstract void setCount(Long g, int val);
	public abstract void incrementCount(Long g);
	public abstract void incrementAllCounts();
	public abstract void resetCount(Long g);
	public abstract boolean isCountZeroForGranularity(Long g);
	public abstract boolean isCountZeroForAllGranularity();
	
	public abstract long getLastUpdateTime();
	public abstract void setLastUpdateTime(long lastUpdateTime);
	
}
