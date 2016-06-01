package qa.qcri.aidr.collector.beans;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

public class TwitterCollectionTask extends CollectionTask {

    private String toFollow;
    private String geoLocation;
    private String geoR;
    private String languageFilter;

	public String getToFollow() {
		return toFollow;
	}

	public void setToFollow(String toFollow) {
		this.toFollow = toFollow;
	}

	public String getGeoLocation() {
		return geoLocation;
	}

	public void setGeoLocation(String geoLocation) {
		this.geoLocation = geoLocation;
	}

	public String getGeoR() {
		return geoR;
	}

	public void setGeoR(String geoR) {
		this.geoR = geoR;
	}

	public String getLanguageFilter() {
		return languageFilter;
	}

	public void setLanguageFilter(String languageFilter) {
		this.languageFilter = languageFilter;
	}

	public boolean isToFollowAvailable() {
        if (StringUtils.isNotEmpty(toFollow)) {
            return true;
        } else {
            return false;
        }

    }

    public boolean isGeoLocationAvailable() {
        if (StringUtils.isNotEmpty(geoLocation)) {
            return true;

        } else {
            return false;
        }
    }

    @Override
	public TwitterCollectionTask clone() {

        TwitterCollectionTask newTask = new TwitterCollectionTask();
        newTask.setAccessToken(accessToken);
        newTask.setAccessTokenSecret(accessTokenSecret);
        newTask.setCollectionCode(collectionCode);
        newTask.setCollectionName(collectionName);
        newTask.setConsumerKey(consumerKey);
        newTask.setConsumerSecret(consumerSecret);
        newTask.setGeoLocation(geoLocation);
        newTask.setLastDocument(lastDocument);
        newTask.setStatusCode(statusCode);
        newTask.setStatusMessage(statusMessage);
        newTask.setToFollow(toFollow);
        newTask.setToTrack(toTrack);
        newTask.setCollectionCount(collectionCount);
        newTask.setLanguageFilter(languageFilter);
        newTask.setPersist(persist);
        newTask.setSourceOutage(sourceOutage);
        newTask.setSaveMediaEnabled(saveMediaEnabled);
        return newTask;
    }

    public TwitterCollectionTask(Properties properties){
		this.setConsumerKey(properties.getProperty("consumerKey"));
		this.setConsumerSecret(properties.getProperty("consumerSecret"));
		this.setAccessToken(properties.getProperty("accessToken"));
		this.setAccessTokenSecret(properties.getProperty("accessTokenSecret"));
		this.setToTrack(properties.getProperty("toTrack"));
		this.setCollectionCode(properties.getProperty("collectionCode"));
		this.setCollectionName(properties.getProperty("collectionName"));
		this.setToFollow(properties.getProperty("toFollow"));
		this.setGeoLocation(properties.getProperty("geoLocation"));
		this.setGeoR(properties.getProperty("geoR"));
		this.setLanguageFilter(properties.getProperty("languageFilter"));
		if(properties.getProperty("persist")!=null){
			this.setPersist(Boolean.valueOf(properties.getProperty("persist")));
		}
	}
    
    public TwitterCollectionTask() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "CollectionTask{" + "collectionCode=" + collectionCode
				+ ", collectionName=" + collectionName + ", toTrack=" + toTrack
				+ ", toFollow=" + toFollow + ", geoLocation=" + geoLocation
				+ ", geoR=" + geoR + ", languageFilter=" + languageFilter
				+ ", lastDocument=" + lastDocument + ", statusCode="
				+ statusCode + ", statusMessage=" + statusMessage
				+ ", persist=" + persist + ", consumerKey=" + consumerKey
				+ ", consumerSecret=" + consumerSecret + ", accessToken="
				+ accessToken + ", accessTokenSecret=" + accessTokenSecret
				+ ", collectionCount=" + collectionCount + '}';
	}
}
