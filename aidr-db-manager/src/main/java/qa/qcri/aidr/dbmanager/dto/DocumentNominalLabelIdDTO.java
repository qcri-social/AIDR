package qa.qcri.aidr.dbmanager.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import qa.qcri.aidr.dbmanager.entities.task.DocumentNominalLabelId;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
public class DocumentNominalLabelIdDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3349129032806499784L;

	@XmlElement
	private Long documentId;
	
	@XmlElement
	private Long nominalLabelId;
	
	@XmlElement
	private Long userId;

	public DocumentNominalLabelIdDTO() {
	}

	public DocumentNominalLabelIdDTO(DocumentNominalLabelId id) {
		this.setDocumentId(id.getDocumentId());
		this.setNominalLabelId(id.getNominalLabelId());
		this.setUserId(id.getUserId());
	}
	
	public DocumentNominalLabelIdDTO(Long documentId, Long nominalLabelId,
			Long userId) {
		this.documentId = documentId;
		this.nominalLabelId = nominalLabelId;
		this.userId = userId;
	}

	public Long getDocumentId() {
		return this.documentId;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

	public Long getNominalLabelId() {
		return this.nominalLabelId;
	}

	public void setNominalLabelId(Long nominalLabelId) {
		this.nominalLabelId = nominalLabelId;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public DocumentNominalLabelId toEntity() {
		DocumentNominalLabelId id = new DocumentNominalLabelId(this.documentId, this.nominalLabelId, this.userId);
		return id;
	}

}
