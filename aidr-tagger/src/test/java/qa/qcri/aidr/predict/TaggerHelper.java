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

import qa.qcri.aidr.common.code.JacksonWrapper;
import qa.qcri.aidr.predict.common.TaggerConfigurationProperty;
import qa.qcri.aidr.predict.common.TaggerConfigurator;
import qa.qcri.aidr.predict.util.ResponseWrapper;
import redis.clients.jedis.Jedis;


public class TaggerHelper {
    private static final Logger logger = Logger.getLogger(TaggerHelper.class);

    private Long sleepDuration;
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
	
    private static String[][] tweetWords = {
    							{"neutral", "night", "coal", "ink", "coffee", "w"},
    							{"clouds", "snow", "clear", "light", "neutral", "w"}
							};
    
    public TaggerHelper(Long crisisID, Long userID, Long attributeID, Long modelFamilyID, int nItems, boolean quiet) {
        this.crisisID = crisisID;
        this.userID = userID;
        this.attributeID = attributeID;
        this.modelFamilyID = modelFamilyID;
    	this.nItems = nItems;
		this.sleepDuration = 200L;
		objectMapper = JacksonWrapper.getObjectMapper();
		client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
		this.quiet = quiet;
    }

    public void setNItems(int nItems) {
		this.nItems = nItems;
	}
    
    // publish to redis queue
    public void startPublishing(boolean training, LabelCode labelCode) throws JsonParseException, JsonMappingException, IOException {
    	int tempItemCount;
    	Jedis redis = DataStore.getJedisConnection();
    	
        while (true) {
        	tempItemCount = nItems;
			while(tempItemCount > 0) {
				
				tempItemCount--;
				String tweetText = generateTweet(training, labelCode);
				
				redis.publish(
						TaggerConfigurator
								.getInstance()
								.getProperty(
										TaggerConfigurationProperty.REDIS_INPUT_CHANNEL), tweetText);
				
				if(quiet) {
					System.out.println("[" + System.currentTimeMillis() + "]" 
							+ tweetText.substring(0, Math.min(40, tweetText.length())));
				}
				
		        try {
					Thread.sleep(sleepDuration);
				} catch (InterruptedException e) {
					logger.error("Thread sleep interrupted"+Thread.currentThread().getName());
				}
		        
		        if(training && tempItemCount == 0) {
		        	webResource = client.target(TaggerConfigurator.getInstance().getProperty(TaggerConfigurationProperty.TAGGER_API) 
		    				+ "/document/unlabeled/count/" + crisisID);
					response =  webResource.request(MediaType.APPLICATION_JSON).get();
					
					jsonResponse = response.readEntity(String.class);
					
					Integer count = objectMapper.readValue(jsonResponse, Integer.class);
					if(count < nItems) {
						tempItemCount = 50;
					} else {
						nItems = count;
					}
				}
			}
			
			DataStore.close(redis);
			return;
		}
    }
    
    public void tagDocuments() throws JsonParseException, JsonMappingException, IOException {
    	
    	int tagCount = nItems;
    	int checkPoint = 200;
    	while(true) {
	    	
    		while(tagCount > 0) {
	    		webResource = client.target(TaggerConfigurator.getInstance().getProperty(TaggerConfigurationProperty.TRAINER_API_ROOT) + "/document/getassignabletask/" 
			    		+ TaggerTesterTest.TAGGER_TESTER_USER + "/" + crisisID + "/1");
				
				response =  webResource.request(MediaType.APPLICATION_JSON).get();
				
				jsonResponse = response.readEntity(String.class);
				
				JSONArray jsonArray = new JSONArray(jsonResponse);
				JSONObject jsonObject = jsonArray.getJSONObject(0);
				Integer documentID = (Integer) jsonObject.get("documentID");
				String data = jsonObject.getString("data");
				
				String tweetid = new JSONObject(data).getString("tweetid");
				
				org.json.JSONObject infoJson = new org.json.JSONObject();
				infoJson.put("crisisID", crisisID);
				infoJson.put("documentID", documentID);
				infoJson.put("aidrID", userID);
				infoJson.put("attributeID", attributeID);
				infoJson.put("category", tweetid);
				jsonObject.put("info", infoJson);
		        jsonArray.put(jsonObject);
		
				webResource = client.target(TaggerConfigurator.getInstance().getProperty(TaggerConfigurationProperty.TRAINER_API_ROOT) + "/taskanswer/save");
		
				logger.info("saveTaskAnswer - postData : " + jsonArray.toString());
		
				response = webResource.request(MediaType.APPLICATION_JSON)
						.post(Entity.json(jsonArray.toString()), Response.class);
				
				tagCount--;
				
				if(tagCount == checkPoint) {
					webResource = client.target(TaggerConfigurator.getInstance().getProperty(TaggerConfigurationProperty.TAGGER_API) + "/model/modelFamily/" + modelFamilyID);
					
					response = webResource.request(MediaType.APPLICATION_JSON).get();
					assertEquals(200, response.getStatus());
					
					jsonResponse = response.readEntity(String.class);
					ResponseWrapper responseWrapper = objectMapper.readValue(jsonResponse, ResponseWrapper.class);
					
				
					if(responseWrapper == null || responseWrapper.getModelHistoryWrapper() == null || responseWrapper.getModelHistoryWrapper().length == 0) {
						try {
							Thread.sleep(10000);
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
    
    private String generateTweet(boolean isTrainingTweet, LabelCode labelCode) {
    	String tweetText = "";
    	String tweeid;
		Random random = new Random();
		int tweetTextChoice = random.nextInt(2);
		String tweetWordSelected = "";
		StringBuffer stringBuffer = new StringBuffer();
		
		if(labelCode == null) {
			labelCode = LabelCode.values()[tweetTextChoice];
		}
		
		for(int i = 0; i < 30; i++) {
			int wordChoice = random.nextInt(6);
			tweetWordSelected = tweetWords[labelCode.ordinal()][wordChoice];
			if("w".equals(tweetWordSelected)) {
				tweetWordSelected = "w" +  String.format ("%04d", random.nextInt(10000));
			}
			stringBuffer.append(tweetWordSelected + " ");	
		}
		
		if(isTrainingTweet) {
			tweetText = "{\"user\" : {\"id\" : \"" + userID + "\"}, \"tweetid\":\"" + labelCode.name + "\", \"text\":\"" + stringBuffer.toString() + "\","
			+ " \"aidr\" : {\"crisis_code\":\"" + TaggerTesterTest.TAGGER_TESTER_CRISIS_CODE + "\", \"doctype\":\"twitter\", \"crisis_name\":\"" + TaggerTesterTest.TAGGER_TESTER_CRISIS_NAME + "\"}}";
		} else {
			tweetText = "{\"user\" : {\"id\" : \"" + userID + "\"}, \"text\":\"" + stringBuffer.toString() + "\","
					+ " \"aidr\" : {\"crisis_code\":\"" + TaggerTesterTest.TAGGER_TESTER_CRISIS_CODE + "\", \"doctype\":\"twitter\", \"crisis_name\":\"" + TaggerTesterTest.TAGGER_TESTER_CRISIS_NAME + "\"}}";
		}
    	return tweetText;
    }
    
    enum LabelCode {
        BLACK("black", "Black"),
        WHITE("white", "White"),
        DOES_NOT_APPLY("null", "Does Not Apply");

        private String name;
        private String code;
			

        private LabelCode(String code, String name) {
            this.name = name;
            this.code = code;
        }

        public String getName() {
            return name;
        }
        
        public String getCode() {
            return code;
        }

    }
    
}