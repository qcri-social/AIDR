package qa.qcri.aidr.manager.dto;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class HumanLabeledDocumentListResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlElement private List<HumanLabeledDocumentDTO> labeledData;

	@XmlElement private Integer total;

	@XmlElement private String statusCode;
	
	@XmlElement private String message;
	
	public HumanLabeledDocumentListResponse() {
	}

	public HumanLabeledDocumentListResponse(List<HumanLabeledDocumentDTO> labeledData) {
		if (labeledData != null) {
			this.setLabeledData(labeledData);
		} 
	}

	public HumanLabeledDocumentListResponse(String statusCode) {
		this.setStatusCode(statusCode);
	}
	
	public HumanLabeledDocumentListResponse(String statusCode, String message) {
		this.setStatusCode(statusCode);
		this.setMessage(message);
	}
	
	
	public HumanLabeledDocumentListResponse(String statusCode, List<HumanLabeledDocumentDTO> labeledData) {
		this(labeledData);
		this.setStatusCode(statusCode);
	}
	
	public HumanLabeledDocumentListResponse(String statusCode, List<HumanLabeledDocumentDTO> labeledData, String message) {
		this(labeledData);
		this.setStatusCode(statusCode);
		this.setMessage(message);
	}
	
	
	public List<HumanLabeledDocumentDTO> getLabeledData() {
		return this.labeledData;
	}

	public void setLabeledData(List<HumanLabeledDocumentDTO> labeledData) {
		this.labeledData = labeledData;
		if (labeledData != null && !labeledData.isEmpty()) {
			this.setTotal(labeledData.size());
		}
	}

	public Integer getTotal() {
		return this.total;
	}

	public void setTotal(Integer total) {
		if (total != null) {
			this.total = total;
		}
	}
	 
	public String getStatusCode() {
		return this.statusCode;
	}
	
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
}
