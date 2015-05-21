package qa.qcri.aidr.persister.api;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//import com.sun.jersey.api.json.JSONWithPadding;


import java.net.UnknownHostException;   
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.minidev.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import qa.qcri.aidr.common.filter.DeserializeFilters;
import qa.qcri.aidr.common.filter.JsonQueryList;
import qa.qcri.aidr.persister.tagger.RedisTaggerPersister;
import qa.qcri.aidr.utils.ClassifiedTweet;
import qa.qcri.aidr.utils.DownloadJsonType;
import qa.qcri.aidr.utils.GenericCache;
import qa.qcri.aidr.utils.JsonDeserializer;
import qa.qcri.aidr.utils.ResultStatus;
import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.common.values.DownloadType;
import static qa.qcri.aidr.utils.ConfigProperties.getProperty;

/**
 * REST Web Service
 *
 * @author Imran
 */
@Path("taggerPersister")
public class Persister4TaggerAPI {

	private static Logger logger = Logger.getLogger(Persister4TaggerAPI.class.getName());
	private static ErrorLog elog = new ErrorLog();

	@Context
	private UriInfo context;

	public Persister4TaggerAPI() {
	}

	@GET
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/start")
	public Response startPersister(@QueryParam("file") String fileLocation, @QueryParam("collectionCode") String collectionCode) {
		logger.info(collectionCode + ": In tagger persister start");
		String response = "";
		try {
			fileLocation = getProperty("DEFAULT_PERSISTER_FILE_PATH"); //OVERRIDING PATH RECEIVED FROM EXTERNAL REQUEST
			if (StringUtils.isNotEmpty(fileLocation) && StringUtils.isNotEmpty(collectionCode)) {
				if (GenericCache.getInstance().getTaggerPersisterMap(collectionCode) != null) {
					response = "A tagger persister is already running for this collection code [" + collectionCode + "]";
					return Response.ok(response).build();
				}

				RedisTaggerPersister p = new RedisTaggerPersister(fileLocation, collectionCode);
				p.startMe();
				GenericCache.getInstance().setTaggerPersisterMap(collectionCode, p);
				response = "Started tagger persisting to " + fileLocation + "/output";
				return Response.ok(response).build();
			}
		} catch (Exception ex) {
			logger.error(collectionCode  + ": failed to start persister");
			logger.error(elog.toStringException(ex));
		}
		return Response.ok(response).build();
	}

	@GET
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/stop")
	public Response stopPersister(@QueryParam("collectionCode") String collectionCode) {
		logger.info(collectionCode + ": In tagger persister stop");
		String response;
		RedisTaggerPersister p = (RedisTaggerPersister) GenericCache.getInstance().getTaggerPersisterMap(collectionCode);
		if (p != null) {
			try {
				logger.debug(collectionCode + ": Aborting tagger persister...");
				GenericCache.getInstance().delTaggerPersisterMap(collectionCode);
				p.suspendMe();
				logger.info(collectionCode + ": Aborting done for " + collectionCode);
				response = "Tagger Persistance of [" + collectionCode + "] has been stopped.";
				return Response.ok(response).build();
			} catch (InterruptedException ex) {
				//Logger.getLogger(Persister4TaggerAPI.class.getName()).log(Level.SEVERE, null, ex);
				logger.error(collectionCode + ": failed to stop persister");
				logger.error(elog.toStringException(ex));
			}
		}
		response = "Unable to locate a running tagger persister with the given collection code:[" + collectionCode + "]";
		return Response.ok(response).build();
	}

	@GET
	@Produces("application/json")
	@Path("/ping")
	public Response ping() throws UnknownHostException {
		String response = "{\"application\":\"aidr-taggerPersister\", \"status\":\"RUNNING\"}";
		return Response.ok(response).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/genCSV")
	public Response generateCSVFromLastestJSON(@QueryParam("collectionCode") String collectionCode, 
			@QueryParam("exportLimit") int exportLimit) throws UnknownHostException {
		logger.debug("In tagger-persister genCSV");
		logger.info("Received request for collection: " + collectionCode);
		JsonDeserializer jsonD = new JsonDeserializer();
		exportLimit = Integer.parseInt(getProperty("TWEETS_EXPORT_LIMIT_100K"));		// Koushik: added to override user specs
		String fileName = jsonD.taggerGenerateJSON2CSV_100K_BasedOnTweetCount(collectionCode, exportLimit);
		fileName = getProperty("SCD1_URL") + collectionCode + "/output/" + fileName;

		logger.info("Done processing request for collection: " + collectionCode + ", returning created file: " + fileName);
		//return Response.ok(fileName).build();
		JSONObject obj = new JSONObject();
		try {
			obj.putAll(ResultStatus.getUIWrapper(collectionCode, null, fileName, true));
			logger.info("Returning JSON object: " + ResultStatus.getUIWrapper(collectionCode, null, fileName, true));
			return Response.ok(obj.toJSONString()).build();
		} catch (Exception e) {
			logger.error("Unable to return result ");
			logger.error(elog.toStringException(e));
			obj.putAll(ResultStatus.getUIWrapper(collectionCode, null, fileName, false));
			return Response.ok(obj.toJSONString()).build();

		}
	}

	/**
	 * 
	 * @param collectionCode
	 * @param exportLimit
	 * @return CSV format output from JSON filtered by user provided list of label names 
	 * @throws UnknownHostException
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/filter/genCSV")
	public Response generateCSVFromLastestJSONFiltered(String queryString, 
			@QueryParam("collectionCode") String collectionCode, 
			@QueryParam("exportLimit") Integer exportLimit,
			@QueryParam("userName") String userName) throws UnknownHostException {
		DeserializeFilters des = new DeserializeFilters();
		JsonQueryList queryList = des.deserializeConstraints(queryString);
		JsonDeserializer jsonD = new JsonDeserializer();
		
		logger.info("received request for collection: " + collectionCode + ", count = " + exportLimit + ", from user = " + userName);
		if (queryList != null) {
			logger.info(collectionCode + ": received constraints = " + queryList.toString());
		} else {
			logger.info(collectionCode + ": received constraints = " + queryList);
		}
		
		if (null == exportLimit) exportLimit = Integer.parseInt(getProperty("TWEETS_EXPORT_LIMIT_100K"));
		String fileName = jsonD.taggerGenerateJSON2CSV_100K_BasedOnTweetCountFiltered(collectionCode, exportLimit, queryList, userName);
		logger.info("done processing request for collection: " + collectionCode + ", returning created file: " + fileName);
		
		JSONObject obj = new JSONObject();
		obj.putAll(ResultStatus.getUIWrapper(collectionCode, getProperty("PERSISTER_CHANGE_NOTIFY_MSG"), fileName, true));
		logger.info("Returning JSON object: " + ResultStatus.getUIWrapper(collectionCode, getProperty("PERSISTER_CHANGE_NOTIFY_MSG"), fileName, true));
		return Response.ok(obj.toJSONString())
				.allow("POST", "OPTIONS", "HEAD")
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "POST, OPTIONS, HEAD")
				.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
				.build();
	}  

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/genTweetIds")
	public Response generateTweetsIDSCSVFromAllJSON(@QueryParam("collectionCode") String collectionCode,
			@DefaultValue("true") @QueryParam("downloadLimited") Boolean downloadLimited) throws UnknownHostException {
		logger.debug("In tagger-persister genTweetIds");
		logger.info("received request for collection: " + collectionCode);

		JsonDeserializer jsonD = new JsonDeserializer();
		Map<String, Object> result = jsonD.generateClassifiedJson2TweetIdsCSV(collectionCode, downloadLimited);
		String fileName = getProperty("SCD1_URL") + collectionCode + "/output/" + (String) result.get("fileName");

		logger.info("Done processing request for collection: " + collectionCode + ", returning created file: " + fileName);
		//return Response.ok(fileName).build();
		JSONObject obj = new JSONObject();
		if ((Integer) result.get("count") < Integer.parseInt(getProperty("DEFAULT_TWEETID_VOLUME_LIMIT"))) {
			obj.putAll(ResultStatus.getUIWrapper(collectionCode, getProperty("PERSISTER_CHANGE_NOTIFY_MSG"), fileName, true));
			logger.info("Returning JSON object: " + ResultStatus.getUIWrapper(collectionCode, getProperty("PERSISTER_CHANGE_NOTIFY_MSG"), fileName, true));
			return Response.ok(obj.toJSONString()).build();
		} else {
			obj.putAll(ResultStatus.getUIWrapper(collectionCode,  getProperty("TWEET_DOWNLOAD_LIMIT_MSG_PREFIX") + getProperty("DEFAULT_TWEETID_VOLUME_LIMIT") + getProperty("TWEET_DOWNLOAD_LIMIT_MSG_SUFFIX") + getProperty("PERSISTER_CHANGE_NOTIFY_MSG"), fileName, true));
			logger.info("Returning JSON object: " + ResultStatus.getUIWrapper(collectionCode,  getProperty("TWEET_DOWNLOAD_LIMIT_MSG_PREFIX") + getProperty("DEFAULT_TWEETID_VOLUME_LIMIT") + getProperty("TWEET_DOWNLOAD_LIMIT_MSG_SUFFIX") + getProperty("PERSISTER_CHANGE_NOTIFY_MSG"), fileName, true));
			return Response.ok(obj.toJSONString()).build();
		}
	}

	/**
	 * 
	 * @param collectionCode
	 * @return CSV format output from JSON filtered by user provided list of label names 
	 * @throws UnknownHostException
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/filter/genTweetIds")
	public Response generateTweetsIDSCSVFromAllJSONFiltered(String queryString, 
			@QueryParam("collectionCode") String collectionCode,
			@DefaultValue("true") @QueryParam("downloadLimited") Boolean downloadLimited,
			@QueryParam("userName") String userName) 
					throws UnknownHostException {
		DeserializeFilters des = new DeserializeFilters();
		JsonQueryList queryList = des.deserializeConstraints(queryString);

		JsonDeserializer jsonD = new JsonDeserializer();
		logger.info("received request for collection: " + collectionCode);
		if (queryList != null) {
			logger.info(collectionCode + ": received constraints = " + queryList.toString());
		} else {
			logger.info(collectionCode + ": received constraints = " + queryList);
		}
		Map<String, Object> result = jsonD.generateClassifiedJson2TweetIdsCSVFiltered(collectionCode, queryList, downloadLimited, userName);
		logger.info("Done processing request for collection: " + collectionCode + ", returning created file: " + result.get("fileName"));
		
		JSONObject obj = new JSONObject();
		if ((Integer) result.get("count") < Integer.parseInt(getProperty("DEFAULT_TWEETID_VOLUME_LIMIT"))) {
			obj.putAll(ResultStatus.getUIWrapper(collectionCode, getProperty("PERSISTER_CHANGE_NOTIFY_MSG"), result.get("fileName").toString(), true));
			logger.info("Returning JSON object: " + ResultStatus.getUIWrapper(collectionCode, getProperty("PERSISTER_CHANGE_NOTIFY_MSG"), result.get("fileName").toString(), true));
			return Response.ok(obj.toJSONString())
					.allow("POST", "OPTIONS", "HEAD")
					.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Credentials", "true")
					.header("Access-Control-Allow-Methods", "POST, OPTIONS, HEAD")
					.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
					.build();
		} else {
			obj.putAll(ResultStatus.getUIWrapper(collectionCode,  getProperty("TWEET_DOWNLOAD_LIMIT_MSG_PREFIX") + getProperty("DEFAULT_TWEETID_VOLUME_LIMIT") + getProperty("TWEET_DOWNLOAD_LIMIT_MSG_SUFFIX") + getProperty("PERSISTER_CHANGE_NOTIFY_MSG"), result.get("fileName").toString(), true));
			logger.info("Returning JSON object: " + ResultStatus.getUIWrapper(collectionCode,  getProperty("TWEET_DOWNLOAD_LIMIT_MSG_PREFIX") + getProperty("DEFAULT_TWEETID_VOLUME_LIMIT") + getProperty("TWEET_DOWNLOAD_LIMIT_MSG_SUFFIX") + getProperty("PERSISTER_CHANGE_NOTIFY_MSG"), result.get("fileName").toString(), true));
			return Response.ok(obj.toJSONString())
					.allow("POST", "OPTIONS", "HEAD")
					.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Credentials", "true")
					.header("Access-Control-Allow-Methods", "POST, OPTIONS, HEAD")
					.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
					.build();
		}
	}

	@Deprecated
	@GET
	@Produces({"application/javascript"})
	@Path("/getClassifiedTweets")
	//public JSONWithPadding get_N_LatestClassifiedTweets(@QueryParam("collectionCode") String collectionCode, @QueryParam("exportLimit") int exportLimit, @QueryParam("callback") String callback) throws UnknownHostException {
	public Response get_N_LatestClassifiedTweets(@QueryParam("collectionCode") String collectionCode, 
			@QueryParam("exportLimit") int exportLimit, 
			@QueryParam("callback") String callback) 
					throws UnknownHostException {
		logger.info("In tagger-persister getClassifiedTweets");
		JsonDeserializer jsonD = new JsonDeserializer();
		List<ClassifiedTweet> tweets = jsonD.getNClassifiedTweetsJSON(collectionCode, exportLimit);
		//return Response.ok(tweets).build();
		//return new JSONWithPadding(new GenericEntity<List<ClassifiedTweet>>(tweets) {}, callback);
		return Response.ok(new GenericEntity<List<ClassifiedTweet>>(tweets) {}, callback).build();
	}

	/**
	 * 
	 * @param collectionCode
	 * @param exportLimit
	 * @param callback
	 * @return CSV format output from JSON filtered by user provided list of label names 
	 * @throws UnknownHostException
	 */
	@Deprecated
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)		// {application/json} ?
	@Path("/filter/getClassifiedTweets")
	public Response get_N_LatestClassifiedTweetsFiltered(String queryString, 
			@QueryParam("collectionCode") String collectionCode, 
			@QueryParam("exportLimit") int exportLimit, 
			@QueryParam("callback") String callback) throws UnknownHostException {

		DeserializeFilters des = new DeserializeFilters();
		JsonQueryList queryList = des.deserializeConstraints(queryString);
		JsonDeserializer jsonD = new JsonDeserializer();

		logger.info(collectionCode + ": Received POST list: " + queryList.toString());

		List<ClassifiedTweet> tweets = jsonD.getNClassifiedTweetsJSONFiltered(collectionCode, exportLimit, queryList);
		return Response.ok(new GenericEntity<List<ClassifiedTweet>>(tweets) {}, callback)
				.allow("POST", "OPTIONS", "HEAD")
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "POST, OPTIONS, HEAD")
				.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
				.build();
	}


	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/genJson")
	public Response generateJSONFromLastestJSON(@QueryParam("collectionCode") String collectionCode, 
			@QueryParam("exportLimit") int exportLimit,
			@DefaultValue(DownloadType.TEXT_JSON) @QueryParam("jsonType") String jsonType) throws UnknownHostException {
		logger.debug("In tagger-persister genCSV");
		logger.info("Received request for collection: " + collectionCode + " with jsonType = " + jsonType);
		JsonDeserializer jsonD = new JsonDeserializer();
		
		if (0 == exportLimit) exportLimit = Integer.parseInt(getProperty("TWEETS_EXPORT_LIMIT_100K"));
		
		String fileName = jsonD.taggerGenerateJSON2JSON_100K_BasedOnTweetCount(collectionCode, exportLimit, DownloadJsonType.getDownloadJsonTypeFromString(jsonType));
		fileName = getProperty("SCD1_URL") + collectionCode + "/output/" + fileName;

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
		logger.debug("In tagger-persister genTweetIds");
		logger.info("Received request for collection: " + collectionCode + " with jsonType = " + jsonType);

		JsonDeserializer jsonD = new JsonDeserializer();
		Map<String, Object> result = jsonD.generateClassifiedJson2TweetIdsJSON(collectionCode, downloadLimited, DownloadJsonType.getDownloadJsonTypeFromString(jsonType));
		String fileName = getProperty("SCD1_URL") + collectionCode + "/output/" + result.get("fileName");

		logger.info("Done processing request for collection: " + collectionCode + ", returning created file: " + fileName);
		//return Response.ok(fileName).build();
		JSONObject obj = new JSONObject();
		if ((Integer) result.get("count") < Integer.parseInt(getProperty("DEFAULT_TWEETID_VOLUME_LIMIT"))) {
			obj.putAll(ResultStatus.getUIWrapper(collectionCode, null, fileName, true));
			logger.info("Returning JSON object: " + ResultStatus.getUIWrapper(collectionCode, null, fileName, true));
			return Response.ok(obj.toJSONString())
					.allow("POST", "OPTIONS", "HEAD")
					.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Credentials", "true")
					.header("Access-Control-Allow-Methods", "POST, OPTIONS, HEAD")
					.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
					.build();
		} else {
			obj.putAll(ResultStatus.getUIWrapper(collectionCode,  getProperty("TWEET_DOWNLOAD_LIMIT_MSG_PREFIX") + getProperty("DEFAULT_TWEETID_VOLUME_LIMIT") + getProperty("TWEET_DOWNLOAD_LIMIT_MSG_SUFFIX"), fileName, true));
			logger.info("Returning JSON object: " + ResultStatus.getUIWrapper(collectionCode,  getProperty("TWEET_DOWNLOAD_LIMIT_MSG_PREFIX") + getProperty("DEFAULT_TWEETID_VOLUME_LIMIT") + getProperty("TWEET_DOWNLOAD_LIMIT_MSG_SUFFIX"), fileName, true));
			return Response.ok(obj.toJSONString())
					.allow("POST", "OPTIONS", "HEAD")
					.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Credentials", "true")
					.header("Access-Control-Allow-Methods", "POST, OPTIONS, HEAD")
					.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
					.build();
		}
	}
	
	
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/filter/genJson")
	public Response generateJSONFromLastestJSONFiltered(String queryString, 
			@QueryParam("collectionCode") String collectionCode, 
			@QueryParam("exportLimit") Integer exportLimit,
			@DefaultValue(DownloadType.TEXT_JSON) @QueryParam("jsonType") String jsonType,
			@QueryParam("userName") String userName) throws UnknownHostException {
		logger.debug("In tagger-persister genCSV");
		logger.info("Received request for collection: " + collectionCode + " with jsonType = " + jsonType);
		
		DeserializeFilters des = new DeserializeFilters();
		JsonQueryList queryList = des.deserializeConstraints(queryString);
		if (queryList != null) {
			logger.info(collectionCode + ": received constraints = " + queryList.toString());
		} else {
			logger.info(collectionCode + ": received constraints = " + queryList);
		}
		
		JsonDeserializer jsonD = new JsonDeserializer();
		if (null == exportLimit) exportLimit = Integer.parseInt(getProperty("TWEETS_EXPORT_LIMIT_100K"));		// Koushik: added to override user specs
		String fileName = jsonD.taggerGenerateJSON2JSON_100K_BasedOnTweetCountFiltered(collectionCode, exportLimit, queryList, DownloadJsonType.getDownloadJsonTypeFromString(jsonType), userName);
		logger.info("Done processing request for collection: " + collectionCode + ", returning created file: " + fileName);
		//return Response.ok(fileName).build();
		
		JSONObject obj = new JSONObject();
		obj.putAll(ResultStatus.getUIWrapper(collectionCode, getProperty("PERSISTER_CHANGE_NOTIFY_MSG"), fileName, true));
		logger.info("Returning JSON object: " + ResultStatus.getUIWrapper(collectionCode, getProperty("PERSISTER_CHANGE_NOTIFY_MSG"), fileName, true));
		return Response.ok(obj.toJSONString()).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/filter/genJsonTweetIds")
	public Response generateTweetsIDSJSONFromAllJSONFiltered(String queryString, 
			@QueryParam("collectionCode") String collectionCode, 
			@DefaultValue("true") @QueryParam("downloadLimited") Boolean downloadLimited,
			@DefaultValue(DownloadType.TEXT_JSON) @QueryParam("jsonType") String jsonType,
			@QueryParam("userName") String userName) throws UnknownHostException {
		logger.debug("In tagger-persister genTweetIds");
		logger.info("Received request for collection: " + collectionCode + " with jsonType = " + jsonType);
		DeserializeFilters des = new DeserializeFilters();
		JsonQueryList queryList = des.deserializeConstraints(queryString);
		if (queryList != null) {
			logger.info(collectionCode + ": received constraints = " + queryList.toString());
		} else {
			logger.info(collectionCode + ": received constraints = " + queryList);
		}
		JsonDeserializer jsonD = new JsonDeserializer();
		Map<String, Object> result = jsonD.generateClassifiedJson2TweetIdsJSONFiltered(collectionCode, downloadLimited, queryList, DownloadJsonType.getDownloadJsonTypeFromString(jsonType), userName);
		
		logger.info("Done processing request for collection: " + collectionCode + ", returning created file: " + result.get("fileName"));
		//return Response.ok(fileName).build();
		JSONObject obj = new JSONObject();
		if ((Integer) result.get("count") < Integer.parseInt(getProperty("DEFAULT_TWEETID_VOLUME_LIMIT"))) {
			obj.putAll(ResultStatus.getUIWrapper(collectionCode, getProperty("PERSISTER_CHANGE_NOTIFY_MSG"), result.get("fileName").toString(), true));
			logger.info("Returning JSON object: " + ResultStatus.getUIWrapper(collectionCode, getProperty("PERSISTER_CHANGE_NOTIFY_MSG"), result.get("fileName").toString(), true));
			return Response.ok(obj.toJSONString())
					.allow("POST", "OPTIONS", "HEAD")
					.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Credentials", "true")
					.header("Access-Control-Allow-Methods", "POST, OPTIONS, HEAD")
					.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
					.build();
		} else {
			obj.putAll(ResultStatus.getUIWrapper(collectionCode,  getProperty("TWEET_DOWNLOAD_LIMIT_MSG_PREFIX") + getProperty("DEFAULT_TWEETID_VOLUME_LIMIT") + getProperty("TWEET_DOWNLOAD_LIMIT_MSG_SUFFIX") + getProperty("PERSISTER_CHANGE_NOTIFY_MSG"), result.get("fileName").toString(), true));
			logger.info("Returning JSON object: " + ResultStatus.getUIWrapper(collectionCode,  getProperty("TWEET_DOWNLOAD_LIMIT_MSG_PREFIX") + getProperty("DEFAULT_TWEETID_VOLUME_LIMIT") + getProperty("TWEET_DOWNLOAD_LIMIT_MSG_SUFFIX") + getProperty("PERSISTER_CHANGE_NOTIFY_MSG"), result.get("fileName").toString(), true));
			return Response.ok(obj.toJSONString())
					.allow("POST", "OPTIONS", "HEAD")
					.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Credentials", "true")
					.header("Access-Control-Allow-Methods", "POST, OPTIONS, HEAD")
					.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
					.build();
		}
	}


	/////////////////////////////////////////////////////////////////////////

	// Also set response header in OPTIONS pre-flight to enable CORS
	@OPTIONS
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/filter/genCSV")
	public Response generateCSVFromLastestJSONFiltered(@QueryParam("collectionCode") String collectionCode, 
			@QueryParam("exportLimit") int exportLimit) throws UnknownHostException {
		return Response.ok()
				.allow("POST", "OPTIONS", "HEAD")
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "POST, OPTIONS, HEAD")
				.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
				.build();
	}

	@OPTIONS
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/filter/genTweetIds")
	public Response generateTweetsIDSCSVFromAllJSONFiltered(@QueryParam("collectionCode") String collectionCode,
			@DefaultValue("true") @QueryParam("downloadLimited") Boolean downloadLimited) 
					throws UnknownHostException {
		return Response.ok()
				.allow("POST", "OPTIONS", "HEAD")
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "POST, OPTIONS, HEAD")
				.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
				.build();
	}

	@Deprecated
	@OPTIONS
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/filter/getClassifiedTweets")
	public Response get_N_LatestClassifiedTweetsFiltered(@QueryParam("collectionCode") String collectionCode, 
			@QueryParam("exportLimit") int exportLimit, 
			@QueryParam("callback") String callback) throws UnknownHostException {
		return Response.ok()
				.allow("POST", "OPTIONS", "HEAD")
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "POST, OPTIONS, HEAD")
				.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
				.build();
	}

}