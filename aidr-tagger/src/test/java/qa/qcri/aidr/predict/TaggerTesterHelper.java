package qa.qcri.aidr.predict;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Random;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;

import qa.qcri.aidr.common.code.JacksonWrapper;
import qa.qcri.aidr.predict.common.TaggerConfigurationProperty;
import qa.qcri.aidr.predict.common.TaggerConfigurator;
import qa.qcri.aidr.predict.util.ResponseWrapper;
import redis.clients.jedis.Jedis;

public class TaggerTesterHelper {
	private static final Logger logger = Logger
			.getLogger(TaggerTesterHelper.class);

	private static final Long SLEEP_DURATION_BETWEEN_TWEETS_PUBLISH_IN_MILLIS = 200L;
	private static final int  TWEET_TEXT_WORD_COUNT = 30;
	private static final Long MODEL_CREATION_WAIT_TIME_IN_MILLIS = 10000L;
	private static final int TAGGED_TWEET_COUNT_CHECKPOINT_FOR_MODEL_CREATION = 200;
	private static final int ADDITIONAL_TWEET_COUNT_TO_PUBLISH = 50;
	
	private int nItems;
	private Long crisisID;
	private long userID;
	private long attributeID;
	private long modelFamilyID;
	private WebTarget webResource;
	private String jsonResponse;
	private Response response;
	private ObjectMapper objectMapper;
	private Client client;
	private boolean quiet;

	public TaggerTesterHelper(Long crisisID, Long userID, Long attributeID,
			Long modelFamilyID, int nItems, boolean quiet) {
		this.crisisID = crisisID;
		this.userID = userID;
		this.attributeID = attributeID;
		this.modelFamilyID = modelFamilyID;
		this.nItems = nItems;
		objectMapper = JacksonWrapper.getObjectMapper();
		client = ClientBuilder.newBuilder().register(JacksonFeature.class)
				.build();
		this.quiet = quiet;
	}

	public void setNItems(int nItems) {
		this.nItems = nItems;
	}

	// publish to redis queue
	public void startPublishing(boolean training, LabelCode labelCode)
			throws JsonParseException, JsonMappingException, IOException {
		int tempItemCount;
		Jedis redis = DataStore.getJedisConnection();
		while (true) {
			tempItemCount = nItems;
			while (tempItemCount > 0) {

				String tweet = generateTweet(training, labelCode, tempItemCount);
				redis.publish(
						TaggerConfigurator
								.getInstance()
								.getProperty(
										TaggerConfigurationProperty.REDIS_INPUT_CHANNEL),
						tweet);

				tempItemCount--;

				try {
					Thread.sleep(SLEEP_DURATION_BETWEEN_TWEETS_PUBLISH_IN_MILLIS);
				} catch (InterruptedException e) {
					logger.error("Thread sleep interrupted"
							+ Thread.currentThread().getName());
				}

				if (training && tempItemCount == 0) {
					webResource = client.target(TaggerConfigurator
							.getInstance().getProperty(
									TaggerConfigurationProperty.TAGGER_API)
							+ "/document/unlabeled/count/" + crisisID);
					response = webResource.request(MediaType.APPLICATION_JSON)
							.get();
					assertEquals(200, response.getStatus());
					jsonResponse = response.readEntity(String.class);

					Integer count = objectMapper.readValue(jsonResponse,
							Integer.class);
					System.out.println("Unlabeled count:" + count);
					Assert.assertFalse("Unable to insert documents." , count == 0);
					if (count < nItems) {
						tempItemCount = ADDITIONAL_TWEET_COUNT_TO_PUBLISH;
					} else {
						nItems = count;
					}
				}
			}

			DataStore.close(redis);
			return;
		}
	}

	public void tagDocuments() throws JsonParseException, JsonMappingException,
			IOException {

		int tagCount = nItems;
		int checkPoint = TAGGED_TWEET_COUNT_CHECKPOINT_FOR_MODEL_CREATION;
		while (true) {

			while (tagCount > 0) {
				webResource = client.target(TaggerConfigurator.getInstance()
						.getProperty(
								TaggerConfigurationProperty.TRAINER_API_ROOT)
						+ "/document/getassignabletask/"
						+ TaggerTesterTest.TAGGER_TESTER_USER
						+ "/"
						+ crisisID
						+ "/1");

				response = webResource.request(MediaType.APPLICATION_JSON)
						.get();
				assertEquals(200, response.getStatus());
				jsonResponse = response.readEntity(String.class);

				JSONArray jsonArray = new JSONArray(jsonResponse);
				if (jsonArray.length() == 0) {
					Assert.fail("No document to tag for crisis : " + crisisID);
				}

				JSONObject jsonObject = jsonArray.getJSONObject(0);
				Integer documentID = (Integer) jsonObject.get("documentID");
				String data = jsonObject.getString("data");

				String tweetid = new JSONObject(data).getString("tweetid");

				org.json.JSONObject infoJson = new org.json.JSONObject();
				infoJson.put("crisisID", crisisID);
				infoJson.put("documentID", documentID);
				infoJson.put("aidrID", userID);
				infoJson.put("attributeID", attributeID);
				infoJson.put("category",
						tweetid.substring(0, tweetid.indexOf("-")));
				jsonObject.put("info", infoJson);
				jsonArray.put(jsonObject);

				webResource = client.target(TaggerConfigurator.getInstance()
						.getProperty(
								TaggerConfigurationProperty.TRAINER_API_ROOT)
						+ "/taskanswer/save");

				logger.info("saveTaskAnswer - postData : "
						+ jsonArray.toString());

				response = webResource
						.request(MediaType.APPLICATION_JSON)
						.post(Entity.json(jsonArray.toString()), Response.class);

				assertEquals(204, response.getStatus());

				if (!quiet) {
					System.out.println("Labelled tweet : " + tweetid
							+ " with label : "
							+ tweetid.substring(0, tweetid.indexOf("-")));
				}
				tagCount--;

				if (tagCount == checkPoint) {
					webResource = client.target(TaggerConfigurator
							.getInstance().getProperty(
									TaggerConfigurationProperty.TAGGER_API)
							+ "/model/modelFamily/" + modelFamilyID);

					response = webResource.request(MediaType.APPLICATION_JSON)
							.get();
					assertEquals(200, response.getStatus());

					jsonResponse = response.readEntity(String.class);
					ResponseWrapper responseWrapper = objectMapper.readValue(
							jsonResponse, ResponseWrapper.class);

					if (responseWrapper == null
							|| responseWrapper.getModelHistoryWrapper() == null
							|| responseWrapper.getModelHistoryWrapper().length == 0) {
						try {
							Thread.sleep(MODEL_CREATION_WAIT_TIME_IN_MILLIS);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						checkPoint -= 50;
					} else {
						return;
					}
				}
			}

			return;
		}
	}

	private String generateTweet(boolean isTrainingTweet, LabelCode labelCode,
			int tweetIndex) {
		Random random = new Random();
		int tweetTextChoice = random.nextInt(2);
		String tweetWordSelected = "";
		StringBuffer stringBuffer = new StringBuffer();

		if (labelCode == null) {
			labelCode = LabelCode.values()[tweetTextChoice];
		}

		if (!quiet && isTrainingTweet) {
			System.out.println("Sent tweet : " + labelCode.getCode() + "-"
					+ tweetIndex);
		}

		for (int i = 0; i < TWEET_TEXT_WORD_COUNT; i++) {
			int wordChoice = random.nextInt(labelCode.getTweetWords().length);
			tweetWordSelected = labelCode.getTweetWords()[wordChoice];

			if ("w".equals(tweetWordSelected)) {
				tweetWordSelected = "w"
						+ String.format("%02d", random.nextInt(100));
			}
			stringBuffer.append(tweetWordSelected + " ");
		}

		// preapare tweet
		JSONObject tweetObject = new JSONObject();

		JSONObject user = new JSONObject();
		user.put("id", userID);

		JSONObject aidr = new JSONObject();
		aidr.put("crisis_code", TaggerTesterTest.TAGGER_TESTER_CRISIS_CODE);
		aidr.put("doctype", "twitter");
		aidr.put("crisis_name", TaggerTesterTest.TAGGER_TESTER_CRISIS_NAME);

		tweetObject.put("user", user);
		tweetObject.put("tweetid", labelCode.getCode() + "-" + tweetIndex);
		tweetObject.put("text", stringBuffer.toString());
		tweetObject.put("aidr", aidr);

		return tweetObject.toString();
	}

	enum LabelCode {

		BLACK("black", "Black", new String[] { "neutral", "night", "coal",
				"ink", "coffee", "w" }), WHITE("white", "White", new String[] {
				"clouds", "snow", "clear", "light", "neutral", "w" }), DOES_NOT_APPLY(
				"null", "Does Not Apply", new String[] { "none" });

		private String name;
		private String code;
		private String[] tweetWords;

		private LabelCode(String code, String name, String[] tweetWords) {
			this.name = name;
			this.code = code;
			this.tweetWords = tweetWords;
		}

		public String[] getTweetWords() {
			return tweetWords;
		}

		public String getName() {
			return name;
		}

		public String getCode() {
			return code;
		}

	}

}