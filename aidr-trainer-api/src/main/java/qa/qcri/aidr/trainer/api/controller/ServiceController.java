package qa.qcri.aidr.trainer.api.controller;

import org.springframework.stereotype.Component;
import qa.qcri.aidr.trainer.api.store.CodeLookUp;
import qa.qcri.aidr.trainer.api.store.StatusCodeType;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 2/3/14
 * Time: 3:42 PM
 * To change this template use File | Settings | File Templates.
 */
@Path("/util")
@Component
public class ServiceController {

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Path("/ping/heartbeat")
    public Response getHeartbeat() {
        String returnValue = "{\"status\":200}";
        return Response.status(CodeLookUp.APP_STATUS_ALIVE).entity(returnValue).build();
    }
}
