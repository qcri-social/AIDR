package qa.qcri.aidr.trainer.pybossa.service;

import qa.qcri.aidr.trainer.pybossa.entity.ClientAppAnswer;
import qa.qcri.aidr.trainer.pybossa.entity.TaskQueueResponse;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 10/20/13
 * Time: 1:56 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ClientAppResponseService {

    ClientAppAnswer getClientAppAnswer(Long clientAppID);
    void processTaskQueueResponse(TaskQueueResponse taskQueueResponse);
    List<TaskQueueResponse> getTaskQueueResponse(Long taskQueueID);
    List<TaskQueueResponse> getTaskQueueResponseByContent();
}
