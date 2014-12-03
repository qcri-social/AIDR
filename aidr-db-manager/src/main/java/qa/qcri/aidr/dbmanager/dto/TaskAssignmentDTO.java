package qa.qcri.aidr.dbmanager.dto;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.entities.task.Document;
import qa.qcri.aidr.dbmanager.entities.task.TaskAssignment;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
public class TaskAssignmentDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6766433678441426060L;

	@XmlElement
	private Document document;

	@XmlElement
	private TaskAssignmentIdDTO idDTO;

	@XmlElement
	private Date assignedAt;

	public TaskAssignmentDTO(){

	}

	public TaskAssignmentDTO(TaskAssignment taskAssignment){
		if (taskAssignment.hasDocument()) {
			this.setDocument(taskAssignment.getDocument());
		}
		this.setIdDTO(new TaskAssignmentIdDTO(taskAssignment.getId()));
		this.setAssignedAt(taskAssignment.getAssignedAt());

	}

	public TaskAssignmentDTO(Long documentID, Long userID, Date assignedAt){
		this.getIdDTO().setDocumentId(documentID);
		this.getIdDTO().setUserId(userID);
		this.assignedAt = assignedAt;
	}

	public  TaskAssignment toEntity() throws PropertyNotSetException {
		TaskAssignment taskAssignment = new TaskAssignment(this.getDocumentID(), this.getUserID());
		if (this.getIdDTO() != null) {
			taskAssignment.setId(this.getIdDTO().toEntity());
		}
		if (this.getAssignedAt() != null) {
			taskAssignment.setAssignedAt(getAssignedAt());
		}
		return taskAssignment;
	}

	public  TaskAssignment toEntity(Long documentID, Long userId) {
		TaskAssignment taskAssignment = new TaskAssignment(documentID, userId);
		if (this.getAssignedAt() != null) {
			taskAssignment.setAssignedAt(getAssignedAt());
		}
		return taskAssignment;
	}

	public Document getDocument() throws PropertyNotSetException {
		if (this.document != null) {
			return this.document;
		} else {
			throw new PropertyNotSetException();
		}
	}

	public void setDocument(Document document) {
		if (document == null) {
			throw new IllegalArgumentException("document cannot be null");
		}
		else{
			this.document = document;
		}

	}

	public TaskAssignmentIdDTO getIdDTO() {
		return this.idDTO;
	}

	public void setIdDTO(TaskAssignmentIdDTO idDTO) {
		if (idDTO != null) {
			this.idDTO = idDTO;
		} else {
			throw new IllegalArgumentException("Primary key cannot be null");
		}
	}

	public Long getDocumentID() {
		return document.getDocumentId();
	}

	public void setDocumentID(Long documentID) throws PropertyNotSetException {
		if (documentID == null) {
			throw new IllegalArgumentException("documentID cannot be null");
		}
		else{
			this.getDocument().setDocumentId(documentID);
		}

	}

	public Long getUserID() throws PropertyNotSetException {
		return getIdDTO().getUserId();
	}

	public void setUserID(Long userID) {
		if (userID == null) {
			throw new IllegalArgumentException("userID cannot be null");
		}
		else{
			this.getIdDTO().setUserId(userID);
		}

	}

	public Date getAssignedAt() {
		return assignedAt;
	}

	public void setAssignedAt(Date assignedAt) {
		this.assignedAt = assignedAt;
	}
}
