package qa.qcri.aidr.collector.api;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import qa.qcri.aidr.collector.beans.CollectionTask;
import qa.qcri.aidr.collector.beans.ResponseWrapper;
import qa.qcri.aidr.collector.collectors.FacebookDataListener;
import qa.qcri.aidr.collector.utils.CollectorConfigurationProperty;
import qa.qcri.aidr.collector.utils.CollectorConfigurator;
import qa.qcri.aidr.collector.utils.CollectorErrorLog;
import qa.qcri.aidr.collector.utils.GenericCache;
import facebook4j.Event;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Group;
import facebook4j.Ordering;
import facebook4j.Page;
import facebook4j.Post;
import facebook4j.Reading;
import facebook4j.ResponseList;
import facebook4j.auth.AccessToken;

/**
 * @author Kushal
 * RESTFul APIs to start and stop Facebook collections.
 * TODO: remove non-API related operations such as startPersister to other appropriate classes.
 */
@RestController
@RequestMapping("/facebook")
public class FacebookCollectorAPI {

	@Autowired
	private FacebookDataListener facebookDataListener;
	
    private static Logger logger = Logger.getLogger(FacebookCollectorAPI.class.getName());
    private static CollectorConfigurator configProperties = CollectorConfigurator.getInstance();
    
    @RequestMapping(value = "/start", method={RequestMethod.POST})
    public Response startTask(@RequestBody CollectionTask task) {
    	String collectionCode = task.getCollectionCode();
        logger.info("Collection start request received for " + collectionCode);
        logger.info("Details:\n" + task.toString());
        ResponseWrapper response = new ResponseWrapper();
        GenericCache cache = GenericCache.getInstance();
        
        boolean isCollectionRunning = cache.isCollectionRunning(collectionCode);
		if(!isCollectionRunning){
        	 //check if all facebook specific information is available in the request
            if (!task.isFacebookInfoPresent()) {
                response.setStatusCode(configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_COLLECTION_ERROR));
                response.setMessage("One or more Facebook authentication token(s) are missing");
                return Response.ok(response).build();
            }

            //check if all query parameters are missing in the query
            if (!task.isToTrackAvailable()) {
                response.setStatusCode(configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_COLLECTION_ERROR));
                response.setMessage("Missing keywords.");
                return Response.ok(response).build();
            }
            //check if a task is already running with same configurations
            logger.info("Checking OAuth parameters for " + collectionCode);
            
    		if (cache.isTwtConfigExists(task)) {
                String msg = "Provided OAuth configurations already in use. Please stop this collection and then start again.";
                logger.info(collectionCode + ": " + msg);
                response.setMessage(msg);
                response.setStatusCode(configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_COLLECTION_ERROR));
                return Response.ok(response).build();
            }

    		task.setStatusCode(configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_COLLECTION_INITIALIZING));
    		logger.info("Initializing connection with Facebook Graph API for collection " + collectionCode);
    		
    		cache.incrCounter(collectionCode, new Long(0));
        }
       
		try {
			Facebook facebook = new FacebookFactory().getInstance();
			
			facebook.setOAuthAppId(task.getConsumerKey(),task.getConsumerSecret());
			facebook.setOAuthAccessToken(new AccessToken(task.getAccessToken(),null));
			Date toTimestamp = new Date();
			
			facebookDataListener.pagePosts(facebook, task, toTimestamp);
			facebookDataListener.eventPosts(facebook, task, toTimestamp);
			facebookDataListener.groupPosts(facebook, task, toTimestamp);
			
			
			task.setLastCollectedAt(toTimestamp.getTime());
			// if twitter streaming connection successful then change the status
			// code
			task.setStatusCode(configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_COLLECTION_RUNNING));
			task.setStatusMessage(null);
			cache.setTwtConfigMap(collectionCode, task);
			//cache.setTwitterTracker(cacheKey, tracker);
			if(!isCollectionRunning){
				if(task.getPersist()!=null){
					if(task.getPersist()){
						startPersister(collectionCode, task.isSaveMediaEnabled());
					}
				}
				else{
					if (Boolean.valueOf(configProperties.getProperty(CollectorConfigurationProperty.DEFAULT_PERSISTANCE_MODE))) {
						startPersister(collectionCode, task.isSaveMediaEnabled());
					}
				}
				
				response.setMessage(configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_COLLECTION_INITIALIZING));
				response.setStatusCode(configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_COLLECTION_INITIALIZING));
			}else{
				response.setMessage(null);
				response.setStatusCode(configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_COLLECTION_RUNNING));
			}
			
		} catch (Exception ex) {
			logger.error("Exception in fetching posts for collection " + collectionCode, ex);
			response.setMessage(ex.getMessage());
			response.setStatusCode(configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_COLLECTION_ERROR));
		}
		return Response.ok(response).build();
	}

    @RequestMapping("/stop")
    public Response stopTask(@RequestParam("id") String collectionCode) {
        GenericCache cache = GenericCache.getInstance();
        CollectionTask task = cache.getTwtConfigMap(collectionCode);
        
        cache.delFailedCollection(collectionCode);
        cache.deleteCounter(collectionCode);
        cache.delTwtConfigMap(collectionCode);
        cache.delLastDownloadedDoc(collectionCode);
        cache.delTwitterTracker(collectionCode);
        cache.delReconnectAttempts(collectionCode);

		if (task != null) {
			if(task.getPersist()!=null){
				if(task.getPersist()){
					stopPersister(collectionCode);
				}
			}
			else{
				if (Boolean.valueOf(configProperties.getProperty(CollectorConfigurationProperty.DEFAULT_PERSISTANCE_MODE))) {
					stopPersister(collectionCode);
				}
			}
            logger.info(collectionCode + ": " + "Collector has been successfully stopped.");
        } else {
            logger.info("No collector instances found to be stopped with the given id:" + collectionCode);
        }

        if (task != null) {
        	task.setStatusCode(configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_COLLECTION_STOPPED));
            return Response.ok(task).build();
        }

        ResponseWrapper response = new ResponseWrapper();
        response.setMessage("Invalid key. No running collector found for the given id.");
        response.setStatusCode(configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_COLLECTION_NOTFOUND));
        return Response.ok(response).build();
    }

    private void startPersister(String collectionCode, boolean saveMediaEnabled) {
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
