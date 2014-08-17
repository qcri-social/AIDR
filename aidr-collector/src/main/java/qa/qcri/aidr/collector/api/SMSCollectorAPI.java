/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.collector.api;

//import com.sun.jersey.api.client.Client;
//import com.sun.jersey.api.client.ClientResponse;
//import com.sun.jersey.api.client.WebResource;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import qa.qcri.aidr.collector.beans.ResponseWrapper;
import qa.qcri.aidr.collector.beans.SMS;
import qa.qcri.aidr.collector.beans.SMSStatus;
import qa.qcri.aidr.collector.logging.Loggable;
import qa.qcri.aidr.collector.redis.JedisConnectionPool;
import qa.qcri.aidr.collector.utils.Config;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * REST Web Service
 *
 * @author Imran
 */
@Path("/sms")
public class SMSCollectorAPI extends Loggable {

    private static Logger logger = Logger.getLogger(SMSCollectorAPI.class.getName());
    public static final String CHANNEL = Config.FETCHER_CHANNEL + ".%s_sms";
    private Map<String,SMSStatus> statuses = new HashMap<String, SMSStatus>();
    private ObjectMapper objectMapper = new ObjectMapper();

    @GET
    @Path("/start")
    @Produces(MediaType.APPLICATION_JSON)
    public Response startTask(@QueryParam("collection_code") String code) {
        statuses.put(code, SMSStatus.RUNNING);
        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/stop")
    public Response stopTask(@QueryParam("collection_code") String code) {
        statuses.remove(code);
        return Response.ok().build();
    }

    @POST
    @Path("/endpoint/receive/{collection_code}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response receive(@PathParam("collection_code") String code, SMS sms) {
        SMSStatus status = statuses.get(code);
        if(status == SMSStatus.RUNNING){
            try {
                String channelName = String.format(CHANNEL, code);
                JedisConnectionPool.getJedisConnection().publish(channelName, objectMapper.writeValueAsString(sms));
            } catch (Exception e) {
                logger.error(e,e);
            }
        }
        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/status/all")
    public Response getStatusAll() {
        return Response.ok().build();
    }

}
