package qa.qcri.aidr.trainer.api.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import qa.qcri.aidr.trainer.api.dao.TaskQueueResponseDao;
import qa.qcri.aidr.trainer.api.entity.TaskQueueResponse;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 1/18/14
 * Time: 4:21 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class TaskQueueResponseDaoImpl extends AbstractDaoImpl<TaskQueueResponse, String> implements TaskQueueResponseDao {

    protected TaskQueueResponseDaoImpl(){
        super(TaskQueueResponse.class);
    }

    @Override
    public List<TaskQueueResponse> getTaskQueueResponseByTaskQueueID(Long taskQueueID) {
        return findByCriteria(Restrictions.eq("taskQueueID", taskQueueID));  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<TaskQueueResponse> getTaskQueueResponseByTaskQueueIDBasedOnLastUpdate(Long taskQueueID, Date updated) {

        if(updated == null){
            return findByCriteria(Restrictions.conjunction()
                    .add(Restrictions.eq("taskQueueID", taskQueueID)));
        }
        else{
            return findByCriteria(Restrictions.conjunction()
                    .add(Restrictions.gt("created",updated))
                    .add(Restrictions.eq("taskQueueID", taskQueueID)));
        }

    }
}
