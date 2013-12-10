package qa.qcri.aidr.trainer.pybossa.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qa.qcri.aidr.trainer.pybossa.dao.TaskLogDao;
import qa.qcri.aidr.trainer.pybossa.entity.TaskLog;
import qa.qcri.aidr.trainer.pybossa.service.TaskLogService;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/28/13
 * Time: 1:36 PM
 * To change this template use File | Settings | File Templates.
 */
@Service("taskLogService")
@Transactional(readOnly = false)
public class TaskLogServiceImpl implements TaskLogService {

    @Autowired
    private TaskLogDao taskLogDao;

    @Override
    @Transactional(readOnly = false)
    public void createTaskLog(TaskLog taskLog) {
        taskLogDao.saveOrUpdate(taskLog);
    }

    @Override
    @Transactional(readOnly = false)
    public void updateTaskLog(TaskLog taskLog) {
        taskLogDao.saveOrUpdate(taskLog);
    }

    @Override
    public List<TaskLog> getTaskLog(Long taskQueueID) {

        return taskLogDao.getTaskLog(taskQueueID); //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<TaskLog> getTaskLogByStatus(Long taskQueueID, int status) {
        return taskLogDao.getTaskLogByStatus(taskQueueID, status);  //To change body of implemented methods use File | Settings | File Templates.
    }

}
