/**
 * 
 */
package qa.qcri.aidr.predict;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import javax.ejb.EJB;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import qa.qcri.aidr.common.code.JacksonWrapper;
import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.CrisisDTO;
import qa.qcri.aidr.dbmanager.dto.CrisisTypeDTO;
import qa.qcri.aidr.dbmanager.dto.ModelFamilyDTO;
import qa.qcri.aidr.dbmanager.dto.NominalAttributeDTO;
import qa.qcri.aidr.dbmanager.dto.NominalLabelDTO;
import qa.qcri.aidr.dbmanager.dto.UsersDTO;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.CrisisResourceFacade;
import qa.qcri.aidr.predict.classification.nominal.NominalLabelBC;
import qa.qcri.aidr.predict.common.Serializer;
import qa.qcri.aidr.predict.common.TaggerConfigurationProperty;
import qa.qcri.aidr.predict.common.TaggerConfigurator;
import qa.qcri.aidr.predict.data.Document;
import qa.qcri.aidr.predict.util.ResponseWrapper;
import redis.clients.jedis.Jedis;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author Kushal
 *
 */
public class TaggerTesterTest {
	
	private static Logger logger = Logger.getLogger(TaggerTesterTest.class.getName());
	
	public static final String TAGGER_TESTER_CODE = "tagger_tester";
	public static final String TAGGER_TESTER_USER = "Tagger Tester User";
	public static final String TAGGER_TESTER_CRISIS_NAME = "Tagger Tester Crisis";
	public static final String TAGGER_TESTER_CRISIS_CODE = "tagger_tester";
	public static final String TAGGER_TESTER_NOMINAL_ATTRIBUTE_NAME = "Tagger Tester Classifier";
	public static final String TAGGER_TESTER_NOMINAL_ATTRIBUTE_CODE = "tagger_tester_classifier";
	public static final String TAGGER_TESTER_NOMINAL_ATTRIBUTE_DESC = "Tagger Tester Classifier Desc";
	public static final String remoteEJBJNDIName = TaggerConfigurator
			.getInstance().getProperty(
					TaggerConfigurationProperty.REMOTE_TASK_MANAGER_JNDI_NAME);
	
	private static TaggerConfigurator taggerConfig = (TaggerConfigurator) TaggerConfigurator.getInstance();
	private Long nominalAttributeId;
	private WebTarget webResource;	
	private String jsonResponse;
	private Response response;
	private ObjectMapper objectMapper;
	private Client client;
	private Integer crisisID;
	private Long userID;
	private Long modelFamilyID;
	private int tweetID;
	private Long nominalLabelID, nominalLabelID1, nominalLabelID2;
	CountDownLatch countDownLatch;
	private int whiteCount, blackCount;
	private Boolean quiet;
	private int itemsToTrain;
	private int itemsToTest;
	
	@EJB
	private CrisisResourceFacade crisisResourceFacade;
	
	@Before
	public void setUp() {
		objectMapper = JacksonWrapper.getObjectMapper();
		client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
		itemsToTrain = Integer.parseInt(System.getProperty("nitems-train"));
		
		itemsToTest = Integer.parseInt(System.getProperty("nitems-test"));
		quiet = Boolean.parseBoolean(System.getProperty("quiet"));
		String config = System.getProperty("config");
		
		if(org.apache.commons.lang3.StringUtils.isNotEmpty(config)){
			try (InputStream input = new FileInputStream(config);){
				Properties properties = new Properties();
				properties.load(input);
				for (Object property : properties.keySet()) {
					taggerConfig.setProperty(property.toString(), properties.get(property).toString());
				}
			} catch (IOException e) {
				logger.error("Error in reading config properties file: " + config, e);
			}
		}
	}
	
	@Test
	public void testTagger() throws JsonMappingException, IOException {
		
		
	/*	
		1. Make sure there is no data with code="tagger_tester" 
		in aidr-pridict database in case the tagger tester died abnormally in a previous run.
		If there is data, write a warning message,
		run the CLEANUP routine, and FAIL (forcing the user to run the tagger tester again)
		*/
		// fetch user
		webResource = client.target(taggerConfig.getProperty(TaggerConfigurationProperty.TAGGER_API)+"/user/" + TAGGER_TESTER_USER);

		response = webResource.request(MediaType.APPLICATION_JSON).get();
		assertEquals(200, response.getStatus());
		
		jsonResponse = response.readEntity(String.class);
		UsersDTO usersDTO = objectMapper.readValue(jsonResponse, UsersDTO.class);
		if(usersDTO != null) {
			userID = usersDTO.getUserID();
		}
		
		if(userID != null) {
			// fetch crisis
			webResource = client.target(taggerConfig.getProperty(TaggerConfigurationProperty.TAGGER_API) + "/crisis/code/"
					+ TAGGER_TESTER_CODE);
			
			response =  webResource.request(MediaType.APPLICATION_JSON).get();
			assertEquals(200, response.getStatus());
			jsonResponse = response.readEntity(String.class);
			HashMap<String,Object> result =
			        new ObjectMapper().readValue(jsonResponse, HashMap.class);
			
			
			if(result != null && result.get("crisisId") != null && result.get("crisisId") != new Integer(0)) {
				crisisID = (Integer) result.get("crisisId");
			}
			
			// get attribute id
			webResource = client.target(taggerConfig.getProperty(TaggerConfigurationProperty.TAGGER_API)+"/attribute/code/" + TAGGER_TESTER_NOMINAL_ATTRIBUTE_CODE);

			response = webResource.request(MediaType.APPLICATION_JSON).get();
			assertEquals(200, response.getStatus());
			
			jsonResponse = response.readEntity(String.class);
			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = (JsonObject) jsonParser.parse(jsonResponse);
			nominalAttributeId = jsonObject.get("nominalAttributeID").getAsLong();
			
			// fetch model family
			if(crisisID != null) {
				webResource = client.target(taggerConfig.getProperty(TaggerConfigurationProperty.TAGGER_API)+"/modelfamily/crisis/" + crisisID);
				response = webResource.request(MediaType.APPLICATION_JSON).get();
				assertEquals(200, response.getStatus());
				jsonResponse = response.readEntity(String.class);
				
				ResponseWrapper responseWrapper = objectMapper.readValue(jsonResponse, ResponseWrapper.class);
				
				if(responseWrapper != null && responseWrapper.getModelFamilies() != null  && responseWrapper.getModelFamilies().length > 0) {
					modelFamilyID = responseWrapper.getModelFamilies()[0].getModelFamilyId();
				}
			}
			
			Assert.fail("Setup data is already in database. Clean up required");
		}
		
		//2. Create a test user Tagger Tester User
		UsersDTO user = new UsersDTO();
		user.setName(TAGGER_TESTER_USER);
		user.setRole("normal");
		
		// create user
		webResource = client.target(taggerConfig.getProperty(TaggerConfigurationProperty.TAGGER_API)+"/user");

		response = webResource.request(
				MediaType.APPLICATION_JSON).post(Entity.json(user), Response.class);
		
		assertEquals(200, response.getStatus());
		jsonResponse = response.readEntity(String.class);
		
		// check for user created 
		webResource = client.target(taggerConfig.getProperty(TaggerConfigurationProperty.TAGGER_API) + "/user/"
				+ TAGGER_TESTER_USER);
		
		response =  webResource.request(MediaType.APPLICATION_JSON).get();
		assertEquals(200, response.getStatus());
		jsonResponse = response.readEntity(String.class);
		UsersDTO userDTO = objectMapper.readValue(jsonResponse, UsersDTO.class);
		
		if(userDTO == null || userDTO.getUserID() == null) {
			Assert.fail("User not created with name : " + TAGGER_TESTER_USER);
		}

		userID = userDTO.getUserID();
		
		//3. Create a collection (name="Tagger Tester Crisis", code="tagger_tester")
		
		CrisisDTO crisis = new CrisisDTO();
		crisis.setCode(TAGGER_TESTER_CRISIS_CODE);
		crisis.setName(TAGGER_TESTER_CRISIS_NAME);
		crisis.setCrisisTypeDTO(new CrisisTypeDTO(1100L, "Natural Hazard: Geophysical: Earthquake and/or Tsunami"));
		crisis.setUsersDTO(userDTO);
		webResource = client.target(taggerConfig.getProperty(TaggerConfigurationProperty.TAGGER_API)+"/crisis");

		response = webResource.request(
				MediaType.APPLICATION_JSON).post(Entity.json(crisis), Response.class);
		assertEquals(200, response.getStatus());
		jsonResponse = response.readEntity(String.class);
		
		// check for crisis created 
		webResource = client.target(taggerConfig.getProperty(TaggerConfigurationProperty.TAGGER_API) + "/crisis/code/"
				+ TAGGER_TESTER_CODE);
		
		response =  webResource.request(MediaType.APPLICATION_JSON).get();
		assertEquals(200, response.getStatus());
		jsonResponse = response.readEntity(String.class);
		HashMap<String,Object> map =
		        new ObjectMapper().readValue(jsonResponse, HashMap.class);
		
		
		if(map == null || map.get("crisisId") == null || map.get("crisisId") == new Integer(0)) {
			Assert.fail("Crisis not created with code : " + TAGGER_TESTER_CRISIS_CODE);
		}
		
		crisisID = (Integer) map.get("crisisId");
		
		
		//4. Create a classifier using the following steps:
				
		//a. Create an attribute (name="tagger_tester_classifier") 
	    
		NominalAttributeDTO attributeDTO = new NominalAttributeDTO();
		attributeDTO.setUsersDTO(userDTO);
		attributeDTO.setName(TAGGER_TESTER_NOMINAL_ATTRIBUTE_NAME);
		attributeDTO.setCode(TAGGER_TESTER_NOMINAL_ATTRIBUTE_CODE);
		attributeDTO.setDescription(TAGGER_TESTER_NOMINAL_ATTRIBUTE_DESC);
		webResource = client.target(taggerConfig.getProperty(TaggerConfigurationProperty.TAGGER_API)+"/attribute");

		response = webResource.request(
				MediaType.APPLICATION_JSON).post(Entity.json(attributeDTO), Response.class);
		assertEquals(200, response.getStatus());
		
		jsonResponse = response.readEntity(String.class);
		attributeDTO = objectMapper.readValue(jsonResponse, NominalAttributeDTO.class);
		
		if(attributeDTO == null || attributeDTO.getNominalAttributeId() == null) {
			Assert.fail("NominalAttribute not created with code : " + TAGGER_TESTER_NOMINAL_ATTRIBUTE_CODE);
		} else {
			nominalAttributeId = attributeDTO.getNominalAttributeId();
		}
		
		//b. Create three labels white, black, null
		
		try {
			// white
			NominalLabelDTO nominalLabelDTO = new NominalLabelDTO();
			nominalLabelDTO.setNominalAttributeDTO(attributeDTO);
			nominalLabelDTO.setName("White");
			nominalLabelDTO.setNominalLabelCode("white");
			nominalLabelDTO.setDescription("White test desc");
			nominalLabelDTO.setSequence(101);
			
			webResource = client.target(taggerConfig.getProperty(TaggerConfigurationProperty.TAGGER_API)+"/label");
			response = webResource.request(
					MediaType.APPLICATION_JSON).post(Entity.json(nominalLabelDTO), Response.class);
			assertEquals(200, response.getStatus());
			jsonResponse = response.readEntity(String.class);
			nominalLabelDTO = objectMapper.readValue(jsonResponse, NominalLabelDTO.class);
			
			if(nominalLabelDTO == null || nominalLabelDTO.getNominalLabelId() == null) {
				Assert.fail("NominalLabel not created with code : white");
			} else {
				nominalLabelID = nominalLabelDTO.getNominalLabelId();
			}
			
			// black
			NominalLabelDTO nominalLabelDTO1 = new NominalLabelDTO();
			nominalLabelDTO1.setNominalAttributeDTO(attributeDTO);
			nominalLabelDTO1.setName("Black");
			nominalLabelDTO1.setNominalLabelCode("black");
			nominalLabelDTO1.setDescription("Black test desc");
			nominalLabelDTO1.setSequence(101);
			
			webResource = client.target(taggerConfig.getProperty(TaggerConfigurationProperty.TAGGER_API)+"/label");
			response = webResource.request(
					MediaType.APPLICATION_JSON).post(Entity.json(nominalLabelDTO1), Response.class);
			assertEquals(200, response.getStatus());
			jsonResponse = response.readEntity(String.class);
			nominalLabelDTO1 = objectMapper.readValue(jsonResponse, NominalLabelDTO.class);
			
			if(nominalLabelDTO1 == null || nominalLabelDTO.getNominalLabelId() == null) {
				Assert.fail("NominalLabel not created with code : black");
			} else {
				nominalLabelID1 = nominalLabelDTO1.getNominalLabelId();
			}
			
			
			// does not apply
			NominalLabelDTO nominalLabelDTO2 = new NominalLabelDTO();
			nominalLabelDTO2.setNominalAttributeDTO(attributeDTO);
			nominalLabelDTO2.setName("Does not apply");
			nominalLabelDTO2.setNominalLabelCode("null");
			nominalLabelDTO2.setDescription("Does not apply test desc");
			nominalLabelDTO2.setSequence(101);
			
			webResource = client.target(taggerConfig.getProperty(TaggerConfigurationProperty.TAGGER_API)+"/label");
			response = webResource.request(
					MediaType.APPLICATION_JSON).post(Entity.json(nominalLabelDTO2), Response.class);
			assertEquals(200, response.getStatus());
			jsonResponse = response.readEntity(String.class);
			nominalLabelDTO2 = objectMapper.readValue(jsonResponse, NominalLabelDTO.class);
		
			if(nominalLabelDTO2 == null || nominalLabelDTO.getNominalLabelId() == null) {
				Assert.fail("NominalLabel not created with code : Does not apply");
			} else {
				nominalLabelID2 = nominalLabelDTO2.getNominalLabelId();
			}
			
			
		} catch (PropertyNotSetException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			Assert.fail("Failed to create NominalLabel :" + e.getMessage());
		}
		
		
	   //5. Create a ModelFamily 
		ModelFamilyDTO modelFamilyDTO = new ModelFamilyDTO();
		CrisisDTO crisisDTO = new CrisisDTO();
		crisisDTO.setCrisisID(new Long (crisisID.intValue()));
		modelFamilyDTO.setCrisisDTO(crisisDTO);
		modelFamilyDTO.setNominalAttributeDTO(attributeDTO);
		modelFamilyDTO.setIsActive(true);
		webResource = client.target(taggerConfig.getProperty(TaggerConfigurationProperty.TAGGER_API)+"/modelfamily");
		response = webResource.request(
				MediaType.APPLICATION_JSON).post(Entity.json(modelFamilyDTO), Response.class);
		assertEquals(200, response.getStatus());
		jsonResponse = response.readEntity(String.class);
		
		JsonParser parser = new JsonParser();
		JsonObject json = (JsonObject) parser.parse(jsonResponse);
		modelFamilyID = json.get("entityID").getAsLong();
		
		if(modelFamilyID == -1) {
			Assert.fail("Failed to create ModelFamily with crisis : " + crisisID + " attribute : " + attributeDTO.getNominalAttributeId());
		}
	
	    // 6.  Push training data to redis queue
		 
		crisisDTO.setCode(TAGGER_TESTER_CRISIS_CODE);
		crisisDTO.setName(TAGGER_TESTER_CRISIS_NAME);
	    final TaggerHelper helper = new TaggerHelper(new Long(crisisID), userID, attributeDTO.getNominalAttributeId(), modelFamilyID, itemsToTrain, quiet);
	    helper.startPublishing(true, false); // isBlack is false as it won't have any impact since the training tweets are being generated
	    
	    // 7. tag training data set : human tagging
	    helper.tagDocuments();
	    
	    new Thread(new OutputMatcherProcess()).start();
	    
	   //8.  push white items - unlabeled
		helper.setNItems(itemsToTest);
		helper.startPublishing(false, false);

	  
	   try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			logger.error("Thread sleep interrupted"+Thread.currentThread().getName());
		}
	   
	   if(whiteCount < (int)(itemsToTest*(80/100.0f))) {
			Assert.fail("Failed to tagged documents with label : white : " + whiteCount);
		}	
	   
	    // push black items -unlabeled
	    helper.setNItems(itemsToTest);
		helper.startPublishing(false, true);

	    try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			logger.error("Thread sleep interrupted"+Thread.currentThread().getName());
		}
	    
	    if(blackCount < (int)(itemsToTest*(80/100.0f))) {
			Assert.fail("Failed to tagged documents with label : black : " + blackCount);
		}	    
	    
	}
	
	@After
	public void tearDown() {
		
		// delete complete data related to modelFamily, model, modelNominalLabel
		if(modelFamilyID != null) {
			webResource = client.target(taggerConfig.getProperty(TaggerConfigurationProperty.TAGGER_API)+"/modelfamily/" + modelFamilyID);
			response =  webResource.request(MediaType.APPLICATION_JSON).delete();
		}
		
		// delete nominal attribute data : nominalAttribute, nominalLabel and documentNominalLabel
		if(nominalAttributeId != null) {
			webResource = client.target(taggerConfig.getProperty(TaggerConfigurationProperty.TAGGER_API)+"/attribute/" + nominalAttributeId);
			response =  webResource.request(MediaType.APPLICATION_JSON).delete();
		}
		
		if(crisisID != null) {
			webResource = client.target(taggerConfig.getProperty(TaggerConfigurationProperty.TAGGER_API)+"/document/delete/" + crisisID);
			response =  webResource.request(MediaType.APPLICATION_JSON).delete();
		}
		
		if(crisisID != null) {
			webResource = client.target(taggerConfig.getProperty(TaggerConfigurationProperty.TAGGER_API)+"/crisis/" + crisisID);
			response =  webResource.request(MediaType.APPLICATION_JSON).delete();
		}
		
		if(userID != null) {
			webResource = client.target(taggerConfig.getProperty(TaggerConfigurationProperty.TAGGER_API)+"/user/" + userID);
			response =  webResource.request(MediaType.APPLICATION_JSON).delete();
		}
		
	}
	
	
	class OutputMatcherProcess implements Runnable {

		@Override
		public void run() {
			while (true) {
				Jedis jedis = DataStore.getJedisConnection();
				List<byte[]> byteDoc = jedis
						.blpop(60,
								TaggerConfigurator
										.getInstance()
										.getProperty(
												TaggerConfigurationProperty.REDIS_FOR_OUTPUT_QUEUE)
										.getBytes());
				DataStore.close(jedis);
		        try {
		        	if(byteDoc != null) {
		            	Document document = Serializer.deserialize(byteDoc.get(1));
		            	ArrayList<NominalLabelBC> nominalLabels = document.getLabels(NominalLabelBC.class);
		            	for (NominalLabelBC nominalLabelBC : nominalLabels) {
							if(nominalLabelBC.getNominalLabelID() == nominalLabelID.intValue()){
								whiteCount++;
							} else if(nominalLabelBC.getNominalLabelID() == nominalLabelID.intValue()) {
								blackCount++;
							}
		            	}
		        	}	
		        } catch (ClassNotFoundException | IOException e) {
		            logger.error("Exception when parsing document set from stream.");
		           // logger.error(elog.toStringException(e));
		        }
			}
		}
		
	}
 
}
