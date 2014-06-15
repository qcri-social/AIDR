package qa.qcri.aidr.trainer.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qa.qcri.aidr.trainer.api.dao.ClientAppSourceDao;
import qa.qcri.aidr.trainer.api.entity.ClientApp;
import qa.qcri.aidr.trainer.api.entity.ClientAppSource;
import qa.qcri.aidr.trainer.api.service.ClientAppService;
import qa.qcri.aidr.trainer.api.service.ClientAppSourceService;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 6/11/14
 * Time: 9:50 PM
 * To change this template use File | Settings | File Templates.
 */
@Service("clientAppSourceService")
@Transactional(readOnly = true)
public class ClientAppSourceServiceImpl implements ClientAppSourceService {
    @Autowired
    ClientAppSourceDao clientAppSourceDao;

    @Autowired
    ClientAppService clientAppService;

    @Override
    public void addExternalDataSouceWithClientAppID(String fileURL, Long clientAppID) {
        clientAppSourceDao.acceptSource(fileURL, clientAppID);
    }

    @Override
    public void addExternalDataSouceWithAppType(String fileURL, Integer appType) {

        List<ClientApp> clientApps =  clientAppService.findClientAppByAppType("appType", appType);

        if(clientApps.size() > 0 ){
            ClientApp app = clientApps.get(0) ;
            addExternalDataSouceWithClientAppID(fileURL, app.getClientAppID());
        }
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addExternalDataSouceWithPlatFormInd(String fileURL, Long micromappersID) {
        ClientApp clientApps =  clientAppService.findClientAppByID("platformAppID",micromappersID);
        if(clientApps!= null){
            addExternalDataSouceWithClientAppID(fileURL, clientApps.getClientAppID());
        }
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
