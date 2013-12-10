package qa.qcri.aidr.trainer.api.dao.impl;


import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import qa.qcri.aidr.trainer.api.dao.TaskBufferDao;
import qa.qcri.aidr.trainer.api.entity.TaskBuffer;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/7/13
 * Time: 9:22 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class TaskBufferDaoImpl extends AbstractDaoImpl<TaskBuffer, String> implements TaskBufferDao {

    protected TaskBufferDaoImpl(){
        super(TaskBuffer.class);
    }

    @Override
    public List<TaskBuffer> findAllTaskBuffer(String columnName, Integer value, Integer maxresult) {
        String[] orderBy = {"documentID","valueAsTrainingSample" }  ;
        Criterion criterion = Restrictions.disjunction().add(Restrictions.eq(columnName, value));

        return findByCriteriaByOrder(criterion, orderBy, maxresult)  ;

    }

    @Override
    public List<TaskBuffer> findAllTaskBufferByCririsID(Long cririsID, Integer assignedCount, Integer maxresult) {
        String[] orderBy = {"documentID","valueAsTrainingSample" }  ;
        Criterion criterion = Restrictions.conjunction()
                .add(Restrictions.eq("crisisID",cririsID))
                .add(Restrictions.eq("assignedCount", assignedCount));
        return findByCriteriaByOrder(criterion, orderBy, maxresult)  ;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
