/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.collector.api;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.net.URLEncoder;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import qa.qcri.aidr.collector.beans.ResponseWrapper;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang.StringUtils;
import qa.qcri.aidr.collector.collectors.TwitterStreamTracker;
import qa.qcri.aidr.collector.utils.GenericCache;
import qa.qcri.aidr.collector.beans.CollectionTask;
import qa.qcri.aidr.collector.utils.Config;
import qa.qcri.aidr.collector.logging.Loggable;
import static qa.qcri.aidr.collector.logging.Loggable.LOG_LEVEL;
import qa.qcri.aidr.collector.utils.TwitterStreamQueryBuilder;
import twitter4j.conf.ConfigurationBuilder;

/**
 * REST Web Service
 *
 * @author Imran
 */
@Path("twitter/")
public class TwitterCollectorAPI extends Loggable {

    @Context
    private UriInfo context;
    
    private Client client = new Client();

    public TwitterCollectorAPI() {
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/start")
    public Response startTask(CollectionTask collectionTask) {
        ResponseWrapper response = new ResponseWrapper();

        //check if all twitter specific information is available in the request
        if (!collectionTask.isTwitterInfoPresent()) {
            response.setMessage("One or more Twitter authentication token(s) are missing.");
            response.setStatusCode(Config.STATUS_CODE_COLLECTION_ERROR);
            return Response.ok(response).build();
        }

        //check if both toTrack and toFollow are missing in the query
        if (!collectionTask.isToTrackAvailable() && !collectionTask.isToFollowAvailable() && !collectionTask.isGeoLocationAvailable()) {
            response.setMessage("Missing all [toTrack, toFollow, and geoLocation] fields. At least one field is required.");
            response.setStatusCode(Config.STATUS_CODE_COLLECTION_ERROR);
            return Response.ok(response).build();
        }

        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setDebugEnabled(false)
                .setJSONStoreEnabled(true)
                .setOAuthConsumerKey(collectionTask.getConsumerKey())
                .setOAuthConsumerSecret(collectionTask.getConsumerSecret())
                .setOAuthAccessToken(collectionTask.getAccessToken())
                .setOAuthAccessTokenSecret(collectionTask.getAccessTokenSecret());

        //check if a task is already running with same configutations
        if (GenericCache.getInstance().isTwtConfigExists(collectionTask)) {
            response.setMessage("Provided OAuth configurations already in use. Please stop this collection and then start again.");
            response.setStatusCode(Config.STATUS_CODE_COLLECTION_ERROR);
            return Response.ok(response).build();
        }

        String collectionCode = collectionTask.getCollectionCode();

        //building filter for filtering twitter stream
        TwitterStreamQueryBuilder queryBuilder = null;
        try {
            String langFilter = StringUtils.isNotEmpty(collectionTask.getLanguageFilter()) ? collectionTask.getLanguageFilter() : Config.LANGUAGE_ALLOWED_ALL;

            queryBuilder = new TwitterStreamQueryBuilder(collectionTask.getToTrack(), collectionTask.getToFollow(), collectionTask.getGeoLocation(), langFilter);
        } catch (IllegalArgumentException e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(Config.STATUS_CODE_COLLECTION_ERROR);
            return Response.ok(response).build();
        }

        collectionTask.setStatusCode(Config.STATUS_CODE_COLLECTION_INITIALIZING);
        TwitterStreamTracker tracker = null;
        try {
            tracker = new TwitterStreamTracker(queryBuilder, configurationBuilder, collectionTask);
        } catch (Exception ex) {
            Logger.getLogger(TwitterCollectorAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (Config.DEFAULT_PERSISTER_ENABLED){
            startCollectorPersister(collectionCode);
            startTaggerPersister(collectionCode);
        }

        //preparing callback response
        String msg = "Initializing Twitter stream tracking. It Will be tracking: " + collectionTask.getToTrack()
                + " Following: " + queryBuilder.getToFollow() + " Geo: " + queryBuilder.getGeoLocation();
        log(LOG_LEVEL.INFO, msg);
        response.setMessage(msg);
        response.setStatusCode(Config.STATUS_CODE_COLLECTION_INITIALIZING);
        return Response.ok(response).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/stop")
    public Response stopTask(@QueryParam("id") String collectionCode) throws InterruptedException {
        ResponseWrapper response = new ResponseWrapper();
        TwitterStreamTracker tracker = (TwitterStreamTracker) GenericCache.getInstance().getTwitterTracker(collectionCode);
        String responseMsg = null;
        if (tracker != null) {
            tracker.abortCollection();
            
            GenericCache.getInstance().delFailedCollection(collectionCode); 
            GenericCache.getInstance().deleteCounter(collectionCode);
            GenericCache.getInstance().delTwtConfigMap(collectionCode);
            GenericCache.getInstance().delLastDownloadedDoc(collectionCode);
            GenericCache.getInstance().delTwitterTracker(collectionCode);
            
            if (Config.DEFAULT_PERSISTER_ENABLED){
                stopCollectorPersister(collectionCode);
                stopTaggerPersister(collectionCode);
            }
            
            responseMsg = "Collector has been successfully stopped.";
            System.out.println(collectionCode + ": " + responseMsg);
            response.setMessage(responseMsg);
            response.setStatusCode(Config.STATUS_CODE_COLLECTION_STOPPED);
            log(LOG_LEVEL.INFO, responseMsg);
            return Response.ok(response).build();
        } else {

            GenericCache.getInstance().delTwitterTracker(collectionCode);
            GenericCache.getInstance().deleteCounter(collectionCode);
            GenericCache.getInstance().delTwtConfigMap(collectionCode);
            GenericCache.getInstance().delLastDownloadedDoc(collectionCode);
            responseMsg = "No collector instances found to be stopped with the given id:" + collectionCode;
            response.setMessage(responseMsg);
            response.setStatusCode(Config.STATUS_CODE_COLLECTION_NOTFOUND);
            log(LOG_LEVEL.INFO, responseMsg);
            return Response.ok(response).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/status")
    public Response getStatus(@QueryParam("id") String id) {
        ResponseWrapper response = new ResponseWrapper();
        String responseMsg = null;
        if (StringUtils.isEmpty(id)) {
            response.setMessage("Invalid key. No running collector found with the given id.");
            response.setStatusCode(Config.STATUS_CODE_COLLECTION_NOTFOUND);
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

        response.setMessage("Invalid key. No running collector found with the given id.");
        response.setStatusCode(Config.STATUS_CODE_COLLECTION_NOTFOUND);
        return Response.ok(response).build();

    }
    
    //TODO: PENDING SERVICE
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/restart")
    public Response restartCollection(@QueryParam("code") String collectionCode) throws InterruptedException {
        List<CollectionTask> collections = GenericCache.getInstance().getAllRunningCollectionTasks();
        CollectionTask collectionToRestart = null;
        for(CollectionTask collection : collections){
            if (collection.getCollectionCode().equalsIgnoreCase(collectionCode)){
                collectionToRestart = collection;
                break;
            }
        }
        stopTask(collectionCode);
        Thread.sleep(3000);
        Response response =  startTask(collectionToRestart);
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
    
    public void startCollectorPersister(String collectionCode){
        
        try{
        WebResource webResource = client.resource(Config.PERSISTER_REST_URI + "persister/start?file=" 
                + URLEncoder.encode(Config.DEFAULT_PERSISTER_FILE_LOCATION) 
                + "&collectionCode=" + URLEncoder.encode(collectionCode));
            ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
            String jsonResponse = clientResponse.getEntity(String.class);
            System.out.println("Collector persister response: " + jsonResponse );
        }catch(RuntimeException e){
            
            System.out.println("Could not start persister. Is persister running?");
        }
    }
    
    public void stopCollectorPersister(String collectionCode){
        try{
        WebResource webResource = client.resource(Config.PERSISTER_REST_URI + "persister/stop?collectionCode=" + URLEncoder.encode(collectionCode));
            ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
            String jsonResponse = clientResponse.getEntity(String.class);
            System.out.println("Collector persister response: " + jsonResponse );
        }catch(RuntimeException e){
         System.out.println("Could not stop persister. Is persister running?");   
        }
    }
    
     public void startTaggerPersister(String collectionCode){
        
        try{
        WebResource webResource = client.resource(Config.PERSISTER_REST_URI + "taggerPersister/start?file=" 
                + URLEncoder.encode(Config.DEFAULT_PERSISTER_FILE_LOCATION) 
                + "&collectionCode=" + URLEncoder.encode(collectionCode));
            ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
            String jsonResponse = clientResponse.getEntity(String.class);
            System.out.println("Tagger persister response: " + jsonResponse );
        }catch(RuntimeException e){
            
            System.out.println("Could not start persister. Is persister running?");
        }
    }
     
     public void stopTaggerPersister(String collectionCode){
        try{
        WebResource webResource = client.resource(Config.PERSISTER_REST_URI + "taggerPersister/stop?collectionCode=" + URLEncoder.encode(collectionCode));
            ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
            String jsonResponse = clientResponse.getEntity(String.class);
            System.out.println("Tagger persister response: " + jsonResponse );
        }catch(RuntimeException e){
         System.out.println("Could not stop persister. Is persister running?");   
        }
    }
}
