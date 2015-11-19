/**
 * 
 */
package qa.qcri.aidr.manager.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import qa.qcri.aidr.manager.util.CollectionStatus;

/**
 * @author Latika
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class CollectionDetailsInfo {

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
    private String track;
    private String follow;
    private String geo;
    private String geoR;
    private String langFilters;
    private String target;
    private Integer durationHours;
    private Long crisisType;
    
    public Long getCrisisType() {
		return crisisType;
	}
	public void setCrisisType(Long crisisType) {
		this.crisisType = crisisType;
	}
	public String getTrack() {
		return track;
	}
	public void setTrack(String track) {
		this.track = track;
	}
	public String getFollow() {
		return follow;
	}
	public void setFollow(String follow) {
		this.follow = follow;
	}
	public String getGeo() {
		return geo;
	}
	public void setGeo(String geo) {
		this.geo = geo;
	}
	public String getGeoR() {
		return geoR;
	}
	public void setGeoR(String geoR) {
		this.geoR = geoR;
	}
	public String getLangFilters() {
		return langFilters;
	}
	public void setLangFilters(String langFilters) {
		this.langFilters = langFilters;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public Integer getDurationHours() {
		return durationHours;
	}
	public void setDurationHours(Integer durationHours) {
		this.durationHours = durationHours;
	}
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
