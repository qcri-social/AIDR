package qa.qcri.aidr.analysis.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(catalog = "aidr_analysis",name = "tag_data")
@IdClass(value=TagDataPK.class)
@XmlRootElement
public class TagData implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlElement
    @Id
	@Column(name="crisis_code", nullable=false)
	private String crisisCode;
	
	@XmlElement
    @Id
	@Column(name="timestamp", nullable=false)
	private Long timestamp;
	
	@XmlElement
	@Id
	@Column(name="granularity", nullable=false)
	private Long granularity;
	
	@XmlElement
    @Id
	@Column(name="attribute_code", nullable=false)
	private String attributeCode;
	
	@XmlElement
    @Id
	@Column(name="label_code", nullable=false)
	private String labelCode;
	
	/*
	@XmlElement
	@Column(name="tag_text", nullable=true)
	private String tagText;
	*/
	
	@XmlElement
	@Column(name="count", nullable=false)
	private Integer count;
	
	@XmlElement
	@Column(name="min_created_at", nullable=true)
	private Long minCreatedAt;
	
	@XmlElement
	@Column(name="max_created_at", nullable=true)
	private Long maxCreatedAt;
	
	public TagData() {}
	
	public TagData(String crisisCode, Long timestamp, Long granularity, String attributeCode, String labelCode, Integer count) {
		this.crisisCode = crisisCode;
		this.timestamp = timestamp;
		this.granularity = granularity;
		this.attributeCode = attributeCode;
		this.labelCode = labelCode;
				
		this.count = count;
	}
	
	public String getCrisisCode() {
		return this.crisisCode;
	}
	
	public void setCrisisCode(String crisisCode) {
		this.crisisCode = crisisCode; 
	}
	
	public Long getTimestamp() {
		return this.timestamp;
	}
	
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	
	public Long getGranularity() {
		return this.granularity;
	}
	
	public void setGranularity(Long granularity) {
		this.granularity = granularity;
	}
	
	public String getAttributeCode() {
		return this.attributeCode;
	}
	
	public void setAttributeCode(String attributeCode) {
		this.attributeCode = attributeCode;
	}
	
	public String getLabelCode() {
		return this.labelCode;
	}
	
	public void setLabelCode(String labelCode) {
		this.labelCode = labelCode;
	}
	
	/*
	public String getTagText() {
		return this.tagText;
	}
	
	public void setTagText(String tagText) {
		this.tagText = tagText;
	}
	*/
	
	public Integer getCount() {
		return this.count;
	}
	
	public void setCount(Integer count) {
		this.count = count;
	}
	
	public Long getMinCreatedAt() {
		return this.minCreatedAt;
	}
	
	public void setMinCreatedAt(Long minCreatedAt) {
		this.minCreatedAt = minCreatedAt;
	}
	
	public Long getMaxCreatedAt() {
		return this.maxCreatedAt;
	}
	
	public void setMaxCreatedAt(Long maxCreatedAt) {
		this.maxCreatedAt = maxCreatedAt;
	}
}
