/**
 * 
 */
package qa.qcri.aidr.manager.dto;

import qa.qcri.aidr.manager.util.CollectionStatus;

/**
 * @author Latika
 *
 */
public class CollectionInfo {

	private String code;
	private String name;
	private String createdBy;
	private boolean isPublic;
	private boolean isClassifierEnabled;
	private Long classifierCount;
	private String lastDocument;
	private String provider;
	private CollectionStatus status;
	private boolean isTrashed;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public boolean isPublic() {
		return isPublic;
	}
	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}
	public boolean isClassifierEnabled() {
		return isClassifierEnabled;
	}
	public void setClassifierEnabled(boolean isClassifierEnabled) {
		this.isClassifierEnabled = isClassifierEnabled;
	}
	public Long getClassifierCount() {
		return classifierCount;
	}
	public void setClassifierCount(Long classifierCount) {
		this.classifierCount = classifierCount;
	}
	public String getLastDocument() {
		return lastDocument;
	}
	public void setLastDocument(String lastDocument) {
		this.lastDocument = lastDocument;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public CollectionStatus getStatus() {
		return status;
	}
	public void setStatus(CollectionStatus status) {
		this.status = status;
	}
	public boolean isTrashed() {
		return isTrashed;
	}
	public void setTrashed(boolean isTrashed) {
		this.isTrashed = isTrashed;
	}
}