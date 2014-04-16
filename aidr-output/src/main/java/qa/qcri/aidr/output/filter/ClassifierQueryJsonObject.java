package qa.qcri.aidr.output.filter;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

import qa.qcri.aidr.output.filter.ComparatorType;

@SuppressWarnings("serial")
@XmlRootElement(name="ClassifierQueryJsonObject")
public class ClassifierQueryJsonObject extends QueryJsonObject {
	
	public ClassifierQueryJsonObject() {
		super();
		min_confidence = DEFAULT_CONFIDENCE_VALUE;					// default setting
		comparator = ComparatorType.has_confidence;				// default setting
	}
	
	
	@Override
	public QueryType getQueryType() {
		return queryType;
	}
	
	@Override
	public void setQueryType(QueryType queryType) {
		this.queryType = queryType;
	}
	
	
	@JsonProperty("classifier_code")
	public String getClassifierCode() {
		return classifier_code;
	}
	
	
	public void setClassifierCode(String classifier_code) {
		this.classifier_code = classifier_code;
	}
	
	@JsonProperty("label_code")
	public String getLabelCode() {
		return label_code;
	}
	
	public void setLabelCode(String label_code) {
		this.label_code = label_code;
	}
	
	@JsonProperty("min_confidence")
	public float getConfidence() {
		return min_confidence;
	}
	
	@JsonProperty("confidence")
	public void setConfidence(Float min_confidence) {
		this.min_confidence = min_confidence;
	}
	
	@JsonProperty("comparator")
	public ComparatorType getComparator() {
		return comparator;
	}
	
	public void setComparator(ComparatorType comparator) {
		this.comparator = comparator;
	}


	@Override
	public long getTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setTime(long time) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public String toString() {
		StringBuilder object = new StringBuilder();
		object.append("{\"query_type\": \"").append(queryType).append("\", ");
		if (classifier_code != null) {
			object.append("\"classifier_code\": \"").append(classifier_code).append("\", ");
		} else {
			object.append("\"classifier_code\": null, ");
		}
		object.append("\"comparator\": \"").append(comparator).append("\", ");
		if (label_code != null) {
			object.append("\"label_code\": \"").append(label_code).append("\", ");
		} else {
			object.append("\"label_code\": null, ");
		}
		object.append("\"min_confidence\": ").append(min_confidence).append("}");
		return object.toString();
	}


	@Override
	public Date getDate() {
		return null;
	}

}
