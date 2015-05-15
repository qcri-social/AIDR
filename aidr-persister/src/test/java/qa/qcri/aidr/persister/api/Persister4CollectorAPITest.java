package qa.qcri.aidr.persister.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static qa.qcri.aidr.utils.ConfigProperties.getProperty;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

public class Persister4CollectorAPITest {
	//
	static Persister4CollectorAPI persister4CollectorAPI;
	static String existedCollectionCode = "test_collection_code";
	static String sampleCollectionCode = "sample_collection_ode";
	private static Logger logger = Logger.getLogger(Persister4CollectorAPITest.class.getName());
	
	@BeforeClass
	public static void setUpBeforeClass() {
		persister4CollectorAPI = new Persister4CollectorAPI();
		logger.info("Executing setUpBeforeClass In Persister4CollectorAPITest");
		//Creating a sample persister directory
		File folderLocation = new File(getProperty("DEFAULT_PERSISTER_FILE_PATH") + existedCollectionCode);
		if(!folderLocation.exists()){
			assertTrue("Unable to create sample directory",folderLocation.mkdirs());
		}
		File sampleFile = new File(System.getProperty("PROJECT_HOME") + "/src/test/resources/qa/qcri/aidr/persister/api/test_collection_code_vol-1.json");
		try {
			FileUtils.copyFileToDirectory(sampleFile, folderLocation, false);
		} catch (IOException e) {
			logger.info("Unable to copy a sample file in unit test in Persister4CollectorAPITest");
			e.printStackTrace();
		}
		
	}
	
	//Will clean up in Persister4TaggerAPITest
	/*@AfterClass
	public static void tearDownAfterClass() {
		//Deleting the sample persister directory
		logger.info("Executing tearDownAfterClass In Persister4CollectorAPITest");
		File folderLocation = new File(getProperty("DEFAULT_PERSISTER_FILE_PATH") + existedCollectionCode);
		
		try {
			FileUtils.deleteDirectory(folderLocation);
		} catch (IOException e) {
			logger.info("Unable to delete sample directory in unit test in Persister4CollectorAPITest");
			e.printStackTrace();
		}
	}*/
	
	@Test
	public void testGenerateCSVFromLastestJSON() throws UnknownHostException {
		int exportLimit= 50;
		logger.info("Executing testGenerateCSVFromLastestJSON In Persister4CollectorAPITest");
		Response response = persister4CollectorAPI.generateCSVFromLastestJSON(existedCollectionCode, exportLimit);
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
		
		response = persister4CollectorAPI.generateCSVFromLastestJSON(existedCollectionCode, 0);
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
	}

	@Test(expected = NullPointerException.class)
	public void testNullPointerExceptionOnInvalidCollectionCodeInGenerateCSVFromLastestJSON() throws UnknownHostException {
		int exportLimit= 50;
		logger.info("Executing testNullPointerExceptionOnInvalidCollectionCodeInGenerateCSVFromLastestJSON In Persister4CollectorAPITest");
		persister4CollectorAPI.generateCSVFromLastestJSON(sampleCollectionCode, exportLimit);
	}
	
	@Test
	public void testGenerateTweetsIDSCSVFromAllJSON() throws UnknownHostException {
		boolean downloadLimited = false;
		logger.info("Executing testGenerateTweetsIDSCSVFromAllJSON In Persister4CollectorAPITest");
		Response response = persister4CollectorAPI.generateTweetsIDSCSVFromAllJSON(existedCollectionCode , downloadLimited);
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
		
		downloadLimited=true;
		response = persister4CollectorAPI.generateTweetsIDSCSVFromAllJSON(existedCollectionCode , downloadLimited);
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
	}
	
	
	@Test(expected = NullPointerException.class)
	public void testNullPointerExceptionOnInvalidCollectionCodeInGenerateTweetsIDSCSVFromAllJSON() throws UnknownHostException {
		boolean downloadLimited = false;
		logger.info("Executing testNullPointerExceptionOnInvalidCollectionCodeInGenerateTweetsIDSCSVFromAllJSON In Persister4CollectorAPITest");
		persister4CollectorAPI.generateTweetsIDSCSVFromAllJSON(sampleCollectionCode , downloadLimited);
	}

	@Test
	public void testGenerateJSONFromLastestJSONTest() throws UnknownHostException {
		String jsonType = "TEXT_JSON";
		logger.info("Executing testGenerateJSONFromLastestJSONTest In Persister4CollectorAPITest");
		Response response = persister4CollectorAPI.generateJSONFromLastestJSON(existedCollectionCode, jsonType);
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
		
		jsonType = "JSON";
		response = persister4CollectorAPI.generateJSONFromLastestJSON(existedCollectionCode, jsonType);
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
	}
	
	@Test(expected = NullPointerException.class)
	public void testNullPointerExceptionOnInvalidCollectionCodeInGenerateJSONFromLastestJSON() throws UnknownHostException {
		String jsonType = "TEXT_JSON";
		logger.info("Executing testNullPointerExceptionOnInvalidCollectionCodeInGenerateJSONFromLastestJSON In Persister4CollectorAPITest");
		persister4CollectorAPI.generateJSONFromLastestJSON(sampleCollectionCode, jsonType);
	}
	
	@Test(expected = NullPointerException.class)
	public void testNullPointerExceptionOnInvalidJsonTypeInGenerateJSONFromLastestJSON() throws UnknownHostException {
		String jsonType = null;
		logger.info("Executing testNullPointerExceptionOnInvalidJsonTypeInGenerateJSONFromLastestJSON In Persister4CollectorAPITest");
		persister4CollectorAPI.generateJSONFromLastestJSON(existedCollectionCode, jsonType);
	}
	
	@Test
	public void testGenerateTweetsIDSJSONFromAllJSON() throws UnknownHostException {
		Boolean downloadLimited = true;
		String jsonType = "json";
		logger.info("Executing testGenerateTweetsIDSJSONFromAllJSON In Persister4CollectorAPITest");
		Response response = persister4CollectorAPI.generateTweetsIDSJSONFromAllJSON(existedCollectionCode, downloadLimited, jsonType);
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
		
		downloadLimited = false;
		response = persister4CollectorAPI.generateTweetsIDSJSONFromAllJSON(existedCollectionCode, downloadLimited, jsonType);
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
	}

	@Test(expected = NullPointerException.class)
	public void testNullPointerExceptionOnInvalidCollectionCodeInGenerateTweetsIDSJSONFromAllJSON() throws UnknownHostException {
		Boolean downloadLimited = true;
		String jsonType = "json";
		logger.info("Executing testNullPointerExceptionOnInvalidCollectionCodeInGenerateTweetsIDSJSONFromAllJSON In Persister4CollectorAPITest");
		Response response = persister4CollectorAPI.generateTweetsIDSJSONFromAllJSON(sampleCollectionCode, downloadLimited, jsonType);
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
	}

	@Test(expected = NullPointerException.class)
	public void testNullPointerExceptionOnInvalidJsonTypeInGenerateTweetsIDSJSONFromAllJSON() throws UnknownHostException {
		Boolean downloadLimited = true;
		String jsonType = null;
		logger.info("Executing testNullPointerExceptionOnInvalidJsonTypeInGenerateTweetsIDSJSONFromAllJSON In Persister4CollectorAPITest");
		Response response = persister4CollectorAPI.generateTweetsIDSJSONFromAllJSON(existedCollectionCode, downloadLimited, jsonType);
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
		
	}
}
