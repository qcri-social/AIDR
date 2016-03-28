package qa.qcri.aidr.trainer.api.controller;

import java.util.List;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import qa.qcri.aidr.trainer.api.entity.ClientApp;
import qa.qcri.aidr.trainer.api.service.ClientAppService;
import qa.qcri.aidr.trainer.api.store.CodeLookUp;
import qa.qcri.aidr.trainer.api.store.StatusCodeType;
import qa.qcri.aidr.trainer.api.template.ClientAppModel;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 2/21/14
 * Time: 1:44 AM
 * To change this template use File | Settings | File Templates.
 */
@RequestMapping("/clientapp")
@RestController
public class ClientAppController {
	
    @Autowired
    private ClientAppService clientAppService;

    private static Logger logger = Logger.getLogger(ClientAppController.class);
    
    @RequestMapping("/delete/{crisiscode}/{attributecode}")
    public Response getCrisisByID(@PathParam("crisiscode") String crisiscode,
                                @PathParam("attributecode") String attributecode){

        String shortName = crisiscode+"_"+ attributecode;
        clientAppService.updateClientAppByShortName(shortName, StatusCodeType.CLIENT_APP_INACTIVE);

        return Response.status(CodeLookUp.APP_SERVICE_COMPLETED).entity(StatusCodeType.POST_COMPLETED).build();

    }

    @RequestMapping("/delete/crisis/{crisisID}")
    public Response disableClientApps(@PathParam("crisisID") Long crisisID){

        try{
            clientAppService.deactivateClientAppByCrisis(crisisID);
        }
        catch(Exception e){
            logger.error("disableClientApps exception for crisisID: " + crisisID);
        }


        return Response.status(CodeLookUp.APP_SERVICE_COMPLETED).entity(StatusCodeType.POST_COMPLETED).build();
    }

    @RequestMapping("/delete/attribute/{crisisID}/{attributeID}")
    public Response disableClientApp(@PathParam("crisisID") Long crisisID, @PathParam("attributeID") Long attributeID){

        try{
            clientAppService.deactivateClientAppByAttribute(crisisID, attributeID);
        }
        catch(Exception e){
            logger.error("disableClientApps exception in deleting for crisisId:" + crisisID + ", attributeID: " + attributeID);
        }


        return Response.status(CodeLookUp.APP_SERVICE_COMPLETED).entity(StatusCodeType.POST_COMPLETED).build();
    }

    @RequestMapping("/allactive")
    public List<ClientAppModel> getAllActive(){
        return clientAppService.getAllActiveClientApps();
    }

    @RequestMapping("/get/crisis/{crisisID}")
    public List<ClientApp> getClientAppsByCrisisID(@PathParam("crisisID") Long crisisID){
        return clientAppService.getAllClientAppByCrisisID(crisisID);
    }

    @RequestMapping("/activate/mobile/crisis/{crisisID}")
    public Response enableMobileClientAppByCrisisID(@PathParam("crisisID") Long crisisID){
        Integer status = StatusCodeType.AIDR_MICROMAPPER_BOTH;
        String returnValue = clientAppService.enableForClientAppStatusByCrisisID(crisisID, status);
        return Response.status(CodeLookUp.APP_SERVICE_COMPLETED).entity(returnValue).build();
    }
}
