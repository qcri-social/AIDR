package qa.qcri.aidr.manager.dto;

import java.io.Serializable;

public class FetcheResponseDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String statusCode;
	private String message;
	private String collectionName;
	private String collectionCode;
	private String toTrack;
	private String toFollow;
	private String geoLocation;
	private Integer tweetsCount;
	private String lastDocument;
        private String languageFilter;
        private String statusMessage;
        private Object dataObject;
        private String consumerKey;
        private String consumerSecret;
        private String accessToken;
        private String accessTokenSecret;
        private String twitterInfoPresent;
        private String toTrackAvailable;
        private String toFollowAvailable;
        private String geoLocationAvailable;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCollectionName() {
		return collectionName;
	}
	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}
	public String getCollectionCode() {
		return collectionCode;
	}
	public void setCollectionCode(String collectionCode) {
		this.collectionCode = collectionCode;
	}
	public String getToTrack() {
		return toTrack;
	}
	public void setToTrack(String toTrack) {
		this.toTrack = toTrack;
	}
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
	public Integer getTweetsCount() {
		return tweetsCount;
	}
	public void setTweetsCount(Integer tweetsCount) {
		this.tweetsCount = tweetsCount;
	}
	public String getLastDocument() {
		return lastDocument;
	}
	public void setLastDocument(String lastDocument) {
		this.lastDocument = lastDocument;
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

    /**
     * @return the dataObject
     */
    public Object getDataObject() {
        return dataObject;
    }

    /**
     * @param dataObject the dataObject to set
     */
    public void setDataObject(Object dataObject) {
        this.dataObject = dataObject;
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
    public String getAccessTokenSecret() {
        return accessTokenSecret;
    }

    /**
     * @param accessTokenSecret the accessTokenSecret to set
     */
    public void setAccessTokenSecret(String accessTokenSecret) {
        this.accessTokenSecret = accessTokenSecret;
    }

    /**
     * @return the twitterInfoPresent
     */
    public String getTwitterInfoPresent() {
        return twitterInfoPresent;
    }

    /**
     * @param twitterInfoPresent the twitterInfoPresent to set
     */
    public void setTwitterInfoPresent(String twitterInfoPresent) {
        this.twitterInfoPresent = twitterInfoPresent;
    }

    /**
     * @return the toTrackAvailable
     */
    public String getToTrackAvailable() {
        return toTrackAvailable;
    }

    /**
     * @param toTrackAvailable the toTrackAvailable to set
     */
    public void setToTrackAvailable(String toTrackAvailable) {
        this.toTrackAvailable = toTrackAvailable;
    }

    /**
     * @return the toFollowAvailable
     */
    public String getToFollowAvailable() {
        return toFollowAvailable;
    }

    /**
     * @param toFollowAvailable the toFollowAvailable to set
     */
    public void setToFollowAvailable(String toFollowAvailable) {
        this.toFollowAvailable = toFollowAvailable;
    }

    /**
     * @return the geoLocationAvailable
     */
    public String getGeoLocationAvailable() {
        return geoLocationAvailable;
    }

    /**
     * @param geoLocationAvailable the geoLocationAvailable to set
     */
    public void setGeoLocationAvailable(String geoLocationAvailable) {
        this.geoLocationAvailable = geoLocationAvailable;
    }

	
	
	
}
