package qa.qcri.aidr.common.filter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.xml.bind.annotation.XmlRootElement;

import qa.qcri.aidr.common.code.DateFormatConfig;

import com.fasterxml.jackson.annotation.JsonProperty;

//import org.codehaus.jackson.annotate.JsonProperty;

@SuppressWarnings("serial")
@XmlRootElement(name="GenericInputQuery")
public class GenericInputQuery extends QueryJsonObject implements Serializable {
	
	public GenericInputQuery() {
		min_confidence = DEFAULT_CONFIDENCE_VALUE;
	}
	
	@Override
	public String getClassifierCode() {
		return classifier_code;
	}

	@Override
	public void setClassifierCode(String classifier_code) {
		this.classifier_code = classifier_code;
		
	}

	@Override
	public String getLabelCode() {
		return label_code;
	}

	@Override
	public void setLabelCode(String label_code) {
		this.label_code = label_code;
		
	}

	@Override
	@JsonProperty("min_confidence")
	public float getConfidence() {
		return min_confidence;
	}
	
	@Override
	@JsonProperty("min_confidence")
	public void setConfidence(Float min_confidence) {
		this.min_confidence = min_confidence;
	}
	
	@Override
	public ComparatorType getComparator() {
		return comparator;
	}

	@Override
	public void setComparator(ComparatorType comparator) {
		this.comparator = comparator;
		
	}

	@Override
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
	public QueryType getQueryType() {
		return queryType;
	}

	@Override
	public void setQueryType(QueryType queryType) {
		this.queryType = queryType;
	}

	@Override
	public Date getDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormatConfig.ISODateFormat);
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		Date date = new Date(this.timestamp * 1000);
		
		return date;
	}

}
