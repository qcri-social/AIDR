package qa.qcri.aidr.dbmanager.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import qa.qcri.aidr.dbmanager.entities.task.TaskAnswer;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
public class TaskAnswerDTO implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 5525860415454423643L;

	@XmlElement
	private Long taskID;

	@XmlElement
	private Long documentID;

	@XmlElement
	private Long userID;

	@XmlElement
	private String answer;

	@XmlElement
	private Date timestamp;

	@XmlElement
	private boolean fromTrustedUser;


	public TaskAnswerDTO(){
		// default
	}

	public  TaskAnswerDTO(Long documentID, Long userID, String answer){
		this.documentID = documentID;
		this.userID = userID;
		this.answer = answer;
	}

	public TaskAnswerDTO(TaskAnswer taskAnswer){
		if (taskAnswer != null) {
			if (taskAnswer.getId() != null) {
				if (taskAnswer.getId().getTaskId() != null) {
					this.setTaskID(taskAnswer.getId().getTaskId());
				}
				this.setDocumentID(taskAnswer.getId().getDocumentId());
				this.setUserID(taskAnswer.getId().getUserId());
			}
			this.setAnswer(taskAnswer.getAnswer());
			this.setTimestamp(taskAnswer.getTimestamp());
		}
	}


	public TaskAnswer toEntity() {
		TaskAnswer entity = new TaskAnswer(this.getDocumentID(), this.getUserID(), this.getAnswer(), this.isFromTrustedUser());
		if (this.getTaskID() != null) {
			entity.getId().setTaskId(this.getTaskID());
		}
		if (this.getTimestamp() != null) {
			entity.setTimestamp(this.getTimestamp());
		}
		return entity;
	}

	public List<TaskAnswer> toEntity(List<TaskAnswerDTO> answers){
		List<TaskAnswer> taskAnswers = new ArrayList<TaskAnswer>();

		for (TaskAnswerDTO dto: answers) {
			TaskAnswer t = new TaskAnswer(dto.getDocumentID(), dto.getUserID(), dto.getAnswer(), dto.isFromTrustedUser());
			if (this.getTaskID() != null) {
				t.getId().setTaskId(this.getTaskID());
			}
			if (this.getTimestamp() != null) {
				t.setTimestamp(this.getTimestamp());
			}
			taskAnswers.add(t);
		}

		return taskAnswers;
	}


	public Long getDocumentID() {
		return documentID;
	}

	public void setDocumentID(Long documentID) {
		if(documentID == null)
			throw new IllegalArgumentException("documentID cannot be null");

		this.documentID = documentID;
	}

	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		if(userID == null)
			throw new IllegalArgumentException("userID cannot be null");

		this.userID = userID;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public boolean isFromTrustedUser() {
		return fromTrustedUser;
	}

	public void setFromTrustedUser(boolean fromTrustedUser) {
		this.fromTrustedUser = fromTrustedUser;
	}

	public Long getTaskID() {
		return taskID;
	}

	public void setTaskID(Long taskID) {
		this.taskID = taskID;
	}


}
