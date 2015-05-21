/**
 * 
 */
package qa.qcri.aidr.collector.collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import qa.qcri.aidr.collector.api.TwitterCollectorAPI;
import qa.qcri.aidr.collector.beans.CollectionTask;
import qa.qcri.aidr.collector.beans.ResponseWrapper;
import redis.clients.jedis.exceptions.JedisConnectionException;

import com.google.common.io.Files;
import com.google.gson.Gson;


/**
 * @author Kushal
 *
 */
public class CollectorTesterTest {
	private static Logger logger = Logger.getLogger(CollectorTesterTest.class.getName());
	private TwitterCollectorAPI instance;
	private CollectionTask collectionTask;
	private Long time;
	private Properties properties = new Properties();

	@Before
	public void setUp() throws Exception {
		instance = new TwitterCollectorAPI();
		time = Long.parseLong(System.getProperty("time"));
		String collectionTaskPath = System.getProperty("collectionTask");
		collectionTask = new CollectionTask();
		if(StringUtils.isNotEmpty(collectionTaskPath)){
			collectionTask = getCollectionTask(collectionTaskPath);
		}
		else{
			collectionTask.setConsumerKey("wehsfsdfaaQ");
			collectionTask.setConsumerSecret("wsdfasdfuQS3w5YpR0naYyHSYCY");
			collectionTask.setAccessToken("fsdfDBboduYzOFikHDJ9zXVXR0g");
			collectionTask.setAccessTokenSecret("sszkEXx1z68oks4hm8JCUGeRDw");
			collectionTask.setToTrack("earthquake");
			collectionTask.setCollectionCode("collectionCode");
			collectionTask.setCollectionName("collectionName");
		}
	}
	
//	@After
//	public void tearDown(){
//		instance.stopTask(collectionTask.getCollectionCode());
//	}
	@Test
	public void testCollector(){
		
		Response response;
		String collectionCode = collectionTask.getCollectionCode();
		
	//Redis Connection
		logger.info("Checking Redis connection");
		try{
			JedisPublisher.newInstance();
		}
		catch(JedisConnectionException e){
			fail("Couldn't establish a Redis connection. Check whether redis is running or not");
		}
		
	//start
		logger.info("Starting task for the collection : "+collectionCode);
		response = instance.startTask(collectionTask);
		ResponseWrapper responseWrapper;
		responseWrapper = (ResponseWrapper) response.getEntity();
		assertEquals("Unable to initialize the task "+collectionCode,"OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
		//assertEquals("Unable to initialize the task "+collectionCode,ConfigProperties.getProperty("STATUS_CODE_COLLECTION_INITIALIZING"), responseWrapper.getStatusCode());
	//Wait for half of the time
		try {
			logger.info("Waiting for : "+(time/2)+" seconds");
			Thread.sleep(time*1000/2);
		} catch (InterruptedException e) {
			logger.error("Interrupted in the mid "+e.getMessage());
			instance.stopTask(collectionTask.getCollectionCode());
		}
	//status
		logger.info("Fetching status of the task after starting the collection : "+collectionCode);
		response = instance.getStatus(collectionCode);
		CollectionTask status = (CollectionTask) response.getEntity();
		assertEquals("Unable to fetch the status of the task after initializing the task"+collectionCode,"OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
		//assertEquals("Unable to fetch the status of the task after initializing the task"+collectionCode,ConfigProperties.getProperty("STATUS_CODE_COLLECTION_RUNNING"), status.getStatusCode());
	//Wait for half of the time
		try {
			logger.info("Waiting for : "+(time/2)+" seconds");
			Thread.sleep(time*1000/2);
		} catch (InterruptedException e) {
			logger.error("Interrupted in the mid "+e.getMessage());
			instance.stopTask(collectionTask.getCollectionCode());
		}
	//stop
		logger.info("Stopping task for the collection : "+collectionCode);
		response = instance.stopTask(collectionCode);
		status = (CollectionTask) response.getEntity();
		Long collectionCount = status.getCollectionCount();
		assertEquals("Unable to stop the task "+collectionCode,"OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
		//assertEquals("Unable to stop the task "+collectionCode,ConfigProperties.getProperty("STATUS_CODE_COLLECTION_RUNNING"), status.getStatusCode());
	//status
		logger.info("Fetching status of the task after stopping the collection : "+collectionCode);
		response = instance.getStatus(collectionCode);
		responseWrapper = (ResponseWrapper) response.getEntity();
		assertEquals("Unable to fetch the status of the task after stopping the task "+collectionCode,"OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
		//assertEquals("Unable to fetch the status of the task after stopping the task "+collectionCode,ConfigProperties.getProperty("STATUS_CODE_COLLECTION_NOTFOUND"), responseWrapper.getStatusCode());
		
		assertNotSame("No. Of tweets reeived so  far is 0 , that's why it fails",0L, collectionCount);
		logger.info("No. of tweets received = "+collectionCount);
	}
	
	private CollectionTask getCollectionTask(String collectionTaskPath){
		String fileType = Files.getFileExtension(collectionTaskPath);
		switch(fileType){
		case "json" : 		Gson gson = new Gson();
							BufferedReader br;
							try {
								br = new BufferedReader(new FileReader(collectionTaskPath));
								String currentLine = br.readLine();
								if (currentLine!=null){
									collectionTask = gson.fromJson(currentLine, CollectionTask.class);
								}
							} catch (FileNotFoundException e) {
								logger.error("File not found in CollectorTester.getCollectionTask "+collectionTaskPath, e);
							} catch (IOException e) {
								logger.error("Error while reading the file "+collectionTaskPath);
							}	
							break;
							
		case "properties" : try (InputStream input = new FileInputStream(collectionTaskPath);){
								properties.load(input);
								collectionTask = propertiesToCollectionTask(properties);
								
							} catch (IOException e) {
					            logger.error("Error in reading config properties file: " + collectionTaskPath, e);
							}
							break;
		case "xml" :		 try (InputStream input = new FileInputStream(collectionTaskPath);){
								properties.loadFromXML(input);
								collectionTask = propertiesToCollectionTask(properties);
							} catch (IOException e) {
					            logger.error("Error in reading config xml file: " + collectionTaskPath, e);
							}			
							break;
		}
		return collectionTask;
	}
	
	private CollectionTask propertiesToCollectionTask(Properties properties){
		collectionTask.setConsumerKey(properties.getProperty("consumerKey"));
		collectionTask.setConsumerSecret(properties.getProperty("consumerSecret"));
		collectionTask.setAccessToken(properties.getProperty("accessToken"));
		collectionTask.setAccessTokenSecret(properties.getProperty("accessTokenSecret"));
		
		collectionTask.setToTrack(properties.getProperty("toTrack"));
		collectionTask.setCollectionCode(properties.getProperty("collectionCode"));
		collectionTask.setCollectionName(properties.getProperty("collectionName"));
		collectionTask.setToFollow(properties.getProperty("toFollow"));
		collectionTask.setGeoLocation(properties.getProperty("geoLocation"));
		collectionTask.setGeoR(properties.getProperty("geoR"));
		collectionTask.setLanguageFilter(properties.getProperty("languageFilter"));
		return collectionTask;
	}
	
}
