package qa.qcri.aidr.output.filter;

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
	@JsonProperty("attribute_code") public String attibute_code;
	
	@XmlElement 
	@JsonProperty("attribute_name") public String attribute_name;
	
	public NominalLabel() {}
}