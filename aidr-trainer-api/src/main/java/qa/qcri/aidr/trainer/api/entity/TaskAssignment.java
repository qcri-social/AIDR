package qa.qcri.aidr.trainer.api.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table (catalog = "aidr_predict",name = "task_assignment")
public class TaskAssignment {

    @Id
    @Column (name = "documentID")
    private Long documentID;

    @Column (name = "userID", nullable = false)
    private Long userID;

    @Version
    @Column (name = "assignedAt", nullable = false)
    private Date assignedAt;

    public TaskAssignment(){

    }

    public TaskAssignment(Long documentID, Long userID, Date assignedAt){
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

        return documentID.equals(taskAssignment.documentID);
    }

}
