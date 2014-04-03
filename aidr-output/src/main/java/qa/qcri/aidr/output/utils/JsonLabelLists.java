package qa.qcri.aidr.output.utils;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@SuppressWarnings("serial")
@XmlRootElement(name="JsonLabelLists")
public class JsonLabelLists implements Serializable {
	@XmlElement private ArrayList<String> labelNames;
	
	public JsonLabelLists() {
		labelNames = new ArrayList<String>();
	}		
	
	public void setLabelNames(JsonLabelLists labels) {
		//labelNames = new ArrayList<String>();
		labelNames.addAll(labels.labelNames);
	}
	
	public void createLabelNames(ArrayList<String> labels) {
		labelNames.addAll(labels);
	}
	
	public ArrayList<String> getLabelNames() {
		return labelNames;
	}
	
	
	@Override
	public String toString() {
		if (labelNames.isEmpty()) {
			return null;
		}
		StringBuilder value = new StringBuilder().append("{");
		for (String e: labelNames) {
			System.out.println("label: " + e);
			value.append(e).append(", ");
		}
		return value.substring(0, value.lastIndexOf(", ")).concat("}");
	}
}
