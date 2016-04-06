package qa.qcri.aidr.trainer.pybossa.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qa.qcri.aidr.trainer.pybossa.dao.TaskLogDao;
import qa.qcri.aidr.trainer.pybossa.entity.TaskLog;
import qa.qcri.aidr.trainer.pybossa.service.TaskLogService;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/28/13
 * Time: 1:36 PM
 * To change this template use File | Settings | File Templates.
 */
@Service("taskLogService")
@Transactional(readOnly = true)
public class TaskLogServiceImpl implements TaskLogService {
	
	private static Logger logger = Logger.getLogger(TaskLogServiceImpl.class);

	
    @Autowired
    private TaskLogDao taskLogDao;

    @Override
    @Transactional(readOnly = false)
    public void createTaskLog(TaskLog taskLog) {
        try{
            taskLogDao.createTaskLog(taskLog);
        }
        catch(Exception ex){
            logger.error("createTaskLog exception : " + taskLog.getTaskQueueID());

            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void updateTaskLog(TaskLog taskLog) {
        try{
            taskLogDao.saveOrUpdateTaskLog(taskLog);
        }
        catch(Exception ex){
            logger.error("updateTaskLog Exception: " + taskLog.getTaskQueueID());

            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public List<TaskLog> getTaskLog(Long taskQueueID) {

        return taskLogDao.getTaskLog(taskQueueID); //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<TaskLog> getTaskLogByStatus(Long taskQueueID, int status) {
        return taskLogDao.getTaskLogByStatus(taskQueueID, status);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteAbandonedTaskLog(Long taskQueueID) {
        taskLogDao.deleteTaskLog(taskQueueID);
    }

}
