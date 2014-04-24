package qa.qcri.aidr.trainer.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import qa.qcri.aidr.trainer.api.service.ClientAppService;
import qa.qcri.aidr.trainer.api.store.CodeLookUp;
import qa.qcri.aidr.trainer.api.store.StatusCodeType;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Path("/delete/{crisiscode}/{attributecode}")
    public Response getCrisisByID(@PathParam("crisiscode") String crisiscode,
                                @PathParam("attributecode") String attributecode){

        String shortName = crisiscode+"_"+ attributecode;
        clientAppService.updateClientAppByShortName(shortName, StatusCodeType.CLIENT_APP_INACTIVE);

        String returnValue = "{\"status\":200}";
        return Response.status(CodeLookUp.APP_SERVICE_COMPLETED).entity(returnValue).build();

    }

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Path("/delete/crisis/{crisisID}")
    public Response disableClientApps(@PathParam("crisisID") Long crisisID){

        try{
            clientAppService.deactivateClientAppByCrisis(crisisID);
        }
        catch(Exception e){
            System.out.println("disableClientApps : " + e);
        }


        return Response.status(CodeLookUp.APP_SERVICE_COMPLETED).entity(StatusCodeType.POST_COMPLETED).build();
    }

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Path("/delete/attribute/{crisisID}/{attributeID}")
    public Response disableClientApp(@PathParam("crisisID") Long crisisID, @PathParam("attributeID") Long attributeID){

        try{
            clientAppService.deactivateClientAppByAttribute(crisisID, attributeID);
        }
        catch(Exception e){
            System.out.println("disableClientApps : " + e);
        }


        return Response.status(CodeLookUp.APP_SERVICE_COMPLETED).entity(StatusCodeType.POST_COMPLETED).build();
    }




}
