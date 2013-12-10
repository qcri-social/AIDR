package qa.qcri.aidr.trainer.pybossa.entity;

import qa.qcri.aidr.trainer.pybossa.entity.keychain.TaskLogKey;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity @IdClass(TaskLogKey.class)
@Table(name = "taskLog")
public class TaskLog implements Serializable {

    private static final long serialVersionUID = -5527566248002296042L;

    public TaskLog(){}

    public TaskLog(Long taskQueueID, Integer status)
    {
       this.taskQueueID =   taskQueueID;
       this.status = status;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getTaskQueueID() {
        return taskQueueID;
    }

    public void setTaskQueueID(Long taskQueueID) {
        this.taskQueueID = taskQueueID;
    }


    @Id
    @Column(name = "taskQueueID")
    private Long taskQueueID;

    @Id
    @Column (name = "status", nullable = false)
    private int status;

    @Column (name = "created", nullable = false)
    private Date created;
}
