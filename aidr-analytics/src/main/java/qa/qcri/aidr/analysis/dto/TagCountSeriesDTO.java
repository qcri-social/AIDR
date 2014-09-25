package qa.qcri.aidr.analysis.dto;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TagCountSeriesDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlElement
	private String crisisCode;
	
	@XmlElement
	private String attributeCode;
	
	@XmlElement
	private Long granularity;
	
	@XmlElement 
	private Integer total;
	
	@XmlElement
	private List<TimeWindowTagCountDTO> timeSeriesData;
	
	public TagCountSeriesDTO() {}
	
	public String getCrisisCode() {
		return this.crisisCode;
	}
	
	public void setCrisisCode(String crisisCode) {
		this.crisisCode = crisisCode; 
	}
	
	public String getAttributeCode() {
		return this.attributeCode;
	}
	
	public void setAttributeCode(String attributeCode) {
		this.attributeCode = attributeCode;
	}
	
	public Long getGranularity() {
		return this.granularity;
	}
	
	public void setGranularity(Long granularity) {
		this.granularity = granularity;
	}
	
	public Integer getTotal() {
		return this.total;
	}
	
	public void setTotal(Integer total) {
		this.total = total;
	}
	
	public List<TimeWindowTagCountDTO> getTimeSeriesData() {
		return this.timeSeriesData;
	}
	
	public void setTimeSeriesData(List<TimeWindowTagCountDTO> timeSeriesData) {
		this.timeSeriesData = timeSeriesData;
	}
	
}
