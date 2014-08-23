package qa.qcri.aidr.trainer.api.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import qa.qcri.aidr.trainer.api.util.ErrorLog;
import qa.qcri.aidr.trainer.api.entity.ClientApp;
import qa.qcri.aidr.trainer.api.service.ClientAppService;
import qa.qcri.aidr.trainer.api.store.CodeLookUp;
import qa.qcri.aidr.trainer.api.store.StatusCodeType;
import qa.qcri.aidr.trainer.api.template.ClientAppModel;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 2/21/14
 * Time: 1:44 AM
 * To change this template use File | Settings | File Templates.
 */
@Path("/clientapp")
@Component
public class ClientAppController {
    @Autowired
    private ClientAppService clientAppService;

    private static Logger logger = Logger.getLogger(ClientAppController.class);
    private static ErrorLog elog = new ErrorLog();
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/delete/{crisiscode}/{attributecode}")
    public Response getCrisisByID(@PathParam("crisiscode") String crisiscode,
                                @PathParam("attributecode") String attributecode){

        String shortName = crisiscode+"_"+ attributecode;
        clientAppService.updateClientAppByShortName(shortName, StatusCodeType.CLIENT_APP_INACTIVE);

        return Response.status(CodeLookUp.APP_SERVICE_COMPLETED).entity(StatusCodeType.POST_COMPLETED).build();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/delete/crisis/{crisisID}")
    public Response disableClientApps(@PathParam("crisisID") Long crisisID){

        try{
            clientAppService.deactivateClientAppByCrisis(crisisID);
        }
        catch(Exception e){
            logger.error("disableClientApps exception for crisisID: " + crisisID);
            logger.error(elog.toStringException(e));
        }


        return Response.status(CodeLookUp.APP_SERVICE_COMPLETED).entity(StatusCodeType.POST_COMPLETED).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/delete/attribute/{crisisID}/{attributeID}")
    public Response disableClientApp(@PathParam("crisisID") Long crisisID, @PathParam("attributeID") Long attributeID){

        try{
            clientAppService.deactivateClientAppByAttribute(crisisID, attributeID);
        }
        catch(Exception e){
            logger.error("disableClientApps exception in deleting for crisisId:" + crisisID + ", attributeID: " + attributeID);
            logger.error(elog.toStringException(e));
        }


        return Response.status(CodeLookUp.APP_SERVICE_COMPLETED).entity(StatusCodeType.POST_COMPLETED).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/allactive")
    public List<ClientAppModel> getAllActive(){
        return clientAppService.getAllActiveClientApps();
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get/crisis/{crisisID}")
    public List<ClientApp> getClientAppsByCrisisID(@PathParam("crisisID") Long crisisID){
        return clientAppService.getAllClientAppByCrisisID(crisisID);
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/activate/mobile/crisis/{crisisID}")
    public Response enableMobileClientAppByCrisisID(@PathParam("crisisID") Long crisisID){
        Integer status = StatusCodeType.AIDR_MICROMAPPER_BOTH;
        String returnValue = clientAppService.enableForClientAppStatusByCrisisID(crisisID, status);
        return Response.status(CodeLookUp.APP_SERVICE_COMPLETED).entity(returnValue).build();
    }




}
