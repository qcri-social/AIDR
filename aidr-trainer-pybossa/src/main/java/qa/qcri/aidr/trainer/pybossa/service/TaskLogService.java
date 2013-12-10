package qa.qcri.aidr.trainer.pybossa.service;

import qa.qcri.aidr.trainer.pybossa.entity.TaskLog;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/28/13
 * Time: 1:34 PM
 * To change this template use File | Settings | File Templates.
 */
public interface TaskLogService {
    void createTaskLog(TaskLog taskLog);
    void updateTaskLog(TaskLog taskLog);
    List<TaskLog> getTaskLog(Long taskQueueID);
    List<TaskLog> getTaskLogByStatus(Long taskQueueID, int status);
}
