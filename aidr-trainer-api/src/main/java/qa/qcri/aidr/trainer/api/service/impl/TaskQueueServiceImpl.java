package qa.qcri.aidr.trainer.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qa.qcri.aidr.trainer.api.dao.TaskQueueDao;
import qa.qcri.aidr.trainer.api.entity.TaskQueue;
import qa.qcri.aidr.trainer.api.service.TaskQueueService;

import java.util.List;

@Service("taskStatusLookUpService")
@Transactional(readOnly = false)
public class TaskQueueServiceImpl implements TaskQueueService {

    @Autowired
    private TaskQueueDao taskQueueDao;

    @Override
    public List<TaskQueue> getTaskQueueSet(Long taskID, Long clientAppID, Long documentID) {
        return taskQueueDao.findTaskQueue(taskID,clientAppID, documentID );  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<TaskQueue> getTaskQueueByDocument(Long clientAppID, Long documentID) {
        return taskQueueDao.findTaskQueueByDocument(clientAppID, documentID );  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<TaskQueue> getTaskQueueByStatus(String column, Integer status) {
        return taskQueueDao.findTaskQueueByStatus(column,status);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<TaskQueue> getTaskQueueByClientAppStatus(Long clientAppID, Integer status) {
        return taskQueueDao.findTaskQueueSetByStatus(clientAppID, status);  //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    public Integer getCountTaskQeueByStatus(String column, Integer status) {
        return taskQueueDao.findTaskQueueByStatus(column,status).size();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Integer getCountTaskQeueByStatusAndClientApp(Long clientAppID, Integer status) {
        List<TaskQueue> taskQueueList = taskQueueDao.findTaskQueueSetByStatus(clientAppID, status);
        if(taskQueueList!=null)
            return taskQueueList.size();  //To change body of implemented methods use File | Settings | File Templates.
        return 0;
    }

    @Override
    public List<TaskQueue> getTaskQueueByClientApp(Long clientAppID) {
        return taskQueueDao.findTaskQueueSetByclientApp(clientAppID);
    }
}
