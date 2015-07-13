/**
 * primary key implementation for the tag_data table
 */
package qa.qcri.aidr.analysis.entity;

import java.io.Serializable;

public class TagDataPK implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String crisisCode;
	private Long timestamp;
	private Long granularity;
	private String attributeCode;
	private String labelCode;
	
	public TagDataPK() {}
	
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
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TagDataPK) {
			TagDataPK tagDataPK = (TagDataPK) obj;
			if (!tagDataPK.getCrisisCode().equals(crisisCode)) return false;
			if (tagDataPK.getTimestamp() != timestamp) return false;
			if (tagDataPK.getGranularity() != granularity) return false;
			if (!tagDataPK.getAttributeCode().equals(attributeCode)) return false;
			if (!tagDataPK.getLabelCode().equals(labelCode)) return false;
			
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int code = 0;
		code += (crisisCode != null ? crisisCode.hashCode() : 0);
		code += (timestamp != null ? timestamp : 0);
		code += (granularity != null ? granularity : 0);
		code += (attributeCode != null ? attributeCode.hashCode() : 0);
		code += (labelCode != null ? labelCode.hashCode() : 0);
		
		return code;
	}
}
