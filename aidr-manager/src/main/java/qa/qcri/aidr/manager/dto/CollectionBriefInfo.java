/**
 * 
 */
package qa.qcri.aidr.manager.dto;

/**
 * @author Latika
 *
 */
public class CollectionBriefInfo {

	private Long collectionId;
	private String collectionCode;
	private String collectionName;
	private String provider;
	private boolean isMicromappersEnabled;
	
	public Long getCollectionId() {
		return collectionId;
	}
	public void setCollectionId(Long collectionId) {
		this.collectionId = collectionId;
	}
	public String getCollectionCode() {
		return collectionCode;
	}
	public void setCollectionCode(String collectionCode) {
		this.collectionCode = collectionCode;
	}
	public String getCollectionName() {
		return collectionName;
	}
	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}
	public boolean isMicromappersEnabled() {
		return isMicromappersEnabled;
	}
	public void setMicromappersEnabled(boolean isMicromappersEnabled) {
		this.isMicromappersEnabled = isMicromappersEnabled;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
}
