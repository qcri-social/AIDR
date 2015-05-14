package qa.qcri.aidr.collector.api;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;

import qa.qcri.aidr.collector.beans.CollectionTask;
import qa.qcri.aidr.collector.beans.ResponseWrapper;
import qa.qcri.aidr.collector.utils.ConfigProperties;

public class TwitterCollectorAPIRequestValidationTest {
	
	private TwitterCollectorAPI instance;

	@Before
	public void setUp() throws Exception {
		instance = new TwitterCollectorAPI();
	}

	@Test
	public void testStartTask() {
		//Default task with all values null
		//Will fail because of twitterConfiguration not available
		CollectionTask collectionTask = new CollectionTask();
		Response response = instance.startTask(collectionTask);
		ResponseWrapper responseWrapper = (ResponseWrapper) response.getEntity();
		assertEquals(ConfigProperties.getProperty("STATUS_CODE_COLLECTION_ERROR"), responseWrapper.getStatusCode());

		//Default task with all values null except toTrack 
		//Will fail because of twitterConfiguration not available
		collectionTask = new CollectionTask();
		collectionTask.setToTrack("earthquake");
		response = instance.startTask(collectionTask);
		responseWrapper = (ResponseWrapper) response.getEntity();
		assertEquals(ConfigProperties.getProperty("STATUS_CODE_COLLECTION_ERROR"), responseWrapper.getStatusCode());
		
		//Default task with all values null except toTrack & toFollow
		//Will fail because of twitterConfiguration not available
		collectionTask = new CollectionTask();
		collectionTask.setToTrack("earthquake");
		collectionTask.setToFollow("2589756661");
		response = instance.startTask(collectionTask);
		responseWrapper = (ResponseWrapper) response.getEntity();
		assertEquals(ConfigProperties.getProperty("STATUS_CODE_COLLECTION_ERROR"), responseWrapper.getStatusCode());
		
		//Default task with all values null except toTrack, toFollow & geoLocation
		//Will fail because of twitterConfiguration not available
		collectionTask = new CollectionTask();
		collectionTask.setToTrack("earthquake");
		collectionTask.setToFollow("2589756661");
		collectionTask.setGeoLocation("-74,40,-73,41");
		response = instance.startTask(collectionTask);
		responseWrapper = (ResponseWrapper) response.getEntity();
		assertEquals(ConfigProperties.getProperty("STATUS_CODE_COLLECTION_ERROR"), responseWrapper.getStatusCode());
		
		//Default task with all values null except consumerKey and accessToken
		//Will fail because of twitter's ConsumerSecret and AccessTokenSecret not available
		collectionTask = new CollectionTask();
		collectionTask.setConsumerKey("eCbYFSSkRi20hsfsdfaaQ");
		collectionTask.setAccessToken("eJSZ34XesdfsdfDBboduYzOFikHDJ9zXVXR0g");
		response = instance.startTask(collectionTask);
		responseWrapper = (ResponseWrapper) response.getEntity();
		assertEquals(ConfigProperties.getProperty("STATUS_CODE_COLLECTION_ERROR"), responseWrapper.getStatusCode());
		
		//Default task with all values null except twitterConfig
		//Will fail because of task details not available
		collectionTask = new CollectionTask();
		collectionTask.setConsumerKey("ekRi20hsfsdfaaQ");
		collectionTask.setConsumerSecret("eqBmiIRVLbasdfasdfuQS3w5YpR0naYyHSYCY");
		collectionTask.setAccessToken("32fsdfDBboduYzOFikHDJ9zXVXR0g");
		collectionTask.setAccessTokenSecret("fkEXx1z68oks4hm8JCUGeRDw");
		response = instance.startTask(collectionTask);
		responseWrapper = (ResponseWrapper) response.getEntity();
		assertEquals(ConfigProperties.getProperty("STATUS_CODE_COLLECTION_ERROR"), responseWrapper.getStatusCode());
		
		//Default task with all values null except twitterConfig & toTrack
		//Will fail because of collectionCode and collectionName not present
		collectionTask = new CollectionTask();
		collectionTask.setConsumerKey("wehsfsdfaaQ");
		collectionTask.setConsumerSecret("wsdfasdfuQS3w5YpR0naYyHSYCY");
		collectionTask.setAccessToken("fsdfDBboduYzOFikHDJ9zXVXR0g");
		collectionTask.setAccessTokenSecret("sszkEXx1z68oks4hm8JCUGeRDw");
		collectionTask.setToTrack("earthquake");
		response = instance.startTask(collectionTask);
		responseWrapper = (ResponseWrapper) response.getEntity();
		assertEquals(ConfigProperties.getProperty("STATUS_CODE_COLLECTION_ERROR"), responseWrapper.getStatusCode());
		
		/*TODO 1)Write positive test cases whenever task is in initializing or running state. 
		 *     2) Write test cases for the persister whenever default persister mode is true.
		 * Both types of test cases will be covered in integration testing since it requires 
		 * external resources to be present like redis-server should be running  and aidr-persister 
		 * should be deployed.
		 */
	}
	
	@Test
	public void testStopTask() throws InterruptedException{
		//Will fail because of collection code does not exist
		Response response = instance.stopTask("collectionCode");
		ResponseWrapper responseWrapper = (ResponseWrapper) response.getEntity();
		assertEquals(ConfigProperties.getProperty("STATUS_CODE_COLLECTION_NOTFOUND"), responseWrapper.getStatusCode());
	}

}