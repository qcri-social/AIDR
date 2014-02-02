package qa.qcri.aidr.trainer.api.dao;

import qa.qcri.aidr.trainer.api.entity.TaskQueueResponse;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 1/18/14
 * Time: 4:14 PM
 * To change this template use File | Settings | File Templates.
 */
public interface TaskQueueResponseDao {

    List<TaskQueueResponse> getTaskQueueResponseByTaskQueueID(Long taskQueueID);
    List<TaskQueueResponse> getTaskQueueResponseByTaskQueueIDBasedOnLastUpdate(Long taskQueueID, Date updated);
}
