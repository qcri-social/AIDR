package qa.qcri.aidr.dbmanager.dto;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import qa.qcri.aidr.dbmanager.entities.misc.Users;
import qa.qcri.aidr.dbmanager.entities.task.Document;
import qa.qcri.aidr.dbmanager.entities.task.TaskAssignment;

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

    public  TaskAssignment toEntity(Long documentID, Long userId) {
        TaskAssignment taskAssignment = new TaskAssignment(documentID, userId);
        return taskAssignment;
    }

	public Long getDocumentID() {
		return documentID;
	}

	public void setDocumentID(Long documentID) {
        if (documentID == null) {
            throw new IllegalArgumentException("documentID cannot be null");
        }
        else{
            this.documentID = documentID;
        }

	}

	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
        if (userID == null) {
            throw new IllegalArgumentException("userID cannot be null");
        }
        else{
            this.userID = userID;
        }

	}

	public Date getAssignedAt() {
		return assignedAt;
	}

	public void setAssignedAt(Date assignedAt) {
        if (assignedAt == null) {
            throw new IllegalArgumentException("assignedAt cannot be null");
        }
        else{
		    this.assignedAt = assignedAt;
        }
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TaskAssignmentDTO taskAssignment = (TaskAssignmentDTO) o;

		return documentID.equals(taskAssignment.getDocumentID());
	}

}
