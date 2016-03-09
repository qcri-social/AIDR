package qa.qcri.aidr.collector.beans;

import java.util.Properties;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author Imran
 * A JAVA POJO class used to define a collection (i.e. Twitter collection) details.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "config", propOrder = {
    "collectionCode",
    "collectionName",
    "toTrack",
    "toFollow",
    "geoLocation",
    "geoR",
    "languageFilter",
    "collectionCount",
    "statusCode",
    "statusMessage",
    "persist"
})
@XmlRootElement(name = "config")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class CollectionTask {

    private String collectionCode;
    private String collectionName;
    private String toTrack;
    private String toFollow;
    private String geoLocation, geoR;
    private String languageFilter;
    private String lastDocument;
    private String statusCode;
    private String statusMessage;
    private Boolean persist;
    private boolean sourceOutage;
    private boolean saveMediaEnabled;
    /**
     *
     */
    //@XmlTransient
    protected String consumerKey;
    protected String consumerSecret;
    //@XmlTransient
    protected String accessToken;
    //@XmlTransient
    protected String accessTokenSecret;
    protected Long collectionCount;
    
    public CollectionTask() {}		
    
    /**
     * @return the toTrack
     */
    public String getToTrack() {
        return toTrack;
    }

    /**
     * @param toTrack the toTrack to set
     */
    public void setToTrack(String toTrack) {
        this.toTrack = toTrack;
    }

    /**
     * @return the toFollow
     */
    public String getToFollow() {
        return toFollow;
    }
    
    

    /**
     * @param toFollow the toFollow to set
     */
    public void setToFollow(String toFollow) {
        this.toFollow = toFollow;
    }

    /**
     * @return the consumerKey
     */
    public String getConsumerKey() {
        return consumerKey;
    }

    /**
     * @param consumerKey the consumerKey to set
     */
    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    /**
     * @return the consumerSecret
     */
    //@XmlTransient
    public String getConsumerSecret() {
        return consumerSecret;
    }

    /**
     * @param consumerSecret the consumerSecret to set
     */
    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    /**
     * @return the accessToken
     */
    //@XmlTransient
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * @param accessToken the accessToken to set
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * @return the accessTokenSecret
     */
    //@XmlTransient
    public String getAccessTokenSecret() {
        return accessTokenSecret;
    }

    /**
     * @param accessTokenSecret the accessTokenSecret to set
     */
    public void setAccessTokenSecret(String accessTokenSecret) {
        this.accessTokenSecret = accessTokenSecret;
    }

    public boolean isToTrackAvailable() {
        if (StringUtils.isNotEmpty(toTrack)) {
            return true;
        } else {
            return false;
        }
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

    public boolean isTwitterInfoPresent() {
        return StringUtils.isNotEmpty(getAccessToken())
                && StringUtils.isNotEmpty(getAccessTokenSecret())
                && StringUtils.isNotEmpty(getConsumerKey())
                && StringUtils.isNotEmpty(getConsumerSecret());
    }

    /**
     * @return the collectionName
     */
    public String getCollectionName() {
        return collectionName;
    }

    /**
     * @param collectionName the collectionName to set
     */
    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    /**
     * @return the collectionCode
     */
    public String getCollectionCode() {
        return collectionCode;
    }

    /**
     * @param collectionCode the collectionCode to set
     */
    public void setCollectionCode(String collectionCode) {
        this.collectionCode = collectionCode;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CollectionTask other = (CollectionTask) obj;
        if ((this.accessToken == null) ? (other.accessToken != null) : !this.accessToken.equals(other.accessToken)) {
            return false;
        }
        if ((this.accessTokenSecret == null) ? (other.accessTokenSecret != null) : !this.accessTokenSecret.equals(other.accessTokenSecret)) {
            return false;
        }
        return true;
    }

    /**
     * @return the collectionCount
     */
    public Long getCollectionCount() {
        return collectionCount;
    }

    /**
     * @param collectionCount the collectionCount to set
     */
    public void setCollectionCount(Long collectionCount) {
        this.collectionCount = collectionCount;
    }

    /**
     * @return the lastDocument
     */
    public String getLastDocument() {
        return lastDocument;
    }

    /**
     * @param lastDocument the lastDocument to set
     */
    public void setLastDocument(String lastDocument) {
        this.lastDocument = lastDocument;
    }

    /**
     * @return the geoLocation
     */
    public String getGeoLocation() {
        return geoLocation;
    }

    /**
     * @param geoLocation the geoLocation to set
     */
    public void setGeoLocation(String geoLocation) {
        this.geoLocation = geoLocation;
    }

    public String getGeoR() {
		return geoR;
	}

	public void setGeoR(String geoR) {
		this.geoR = geoR;
	}

	/**
     * @return the status
     */
    public String getStatusCode() {
        return statusCode;
    }

    /**
     * @param status the status to set
     */
    public void setStatusCode(String status) {
        this.statusCode = status;
    }

    /**
     * @return the statusMessage
     */
    public String getStatusMessage() {
        return statusMessage;
    }

    /**
     * @param statusMessage the statusMessage to set
     */
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    @Override
	public CollectionTask clone() {

        CollectionTask newTask = new CollectionTask();
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

    /**
     * @return the languageFilter
     */
    public String getLanguageFilter() {
        return languageFilter;
    }

    /**
     * @param languageFilter the languageFilter to set
     */
    public void setLanguageFilter(String languageFilter) {
        this.languageFilter = languageFilter;
    }

    public CollectionTask(Properties properties){
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
    
    public Boolean getPersist() {
		return persist;
	}

	public void setPersist(Boolean persist) {
		this.persist = persist;
	}

	public boolean isSourceOutage() {
		return sourceOutage;
	}

	public void setSourceOutage(boolean sourceOutage) {
		this.sourceOutage = sourceOutage;
	}

	public boolean isSaveMediaEnabled() {
		return saveMediaEnabled;
	}

	public void setSaveMediaEnabled(boolean saveMediaEnabled) {
		this.saveMediaEnabled = saveMediaEnabled;
	}
}
