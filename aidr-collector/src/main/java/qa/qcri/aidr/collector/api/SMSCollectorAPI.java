/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.collector.api;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.glassfish.jersey.jackson.JacksonFeature;
import qa.qcri.aidr.collector.beans.CollectionTask;
import qa.qcri.aidr.collector.beans.ResponseWrapper;
import qa.qcri.aidr.collector.beans.SMS;
import qa.qcri.aidr.collector.logging.ErrorLog;
import qa.qcri.aidr.collector.logging.Loggable;
import qa.qcri.aidr.collector.redis.JedisConnectionPool;
import qa.qcri.aidr.collector.utils.GenericCache;
import qa.qcri.aidr.common.redis.LoadShedder;

import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static qa.qcri.aidr.collector.utils.ConfigProperties.getProperty;

/**
 * REST Web Service
 *
 * @author Imran
 */
@Path("/sms")
public class SMSCollectorAPI extends Loggable {
    
    private static Logger logger = Logger.getLogger(SMSCollectorAPI.class.getName());
    private static ErrorLog elog = new ErrorLog();
    
    public static final String CHANNEL = getProperty("FETCHER_CHANNEL") + ".%s";
    private static ConcurrentHashMap<String, LoadShedder> redisLoadShedder = null;
    
    @GET
    @Path("/start")
    @Produces(MediaType.APPLICATION_JSON)
    public Response startTask(@QueryParam("collection_code") String collectionCode) {
        if (null == redisLoadShedder) {
            redisLoadShedder = new ConcurrentHashMap<String, LoadShedder>(20);
        }
        String channelName = String.format(CHANNEL, collectionCode);
        redisLoadShedder.put(channelName,
                new LoadShedder(Integer.parseInt(getProperty("PERSISTER_LOAD_LIMIT")), Integer.parseInt(getProperty("PERSISTER_LOAD_CHECK_INTERVAL_MINUTES")), true));
        GenericCache.getInstance().putSMSCollection(collectionCode, getProperty("STATUS_CODE_COLLECTION_RUNNING"));
        startPersister(collectionCode);
        return Response.ok().build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/stop")
    public Response stopTask(@QueryParam("collection_code") String collectionCode) {
        GenericCache cache = GenericCache.getInstance();
        CollectionTask task = cache.getSMSConfig(collectionCode);
        cache.removeSMSCollection(collectionCode);
        stopPersister(collectionCode);

        cache.deleteCounter(collectionCode);
        cache.delLastDownloadedDoc(collectionCode);

        if (task != null)
            return Response.ok(task).build();

        ResponseWrapper response = new ResponseWrapper();
        response.setMessage("Invalid key. No running collector found for the given id.");
        response.setStatusCode(getProperty("STATUS_CODE_COLLECTION_NOTFOUND"));
        return Response.ok(response).build();
    }
    
    @POST
    @Path("/endpoint/receive/{collection_code}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response receive(@PathParam("collection_code") String code, SMS sms) {
        GenericCache cache = GenericCache.getInstance();
        String smsCollections = cache.getSMSCollection(code.trim());
        
        if (getProperty("STATUS_CODE_COLLECTION_RUNNING").equals(smsCollections)) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String channelName = String.format(CHANNEL, code);
                if (redisLoadShedder.get(channelName).canProcess(channelName)) {
                    JedisConnectionPool.getJedisConnection().publish(channelName, objectMapper.writeValueAsString(sms));
                    cache.increaseSMSCount(code);
                    cache.setLastDownloadedDoc(code, sms.getText());
                }
            } catch (Exception e) {
                logger.error("Exception in receiving from SMS collection: " + code + ", data: " + sms);
                logger.error(elog.toStringException(e));
            }
        }
        return Response.ok().build();
    }
    
    @GET
    @Path("/status")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStatus(@QueryParam("collection_code") String code) {
        CollectionTask config = GenericCache.getInstance().getSMSConfig(code);
        return Response.ok(config).build();
    }
    
    @GET
    @Path("/status/all")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Map getStatusAll() {
        return GenericCache.getInstance().getSMSCollections();
    }
    
    public void startPersister(String collectionCode) {
        Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
        try {
            WebTarget webResource = client.target(getProperty("PERSISTER_REST_URI") + "collectionPersister/start?channel_provider="
                    + URLEncoder.encode(getProperty("TAGGER_CHANNEL"), "UTF-8")
                    + "&collection_code=" + URLEncoder.encode(collectionCode, "UTF-8"));
            Response clientResponse = webResource.request(MediaType.APPLICATION_JSON).get();
            String jsonResponse = clientResponse.readEntity(String.class);
            
            logger.info(collectionCode + ": Collector persister response = " + jsonResponse);
        } catch (RuntimeException e) {
            logger.error(collectionCode + ": Could not start persister. Is persister running?");
            logger.error(elog.toStringException(e));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            logger.error(collectionCode + ": Unsupported Encoding scheme used");
        }
    }
    
    public void stopPersister(String collectionCode) {
        Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
        try {
            WebTarget webResource = client.target(getProperty("PERSISTER_REST_URI")
                    + "collectionPersister/stop?collection_code=" + URLEncoder.encode(collectionCode, "UTF-8"));
            Response clientResponse = webResource.request(MediaType.APPLICATION_JSON).get();
            String jsonResponse = clientResponse.readEntity(String.class);
            logger.info(collectionCode + ": Collector persister response =  " + jsonResponse);
        } catch (RuntimeException e) {
            logger.error(collectionCode + ": Could not stop persister. Is persister running?");
            logger.error(elog.toStringException(e));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            logger.error(collectionCode + ": Unsupported Encoding scheme used");
        }
    }
}