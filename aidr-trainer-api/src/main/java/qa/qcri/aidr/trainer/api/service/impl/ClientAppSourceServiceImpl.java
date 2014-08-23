package qa.qcri.aidr.trainer.api.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qa.qcri.aidr.trainer.api.dao.ClientAppSourceDao;
import qa.qcri.aidr.trainer.api.entity.ClientApp;
import qa.qcri.aidr.trainer.api.entity.ClientAppSource;
import qa.qcri.aidr.trainer.api.service.ClientAppService;
import qa.qcri.aidr.trainer.api.service.ClientAppSourceService;
import qa.qcri.aidr.trainer.api.store.StatusCodeType;

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
    
    private static Logger logger = Logger.getLogger(ClientAppSourceServiceImpl.class);
    
    @Override
    @Transactional(readOnly = false)
    public void addExternalDataSouceWithClientAppID(String fileURL, Long clientAppID) {
        logger.info("fileURL : " + fileURL );
        logger.info("clientAppID : " + clientAppID );

        boolean dublicateFound = clientAppSourceDao.findDuplicateSource(fileURL, clientAppID);

        if(!dublicateFound){
            List<ClientAppSource>  sources = clientAppSourceDao.findActiveSourcePerClient( clientAppID );

            if(sources.size() > 0){
                logger.info("sources : EXTERNAL_DATA_SOURCE_UPLOADED");
                ClientAppSource ca1 = new ClientAppSource(clientAppID, StatusCodeType.EXTERNAL_DATA_SOURCE_UPLOADED, fileURL);
                clientAppSourceDao.createNewSource(ca1);

            }
            else{
                logger.info("sources : EXTERNAL_DATA_SOURCE_ACTIVE");
                ClientAppSource ca2 = new ClientAppSource(clientAppID, StatusCodeType.EXTERNAL_DATA_SOURCE_ACTIVE, fileURL);
                clientAppSourceDao.createNewSource(ca2);
            }
        }

    }

    @Override
    @Transactional(readOnly = false)
    public void addExternalDataSouceWithAppType(String fileURL, Integer appType) {

        List<ClientApp> clientApps =  clientAppService.findClientAppByAppType("appType", appType);

        for(ClientApp app : clientApps)
        {
            addExternalDataSouceWithClientAppID(fileURL, app.getClientAppID());
        }

    }

    @Override
    @Transactional(readOnly = false)
    public void addExternalDataSouceWithPlatFormInd(String fileURL, Long micromappersID) {
        ClientApp clientApps =  clientAppService.findClientAppByID("platformAppID",micromappersID);
        if(clientApps!= null){
            addExternalDataSouceWithClientAppID(fileURL, clientApps.getClientAppID());
        }
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
