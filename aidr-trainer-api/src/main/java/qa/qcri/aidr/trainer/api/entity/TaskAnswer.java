package qa.qcri.aidr.trainer.api.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import qa.qcri.aidr.dbmanager.dto.TaskAnswerDTO;

import java.io.Serializable;
import java.util.Date;

/**
 * reated by: koushik
 */

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
public class TaskAnswer implements Serializable {

	private static final long serialVersionUID = -5527566248002296042L;

	public TaskAnswer(){
		// default
	}

	public  TaskAnswer(Long documentID, Long userID, String answer){
		this.documentID = documentID;
		this.userID = userID;
		this.answer = answer;
		this.timestamp = new Date();

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

	public TaskAnswer toLocalTaskAnswer(TaskAnswerDTO t) {
		try {
			if (t != null) {
				TaskAnswer taskAnswer = new TaskAnswer(t.getDocumentID(), t.getUserID(), t.getAnswer());
				return taskAnswer;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public TaskAnswerDTO toDTO() {
		try {	
			TaskAnswerDTO dto = new TaskAnswerDTO(this.getDocumentID(), this.getUserID(), this.getAnswer());
			dto.setFromTrustedUser(this.isFromTrustedUser());
			return dto;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

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


}
