package qa.qcri.aidr.task.dto;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import qa.qcri.aidr.task.entities.TaskAssignment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
public class TaskAssignmentDTO implements Serializable {
	@XmlElement
	private Long documentID;

	@XmlElement
	private Long userID;

	@XmlElement
	private Date assignedAt;

	public TaskAssignmentDTO(){

	}

	public TaskAssignmentDTO(Long documentID, Long userID, Date assignedAt){
		this.documentID = documentID;
		this.userID = userID;
		this.assignedAt = assignedAt;
	}

	public Long getDocumentID() {
		return documentID;
	}

	public void setDocumentID(Long documentID) {
		this.documentID = documentID;
	}

	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

	public Date getAssignedAt() {
		return assignedAt;
	}

	public void setAssignedAt(Date assignedAt) {
		this.assignedAt = assignedAt;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TaskAssignment taskAssignment = (TaskAssignment) o;

		return documentID.equals(taskAssignment.getDocumentID());
	}

}
