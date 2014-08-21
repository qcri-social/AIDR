/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.collector.api;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import qa.qcri.aidr.collector.beans.SMS;
import qa.qcri.aidr.collector.logging.ErrorLog;
import qa.qcri.aidr.collector.logging.Loggable;
import qa.qcri.aidr.collector.redis.JedisConnectionPool;
import qa.qcri.aidr.collector.utils.Config;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Map;

import qa.qcri.aidr.collector.utils.GenericCache;

/**
 * REST Web Service
 *
 * @author Imran
 */
@Path("/sms")
public class SMSCollectorAPI extends Loggable {

    private static Logger logger = Logger.getLogger(SMSCollectorAPI.class.getName());
    private static ErrorLog elog = new ErrorLog();
    
    public static final String CHANNEL = Config.FETCHER_CHANNEL + ".%s_sms";
    private ObjectMapper objectMapper = new ObjectMapper();

    @GET
    @Path("/start")
    @Produces(MediaType.APPLICATION_JSON)
    public Response startTask(@QueryParam("collection_code") String code) {
        GenericCache.getInstance().putSMSCollection(code, Config.STATUS_CODE_COLLECTION_RUNNING);
        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/stop")
    public Response stopTask(@QueryParam("collection_code") String code) {
        GenericCache.getInstance().removeSMSCollection(code);
        return Response.ok().build();
    }

    @POST
    @Path("/endpoint/receive/{collection_code}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response receive(@PathParam("collection_code") String code, SMS sms) {
        String smsCollections = GenericCache.getInstance().getSMSCollection(code.trim());
        if (Config.STATUS_CODE_COLLECTION_RUNNING.equals(smsCollections)) {
            try {
                String channelName = String.format(CHANNEL, code);
                JedisConnectionPool.getJedisConnection().publish(channelName, objectMapper.writeValueAsString(sms));
            } catch (Exception e) {
            	logger.error("Exception in receiving from SMS collection: " + code + ", data: " + sms);
                logger.error(elog.toStringException(e));
            }
        }
        return Response.ok().build();
    }

    @GET
    @Path("/status/all")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Map getStatusAll() {
        return GenericCache.getInstance().getSMSCollections();
    }
}
