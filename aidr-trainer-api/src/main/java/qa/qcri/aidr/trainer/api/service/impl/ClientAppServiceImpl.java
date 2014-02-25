package qa.qcri.aidr.trainer.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qa.qcri.aidr.trainer.api.dao.ClientAppDao;
import qa.qcri.aidr.trainer.api.entity.ClientApp;
import qa.qcri.aidr.trainer.api.service.ClientAppService;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/19/13
 * Time: 12:01 PM
 * To change this template use File | Settings | File Templates.
 */
@Service("clientAppService")
@Transactional(readOnly = false)
public class ClientAppServiceImpl implements ClientAppService {

    @Autowired
    private ClientAppDao clientAppDao;

    @Override
    public ClientApp findClientAppByID(String columnName, Long id) {
        return clientAppDao.findClientAppByID(columnName, id);//To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ClientApp findClientAppByCriteria(String columnName, String value) {
        return clientAppDao.findClientAppByCriteria(columnName, value);
       // return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<ClientApp> getAllClientAppByClientID(Long clientID) {
        return clientAppDao.findAllClientApp(clientID) ;
    }

    @Override
    public List<ClientApp> findClientAppByStatus(Integer status) {
        return clientAppDao.findAllClientAppByStatus(status);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<ClientApp> getAllClientAppByCrisisID(Long crisisID) {
        return clientAppDao.findAllClientAppByCrisisID(crisisID);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<ClientApp> findClientAppByAppType(String columnName, Integer typeID) {
        return clientAppDao.findClientAppByAppType(columnName, typeID);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateClientAppByShortName(String shortName, Integer status) {
       ClientApp clientApp = findClientAppByCriteria("shortName",shortName);
       if(clientApp != null){
           clientApp.setStatus(status);
           clientAppDao.saveOrUpdate(clientApp);
       }
    }
}
