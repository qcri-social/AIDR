package qa.qcri.aidr.collector.beans;

import java.util.Date;
import java.util.Properties;

public class FacebookCollectionTask extends CollectionTask {

	private Date lastExecutionTime;
	private int fetchInterval;
	
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
        newTask.setLastExecutionTime(lastExecutionTime);
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
		this.setLastExecutionTime(new Date(properties.getProperty("lastExecutionTime")));
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
				+ ", lastExecutionTime=" + lastExecutionTime + ", fetchInterval=" + fetchInterval
				+ ", lastDocument=" + lastDocument + ", statusCode="
				+ statusCode + ", statusMessage=" + statusMessage
				+ ", persist=" + persist + ", accessToken="
				+ accessToken + ", accessTokenSecret=" + accessTokenSecret
				+ ", collectionCount=" + collectionCount + '}';
	}

}
