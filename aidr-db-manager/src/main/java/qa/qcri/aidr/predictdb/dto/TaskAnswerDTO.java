package qa.qcri.aidr.predictdb.dto;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
public class TaskAnswerDTO implements Serializable {


    public TaskAnswerDTO(){
      // default
    }

    public  TaskAnswerDTO(Long documentID, Long userID, String answer){
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
