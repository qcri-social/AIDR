package qa.qcri.aidr.trainer.pybossa.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import qa.qcri.aidr.trainer.pybossa.dao.TaskQueueResponseDao;
import qa.qcri.aidr.trainer.pybossa.entity.TaskQueueResponse;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 10/20/13
 * Time: 1:46 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class TaskQueueResponseDaoImpl extends AbstractDaoImpl<TaskQueueResponse, String> implements TaskQueueResponseDao {

    protected TaskQueueResponseDaoImpl(){
        super(TaskQueueResponse.class);
    }

    @Override
    public void addTaskQueueResponse(TaskQueueResponse taskQueueResponse) {
        //To change body of implemented methods use File | Settings | File Templates.
    	if(taskQueueResponse.getCreated()==null){
    		taskQueueResponse.setCreated(new Date());
    	}
        saveOrUpdate(taskQueueResponse);
    }

    @Override
    public List<TaskQueueResponse> getTaskQueueResponse(Long taskQueueID) {
       return  findByCriteria(Restrictions.eq("taskQueueID", taskQueueID));
    }

    @Override
    public List<TaskQueueResponse> getTaskQueueResponseByContent() {
        return  findByCriteria(Restrictions.eq("response", "geo"));
    }
}
