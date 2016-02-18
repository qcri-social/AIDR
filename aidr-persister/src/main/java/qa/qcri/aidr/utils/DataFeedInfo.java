/**
 * POJO for (de)serialization of tweets coming from postgres db
 * 
 * @author Kushal
 */


package qa.qcri.aidr.utils;

import java.io.Serializable;
import java.util.Date;


public class DataFeedInfo implements Serializable {
	private static final long serialVersionUID = 7200501237905773286L;
	private String text;
	private Date createdAt;
	private String geo;
	private String place;
	private String labelCode;
	private String labelName;
	private String attributeCode;
	private String attributeName;
	private Double confidence;
	
	public Date getCreatedAt() {
		return createdAt;
	}
	public String getGeo() {
		return geo;
	}
	public String getPlace() {
		return place;
	}
	public String getLabelCode() {
		return labelCode;
	}
	public String getLabelName() {
		return labelName;
	}
	public String getAttributeCode() {
		return attributeCode;
	}
	public String getAttributeName() {
		return attributeName;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public void setGeo(String geo) {
		this.geo = geo;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public void setLabelCode(String labelCode) {
		this.labelCode = labelCode;
	}
	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}
	public void setAttributeCode(String attributeCode) {
		this.attributeCode = attributeCode;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	public Double getConfidence() {
		return confidence;
	}
	public void setConfidence(Double confidence) {
		this.confidence = confidence;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}