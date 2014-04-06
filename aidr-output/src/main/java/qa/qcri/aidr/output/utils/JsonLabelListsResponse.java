package qa.qcri.aidr.output.utils;
import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@SuppressWarnings("serial")
@XmlRootElement(name="JsonLabelListsResponse")
public class JsonLabelListsResponse implements Serializable {
	private String id;
	private int value;
	private ArrayList<String> labelNames;
	private int status;
	
	public JsonLabelListsResponse() {
		labelNames = new ArrayList<String>();
	}		
	
	public void setLabelNames(JsonLabelListsResponse labels) {
		labelNames.addAll(labels.labelNames);
	}
	
	public void setLabelNames(ArrayList<String> labels) {
		labelNames.addAll(labels);
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public void createLabelNames(ArrayList<String> labels) {
		labelNames.addAll(labels);
	}
	
	public ArrayList<String> getLabelNames() {
		return labelNames;
	}
	
	public String getId() {
		return id;
	}
	
	public int getValue() {
		return value;
	}
	
	public int getStatus() {
		return status;
	}
	
	@Override
	public String toString() {
		if (labelNames.isEmpty()) {
			return null;
		}
		StringBuilder value = new StringBuilder().append("{id:").append(id).append(",");
		value.append("value:").append(value).append(",labelNames:[");
		for (String e: labelNames) {
			System.out.println("label: " + e);
			value.append(e).append(", ");
		}
		return value.substring(0, value.lastIndexOf(", ")).concat("]}");
	}

}
