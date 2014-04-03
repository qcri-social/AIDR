package qa.qcri.aidr.trainer.pybossa.dao;

import qa.qcri.aidr.trainer.pybossa.entity.TaskLog;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/28/13
 * Time: 12:38 PM
 * To change this template use File | Settings | File Templates.
 */
public interface TaskLogDao extends AbstractDao<TaskLog, String>  {

    void createTaskLog(TaskLog taskLog);
    List<TaskLog> getTaskLog(Long taskQueueID);
    List<TaskLog> getTaskLogByStatus(Long taskQueueID, int status);
    void deleteTaskLog(Long taskQueueID);

}
