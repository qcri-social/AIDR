package qa.qcri.aidr.data.model;

import java.util.Date;


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
	private Date collectionCreationDate;
	private Date endDate;
	private Date startDate;
	private String keywords;
	private String geo;
	private String status;
	private Integer labelCount;
	private Integer humanTaggedCount;
	private boolean publiclyListed;
	private String provider;
	
	public boolean isPubliclyListed() {
		return publiclyListed;
	}
	public void setPubliclyListed(boolean publiclyListed) {
		this.publiclyListed = publiclyListed;
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
	public Date getCollectionCreationDate() {
		return collectionCreationDate;
	}
	public void setCollectionCreationDate(Date collectionCreationDate) {
		this.collectionCreationDate = collectionCreationDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getGeo() {
		return geo;
	}
	public void setGeo(String geo) {
		this.geo = geo;
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
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
}
