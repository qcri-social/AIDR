package qa.qcri.aidr.analysis.dto;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TimeWindowTagCountDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@XmlElement
	private Long timestamp;
	
	@XmlElement
	private List<TagCountDTO> tagCountData;
	
	public TimeWindowTagCountDTO() {}
	
	public Long getTimestamp() {
		return this.timestamp;
	}
	
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	
	public List<TagCountDTO> getTagCountData() {
		return this.tagCountData;
	}
	
	public void setTagCountData(List<TagCountDTO> tagCountData) {
		this.tagCountData = tagCountData;
	}
}
