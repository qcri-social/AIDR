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

	
	
	
}
