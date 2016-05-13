package qa.qcri.aidr.manager.dto;

public class CollectionStatsInfo {

	private long totalCollectionsCount;
	private long runningCollectionCount;
	private long totalDataCount;
	private long offlineCollectionCount;
	
	public long getTotalCollectionsCount() {
		return totalCollectionsCount;
	}
	public void setTotalCollectionsCount(long totalCollectionsCount) {
		this.totalCollectionsCount = totalCollectionsCount;
	}
	public long getRunningCollectionCount() {
		return runningCollectionCount;
	}
	public void setRunningCollectionCount(long runningCollectionCount) {
		this.runningCollectionCount = runningCollectionCount;
	}
	public long getTotalDataCount() {
		return totalDataCount;
	}
	public void setTotalDataCount(long totalDataCount) {
		this.totalDataCount = totalDataCount;
	}
	public long getOfflineCollectionCount() {
		return offlineCollectionCount;
	}
	public void setOfflineCollectionCount(long offlineCollectionCount) {
		this.offlineCollectionCount = offlineCollectionCount;
	}

}
