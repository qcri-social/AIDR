package qa.qcri.aidr.output.filter;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

//import org.codehaus.jackson.annotate.JsonProperty;

@SuppressWarnings("serial")
@XmlRootElement(name="JsonQueryList")
public class JsonQueryList implements Serializable {
	@XmlElement private ArrayList<GenericInputQuery> constraints;
	
	public JsonQueryList() {
		constraints = new ArrayList<GenericInputQuery>();
	}
	
	public void setConstraints(JsonQueryList query) {
		constraints.addAll(query.constraints);
	}
	
	
	public void createConstraint(GenericInputQuery query) {
		constraints.add(query);
	}
	
	//@JsonProperty("constraints")
	public ArrayList<QueryJsonObject> getConstraints() {
		ArrayList<QueryJsonObject> temp = new ArrayList<QueryJsonObject>();
		for (GenericInputQuery g: constraints) {
			QueryJsonObject t = (QueryJsonObject) g;
			temp.add(t);
		}
		return temp;
	}
	
	@Override
	public String toString() {
		StringBuilder object = new StringBuilder();
		int constraintCount = 0;
		object.append("{\"constraints\": [");
		for (GenericInputQuery q: constraints) {
			object.append("{\"query_type\": \"").append(q.queryType).append("\", ");
			if (q.classifier_code != null) {
				object.append("\"classifier_code\": \"").append(q.classifier_code).append("\", ");
			} else {
				object.append("\"classifier_code\": null, ");
			}
			object.append("\"comparator\": \"").append(q.comparator).append("\", ");
			object.append("\"timestamp\": ").append(q.timestamp).append(", ");
			if (q.label_code != null) {
				object.append("\"label_code\": \"").append(q.label_code).append("\", ");
			} else {
				object.append("\"label_code\": null, ");
			}
			object.append("\"min_confidence\": ").append(q.min_confidence).append("},");
			++constraintCount;
		}
		if (constraintCount == 0)  
			object.append("]}");
		else 
			object.deleteCharAt(object.length()-1).append("]}");
		return object.toString();
	}
}
