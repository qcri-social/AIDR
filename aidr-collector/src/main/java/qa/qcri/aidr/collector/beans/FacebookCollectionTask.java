package qa.qcri.aidr.collector.beans;

import java.util.Date;
import java.util.Properties;

public class FacebookCollectionTask extends CollectionTask {

	private Date lastExecutionTime;
	private int fetchInterval;
	private boolean pullInProgress;
	private int fetchFrom;
	
	public Date getLastExecutionTime() {
		return lastExecutionTime;
	}
	public void setLastExecutionTime(Date lastExecutionTime) {
		this.lastExecutionTime = lastExecutionTime;
	}
	public int getFetchInterval() {
		return fetchInterval;
	}
	public void setFetchInterval(int fetchInterval) {
		this.fetchInterval = fetchInterval;
	}
    public int getFetchFrom() {
		return fetchFrom;
	}
	public void setFetchFrom(int fetchFrom) {
		this.fetchFrom = fetchFrom;
	}
	
	public FacebookCollectionTask() {
		// TODO Auto-generated constructor stub
	}

	public boolean isPullInProgress() {
		return pullInProgress;
	}
	public void setPullInProgress(boolean pullInProgress) {
		this.pullInProgress = pullInProgress;
	}
	
	@Override
	public FacebookCollectionTask clone() {

    	FacebookCollectionTask newTask = new FacebookCollectionTask();
        newTask.setAccessToken(accessToken);
        newTask.setAccessTokenSecret(accessTokenSecret);
        newTask.setCollectionCode(collectionCode);
        newTask.setCollectionName(collectionName);
        newTask.setLastDocument(lastDocument);
        newTask.setStatusCode(statusCode);
        newTask.setStatusMessage(statusMessage);
        newTask.setToTrack(toTrack);
        newTask.setToFollow(toFollow);
        newTask.setCollectionCount(collectionCount);
        newTask.setPersist(persist);
        newTask.setSourceOutage(sourceOutage);
        newTask.setSaveMediaEnabled(saveMediaEnabled);
        newTask.setLastExecutionTime(lastExecutionTime);
        newTask.setFetchInterval(fetchInterval);
        newTask.setFetchFrom(fetchFrom);
        return newTask;
    }

    @SuppressWarnings("deprecation")
	public FacebookCollectionTask(Properties properties){
		this.setAccessToken(properties.getProperty("accessToken"));
		this.setAccessTokenSecret(properties.getProperty("accessTokenSecret"));
		this.setToTrack(properties.getProperty("toTrack"));
		this.setToFollow(properties.getProperty("toFollow"));
		this.setCollectionCode(properties.getProperty("collectionCode"));
		this.setCollectionName(properties.getProperty("collectionName"));
		this.setLastExecutionTime(new Date(properties.getProperty("lastExecutionTime")));
		this.setFetchInterval(Integer.valueOf(properties.getProperty("fetchInterval")));
		this.setFetchFrom(Integer.valueOf(properties.getProperty("fetchFrom")));
		if(properties.getProperty("persist")!=null){
			this.setPersist(Boolean.valueOf(properties.getProperty("persist")));
		}
	}
    
    @Override
	public String toString() {
		return "FacebookCollectionTask [lastExecutionTime=" + lastExecutionTime + ", fetchInterval=" + fetchInterval
				+ ", pullInProgress=" + pullInProgress + ", fetchFrom=" + fetchFrom + ", collectionCode="
				+ collectionCode + ", collectionName=" + collectionName + ", toTrack=" + toTrack + ", lastDocument="
				+ lastDocument + ", statusCode=" + statusCode + ", statusMessage=" + statusMessage + ", persist="
				+ persist + ", sourceOutage=" + sourceOutage + ", saveMediaEnabled=" + saveMediaEnabled
				+ ", accessToken=" + accessToken + ", accessTokenSecret=" + accessTokenSecret + ", collectionCount="
				+ collectionCount + ", provider=" + provider + ", toFollow=" + toFollow + "]";
	}
    
}
