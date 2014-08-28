package qa.qcri.aidr.persister.api;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.net.UnknownHostException;
//import java.util.logging.Level;
//import java.util.logging.Logger;




import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.glassfish.jersey.jackson.JacksonFeature;

import qa.qcri.aidr.logging.ErrorLog;
import qa.qcri.aidr.persister.collector.RedisCollectorPersister;
import qa.qcri.aidr.utils.CollectionStatus;
import qa.qcri.aidr.utils.Config;
import qa.qcri.aidr.utils.GenericCache;
import qa.qcri.aidr.utils.JsonDeserializer;

/**
 * REST Web Service
 *
 * @author Imran
 */
@Path("persister")
public class Persister4CollectorAPI {
	
	private static Logger logger = Logger.getLogger(Persister4CollectorAPI.class.getName());
	private static ErrorLog elog = new ErrorLog();
	
    @Context
    private UriInfo context;

    public Persister4CollectorAPI() {
    }
    
    @GET
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/start")
  public Response startPersister(@QueryParam("file") String fileLocation, @QueryParam("collectionCode") String collectionCode) {
        String response="";
        try{
            fileLocation = Config.DEFAULT_PERSISTER_FILE_PATH; //OVERRIDING PATH RECEIVED FROM EXTERNAL REQUEST
        if (StringUtils.isNotEmpty(fileLocation) && StringUtils.isNotEmpty(collectionCode)) {
            if (GenericCache.getInstance().getPersisterObject(collectionCode) != null) {
                response="A persister is already running for this collection code [" + collectionCode + "]";
                return Response.ok(response).build();
            }
            
            RedisCollectorPersister p = new RedisCollectorPersister(fileLocation, collectionCode);
            p.startMe();
            GenericCache.getInstance().setPersisterObject(collectionCode, p);
            response = "Started persisting to " + fileLocation;
            return Response.ok(response).build();
        }
        }catch (Exception ex) {
            //Logger.getLogger(Persister4CollectorAPI.class.getName()).log(Level.SEVERE, null, ex);
        	logger.error(collectionCode +  ": Failed to start persister");
        	logger.error(elog.toStringException(ex));
        }
        return Response.ok(response).build();
    }

    @GET
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/stop")
    public Response stopPersister(@QueryParam("collectionCode") String collectionCode) {
        String response;
        RedisCollectorPersister p = (RedisCollectorPersister) GenericCache.getInstance().getPersisterObject(collectionCode);
        if (p != null) {
            try {
                logger.debug(collectionCode + "Aborting persister...");
                GenericCache.getInstance().delPersisterObject(collectionCode);
                p.suspendMe();
                logger.info("Aborting done for " + collectionCode);
                response = "Persistance of [" + collectionCode + "] has been stopped.";
                return Response.ok(response).build();
            } catch (InterruptedException ex) {
                //Logger.getLogger(Persister4CollectorAPI.class.getName()).log(Level.SEVERE, null, ex);
            	logger.error(collectionCode + ": Failed to stop persister");
            	logger.error(elog.toStringException(ex));
            }
        }
        response = "Unable to locate a running persister with the given collection code:[" + collectionCode + "]";
        return Response.ok(response).build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/genCSV")
    public Response generateCSVFromLastestJSON(@QueryParam("collectionCode") String collectionCode) throws UnknownHostException {
    	logger.info("Received request for collection: " + collectionCode);
    	JsonDeserializer jsonD = new JsonDeserializer();
        String fileName = jsonD.generateJSON2CSV_100K_BasedOnTweetCount(collectionCode);
        fileName = Config.SCD1_URL + collectionCode+"/"+fileName;
        
        logger.info("Done processing request for collection: " + collectionCode + ", returning created file: " + fileName);
        //return Response.ok(fileName).build();
        
        CollectionStatus s = new CollectionStatus();
        return Response.ok(s.getUIWrapper(collectionCode, null, fileName, true)).build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/genTweetIds")
    public Response generateTweetsIDSCSVFromAllJSON(@QueryParam("collectionCode") String collectionCode,
    		@DefaultValue("true") @QueryParam("downloadLimited") Boolean downloadLimited) throws UnknownHostException {
        logger.info("Received request for collection: " + collectionCode);
    	JsonDeserializer jsonD = new JsonDeserializer();
        String fileName = jsonD.generateJson2TweetIdsCSV(collectionCode, downloadLimited);
        fileName = Config.SCD1_URL + collectionCode+"/"+fileName;
        logger.info("Done processing request for collection: " + collectionCode + ", returning created file: " + fileName);
        
        CollectionStatus s = new CollectionStatus();
        if (s.getTotalDownloadedCount(collectionCode) > Config.DEFAULT_TWEETID_VOLUME_LIMIT) {
        	return Response.ok(s.getUIWrapper(collectionCode, null, fileName, true)).build();
        } else {
        	return Response.ok(s.getUIWrapper(collectionCode, Config.TWEET_DOWNLOAD_LIMIT_MSG, fileName, true)).build();
        }
    }
    
    @GET
    @Produces("application/json")
    @Path("/ping")
    public Response ping() throws UnknownHostException {
        String response = "{\"application\":\"aidr-persister\", \"status\":\"RUNNING\"}";
        return Response.ok(response).build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/genJson")
    public Response generateJSONFromLastestJSON(@QueryParam("collectionCode") String collectionCode) throws UnknownHostException {
    	logger.info("Received request for collection: " + collectionCode);
    	JsonDeserializer jsonD = new JsonDeserializer();
        String fileName = jsonD.generateJSON2JSON_100K_BasedOnTweetCount(collectionCode);
        fileName = Config.SCD1_URL + collectionCode+"/"+fileName;
        
        logger.info("Done processing request for collection: " + collectionCode + ", returning created file: " + fileName);
        //return Response.ok(fileName).build();
        
        CollectionStatus s = new CollectionStatus();
        return Response.ok(s.getUIWrapper(collectionCode, null, fileName, true)).build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/genJsonTweetIds")
    public Response generateTweetsIDSJSONFromAllJSON(@QueryParam("collectionCode") String collectionCode,
    			@DefaultValue("true") @QueryParam("downloadLimited") Boolean downloadLimited) throws UnknownHostException {
        logger.info("Received request for collection: " + collectionCode);
    	JsonDeserializer jsonD = new JsonDeserializer();
        String fileName = jsonD.generateJson2TweetIdsJson(collectionCode, downloadLimited);
        fileName = Config.SCD1_URL + collectionCode+"/"+fileName;
        logger.info("Done processing request for collection: " + collectionCode + ", returning created file: " + fileName);
        //return Response.ok(fileName).build();
        
        CollectionStatus s = new CollectionStatus();
        if (s.getTotalDownloadedCount(collectionCode) > Config.DEFAULT_TWEETID_VOLUME_LIMIT) {
        	return Response.ok(s.getUIWrapper(collectionCode, null, fileName, true)).build();
        } else {
        	return Response.ok(s.getUIWrapper(collectionCode, Config.TWEET_DOWNLOAD_LIMIT_MSG, fileName, true)).build();
        }
    }

}
