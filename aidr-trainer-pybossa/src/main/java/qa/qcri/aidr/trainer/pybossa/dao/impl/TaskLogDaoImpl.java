package qa.qcri.aidr.trainer.pybossa.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import qa.qcri.aidr.trainer.pybossa.dao.TaskLogDao;
import qa.qcri.aidr.trainer.pybossa.entity.TaskLog;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/28/13
 * Time: 1:11 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class TaskLogDaoImpl extends AbstractDaoImpl<TaskLog, String> implements TaskLogDao {

    protected TaskLogDaoImpl(){
        super(TaskLog.class);
    }

    @Override
    public void createTaskLog(TaskLog taskLog) {
    	if(taskLog.getCreated() == null){
    		taskLog.setCreated(new Date());
    	}
        save(taskLog);
    }
    
    @Override
    public void saveOrUpdateTaskLog(TaskLog taskLog) {
    	if(taskLog.getCreated()!=null){
    		taskLog.setCreated(new Date());
    	}
    	saveOrUpdate(taskLog);
    }

    @Override
    public List<TaskLog> getTaskLog(Long taskQueueID) {
        return findByCriteria(Restrictions.eq("taskQueueID", taskQueueID)); //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<TaskLog> getTaskLogByStatus(Long taskQueueID, int status) {

        return findByCriteria(Restrictions.conjunction()
                .add(Restrictions.eq("taskQueueID",taskQueueID))
                .add(Restrictions.eq("status", status)));

    }

    @Override
    public void deleteTaskLog(Long taskQueueID) {
        List<TaskLog> logs = this.getTaskLog(taskQueueID) ;
        for(int i=0; i < logs.size(); i++){
            delete(logs.get(i));
        }
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
