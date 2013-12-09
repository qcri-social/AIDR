package qa.qcri.aidr.trainer.api.entity.keychain;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 10/3/13
 * Time: 5:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class TaskLogKey implements java.io.Serializable{


    private Long taskQueueID;
    private Integer status;
    public TaskLogKey() { }

    public TaskLogKey(Long taskQueueID, Integer status) {
        this.taskQueueID = taskQueueID;
        this.status = status;
    }

    public Long getTaskQueueID() {
        return taskQueueID;
    }

    public void setTaskQueueID(Long taskQueueID) {
        this.taskQueueID = taskQueueID;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public boolean equals(Object key) {
        boolean result = true;
        if (!(key instanceof TaskLogKey)) {return false;}

        Long otherTaskQueueID = ((TaskLogKey)key).getTaskQueueID();
        Integer otherStatus = ((TaskLogKey)key).getStatus();

        if (otherTaskQueueID == null || taskQueueID == null) {
            result = false;
        }else {
            result = taskQueueID.equals(otherTaskQueueID);
        }

        if (status == null || otherStatus == null) {
            result = false;
        }else {
            result = status.equals(otherStatus);
        }

        return result;
    }

    public int hashCode() {
        int code = 0;
        if (taskQueueID!=null) {code +=taskQueueID;}
        if (status!=null) {code +=status;}
        return code;
    }
}
