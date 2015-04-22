package qa.qcri.aidr.trainer.pybossa.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qa.qcri.aidr.trainer.pybossa.dao.ClientAppAnswerDao;
import qa.qcri.aidr.trainer.pybossa.dao.TaskQueueResponseDao;
import qa.qcri.aidr.trainer.pybossa.entity.ClientAppAnswer;
import qa.qcri.aidr.trainer.pybossa.entity.TaskQueueResponse;
import qa.qcri.aidr.trainer.pybossa.service.ClientAppResponseService;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 10/20/13
 * Time: 1:58 AM
 * To change this template use File | Settings | File Templates.
 */
@Service("ClientAppResponseService")
@Transactional(readOnly = true)
public class ClientAppResponseServiceImpl implements ClientAppResponseService {

    @Autowired
    private ClientAppAnswerDao clientAppAnswerDao;

    @Autowired
    private TaskQueueResponseDao taskQueueResponseDao;

    @Override
    public ClientAppAnswer getClientAppAnswer(Long clientAppID) {
        return clientAppAnswerDao.findClientAppAnswerByID(clientAppID);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    @Transactional(readOnly = false)
    public void processTaskQueueResponse(TaskQueueResponse taskQueueResponse) {

        taskQueueResponseDao.addTaskQueueResponse(taskQueueResponse);
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<TaskQueueResponse> getTaskQueueResponse(Long taskQueueID) {
        return taskQueueResponseDao.getTaskQueueResponse(taskQueueID);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<TaskQueueResponse> getTaskQueueResponseByContent() {
        return taskQueueResponseDao.getTaskQueueResponseByContent();
    }

    @Override
    @Transactional(readOnly = false)
    public void saveClientAppAnswer(Long clientAppID, String answerJson, int cutOffValue) {
        clientAppAnswerDao.addClientAppAnswer(clientAppID, answerJson, cutOffValue);
    }

}
