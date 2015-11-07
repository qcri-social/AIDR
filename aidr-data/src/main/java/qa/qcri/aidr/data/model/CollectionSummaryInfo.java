package qa.qcri.aidr.data.model;


/**
 * @author Latika
 *
 */
public class CollectionSummaryInfo {

	private String code;
	private String name;
	private Long totalCount;
	private String language;
	private String curator;
	private Long createdAt;
	private Long stoppedAt;
	private String keywords;
	private String geo;
	private String status;
	private Integer labelCount;
	private Integer humanTaggedCount;
	
	public String getGeo() {
		return geo;
	}
	public void setGeo(String geo) {
		this.geo = geo;
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
	public Long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getCurator() {
		return curator;
	}
	public void setCurator(String curator) {
		this.curator = curator;
	}
	public Long getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}
	public Long getStoppedAt() {
		return stoppedAt;
	}
	public void setStoppedAt(Long stoppedAt) {
		this.stoppedAt = stoppedAt;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getLabelCount() {
		return labelCount;
	}
	public void setLabelCount(Integer labelCount) {
		this.labelCount = labelCount;
	}
	public Integer getHumanTaggedCount() {
		return humanTaggedCount;
	}
	public void setHumanTaggedCount(Integer humanTaggedCount) {
		this.humanTaggedCount = humanTaggedCount;
	}
	
}
