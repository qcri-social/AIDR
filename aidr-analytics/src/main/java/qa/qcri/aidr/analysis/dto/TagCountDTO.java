package qa.qcri.aidr.analysis.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import qa.qcri.aidr.analysis.entity.TagData;

@XmlRootElement
public class TagCountDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlElement
	private String labelCode;
	
	@XmlElement
	private Integer count;
	
	public TagCountDTO() {}
	
	public String getLabelCode() {
		return this.labelCode;
	}
	
	public void setLabelCode(String labelCode) {
		this.labelCode = labelCode;
	}
	
	public Integer getCount() {
		return this.count;
	}
	
	public void setCount(Integer count) {
		this.count = count;
	}
}
