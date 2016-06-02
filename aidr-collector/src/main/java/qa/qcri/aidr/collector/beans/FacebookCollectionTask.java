package qa.qcri.aidr.collector.beans;

import java.util.Date;
import java.util.Properties;

public class FacebookCollectionTask extends CollectionTask {

	private Date lastRunTime;
	private int fetchInterval;
	
	public Date getLastRunTime() {
		return lastRunTime;
	}
	public void setLastRunTime(Date lastRunTime) {
		this.lastRunTime = lastRunTime;
	}
	public int getFetchInterval() {
		return fetchInterval;
	}
	public void setFetchInterval(int fetchInterval) {
		this.fetchInterval = fetchInterval;
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
        newTask.setCollectionCount(collectionCount);
        newTask.setPersist(persist);
        newTask.setSourceOutage(sourceOutage);
        newTask.setSaveMediaEnabled(saveMediaEnabled);
        newTask.setLastRunTime(lastRunTime);
        newTask.setFetchInterval(fetchInterval);
        return newTask;
    }

    @SuppressWarnings("deprecation")
	public FacebookCollectionTask(Properties properties){
		this.setAccessToken(properties.getProperty("accessToken"));
		this.setAccessTokenSecret(properties.getProperty("accessTokenSecret"));
		this.setToTrack(properties.getProperty("toTrack"));
		this.setCollectionCode(properties.getProperty("collectionCode"));
		this.setCollectionName(properties.getProperty("collectionName"));
		this.setLastRunTime(new Date(properties.getProperty("lastRunTime")));
		this.setFetchInterval(Integer.valueOf(properties.getProperty("fetchInterval")));
		if(properties.getProperty("persist")!=null){
			this.setPersist(Boolean.valueOf(properties.getProperty("persist")));
		}
	}
    
    public FacebookCollectionTask() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "CollectionTask{" + "collectionCode=" + collectionCode
				+ ", collectionName=" + collectionName + ", toTrack=" + toTrack
				+ ", lastRunTime=" + lastRunTime + ", fetchInterval=" + fetchInterval
				+ ", lastDocument=" + lastDocument + ", statusCode="
				+ statusCode + ", statusMessage=" + statusMessage
				+ ", persist=" + persist + ", accessToken="
				+ accessToken + ", accessTokenSecret=" + accessTokenSecret
				+ ", collectionCount=" + collectionCount + '}';
	}

}
