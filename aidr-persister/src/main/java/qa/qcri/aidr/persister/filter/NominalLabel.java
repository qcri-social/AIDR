package qa.qcri.aidr.persister.filter;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

@SuppressWarnings("serial")
@XmlRootElement(name="NominalLabel")
public class NominalLabel implements Serializable {
	
	@XmlElement 
	@JsonProperty("label_name") public String label_name;
	
	@XmlElement 
	@JsonProperty("source_id") public int source_id;
	
	@XmlElement 
	@JsonProperty("from_human") public boolean from_human;
	
	@XmlElement 
	@JsonProperty("label_code") public String label_code;
	
	@XmlElement 
	@JsonProperty("label_description") public String label_description;
	
	@XmlElement
	@JsonProperty("confidence") public float confidence;
	
	@XmlElement 
	@JsonProperty("attribute_description") public String attribute_description;
	
	@XmlElement 
	@JsonProperty("attribute_code") public String attribute_code;
	
	@XmlElement 
	@JsonProperty("attribute_name") public String attribute_name;
	
	public NominalLabel() {}
	
	public String getLabelName() {
		return label_name;
	}
	
	public void setLabelName(String label_name) {
		this.label_name = label_name;
	}
	
	public int getSourceId() {
		return source_id;
	}
	
	public void setSourceId(int source_id) {
		this.source_id = source_id;
	}
	
	public boolean getFromHuman() {
		return from_human;
	}
	
	public void setFromHuman(boolean from_human) {
		this.from_human = from_human;
	}
	
	public String getLabelCode() {
		return label_code;
	}
	
	public void setLabelCode(String label_code) {
		this.label_code = label_code;
	}
	
	public String getLabelDescription() {
		return label_description;
	}
	
	public void setLabelDescription(String label_description) {
		this.label_description = label_description;
	}
	
	public float getConfidence() {
		return confidence;
	}
	
	public void setConfidence(float confidence) {
		this.confidence = confidence;
	}
	
	public String getAttributeDescription() {
		return attribute_description;
	}
	
	public void setAttributeDescription(String attribute_description) {
		this.attribute_description = attribute_description;
	}
	
	public String getAttributeCode() {
		return attribute_code;
	}
	
	public void setAttributeCode(String attribute_code) {
		this.attribute_code = attribute_code;
	}
	
	public String getAttributeName() {
		return attribute_name;
	}
	
	public void setAttributeName(String attribute_name) {
		this.attribute_name = attribute_name;
	}
}
