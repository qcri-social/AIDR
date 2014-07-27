/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.collector.api;

//import com.sun.jersey.api.client.Client;
//import com.sun.jersey.api.client.ClientResponse;
//import com.sun.jersey.api.client.WebResource;

import org.apache.log4j.Logger;
import qa.qcri.aidr.collector.beans.SMS;
import qa.qcri.aidr.collector.logging.Loggable;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author Imran
 */
@Path("/sms")
public class SMSCollectorAPI extends Loggable {

    private static Logger logger = Logger.getLogger(SMSCollectorAPI.class.getName());

    @GET
    @Path("/start")
    @Produces(MediaType.APPLICATION_JSON)
    public Response startTask(@QueryParam("collection_code") String code) {
        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/stop")
    public Response stopTask(@QueryParam("collection_code") String code) {
        return Response.ok().build();
    }

    @POST
    @Path("/endpoint/receive/{collection_code}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public SMS receive(@PathParam("collection_code") String code, SMS sms) {
        return sms;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/status/all")
    public Response getStatusAll() {
        return Response.ok().build();
    }

}
