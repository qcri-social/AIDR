package qa.qcri.aidr.data.persistence.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class CollectionSummary extends BaseEntity {

	private static final long serialVersionUID = 1L;

    private String name;

    private String code;

	private Long totalCount;
	
    private Date startDate;

	private Date endDate;
	
	private Date collectionCreationDate;
	
	@Column(length = 1000)	
    private String geo;

	private String curator;
	
	private Integer labelCount;

	@Column(length = 5000)
    private String keywords;

	private String language;
	
	private String status;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getGeo() {
		return geo;
	}

	public void setGeo(String geo) {
		this.geo = geo;
	}

	public String getCurator() {
		return curator;
	}

	public void setCurator(String curator) {
		this.curator = curator;
	}

	public Integer getLabelCount() {
		return labelCount;
	}

	public void setLabelCount(Integer labelCount) {
		this.labelCount = labelCount;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public Date getCollectionCreationDate() {
		return collectionCreationDate;
	}

	public void setCollectionCreationDate(Date collectionCreationDate) {
		this.collectionCreationDate = collectionCreationDate;
	}
	
}
