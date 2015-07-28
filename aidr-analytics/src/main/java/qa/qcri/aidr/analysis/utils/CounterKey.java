package qa.qcri.aidr.analysis.utils;

import java.io.Serializable;

/**
 * Key for storing classifier tag/label related data
 * 
 * @author koushik
 *
 */
public class CounterKey implements Serializable {

	private static final long serialVersionUID = 9168204241878331959L;
	
	private String crisisCode = null;
	private String attributeCode = null;
	private String labelCode = null;

	public CounterKey() {}

	public CounterKey(String crisisCode, String attributeCode, String labelCode) {
		if (crisisCode != null && attributeCode != null && labelCode != null) {
			this.setCrisisCode(crisisCode);
			this.setAttributeCode(attributeCode);
			this.setLabelCode(labelCode);
		}
	}

	public String getCrisisCode() {
		return crisisCode;
	}

	public void setCrisisCode(String crisisCode) {
		this.crisisCode = crisisCode;
	}

	public String getAttributeCode() {
		return attributeCode;
	}

	public void setAttributeCode(String attributeCode) {
		this.attributeCode = attributeCode;
	}

	public String getLabelCode() {
		return labelCode;
	}

	public void setLabelCode(String labelCode) {
		this.labelCode = labelCode;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((attributeCode == null) ? 0 : attributeCode.hashCode());
		result = prime * result
				+ ((crisisCode == null) ? 0 : crisisCode.hashCode());
		result = prime * result
				+ ((labelCode == null) ? 0 : labelCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CounterKey other = (CounterKey) obj;
		if (attributeCode == null) {
			if (other.attributeCode != null)
				return false;
		} else if (!attributeCode.equals(other.attributeCode))
			return false;
		if (crisisCode == null) {
			if (other.crisisCode != null)
				return false;
		} else if (!crisisCode.equals(other.crisisCode))
			return false;
		if (labelCode == null) {
			if (other.labelCode != null)
				return false;
		} else if (!labelCode.equals(other.labelCode))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "CounterKey [crisisCode=" + crisisCode + ", attributeCode="
				+ attributeCode + ", labelCode=" + labelCode + "]";
	}
		
}
