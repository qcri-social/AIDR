/**
 * 
 */
package qa.qcri.aidr.collector.collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import qa.qcri.aidr.collector.beans.CollectionTask;
import qa.qcri.aidr.collector.utils.CollectorConfigurationProperty;
import qa.qcri.aidr.collector.utils.CollectorConfigurator;
import qa.qcri.aidr.collector.utils.CollectorSubscriber;
import qa.qcri.aidr.common.code.JacksonWrapper;
import redis.clients.jedis.Jedis;

import com.google.common.io.Files;

/**
 * @author Kushal
 *
 */
public class CollectorTesterTest {
	private static Logger logger = Logger.getLogger(CollectorTesterTest.class.getName());
	private CollectionTask collectionTask;
	private Long time;
	private Boolean quiet;
	private static CollectorConfigurator configProperties = CollectorConfigurator.getInstance();
	private static String BASE_URI =null;

	@Before
	public void setUp() throws Exception {
		time = Long.parseLong(System.getProperty("time"));
		quiet = Boolean.parseBoolean(System.getProperty("quiet"));
		String config = System.getProperty("config");
		Properties properties;
		if(StringUtils.isNotEmpty(config)){
			try (InputStream input = new FileInputStream(config);){
				properties = new Properties();
				properties.load(input);
				for (Object property : properties.keySet()) {
					configProperties.setProperty(property.toString(), properties.get(property).toString());
				}
			} catch (IOException e) {
				logger.error("Error in reading config properties file: " + config, e);
			}
		}
		BASE_URI = configProperties.getProperty(CollectorConfigurationProperty.COLLECTOR_REST_URI);
		String collectionTaskPath = System.getProperty("collectionTask");
		if(StringUtils.isNotEmpty(collectionTaskPath)){
			String fileExtension = Files.getFileExtension(collectionTaskPath);
			if(fileExtension.equals("properties")){
				try (InputStream input = new FileInputStream(collectionTaskPath);){
					properties = new Properties();
					properties.load(input);
					collectionTask = new CollectionTask(properties);
				} catch (IOException e) {
					logger.error("Error in reading Collection Task properties file: " + collectionTaskPath, e);
				}
			}
			else{
				fail("Extension of collectionTask file does not match. It should be .properties.");
			}
		}
		else{
			fail("Collection task file is not present");
		}
	}

	@Test
	public void testCollector() throws JsonParseException, JsonMappingException, IOException{
		Response response;
		final String collectionCode = collectionTask.getCollectionCode();
		final String CHANNEL_NAME = configProperties.getProperty(CollectorConfigurationProperty.COLLECTOR_CHANNEL)+"."+collectionCode;

		final CollectorSubscriber collectorSubscriber;
		CollectionTask result;
		Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
		ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();
		WebTarget webResource;	
		String jsonResponse;

		//Subscribing to Redis
		JedisPublisher jedisPublisher = JedisPublisher.newInstance();
		final Jedis subscriberJedis = jedisPublisher.getDelegate();
		collectorSubscriber = new CollectorSubscriber(quiet);
		try{
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						logger.info("Subscribing to Redis channel "+CHANNEL_NAME);
						subscriberJedis.subscribe(collectorSubscriber, CHANNEL_NAME);
					} catch (Exception e) {
						logger.error("Subscribing to Redis channel "+CHANNEL_NAME+" failed.", e);
					}
				}
			}).start();

			//Attempting to stop the task if it is already exists with the current collection code
			webResource = client.target(BASE_URI+"twitter/stop?id="+URLEncoder.encode(collectionTask.getCollectionCode(), "UTF-8"));
			response =  webResource.request(MediaType.APPLICATION_JSON).get();
			jsonResponse = response.readEntity(String.class);
			result= objectMapper.readValue(jsonResponse, CollectionTask.class);
			assertEquals(200, response.getStatus());

			//Starting the task
			logger.info("Starting task for the collection : "+collectionCode);
			webResource = client.target(BASE_URI+"twitter/start");
			response =  webResource.request(MediaType.APPLICATION_JSON).post(Entity.json(collectionTask), Response.class);
			jsonResponse = response.readEntity(String.class);
			result= objectMapper.readValue(jsonResponse, CollectionTask.class);
			assertEquals(200, response.getStatus());
			assertEquals("Unable to initialize the task "+collectionCode, configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_COLLECTION_INITIALIZING), result.getStatusCode());

			//Waiting for half of the time
			logger.info("Waiting for : "+(time/2)+" seconds");
			Thread.sleep(time*1000/2);

			//Getting status of the task
			logger.info("Fetching status of the task after starting the collection : "+collectionCode);
			webResource = client.target(BASE_URI+"twitter/status?id="+URLEncoder.encode(collectionCode, "UTF-8"));
			response =  webResource.request(MediaType.APPLICATION_JSON).get();
			jsonResponse = response.readEntity(String.class);
			result= objectMapper.readValue(jsonResponse, CollectionTask.class);
			assertEquals(200, response.getStatus());
			assertEquals("Unable to fetch the status of the task after initializing the task "+collectionCode + result.getStatusMessage(), configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_COLLECTION_RUNNING), result.getStatusCode());

			//Waiting for half of the time
			logger.info("Waiting for : "+(time/2)+" seconds");
			Thread.sleep(time*1000/2);
		} catch (Exception e) {
			logger.error("Interrupted in the mid "+e.getMessage());
			webResource = client.target(BASE_URI+"twitter/stop?id="+URLEncoder.encode(collectionCode, "UTF-8"));
			webResource.request(MediaType.APPLICATION_JSON).get();
			collectorSubscriber.unsubscribe(CHANNEL_NAME);
		}

		//Stopping the task
		try{
			logger.info("Stopping task for the collection : "+collectionCode);
			webResource = client.target(BASE_URI+"twitter/stop?id="+URLEncoder.encode(collectionCode, "UTF-8"));
			response =  webResource.request(MediaType.APPLICATION_JSON).get();
			jsonResponse = response.readEntity(String.class);
			result= objectMapper.readValue(jsonResponse, CollectionTask.class);

			assertEquals(200, response.getStatus());
			assertEquals("Unable to stop the task "+collectionCode + result.getStatusMessage(), configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_COLLECTION_STOPPED), result.getStatusCode());

			//Getting status of the task
			logger.info("Fetching status of the task after stopping the collection : "+collectionCode);
			webResource = client.target(BASE_URI+"twitter/status?id="+URLEncoder.encode(collectionCode, "UTF-8"));
			response =  webResource.request(MediaType.APPLICATION_JSON).get();
			jsonResponse = response.readEntity(String.class);
			result= objectMapper.readValue(jsonResponse, CollectionTask.class);		

			assertEquals(200, response.getStatus());
			assertEquals("Unable to fetch the status of the task after stopping the task "+collectionCode , configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_COLLECTION_NOTFOUND), result.getStatusCode());

			Long collectionCount = collectorSubscriber.getTweetCount();
			assertTrue("No. Of tweets reeived is 0 , that's why fails", Long.valueOf(0L) < collectionCount);
			logger.info("No. of tweets received = "+collectionCount);

			//Unsubscribing to Redis channel	
			collectorSubscriber.unsubscribe(CHANNEL_NAME);
		}catch(Exception e){
			logger.error("Interrupted in the mid "+e.getMessage());
			collectorSubscriber.unsubscribe(CHANNEL_NAME);
		}
	}

	@After
	public void tearDown() throws UnsupportedEncodingException{
		Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
		WebTarget webResource = client.target(BASE_URI+"twitter/stop?id="+URLEncoder.encode(collectionTask.getCollectionCode(), "UTF-8"));
		webResource.request(MediaType.APPLICATION_JSON).get();
	}
}
