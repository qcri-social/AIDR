package qa.qcri.aidr.collector.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import qa.qcri.aidr.collector.beans.CollectionTask;
import qa.qcri.aidr.collector.beans.ResponseWrapper;
import qa.qcri.aidr.collector.beans.SMS;
import qa.qcri.aidr.collector.collectors.JedisPublisher;
import qa.qcri.aidr.collector.utils.CollectorConfigurationProperty;
import qa.qcri.aidr.collector.utils.CollectorConfigurator;
import qa.qcri.aidr.collector.utils.CollectorErrorLog;
import qa.qcri.aidr.collector.utils.GenericCache;
import qa.qcri.aidr.common.redis.LoadShedder;

/**
 * @author Imran
 * Provides RESTFul APIS to start and stop SMS collections.
 * TODO: remove non-API methods from this class.
 */
@Controller
@RequestMapping("/sms")
public class SMSCollectorAPI  {
    
    private static Logger logger = Logger.getLogger(SMSCollectorAPI.class.getName());
    
    private static CollectorConfigurator configProperties= CollectorConfigurator.getInstance();
    
    public static final String CHANNEL = configProperties.getProperty(CollectorConfigurationProperty.COLLECTOR_CHANNEL) + ".%s";
    private static ConcurrentHashMap<String, LoadShedder> redisLoadShedder = null;
    
    @RequestMapping("/start")
    @ResponseBody
    public Response startTask(@RequestParam("collection_code") String collectionCode) {
        if (null == redisLoadShedder) {
            redisLoadShedder = new ConcurrentHashMap<String, LoadShedder>(20);
        }
        String channelName = String.format(CHANNEL, collectionCode);
        redisLoadShedder.put(channelName, new LoadShedder(
                		Integer.parseInt(configProperties.getProperty(CollectorConfigurationProperty.PERSISTER_LOAD_LIMIT)), 
                		Integer.parseInt(configProperties.getProperty(CollectorConfigurationProperty.PERSISTER_LOAD_CHECK_INTERVAL_MINUTES)), 
                		true,channelName));
        GenericCache.getInstance().putSMSCollection(collectionCode, configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_COLLECTION_RUNNING));
        startPersister(collectionCode);
        return Response.ok().build();
    }
    
    @RequestMapping("/stop")
    @ResponseBody
    public Response stopTask(@RequestParam("collection_code") String collectionCode) {
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
        response.setStatusCode(configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_COLLECTION_NOTFOUND));
        return Response.ok(response).build();
    }
    
    @RequestMapping(value = "/endpoint/receive/{collection_code}", method={RequestMethod.POST})
    @ResponseBody
    public Response receive(@PathVariable("collection_code") String code, SMS sms) {
        GenericCache cache = GenericCache.getInstance();
        String smsCollections = cache.getSMSCollection(code.trim());
        if (configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_COLLECTION_RUNNING).equals(smsCollections)) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String channelName = String.format(CHANNEL, code);
                if (redisLoadShedder.get(channelName).canProcess()) {
                    JedisPublisher publisherJedis = JedisPublisher.newInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZ yyyy");
            		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                    sms.setCreated_at(sdf.format(new Date()));
                    publisherJedis.publish(channelName, objectMapper.writeValueAsString(sms));
                    cache.increaseSMSCount(code);
                    cache.setLastDownloadedDoc(code, sms.getText());
                    publisherJedis.close();
                }
            } catch (Exception e) {
                logger.error("Exception in receiving from SMS collection: " + code + ", data: " + sms);
            }
        }
        return Response.ok().build();
    }
    
    @RequestMapping("/status")
    @ResponseBody
    public Response getStatus(@RequestParam("collection_code") String code) {
        CollectionTask config = GenericCache.getInstance().getSMSConfig(code);
        return Response.ok(config).build();
    }
    
    @RequestMapping("/status/all")
    public Map getStatusAll() {
        return GenericCache.getInstance().getSMSCollections();
    }
    
    public void startPersister(String collectionCode) {
        Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
        try {
            WebTarget webResource = client.target(configProperties.getProperty(CollectorConfigurationProperty.PERSISTER_REST_URI) + "collectionPersister/start?channel_provider="
                    + URLEncoder.encode(configProperties.getProperty(CollectorConfigurationProperty.TAGGER_CHANNEL), "UTF-8")
                    + "&collection_code=" + URLEncoder.encode(collectionCode, "UTF-8"));
            Response clientResponse = webResource.request(MediaType.APPLICATION_JSON).get();
            String jsonResponse = clientResponse.readEntity(String.class);
            
            logger.info(collectionCode + ": Collector persister response = " + jsonResponse);
        } catch (RuntimeException e) {
            logger.error(collectionCode + ": Could not start persister. Is persister running?");
            CollectorErrorLog.sendErrorMail(collectionCode, "Unable to start persister.");
        } catch (UnsupportedEncodingException e) {
            logger.error(collectionCode + ": Unsupported Encoding scheme used");
        }
    }
    
    public void stopPersister(String collectionCode) {
        Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
        try {
            WebTarget webResource = client.target(configProperties.getProperty(CollectorConfigurationProperty.PERSISTER_REST_URI)
                    + "collectionPersister/stop?collection_code=" + URLEncoder.encode(collectionCode, "UTF-8"));
            Response clientResponse = webResource.request(MediaType.APPLICATION_JSON).get();
            String jsonResponse = clientResponse.readEntity(String.class);
            logger.info(collectionCode + ": Collector persister response =  " + jsonResponse);
        } catch (RuntimeException e) {
            logger.error(collectionCode + ": Could not stop persister. Is persister running?");
        } catch (UnsupportedEncodingException e) {
            logger.error(collectionCode + ": Unsupported Encoding scheme used");
        }
    }
}