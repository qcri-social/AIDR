package qa.qcri.aidr.trainer.api.entity;

import javax.persistence.*;
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


    @Id
    @GeneratedValue
    @Column (name = "taskID")
    private Long taskID;

    @Column(name = "documentID", nullable = false)
    private Long documentID;

    @Column (name = "userID", nullable = false)
    private Long userID;

    @Column (name = "answer", nullable = true)
    private String answer;

    @Column (name = "timestamp", nullable = false)
    private Date timestamp;

    @Column (name = "fromTrustedUser", nullable = false)
    private boolean fromTrustedUser;


}
