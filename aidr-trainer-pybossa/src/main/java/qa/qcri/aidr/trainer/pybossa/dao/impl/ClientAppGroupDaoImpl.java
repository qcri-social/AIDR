package qa.qcri.aidr.trainer.pybossa.dao.impl;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import qa.qcri.aidr.trainer.pybossa.dao.ClientAppGroupDao;
import qa.qcri.aidr.trainer.pybossa.entity.ClientAppGroup;


import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 4/16/15
 * Time: 5:22 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class ClientAppGroupDaoImpl extends AbstractDaoImpl<ClientAppGroup, String> implements ClientAppGroupDao {

    protected ClientAppGroupDaoImpl(){
        super(ClientAppGroup.class);
    }


    @Override
    public List<ClientAppGroup> findGroup(Long groupID) {
        return findByCriteria(Restrictions.eq("groupID", groupID));
    }

    @Override
    public List<ClientAppGroup> findGroupByCrisisID(Long crisisID) {
        return findByCriteria(Restrictions.conjunction()
                .add(Restrictions.eq("crisisID",crisisID))
                .add(Restrictions.isNotNull("groupID")));
       // return findByCriteria(Restrictions.eq("crisisID", crisisID));  //To change body of implemented methods use File | Settings | File Templates.
    }
}
