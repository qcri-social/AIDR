package qa.qcri.aidr.collector.api;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import qa.qcri.aidr.collector.beans.CollectionTask;
import qa.qcri.aidr.collector.beans.ResponseWrapper;
import qa.qcri.aidr.collector.beans.TwitterCollectionTask;
import qa.qcri.aidr.collector.collectors.TwitterStreamTracker;
import qa.qcri.aidr.collector.utils.CollectorConfigurationProperty;
import qa.qcri.aidr.collector.utils.GenericCache;

/**
 * @author Imran
 * RESTFul APIs to start and stop Twitter collections.
 * TODO: remove non-API related operations such as startPersister to other appropriate classes.
 */
@RestController
@RequestMapping("/twitter")
public class TwitterCollectionController extends BaseController {

    @RequestMapping(value = "/start", method={RequestMethod.POST})
    public Response startTask(@RequestBody TwitterCollectionTask task) {
        logger.info("Collection start request received for " + task.getCollectionCode());
        logger.info("Details:\n" + task.toString());
        ResponseWrapper response = new ResponseWrapper();

        //check if all twitter specific information is available in the request
        if (!task.checkSocialConfigInfo()) {
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
		if (cache.isConfigExists(task)) {
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
			cache.setTwitterTracker(cacheKey, tracker);
			cache.setTwtConfigMap(cacheKey, task);
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

    @RequestMapping("/status")
    public Response getStatus(@RequestParam("id") String id) {
        ResponseWrapper response = new ResponseWrapper();
        if (StringUtils.isEmpty(id)) {
            response.setMessage("Invalid key. No running collector found for the given id.");
            response.setStatusCode(configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_COLLECTION_NOTFOUND));
            return Response.ok(response).build();
        }
        CollectionTask task = GenericCache.getInstance().getConfig(id, "twitter");
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

    @RequestMapping("/restart")
    public ResponseWrapper restartCollection(@QueryParam("code") String collectionCode) throws InterruptedException {
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
        return null;//startTask(collectionToRestart);
    }

    @RequestMapping("/status/all")
    public List<CollectionTask> getStatusAll() {
        List<CollectionTask> allTasks = GenericCache.getInstance().getAllConfigs();
        return allTasks;
    }

    @RequestMapping("/failed/all")
    public List<CollectionTask> getAllFailedCollections() {
        List<CollectionTask> allTasks = GenericCache.getInstance().getAllFailedCollections();
        return allTasks;
    }
    
    protected Response stopCollection(String collectionCode) {
    	GenericCache cache = GenericCache.getInstance();
    	TwitterStreamTracker tracker = cache.getTwitterTracker(collectionCode);
        CollectionTask task = cache.getConfig(collectionCode, "twitter");

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
		return null;
    }

}
