package qa.qcri.aidr.common.filter;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

@SuppressWarnings("serial")
@XmlRootElement(name="NominalLabel")
public class NominalLabel implements Serializable {
	
	@XmlElement 
	public String label_name;
	
	@XmlElement 
	public int source_id;
	
	@XmlElement 
	public boolean from_human;
	
	@XmlElement 
	public String label_code;
	
	@XmlElement 
	public String label_description;
	
	@XmlElement
	public float confidence;
	
	@XmlElement 
	public String attribute_description;
	
	@XmlElement 
	public String attribute_code;
	
	@XmlElement 
	public String attribute_name;
	
	public NominalLabel() {}
	
	@JsonProperty("label_name") 
	public String getLabelName() {
		return this.label_name;
	}
	
	public void setLabelName(String label_name) {
		this.label_name = label_name;
	}
	
	@JsonProperty("source_id")
	public int getSourceId() {
		return this.source_id;
	}
	
	public void setSourceId(int source_id) {
		this.source_id = source_id;
	}
	
	@JsonProperty("from_human") 
	public boolean getFromHuman() {
		return this.from_human;
	}
	
	public void setFromHuman(boolean from_human) {
		this.from_human = from_human;
	}
	
	@JsonProperty("label_code") 
	public String getLabelCode() {
		return this.label_code;
	}
	
	public void setLabelCode(String label_code) {
		this.label_code = label_code;
	}
	
	@JsonProperty("label_description") 
	public String getLabelDescription() {
		return this.label_description;
	}
	
	public void setLabelDescription(String label_description) {
		this.label_description = label_description;
	}
	
	@JsonProperty("confidence") 
	public float getConfidence() {
		return this.confidence;
	}
	
	public void setConfidence(float confidence) {
		this.confidence = confidence;
	}
	
	@JsonProperty("attribute_code") 
	public String getAttributeCode() {
		return this.attribute_code;
	}
	
	public void setAttributeCode(String attribute_code) {
		this.attribute_code = attribute_code;
	}
	
	@JsonProperty("attribute_name") 
	public String getAttributeName() {
		return this.attribute_name;
	}
	
	public void setAttributeName(String attribute_name) {
		this.attribute_name = attribute_name;
	}
	
	@JsonProperty("attribute_description") 
	public String getAttributeDescription() {
		return this.attribute_description;
	}
	
	public void setAttributeDescription(String attribute_description) {
		this.attribute_description = attribute_description;
	}
}

