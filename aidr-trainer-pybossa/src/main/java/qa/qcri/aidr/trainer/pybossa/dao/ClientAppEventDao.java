package qa.qcri.aidr.trainer.pybossa.dao;

import qa.qcri.aidr.trainer.pybossa.entity.ClientAppEvent;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 12/5/13
 * Time: 12:45 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ClientAppEventDao extends AbstractDao<ClientAppEvent, String>{

    List<ClientAppEvent> getClientAppEventByClientAPP(Long clientAppID);
    List<ClientAppEvent> getClientAppEventByEvent(Long clientAppEventID);
}
