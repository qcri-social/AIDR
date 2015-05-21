package qa.qcri.aidr.common.filter;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

//import org.codehaus.jackson.annotate.JsonProperty;

/*
@JsonTypeInfo(  
	    use = JsonTypeInfo.Id.NAME,  
	    include = JsonTypeInfo.As.PROPERTY,  
	    property = "query_type")  
	@JsonSubTypes({  
		@JsonSubTypes.Type(value = DateQueryJsonObject.class, name = "date_query"),  
		@JsonSubTypes.Type(value = ClassifierQueryJsonObject.class, name = "classifier_query") })
 */  
@SuppressWarnings("serial")
@XmlRootElement(name="QueryJsonObject")
public abstract class QueryJsonObject implements Serializable {
	@XmlElement public QueryType queryType;
	@XmlElement public ComparatorType comparator;
	@XmlElement public long timestamp;


	@XmlElement public String classifier_code;		// corresponds to "attribute_code" in nominal_attributes object	
	@XmlElement public String label_code;			// corresponds to "label_code" in nominal_attributes object
	@XmlElement public float min_confidence;		

	public static final float DEFAULT_CONFIDENCE_VALUE = (float) 0.0;

	public QueryJsonObject() {
		min_confidence = DEFAULT_CONFIDENCE_VALUE;
	}


	abstract public QueryType getQueryType();
	abstract public void setQueryType(QueryType queryType);

	abstract public String getClassifierCode();
	abstract public void setClassifierCode(String classifier_code);
	abstract public String getLabelCode();
	abstract public void setLabelCode(String label_code);
	abstract public float getConfidence();
	abstract public void setConfidence( Float confidence);

	abstract public ComparatorType getComparator();

	abstract public void setComparator(ComparatorType comparator);


	abstract public long getTime();
	abstract public void setTime(long time);
	
	abstract public Date getDate();

	@Override
	public String toString() {
		StringBuilder object = new StringBuilder();
		
		object.append("{\"query_type\": \"").append(queryType).append("\", ");
		if (classifier_code != null) {
			object.append("\"classifier_code\": \"").append(classifier_code).append("\", ");
		} else {
			object.append("\"classifier_code\": null, ");
		}
		object.append("\"timestamp\": ").append(timestamp).append(", ");
		object.append("\"comparator\": \"").append(comparator).append("\", ");
		if (label_code != null) {
			object.append("\"label_code\": \"").append(label_code).append("\", ");
		} else {
			object.append("\"label_code\": null, ");
		}
		object.append("\"min_confidence\": ").append(min_confidence).append("}");
		
		return object.toString();
	}
}
