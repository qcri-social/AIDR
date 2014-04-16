package qa.qcri.aidr.output.filter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

import qa.qcri.aidr.output.filter.ComparatorType;

@SuppressWarnings("serial")
@XmlRootElement(name="DateQueryJsonObject")
public class DateQueryJsonObject extends QueryJsonObject {
	
	public DateQueryJsonObject() {
		super();
	}
	
	
	@Override
	public QueryType getQueryType() {
		return queryType;
	}
	
	@Override
	public void setQueryType(QueryType queryType) {
		this.queryType = queryType;
	}
	
	@JsonProperty("comparator")
	public ComparatorType getComparator() {
		return comparator;
	}
	
	public void setComparator(ComparatorType comparator) {
		this.comparator = comparator;
	}
	
	@JsonProperty("timestamp")
	public long getTime() {
		return timestamp;
	}
	
	@Override
	@JsonProperty("timestamp")
	public void setTime(long time) {
		this.timestamp = time;
	}

	@Override
	public Date getDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		Date date = new Date(this.timestamp * 1000L);
		
		return date;
	}

	@Override
	public String getClassifierCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setClassifierCode(String classifier_code) {
		// TODO Auto-generated method stub
	}

	@Override
	public String getLabelCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLabelCode(String label_code) {
		// TODO Auto-generated method stub
	}

	@Override
	@JsonProperty("min_confidence")
	public float getConfidence() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	@JsonProperty("min_confidence")
	public void setConfidence(Float confidence) {
		// TODO Auto-generated method stub	
	}
	
	@Override
	public String toString() {
		StringBuilder object = new StringBuilder();
		object.append("{\"query_type\": \"").append(queryType).append("\", ");
		object.append("\"comparator\": \"").append(comparator).append("\", ");
		object.append("\"timestamp\": ").append(getDate()).append("}");
		return object.toString();
	}

}	


