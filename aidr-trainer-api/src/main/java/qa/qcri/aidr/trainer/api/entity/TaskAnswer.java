package qa.qcri.aidr.trainer.api.entity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/15/13
 * Time: 7:12 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(catalog = "aidr_predict",name = "task_answer")
@XmlRootElement
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

    @XmlElement
    @Id
    @GeneratedValue
    @Column (name = "taskID")
    private Long taskID;

    @XmlElement
    @Column(name = "documentID", nullable = false)
    private Long documentID;
    
    @XmlElement
    @Column (name = "userID", nullable = false)
    private Long userID;
    
    @XmlElement
    @Column (name = "answer", nullable = true)
    private String answer;

    @XmlElement
    @Column (name = "timestamp", nullable = false)
    private Date timestamp;

    @XmlElement
    @Column (name = "fromTrustedUser", nullable = false)
    private boolean fromTrustedUser;


}
