package qa.qcri.aidr.trainer.pybossa.dao.impl;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import qa.qcri.aidr.trainer.pybossa.dao.ClientAppEventDao;
import qa.qcri.aidr.trainer.pybossa.entity.ClientAppEvent;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 12/5/13
 * Time: 12:52 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class ClientAppEventDaoImpl extends AbstractDaoImpl<ClientAppEvent, String> implements ClientAppEventDao {


    protected ClientAppEventDaoImpl(){
        super(ClientAppEvent.class);
    }

    @Override
    public List<ClientAppEvent> getClientAppEventByClientAPP(Long clientAppID) {
        return findByCriteria(Restrictions.eq("clientAppID", clientAppID));
    }

    @Override
    public List<ClientAppEvent> getClientAppEventByEvent(Long clientAppEventID) {
        return findByCriteria(Restrictions.eq("eventID", clientAppEventID));
    }
}
