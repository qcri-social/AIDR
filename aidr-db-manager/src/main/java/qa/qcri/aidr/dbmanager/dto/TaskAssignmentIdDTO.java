package qa.qcri.aidr.dbmanager.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.entities.task.TaskAssignmentId;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
public class TaskAssignmentIdDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7766458582186301990L;

	@XmlElement
	private Long documentId;

	@XmlElement
	private Long userId;

	public TaskAssignmentIdDTO() {
	}

	public TaskAssignmentIdDTO(TaskAssignmentId id) {
		this.setDocumentId(id.getDocumentId());
		this.setUserId(id.getUserId());
	}

	public TaskAssignmentIdDTO(Long documentId, Long userId) {
		this.documentId = documentId;
		this.userId = userId;
	}

	@Column(name = "documentID", nullable = false)
	public Long getDocumentId() {
			return this.documentId;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

	@Column(name = "userID", nullable = false)
	public Long getUserId() {
			return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public TaskAssignmentId toEntity() {
		TaskAssignmentId entity = new TaskAssignmentId(this.getDocumentId(), this.getUserId());
		return entity;
	}
}
