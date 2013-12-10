package qa.qcri.aidr.trainer.api.dao.impl;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import qa.qcri.aidr.trainer.api.dao.TaskQueueDao;
import qa.qcri.aidr.trainer.api.entity.TaskQueue;


import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/25/13
 * Time: 8:10 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class TaskQueueDaoImpl extends AbstractDaoImpl<TaskQueue, String> implements TaskQueueDao {

    protected TaskQueueDaoImpl(){
        super(TaskQueue.class);
    }


    @Override
    public List<TaskQueue> findTaskQueue(Long taskID, Long clientAppID, Long documentID) {

        return findByCriteria(Restrictions.conjunction()
                .add(Restrictions.eq("taskID",taskID))
                .add(Restrictions.eq("clientAppID", clientAppID))
                .add(Restrictions.eq("documentID", documentID)));
                  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<TaskQueue> findTaskQueueByDocument(Long clientAppID, Long documentID) {
        return findByCriteria(Restrictions.conjunction()
                .add(Restrictions.eq("clientAppID", clientAppID))
                .add(Restrictions.eq("documentID", documentID)));
    }

    @Override
    public List<TaskQueue> findTaskQueueByStatus(String column, Integer status) {
        return findByCriteria(Restrictions.eq(column, status));  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<TaskQueue> findTaskQueueSetByStatus(Long clientAppID, Integer status) {
        return findByCriteria(Restrictions.conjunction()
                .add(Restrictions.eq("clientAppID", clientAppID))
                .add(Restrictions.eq("status", status)));
    }

    @Override
    public List<TaskQueue> findTaskQueueSetByclientApp(Long clientAppID) {
        return findByCriteria(Restrictions.eq("clientAppID", clientAppID));
    }
}
