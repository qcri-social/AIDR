package qa.qcri.aidr.analysis.entity;

import java.io.Serializable;

public class FrequencyDataPK implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String crisisCode;
	private Long timestamp;
	private Long granularity;
	private String attributeCode;
	private String labelCode;
	private Integer bin;
	
	public FrequencyDataPK() {}
	
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
	
	public Integer getBin() {
		return bin;
	}
	
	public void setBin(Integer bin) {
		this.bin = bin;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FrequencyDataPK) {
			FrequencyDataPK frequencyDataPK = (FrequencyDataPK) obj;
			if (!frequencyDataPK.getCrisisCode().equals(crisisCode)) return false;
			if (frequencyDataPK.getTimestamp() != timestamp) return false;
			if (frequencyDataPK.getGranularity() != granularity) return false;
			if (!frequencyDataPK.getAttributeCode().equals(attributeCode)) return false;
			if (!frequencyDataPK.getLabelCode().equals(labelCode)) return false;
			if (frequencyDataPK.getBin() != bin) return false;
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
		code += (bin != null ? bin : 0);
		
		return code;
	}
}
