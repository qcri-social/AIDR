package qa.qcri.aidr.collector.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.glassfish.jersey.jackson.JacksonFeature;

import qa.qcri.aidr.collector.beans.CollectionTask;
import qa.qcri.aidr.collector.beans.ResponseWrapper;
import qa.qcri.aidr.collector.collectors.TwitterStreamTracker;
import qa.qcri.aidr.collector.utils.CollectorConfigurationProperty;
import qa.qcri.aidr.collector.utils.CollectorConfigurator;
import qa.qcri.aidr.collector.utils.CollectorErrorLog;
import qa.qcri.aidr.collector.utils.GenericCache;

/**
 * @author Imran
 * RESTFul APIs to start and stop Twitter collections.
 * TODO: remove non-API related operations such as startPersister to other appropriate classes.
 */
@Path("/twitter")
public class TwitterCollectorAPI {

    private static Logger logger = Logger.getLogger(TwitterCollectorAPI.class.getName());
    private static CollectorConfigurator configProperties = CollectorConfigurator.getInstance();
    
    @Context
    private UriInfo context;

    public TwitterCollectorAPI() {
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/start")
    public Response startTask(CollectionTask task) {
        logger.info("Collection start request received for " + task.getCollectionCode());
        logger.info("Details:\n" + task.toString());
        ResponseWrapper response = new ResponseWrapper();

        //check if all twitter specific information is available in the request
        if (!task.isTwitterInfoPresent()) {
            response.setStatusCode(configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_COLLECTION_ERROR));
            response.setMessage("One or more Twitter authentication token(s) are missing");
            return Response.ok(response).build();
        }

        //check if all query parameters are missing in the query
        if (!task.isToTrackAvailable() && !task.isToFollowAvailable() && !task.isGeoLocationAvailable()) {
            response.setStatusCode(configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_COLLECTION_ERROR));
            response.setMessage("Missing one or more fields (toTrack, toFollow, and geoLocation). At least one field is required");
            return Response.ok(response).build();
        }
        String collectionCode = task.getCollectionCode();

        //check if a task is already running with same configurations
        logger.info("Checking OAuth parameters for " + collectionCode);
        GenericCache cache = GenericCache.getInstance();
		if (cache.isTwtConfigExists(task)) {
            String msg = "Provided OAuth configurations already in use. Please stop this collection and then start again.";
            logger.info(collectionCode + ": " + msg);
            response.setMessage(msg);
            response.setStatusCode(configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_COLLECTION_ERROR));
            return Response.ok(response).build();
        }

		task.setStatusCode(configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_COLLECTION_INITIALIZING));
		logger.info("Initializing connection with Twitter streaming API for collection " + collectionCode);
		try {
			TwitterStreamTracker tracker = new TwitterStreamTracker(task);
			tracker.start();

			String cacheKey = task.getCollectionCode();
			cache.incrCounter(cacheKey, new Long(0));

			// if twitter streaming connection successful then change the status
			// code
			task.setStatusCode(configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_COLLECTION_RUNNING));
			task.setStatusMessage(null);
			cache.setTwtConfigMap(cacheKey, task);
			cache.setTwitterTracker(cacheKey, tracker);
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
		} catch (Exception ex) {
			logger.error("Exception in creating TwitterStreamTracker for collection " + collectionCode, ex);
			response.setMessage(ex.getMessage());
			response.setStatusCode(configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_COLLECTION_ERROR));
		}
		return Response.ok(response).build();
	}

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/stop")
    public Response stopTask(@QueryParam("id") String collectionCode) {
        GenericCache cache = GenericCache.getInstance();
        TwitterStreamTracker tracker = cache.getTwitterTracker(collectionCode);
        CollectionTask task = cache.getConfig(collectionCode);

        cache.delFailedCollection(collectionCode);
        cache.deleteCounter(collectionCode);
        cache.delTwtConfigMap(collectionCode);
        cache.delLastDownloadedDoc(collectionCode);
        cache.delTwitterTracker(collectionCode);
        cache.delReconnectAttempts(collectionCode);

		if (tracker != null) {
			try {
				tracker.close();
			} catch (IOException e) {
				ResponseWrapper response = new ResponseWrapper();
				response.setMessage(e.getMessage());
				response.setStatusCode(configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_COLLECTION_NOTFOUND));
				return Response.ok(response).build();
			}
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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/status")
    public Response getStatus(@QueryParam("id") String id) {
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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/restart")
    public Response restartCollection(@QueryParam("code") String collectionCode) throws InterruptedException {
        List<CollectionTask> collections = GenericCache.getInstance().getAllRunningCollectionTasks();
        CollectionTask collectionToRestart = null;
        for (CollectionTask collection : collections) {
            if (collection.getCollectionCode().equalsIgnoreCase(collectionCode)) {
                collectionToRestart = collection;
                break;
            }
        }
        stopTask(collectionCode);
        Thread.sleep(3000);
        Response response = startTask(collectionToRestart);
        return response;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/status/all")
    public Response getStatusAll() {
        List<CollectionTask> allTasks = GenericCache.getInstance().getAllConfigs();
        return Response.ok(allTasks).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/failed/all")
    public Response getAllFailedCollections() {
        List<CollectionTask> allTasks = GenericCache.getInstance().getAllFailedCollections();
        return Response.ok(allTasks).build();
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
