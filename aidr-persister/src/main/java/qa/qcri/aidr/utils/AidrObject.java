package qa.qcri.aidr.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import qa.qcri.aidr.persister.filter.NominalLabel;

@JsonIgnoreProperties(ignoreUnknown=true)
@XmlRootElement
public class AidrObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2457072548903654460L;
	
	@XmlElement private String crisis_code;
	@XmlElement private String crisis_name;
	@XmlElement private List<NominalLabel> nominal_labels;

	public AidrObject() {
		nominal_labels = new ArrayList<NominalLabel>();
	}

	public AidrObject(String crisis_code) {
		this();
		this.setCrisisCode(crisis_code);
	}

	public AidrObject(String crisis_code, String crisis_name) {
		this();
		this.setCrisisCode(crisis_code);
		this.setCrisisName(crisis_name);
	}

	public AidrObject(String crisis_code, String crisis_name, List<NominalLabel> nominal_labels) {
		this();
		this.setCrisisCode(crisis_code);
		this.setCrisisName(crisis_name);
		this.setNominalLabels(nominal_labels);
	}

	public String getCrisisCode() {
		return this.crisis_code;
	}

	public void setCrisisCode(String crisis_code) {
		this.crisis_code = crisis_code;
	}

	public String getCrisisName() {
		return this.crisis_name;
	}

	public void setCrisisName(String crisis_name) {
		this.crisis_name = crisis_name;
	}

	public List<NominalLabel> getNominalLabels() {
		return this.nominal_labels;
	}

	public void setNominalLabels(List<NominalLabel> nominal_labels) {
		this.nominal_labels = nominal_labels;
	}

}