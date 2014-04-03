package qa.qcri.aidr.trainer.pybossa.service;

import qa.qcri.aidr.trainer.pybossa.entity.TaskQueue;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/25/13
 * Time: 9:26 AM
 * To change this template use File | Settings | File Templates.
 */
public interface TaskQueueService {
    void createTaskQueue(TaskQueue taskQueue);
    void updateTaskQueue(TaskQueue taskQueue);
    List<TaskQueue> getTaskQueueSet(Long taskID, Long clientAppID, Long documentID);
    List<TaskQueue> getTaskQueueByDocument(Long clientAppID, Long documentID);
    List<TaskQueue> getTaskQueueByStatus(String column,Integer status);
    List<TaskQueue> getTaskQueueByClientAppStatus(Long clientAppID,Integer status);
    Integer getCountTaskQeueByStatus(String column,Integer status);
    Integer getCountTaskQeueByStatusAndClientApp(Long clientAppID,Integer status);
    void deleteAbandonedTaskQueue(Long taskQueueID);

}
