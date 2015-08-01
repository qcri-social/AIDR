package qa.qcri.aidr.dbmanager.dto;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.entities.task.Document;
import qa.qcri.aidr.dbmanager.entities.task.TaskAssignment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
public class TaskAssignmentDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6766433678441426060L;
	private Logger logger = Logger.getLogger(TaskAssignmentDTO.class);

	@XmlElement
	private DocumentDTO document;

	@XmlElement
	private TaskAssignmentIdDTO idDTO;

	@XmlElement
	private Date assignedAt;

	public TaskAssignmentDTO(){

	}

	public TaskAssignmentDTO(TaskAssignment taskAssignment) throws PropertyNotSetException{
		if (taskAssignment != null) {
			if (taskAssignment.hasDocument()) {
				Document doc = new Document(taskAssignment.getDocument().getCrisis(), taskAssignment.getDocument().isIsEvaluationSet(),
						taskAssignment.getDocument().isHasHumanLabels(), taskAssignment.getDocument().getValueAsTrainingSample(),
						taskAssignment.getDocument().getReceivedAt(), taskAssignment.getDocument().getLanguage(), taskAssignment.getDocument().getDoctype(), 
						taskAssignment.getDocument().getData());
				doc.setWordFeatures(taskAssignment.getDocument().getWordFeatures());
				doc.setGeoFeatures(taskAssignment.getDocument().getGeoFeatures());
				doc.setDocumentId(taskAssignment.getDocument().getDocumentId());
				this.setDocument(new DocumentDTO(doc));
			}
			this.setIdDTO(new TaskAssignmentIdDTO(taskAssignment.getId()));
			this.setAssignedAt(taskAssignment.getAssignedAt());
		}
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

	public DocumentDTO getDocument()  {
		return this.document;
	}

	public void setDocument(DocumentDTO document) {
		this.document = document;
	}

	public TaskAssignmentIdDTO getIdDTO() {
		return this.idDTO;
	}

	public void setIdDTO(TaskAssignmentIdDTO idDTO) {
		if (idDTO != null) {
			this.idDTO = idDTO;
		} else {
			logger.error("Primary key cannot be null");
			throw new IllegalArgumentException("Primary key cannot be null");
		}
	}

	public Long getDocumentID() {
		return document.getDocumentID();
	}

	public void setDocumentID(Long documentID) throws PropertyNotSetException {
		if (documentID == null) {
			logger.error("documentID cannot be null");
			throw new IllegalArgumentException("documentID cannot be null");
		}
		else{
			this.getDocument().setDocumentID(documentID);
		}

	}

	public Long getUserID() {
		return getIdDTO().getUserId();
	}

	public void setUserID(Long userID) {
		if (userID == null) {
			logger.error("userID cannot be null");
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
