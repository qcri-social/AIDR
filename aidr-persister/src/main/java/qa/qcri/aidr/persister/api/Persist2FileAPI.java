package qa.qcri.aidr.persister.api;

import java.net.UnknownHostException;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.minidev.json.JSONObject;

import org.apache.log4j.Logger;

import qa.qcri.aidr.common.values.DownloadType;
import qa.qcri.aidr.dbmanager.dto.taggerapi.HumanLabeledDocumentListWrapper;
import qa.qcri.aidr.common.filter.DeserializeFilters;
import qa.qcri.aidr.common.filter.JsonQueryList;
import qa.qcri.aidr.utils.DownloadJsonType;
import qa.qcri.aidr.utils.JsonDeserializer;
import qa.qcri.aidr.utils.PersisterConfigurationProperty;
import qa.qcri.aidr.utils.PersisterConfigurator;
import qa.qcri.aidr.utils.ResultStatus;

@Path("/listPersister")
public class Persist2FileAPI {
	private static Logger logger = Logger.getLogger(Persist2FileAPI.class.getName());

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
	public Response generateCSVFromListFiltered(HumanLabeledDocumentListWrapper postBody,
			@QueryParam("collectionCode") String collectionCode, 
			@QueryParam("exportLimit") Integer exportLimit,
			@QueryParam("userName") String userName) throws UnknownHostException {

		try {
			DeserializeFilters des = new DeserializeFilters();
			System.out.println("constraints string received = " + postBody.getQueryString());
			JsonQueryList queryList = des.deserializeConstraints(postBody.getQueryString());
			JsonDeserializer jsonD = new JsonDeserializer();

			logger.info("received request for collection: " + collectionCode);
			if (queryList != null) {
				logger.info(collectionCode + ": received constraints = " + queryList.toString());
			} else {
				logger.info(collectionCode + ": received constraints = " + queryList);
			}

			if (null == exportLimit) exportLimit = Integer.parseInt(PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.TWEETS_EXPORT_LIMIT_100K));
			JSONObject obj = new JSONObject();
			String fileName = jsonD.generateClassifiedList2CSV_100K_BasedOnTweetCountFiltered(collectionCode, exportLimit, queryList, postBody.getDtoList(), userName);
			logger.info("Generated fileName = " + fileName);

			obj.putAll(ResultStatus.getUIWrapper(collectionCode, PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.PERSISTER_CHANGE_NOTIFY_MSG), fileName, true));
			logger.info("done processing request for collection: " + collectionCode + ", returning created file: " + fileName);

			logger.info("Returning JSON object: " + ResultStatus.getUIWrapper(collectionCode, PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.PERSISTER_CHANGE_NOTIFY_MSG), fileName, true));
			return Response.ok(obj.toJSONString())
					.allow("POST", "OPTIONS", "HEAD")
					.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Credentials", "true")
					.header("Access-Control-Allow-Methods", "POST, OPTIONS, HEAD")
					.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
					.build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.ok(PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.STATUS_CODE_ERROR)).build();
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
	@Path("/filter/genCSVTweetIds")
	public Response generateTweetsIDSCSVFromListFiltered(HumanLabeledDocumentListWrapper postBody,
			@QueryParam("collectionCode") String collectionCode,
			@DefaultValue("true") @QueryParam("downloadLimited") Boolean downloadLimited,
			@QueryParam("userName") String userName) 
					throws UnknownHostException {

		try {
			DeserializeFilters des = new DeserializeFilters();
			JsonQueryList queryList = des.deserializeConstraints(postBody.getQueryString());

			JsonDeserializer jsonD = new JsonDeserializer();
			logger.info("received request for collection: " + collectionCode);
			if (queryList != null) {
				logger.info(collectionCode + ": received constraints = " + queryList.toString());
			} else {
				logger.info(collectionCode + ": received constraints = " + queryList);
			}

			JSONObject obj = new JSONObject();
			Map<String, Object> result = jsonD.generateClassifiedList2TweetIdsCSVFiltered(collectionCode, queryList, downloadLimited, postBody.getDtoList(), userName);
			String fileName = result.get("fileName") != null ? result.get("fileName").toString() : null;
			if ((Integer) result.get("count") < Integer.parseInt(PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.DEFAULT_TWEETID_VOLUME_LIMIT))) {
				obj.putAll(ResultStatus.getUIWrapper(collectionCode, PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.PERSISTER_CHANGE_NOTIFY_MSG), fileName, true));
				logger.info("Returning JSON object: " + ResultStatus.getUIWrapper(collectionCode, PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.PERSISTER_CHANGE_NOTIFY_MSG), fileName, true));
				return Response.ok(obj.toJSONString())
						.allow("POST", "OPTIONS", "HEAD")
						.header("Access-Control-Allow-Origin", "*")
						.header("Access-Control-Allow-Credentials", "true")
						.header("Access-Control-Allow-Methods", "POST, OPTIONS, HEAD")
						.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
						.build();
			} else {
				obj.putAll(ResultStatus
						.getUIWrapper(
								collectionCode,
								PersisterConfigurator
										.getInstance()
										.getProperty(
												PersisterConfigurationProperty.TWEET_DOWNLOAD_LIMIT_MSG_PREFIX)
										+ PersisterConfigurator
												.getInstance()
												.getProperty(
														PersisterConfigurationProperty.DEFAULT_TWEETID_VOLUME_LIMIT)
										+ PersisterConfigurator
												.getInstance()
												.getProperty(
														PersisterConfigurationProperty.TWEET_DOWNLOAD_LIMIT_MSG_SUFFIX)
										+ PersisterConfigurator
												.getInstance()
												.getProperty(
														PersisterConfigurationProperty.PERSISTER_CHANGE_NOTIFY_MSG),
								fileName, true));
				logger.info("Returning JSON object: "
						+ ResultStatus
								.getUIWrapper(
										collectionCode,
										PersisterConfigurator
												.getInstance()
												.getProperty(
														PersisterConfigurationProperty.TWEET_DOWNLOAD_LIMIT_MSG_PREFIX)
												+ PersisterConfigurator
														.getInstance()
														.getProperty(
																PersisterConfigurationProperty.DEFAULT_TWEETID_VOLUME_LIMIT)
												+ PersisterConfigurator
														.getInstance()
														.getProperty(
																PersisterConfigurationProperty.TWEET_DOWNLOAD_LIMIT_MSG_SUFFIX)
												+ PersisterConfigurator
														.getInstance()
														.getProperty(
																PersisterConfigurationProperty.PERSISTER_CHANGE_NOTIFY_MSG),
										fileName, true));
				return Response.ok(obj.toJSONString())
						.allow("POST", "OPTIONS", "HEAD")
						.header("Access-Control-Allow-Origin", "*")
						.header("Access-Control-Allow-Credentials", "true")
						.header("Access-Control-Allow-Methods", "POST, OPTIONS, HEAD")
						.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
						.build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.ok(
					PersisterConfigurator.getInstance().getProperty(
							PersisterConfigurationProperty.STATUS_CODE_ERROR))
					.build();
	} 
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/filter/genJson")
	public Response generateJSONFromListFiltered(HumanLabeledDocumentListWrapper postBody,
			@QueryParam("collectionCode") String collectionCode, 
			@QueryParam("exportLimit") Integer exportLimit,
			@DefaultValue(DownloadType.TEXT_JSON) @QueryParam("jsonType") String jsonType,
			@QueryParam("userName") String userName)  throws UnknownHostException {
		logger.debug("In list-persister genJson");
		logger.info("Received request for collection: " + collectionCode + " with jsonType = " + jsonType);
		
		try {
			DeserializeFilters des = new DeserializeFilters();
			JsonQueryList queryList = des.deserializeConstraints(postBody.getQueryString());
			if (queryList != null) {
				logger.info(collectionCode + ": received constraints = " + queryList.toString());
			} else {
				logger.info(collectionCode + ": received constraints = " + queryList);
			}

			JsonDeserializer jsonD = new JsonDeserializer();
			if (null == exportLimit) exportLimit = Integer.parseInt(PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.TWEETS_EXPORT_LIMIT_100K));		// Koushik: added to override user specs

			String fileName = jsonD.generateClassifiedList2JSON_100K_BasedOnTweetCountFiltered(collectionCode, exportLimit, queryList, 
					DownloadJsonType.getDownloadJsonTypeFromString(jsonType), postBody.getDtoList(), userName);

			logger.info("Done processing request for collection: " + collectionCode + ", returning created file: " + fileName);

			JSONObject obj = new JSONObject();
			obj.putAll(ResultStatus.getUIWrapper(collectionCode, PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.PERSISTER_CHANGE_NOTIFY_MSG), fileName, true));
			logger.info("Returning JSON object: " + ResultStatus.getUIWrapper(collectionCode, PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.PERSISTER_CHANGE_NOTIFY_MSG), fileName, true));
			return Response.ok(obj.toJSONString()).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.ok(PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.STATUS_CODE_ERROR)).build();
		} 
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/filter/genJsonTweetIds")
	public Response generateTweetsIDSJSONFromListFiltered(HumanLabeledDocumentListWrapper postBody,
			@QueryParam("collectionCode") String collectionCode, 
			@DefaultValue("true") @QueryParam("downloadLimited") Boolean downloadLimited,
			@DefaultValue(DownloadType.TEXT_JSON) @QueryParam("jsonType") String jsonType,
			@QueryParam("userName") String userName)  throws UnknownHostException {
		
		try {
			logger.debug("In list-persister genJsonTweetIds");
			logger.info("Received request for collection: " + collectionCode + " with jsonType = " + jsonType);
			DeserializeFilters des = new DeserializeFilters();
			JsonQueryList queryList = des.deserializeConstraints(postBody.getQueryString());
			if (queryList != null) {
				logger.info(collectionCode + ": received constraints = " + queryList.toString());
			} else {
				logger.info(collectionCode + ": received constraints = " + queryList);
			}
			JsonDeserializer jsonD = new JsonDeserializer();
			Map<String, Object> result = jsonD.generateClassifiedList2TweetIdsJSONFiltered(collectionCode, downloadLimited, queryList, 
					DownloadJsonType.getDownloadJsonTypeFromString(jsonType), postBody.getDtoList(), userName);
			String fileName = result.get("fileName") != null ? result.get("fileName").toString() : null;

			JSONObject obj = new JSONObject();
			if ((Integer) result.get("count") < Integer.parseInt(PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.DEFAULT_TWEETID_VOLUME_LIMIT))) {
				obj.putAll(ResultStatus.getUIWrapper(collectionCode, PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.PERSISTER_CHANGE_NOTIFY_MSG), fileName, true));
				logger.info("Returning JSON object: " + ResultStatus.getUIWrapper(collectionCode, PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.PERSISTER_CHANGE_NOTIFY_MSG), fileName, true));
				return Response.ok(obj.toJSONString())
						.allow("POST", "OPTIONS", "HEAD")
						.header("Access-Control-Allow-Origin", "*")
						.header("Access-Control-Allow-Credentials", "true")
						.header("Access-Control-Allow-Methods", "POST, OPTIONS, HEAD")
						.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
						.build();
			} else {
				obj.putAll(ResultStatus
						.getUIWrapper(
								collectionCode,
								PersisterConfigurator
										.getInstance()
										.getProperty(
												PersisterConfigurationProperty.TWEET_DOWNLOAD_LIMIT_MSG_PREFIX)
										+ PersisterConfigurator
												.getInstance()
												.getProperty(
														PersisterConfigurationProperty.DEFAULT_TWEETID_VOLUME_LIMIT)
										+ PersisterConfigurator
												.getInstance()
												.getProperty(
														PersisterConfigurationProperty.TWEET_DOWNLOAD_LIMIT_MSG_SUFFIX)
										+ PersisterConfigurator
												.getInstance()
												.getProperty(
														PersisterConfigurationProperty.PERSISTER_CHANGE_NOTIFY_MSG),
								fileName, true));
				logger.info("Returning JSON object: "
						+ ResultStatus
								.getUIWrapper(
										collectionCode,
										PersisterConfigurator
												.getInstance()
												.getProperty(
														PersisterConfigurationProperty.TWEET_DOWNLOAD_LIMIT_MSG_PREFIX)
												+ PersisterConfigurator
														.getInstance()
														.getProperty(
																PersisterConfigurationProperty.DEFAULT_TWEETID_VOLUME_LIMIT)
												+ PersisterConfigurator
														.getInstance()
														.getProperty(
																PersisterConfigurationProperty.TWEET_DOWNLOAD_LIMIT_MSG_SUFFIX)
												+ PersisterConfigurator
														.getInstance()
														.getProperty(
																PersisterConfigurationProperty.PERSISTER_CHANGE_NOTIFY_MSG),
										fileName, true));
				return Response.ok(obj.toJSONString())
						.allow("POST", "OPTIONS", "HEAD")
						.header("Access-Control-Allow-Origin", "*")
						.header("Access-Control-Allow-Credentials", "true")
						.header("Access-Control-Allow-Methods", "POST, OPTIONS, HEAD")
						.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
						.build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.ok(PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.STATUS_CODE_ERROR)).build();
		} 
	}

	@GET
	@Produces("application/json")
	@Path("/ping")
	public Response ping() throws UnknownHostException {
		String response = "{\"application\":\"aidr-listPersister\", \"status\":\"RUNNING\"}";
		return Response.ok(response).build();
	}
}
