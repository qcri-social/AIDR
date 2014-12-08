package qa.qcri.aidr.dbmanager.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
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
		this.setDocumentId(documentId);
		this.setNominalLabelId(nominalLabelId);
		this.setUserId(userId);
	}

	public Long getDocumentId() throws PropertyNotSetException {
		if (documentId != null) {
			return this.documentId;
		} else {
			throw new PropertyNotSetException();
		}
	}

	public void setDocumentId(Long documentId) {
		if (documentId != null) {
			this.documentId = documentId;
		} else {
			throw new IllegalArgumentException("Argument cannot be null!");
		}
	}

	public Long getNominalLabelId() throws PropertyNotSetException {
		if (nominalLabelId != null) {
			return this.nominalLabelId;
		} else {
			throw new PropertyNotSetException();
		}
	}

	public void setNominalLabelId(Long nominalLabelId) {
		if (nominalLabelId != null) {
			this.nominalLabelId = nominalLabelId;
		} else {
			throw new IllegalArgumentException("Argument cannot be null!");
		}
	}

	public Long getUserId() throws PropertyNotSetException {
		if (userId != null) {
			return this.userId;
		} else {
			throw new PropertyNotSetException();
		}
	}

	public void setUserId(Long userId) {
		if (userId != null) {
			this.userId = userId;
		} else {
			throw new IllegalArgumentException("Argument cannot be null!");
		}
	}

	public DocumentNominalLabelId toEntity() {
		DocumentNominalLabelId id = new DocumentNominalLabelId(this.documentId, this.nominalLabelId, this.userId);
		return id;
	}

}
