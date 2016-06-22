/**
 * REST Web Service
 *
 * @author Imran, Koushik
 */
package qa.qcri.aidr.persister.api;

/*
 * and open the template in the editor.
 */


import java.net.UnknownHostException;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import net.minidev.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import qa.qcri.aidr.common.values.DownloadType;
import qa.qcri.aidr.persister.collector.RedisCollectorPersister;
import qa.qcri.aidr.utils.DownloadJsonType;
import qa.qcri.aidr.utils.GenericCache;
import qa.qcri.aidr.utils.JsonDeserializer;
import qa.qcri.aidr.utils.PersisterConfigurationProperty;
import qa.qcri.aidr.utils.PersisterConfigurator;
import qa.qcri.aidr.utils.ResultStatus;


@Path("persister")
@Component
public class Persister4CollectorAPI {
	
	private static Logger logger = Logger.getLogger(Persister4CollectorAPI.class.getName());
	
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
            fileLocation = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH); //OVERRIDING PATH RECEIVED FROM EXTERNAL REQUEST
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
        	logger.error(collectionCode +  ": Failed to start persister");
        }
        return Response.ok(response).build();
    }

    @GET
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/stop")
    public Response stopPersister(@QueryParam("collectionCode") String collectionCode) {
        String response;
        RedisCollectorPersister p = GenericCache.getInstance().getPersisterObject(collectionCode);
        if (p != null) {
            try {
                logger.debug(collectionCode + "Aborting persister...");
                GenericCache.getInstance().delPersisterObject(collectionCode);
                p.suspendMe();
                logger.info("Aborting done for " + collectionCode);
                response = "Persistance of [" + collectionCode + "] has been stopped.";
                return Response.ok(response).build();
            } catch (InterruptedException ex) {
            	logger.error(collectionCode + ": Failed to stop persister");
            }
        }
        response = "Unable to locate a running persister with the given collection code:[" + collectionCode + "]";
        return Response.ok(response).build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/genCSV")
    public Response generateCSVFromLastestJSON(@QueryParam("collectionCode") String collectionCode,
    				@QueryParam("exportLimit") int exportLimit) throws UnknownHostException {
    	logger.info("Received request for collection: " + collectionCode);
    	JsonDeserializer jsonD = new JsonDeserializer();
        String fileName = jsonD.generateJSON2CSV_100K_BasedOnTweetCount(collectionCode, exportLimit);
        fileName = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.PERSISTER_DOWNLOAD_URL) + collectionCode+"/"+fileName;
        
        logger.info("Done processing request for collection: " + collectionCode + ", returning created file: " + fileName);
        //return Response.ok(fileName).build();
        
        JSONObject obj = new JSONObject();
        obj.putAll(ResultStatus.getUIWrapper(collectionCode, null, fileName, true));
        logger.info("Returning JSON object: " + ResultStatus.getUIWrapper(collectionCode, null, fileName, true));
        return Response.ok(obj.toJSONString()).build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/genTweetIds")
    public Response generateTweetsIDSCSVFromAllJSON(@QueryParam("collectionCode") String collectionCode,
    		@DefaultValue("true") @QueryParam("downloadLimited") Boolean downloadLimited) throws UnknownHostException {
        logger.info("Received request for collection: " + collectionCode);
    	JsonDeserializer jsonD = new JsonDeserializer();
    	Map<String, Object> result = jsonD.generateJson2TweetIdsCSV(collectionCode, downloadLimited);
        String fileName = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.PERSISTER_DOWNLOAD_URL) + collectionCode+"/" + (String) result.get("fileName");
        logger.info("Done processing request for collection: " + collectionCode + ", returning created file: " + fileName);
        
        JSONObject obj = new JSONObject();
        obj.put("feedCount", result.get("count"));
		if ((Integer) result.get("count") < Integer.parseInt(PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.DEFAULT_TWEETID_VOLUME_LIMIT)) ) {
			obj.putAll(ResultStatus.getUIWrapper(collectionCode, null, fileName, true));
			logger.info("Returning JSON object: " + ResultStatus.getUIWrapper(collectionCode, null, fileName, true));
			return Response.ok(obj.toJSONString()).build();
		} else {
			obj.putAll(ResultStatus.getUIWrapper(collectionCode,PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.TWEET_DOWNLOAD_LIMIT_MSG_PREFIX) + PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.DEFAULT_TWEETID_VOLUME_LIMIT) + PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.TWEET_DOWNLOAD_LIMIT_MSG_SUFFIX), fileName, true));
			logger.info("Returning JSON object: " + ResultStatus.getUIWrapper(collectionCode,  PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.TWEET_DOWNLOAD_LIMIT_MSG_PREFIX) + PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.DEFAULT_TWEETID_VOLUME_LIMIT) + PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.TWEET_DOWNLOAD_LIMIT_MSG_SUFFIX), fileName, true));
			return Response.ok(obj.toJSONString()).build();
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
    public Response generateJSONFromLastestJSON(@QueryParam("collectionCode") String collectionCode,
    		@DefaultValue(DownloadType.TEXT_JSON) @QueryParam("jsonType") String jsonType) throws UnknownHostException {
    	logger.info("Received request for collection: " + collectionCode + " with jsonType = " + jsonType);
    	JsonDeserializer jsonD = new JsonDeserializer();
        String fileName = jsonD.generateJSON2JSON_100K_BasedOnTweetCount(collectionCode, DownloadJsonType.getDownloadJsonTypeFromString(jsonType));
        fileName = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.PERSISTER_DOWNLOAD_URL) + collectionCode+"/"+fileName;
        
        logger.info("Done processing request for collection: " + collectionCode + ", returning created file: " + fileName);
        //return Response.ok(fileName).build();
        JSONObject obj = new JSONObject();
        obj.putAll(ResultStatus.getUIWrapper(collectionCode, null, fileName, true));
        
        logger.info("Returning JSON object: " + ResultStatus.getUIWrapper(collectionCode, null, fileName, true));
        return Response.ok(obj.toJSONString()).build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/genJsonTweetIds")
    public Response generateTweetsIDSJSONFromAllJSON(@QueryParam("collectionCode") String collectionCode,
    			@DefaultValue("true") @QueryParam("downloadLimited") Boolean downloadLimited,
    			@DefaultValue(DownloadType.TEXT_JSON) @QueryParam("jsonType") String jsonType) throws UnknownHostException {
        logger.info("Received request for collection: " + collectionCode + " with jsonType = " + jsonType);
    	JsonDeserializer jsonD = new JsonDeserializer();
    	Map<String, Object> result = jsonD.generateJson2TweetIdsJson(collectionCode, downloadLimited, DownloadJsonType.getDownloadJsonTypeFromString(jsonType));
        String fileName = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.PERSISTER_DOWNLOAD_URL) + collectionCode+"/" + (String)result.get("fileName");
        logger.info("Done processing request for collection: " + collectionCode + ", returning created file: " + fileName);
        //return Response.ok(fileName).build();
        JSONObject obj = new JSONObject();
		if ((Integer) result.get("count") < Integer.parseInt(PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.DEFAULT_TWEETID_VOLUME_LIMIT))) {
			obj.putAll(ResultStatus.getUIWrapper(collectionCode, null, fileName, true));
			logger.info("Returning JSON object: " + ResultStatus.getUIWrapper(collectionCode, null, fileName, true));
			return Response.ok(obj.toJSONString()).build();
		} else {
			obj.putAll(ResultStatus.getUIWrapper(collectionCode,  PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.TWEET_DOWNLOAD_LIMIT_MSG_PREFIX) + PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.DEFAULT_TWEETID_VOLUME_LIMIT) + PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.TWEET_DOWNLOAD_LIMIT_MSG_SUFFIX), fileName, true));
			logger.info("Returning JSON object: " + ResultStatus.getUIWrapper(collectionCode,   PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.TWEET_DOWNLOAD_LIMIT_MSG_PREFIX) + PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.DEFAULT_TWEETID_VOLUME_LIMIT) + PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.TWEET_DOWNLOAD_LIMIT_MSG_SUFFIX), fileName, true));
			return Response.ok(obj.toJSONString()).build();
		}
    }

}
