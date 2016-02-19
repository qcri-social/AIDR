package qa.qcri.aidr.trainer.api.entity;


import qa.qcri.aidr.trainer.api.entity.keychain.TaskLogKey;

import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;

@Entity @IdClass(TaskLogKey.class)
@Table(name = "task_log")
@JsonIgnoreProperties(ignoreUnknown=true)
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
    @Column(name = "task_queue_id")
    private Long taskQueueID;

    @Id
    @Column (name = "status", nullable = false)
    private int status;

    @Column (name = "created", nullable = false)
    private Date created;
}
