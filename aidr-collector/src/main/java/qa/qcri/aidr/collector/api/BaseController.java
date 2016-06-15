package qa.qcri.aidr.collector.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import qa.qcri.aidr.collector.beans.CollectionTask;
import qa.qcri.aidr.collector.beans.ResponseWrapper;
import qa.qcri.aidr.collector.utils.CollectorConfigurationProperty;
import qa.qcri.aidr.collector.utils.CollectorConfigurator;
import qa.qcri.aidr.collector.utils.CollectorErrorLog;
import qa.qcri.aidr.collector.utils.GenericCache;

public abstract class BaseController<T extends CollectionTask> {
    
	protected static Logger logger = Logger.getLogger(BaseController.class);
    protected static CollectorConfigurator configProperties = CollectorConfigurator.getInstance();

    /*@RequestMapping("/status")
    public Response getStatus(@RequestParam("id") String id) {
        ResponseWrapper response = new ResponseWrapper();
        if (StringUtils.isEmpty(id)) {
            response.setMessage("Invalid key. No running collector found for the given id.");
            response.setStatusCode(configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_COLLECTION_NOTFOUND));
            return Response.ok(response).build();
        }
        CollectionTask task = GenericCache.getInstance().getConfig(id);
        if (task != null) { 
        	return Response.ok(task).build();
        }

        CollectionTask failedTask = GenericCache.getInstance().getFailedCollectionTask(id);
        if (failedTask != null) {
            return Response.ok(failedTask).build();
        }

        response.setMessage("Invalid key. No running collector found for the given id.");
        response.setStatusCode(configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_COLLECTION_NOTFOUND));
        return Response.ok(response).build();

    }
*/
    @RequestMapping("/stop")
    public Response stopTask(@RequestParam("id") String collectionCode) {
        
        Response stopTaskResponse = stopCollection(collectionCode);
        
        if(stopTaskResponse == null) {
	        ResponseWrapper response = new ResponseWrapper();
	        response.setMessage("Invalid key. No running collector found for the given id.");
	        response.setStatusCode(configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_COLLECTION_NOTFOUND));
	        stopTaskResponse = Response.ok(response).build();
        } 
       
        return stopTaskResponse;
    }
    
    @RequestMapping("/status/all")
    public List<CollectionTask> getStatusAll() {
        List<CollectionTask> allTasks = GenericCache.getInstance().getAllConfigs();
        return allTasks;
    }

    protected abstract Response stopCollection(String code);
    
    protected void startPersister(String collectionCode, boolean saveMediaEnabled) {
        Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
        try {
            WebTarget webResource = client.target(configProperties.getProperty(CollectorConfigurationProperty.PERSISTER_REST_URI) 
            		+ "collectionPersister/start?channel_provider="
                    + URLEncoder.encode(configProperties.getProperty(CollectorConfigurationProperty.TAGGER_CHANNEL), "UTF-8")
                    + "&collection_code=" + URLEncoder.encode(collectionCode, "UTF-8")
                    + "&saveMediaEnabled=" + saveMediaEnabled);
            Response clientResponse = webResource.request(MediaType.APPLICATION_JSON).get();
            String jsonResponse = clientResponse.readEntity(String.class);

            logger.info(collectionCode + ": Collector persister response = " + jsonResponse);
        } catch (RuntimeException e) {
            logger.error(collectionCode + ": Could not start persister. Is persister running?", e);
            CollectorErrorLog.sendErrorMail(collectionCode, "Unable to start persister. Is persister running");
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
            logger.error(collectionCode + ": Could not stop persister. Is persister running?", e);
        } catch (UnsupportedEncodingException e) {
            logger.error(collectionCode + ": Unsupported Encoding scheme used");
        }
    }
}
