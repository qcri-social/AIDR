package qa.qcri.aidr.trainer.pybossa.dao;

import qa.qcri.aidr.trainer.pybossa.entity.TaskQueueResponse;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 10/20/13
 * Time: 1:45 AM
 * To change this template use File | Settings | File Templates.
 */
public interface TaskQueueResponseDao extends AbstractDao<TaskQueueResponse, String>  {

    void addTaskQueueResponse(TaskQueueResponse taskQueueResponse);
    List<TaskQueueResponse> getTaskQueueResponse(Long taskQueueID);
    List<TaskQueueResponse> getTaskQueueResponseByContent();
}
