package qa.qcri.aidr.trainer.pybossa.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qa.qcri.aidr.trainer.pybossa.dao.ClientAppEventDao;
import qa.qcri.aidr.trainer.pybossa.entity.ClientAppEvent;
import qa.qcri.aidr.trainer.pybossa.service.ClientAppEventService;

import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 12/5/13
 * Time: 1:31 PM
 * To change this template use File | Settings | File Templates.
 */
@Service("clientAppEventService")
@Transactional(readOnly = true)
public class ClientAppEventServiceImpl implements ClientAppEventService {
    @Autowired
    ClientAppEventDao clientAppEventDao;

    @Override
    public ClientAppEvent getNextSequenceClientAppEvent(Long clientAppID) {
        ClientAppEvent nextRunner = null;
        List<ClientAppEvent> clientAppEvents =  clientAppEventDao.getClientAppEventByClientAPP(clientAppID);

        Iterator itr= clientAppEvents.iterator();
        while(itr.hasNext()){
            ClientAppEvent clientAppEvent = (ClientAppEvent)itr.next();
            Long currentEventAppID =  clientAppEvent.getClientAppEventID();
            Long eventID = clientAppEvent.getEventID();
            Integer sequence = clientAppEvent.getSequence();

            List<ClientAppEvent> events =  clientAppEventDao.getClientAppEventByEvent(eventID);
            nextRunner = getNextSequenceClientApp(events, clientAppEvent) ;
            if(nextRunner != null){
                break;
            }

        }

        return nextRunner;  //To change body of implemented methods use File | Settings | File Templates.
    }


    private ClientAppEvent getNextSequenceClientApp(List<ClientAppEvent> eventList, ClientAppEvent currentAppEvent){
        ClientAppEvent nextEventApp = null;
        Iterator itr= eventList.iterator();

        while(itr.hasNext()){
            ClientAppEvent clientAppEvent = (ClientAppEvent)itr.next();

            if(!clientAppEvent.getClientAppEventID().equals(currentAppEvent.getClientAppEventID())){
                if(clientAppEvent.getSequence() > currentAppEvent.getSequence() ){
                    // yes, it is next sequence!
                    nextEventApp = clientAppEvent;
                }
            }
        }
        return nextEventApp;
    }
}
