package qa.qcri.aidr.persister.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import qa.qcri.aidr.common.code.Configurator;
import qa.qcri.aidr.utils.PersisterConfigurationProperty;
import qa.qcri.aidr.utils.PersisterConfigurator;

public class Persister4TaggerAPITest {
	//
	static Persister4TaggerAPI persister4TaggerAPI;
	static String existedCollectionCode = "test_collection_code";
	static String sampleCollectionCode = "sample_collection_ode";
	static String userName = "SinhaKoushik";
	private static Logger logger = Logger.getLogger(Persister4TaggerAPITest.class.getName());	
	private static Configurator configurator = PersisterConfigurator
			.getInstance();

	@BeforeClass
	public static void setUpBeforeClass() {
		configurator.initProperties(PersisterConfigurator.configLoadFileName,
				PersisterConfigurationProperty.values());	persister4TaggerAPI = new Persister4TaggerAPI();
		logger.info("Executing setUpBeforeClass In Persister4TaggerAPITest");
		//Creating a sample persister output directory
		File folderLocation = new File(configurator.getProperty(PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH) + existedCollectionCode +"/output");
		if(!folderLocation.exists()){
			assertTrue("Unable to create sample directory",folderLocation.mkdirs());
		}
		File sampleFile = new File(System.getProperty("PROJECT_HOME") + "/src/test/resources/qa/qcri/aidr/persister/api/test_collection_code_vol-1.json");
		try {
			FileUtils.copyFileToDirectory(sampleFile, folderLocation, false);
			//Renaming the file
			FileUtils.moveFile(new File(folderLocation+"/test_collection_code_vol-1.json"), new File(folderLocation+"/Classified_test_collection_code_20150513_vol-1.json"));
		} catch (IOException e) {
			logger.info("Unable to copy a sample file in unit test for Persister4TaggerAPITest");
			e.printStackTrace();
		}
		
	}
	
	@AfterClass
	public static void tearDownAfterClass() {
		//Deleting the sample persister output directory
		logger.info("Executing setUpBeforeClass In Persister4TaggerAPITest");
		File folderLocation = new File(configurator.getProperty(PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH) + existedCollectionCode);
		
		try {
			FileUtils.deleteDirectory(folderLocation);
		} catch (IOException e) {
			logger.info("Unable to delete sample directory in unit test for Persister4TaggerAPITest");
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGenerateCSVFromLastestJSON() throws UnknownHostException {
		int exportLimit = 100;
		logger.info("Executing test testGenerateCSVFromLastestJSON in Persister4TaggerAPI with valid collection code and export Limit");
		Response response = persister4TaggerAPI.generateCSVFromLastestJSON(existedCollectionCode, exportLimit);
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
	}
	
	@Test(expected = NullPointerException.class)
	public void testNullPointerExceptionOnInvalidCollectionCodeInGenerateCSVFromLastestJSON() throws UnknownHostException {
		int exportLimit = 100;
		logger.info("Executing test for testNullPointerExceptionOnInvalidCollectionCodeInGenerateCSVFromLastestJSON in Persister4TaggerAPI with invalid collection code and valid export Limit");
		persister4TaggerAPI.generateCSVFromLastestJSON(sampleCollectionCode, exportLimit);
	}
	
	@Test
	public void testGenerateTweetsIDSCSVFromAllJSON() throws UnknownHostException {
		Boolean downloadLimited = true;
		logger.info("Executing test for testGenerateTweetsIDSCSVFromAllJSON in Persister4TaggerAPI with valid collection code and downloadLimited=true");
		Response response = persister4TaggerAPI.generateTweetsIDSCSVFromAllJSON(existedCollectionCode, downloadLimited);
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
		
	}
	
	@Test(expected = NullPointerException.class)
	public void  testNullPointerExceptionOnInvalidCollectionCodeInGenerateTweetsIDSCSVFromAllJSON() throws UnknownHostException {
		Boolean downloadLimited = true;
		logger.info("Executing test for testNullPointerExceptionOnInvalidCollectionCodeInGenerateTweetsIDSCSVFromAllJSON in Persister4TaggerAPI with invalid collection code and downloadLimited=true");
		persister4TaggerAPI.generateTweetsIDSCSVFromAllJSON(sampleCollectionCode, downloadLimited);
	}
	
	@Test(expected = NullPointerException.class)
	public void testNullPointerExceptionOnInvalidDownloadLimitedInGenerateTweetsIDSCSVFromAllJSON() throws UnknownHostException {
		logger.info("Executing test for testNullPointerExceptionOnInvalidDownloadLimitedInGenerateTweetsIDSCSVFromAllJSON in Persister4TaggerAPI with valid collection Code and downloadLimited=null");
		persister4TaggerAPI.generateTweetsIDSCSVFromAllJSON(existedCollectionCode, null);
	}
	
	@Test
	public void testGenerateJSONFromLastestJSON() throws UnknownHostException {
		int exportLimit = 50;
		String jsonType = "TEXT_JSON";
		logger.info("Executing test for testGenerateJSONFromLastestJSON in Persister4TaggerAPI with valid collection Code, exportLimit & jsonType");
		Response response = persister4TaggerAPI.generateJSONFromLastestJSON(existedCollectionCode, exportLimit, jsonType);
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
		
	}
	
	@Test
	public void testWithInvalidCollectionCodeInGenerateJSONFromLastestJSON() throws UnknownHostException {
		int exportLimit = 50;
		String jsonType = "TEXT_JSON";
		logger.info("Executing test for testWithInvalidCollectionCodeInGenerateJSONFromLastestJSON in Persister4TaggerAPI with invalid collection Code and valid exportLimit & jsonType");
		Response response = persister4TaggerAPI.generateJSONFromLastestJSON(sampleCollectionCode, exportLimit, jsonType);
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
	}
	
	@Test(expected = NullPointerException.class)
	public void testNullPointerExceptionOnInvalidJsonTypeInGenerateJSONFromLastestJSON() throws UnknownHostException {
		int exportLimit = 50;
		logger.info("Executing test for testNullPointerExceptionOnInvalidJsonTypeInGenerateJSONFromLastestJSON in Persister4TaggerAPI with valid collection Code & exportLimit and jsonType is null");
		persister4TaggerAPI.generateJSONFromLastestJSON(existedCollectionCode, exportLimit, null);

	}

	@Test
	public void testGenerateTweetsIDSJSONFromAllJSON() throws UnknownHostException {
		// this method returns NullPointerException because it's calling deprecated methods
		Boolean downloadLimited = true;
		String jsonType = "TEXT_JSON";
		logger.info("Executing test for testGenerateTweetsIDSJSONFromAllJSON in Persister4TaggerAPI with valid collection Code, downloadLimited and jsonType");
		Response response = persister4TaggerAPI.generateTweetsIDSJSONFromAllJSON(existedCollectionCode, downloadLimited, jsonType);
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
	}
	
	@Test(expected = NullPointerException.class)
	public void testNullPointerExceptionOnInvalidJsonTypeInGenerateTweetsIDSJSONFromAllJSON() throws UnknownHostException {
		// this method returns NullPointerException because it's calling deprecated methods
		Boolean downloadLimited = true;
		logger.info("Executing test for testNullPointerExceptionOnInvalidJsonTypeInGenerateTweetsIDSJSONFromAllJSON in Persister4TaggerAPI with valid collection Code & downloadLimited and jsonType is null");
		persister4TaggerAPI.generateTweetsIDSJSONFromAllJSON(existedCollectionCode, downloadLimited, null);
	}
	
	@Test(expected = NullPointerException.class)
	public void testNullPointerExceptionOnInvalidDownloadLimitedInGenerateTweetsIDSJSONFromAllJSON() throws UnknownHostException {
		// this method returns NullPointerException because it's calling deprecated methods
		Boolean downloadLimited = null;
		String jsonType = "TEXT_JSON";
		logger.info("Executing test for testNullPointerExceptionOnInvalidDownloadLimitedInGenerateTweetsIDSJSONFromAllJSON in Persister4TaggerAPI with valid collection Code & jsonType and downloadLimited is null ");
		persister4TaggerAPI.generateTweetsIDSJSONFromAllJSON(existedCollectionCode, downloadLimited, jsonType);
	}
	
		
	@Test
	public void testGenerateCSVFromLastestJSONFiltered() throws UnknownHostException {
		int exportLimit = 50;
		
		logger.info("Executing test for testGenerateCSVFromLastestJSONFiltered in Persister4TaggerAPI with valid collection code,exportLimit, userName and queryString contains date filter");
		String queryString = "{ \"constraints\": [ { \"queryType\": \"date_query\", \"comparator\": \"is_before\", \"timestamp\": 1427375693 },"
				+ " { \"queryType\": \"date_query\", \"comparator\": \"is_after\", \"timestamp\": 1427352427 } ] }";
	
		Response response = persister4TaggerAPI.generateCSVFromLastestJSONFiltered(queryString, existedCollectionCode, exportLimit, userName);
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
		
		logger.info("Executing test for testGenerateCSVFromLastestJSONFiltered in Persister4TaggerAPI with valid collection code,exportLimit, userName and queryString contains date filter & classifier_query both");
		queryString = "{ \"constraints\": [ { \"queryType\": \"date_query\", \"comparator\": \"is_before\", \"timestamp\": 1427375693 }, "
				+ "{ \"queryType\": \"date_query\", \"comparator\": \"is_after\", \"timestamp\": 1427352427 }, "
				+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": \"praying\", \"comparator\": \"is\", \"min_confidence\": 0.8 }, "
				+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": \"030_info\", \"comparator\": \"is_not\" }, "
				+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": null, \"comparator\": \"has_confidence\", \"min_confidence\": 0.5 } ] }";
		
		response = persister4TaggerAPI.generateCSVFromLastestJSONFiltered(queryString, existedCollectionCode, exportLimit, userName);
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
	}
	
	@Test
	public void testOverloadedMethodGenerateCSVFromLastestJSONFiltered() throws UnknownHostException {
		int exportLimit = 50;
		logger.info("Executing test for testOverloadedMethodGenerateCSVFromLastestJSONFiltered in Persister4TaggerAPI with valid collection code and exportLimit");
		Response response = persister4TaggerAPI.generateCSVFromLastestJSONFiltered(existedCollectionCode, exportLimit);
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=false, closed=false, buffered=false}", response.toString());
	}
	
	@Test(expected = NullPointerException.class)
	public void testNullPointerExceptionOnInvalidCollectionCodeInGenerateCSVFromLastestJSONFiltered() throws UnknownHostException {
		int exportLimit = 50;
			
		logger.info("Executing test for testNullPointerExceptionOnInvalidCollectionCodeInGenerateCSVFromLastestJSONFiltered in Persister4TaggerAPI with invalid collection code and valid exportLimit, userName & queryString contains date filter & classifier_query");
		String queryString = "{ \"constraints\": [ { \"queryType\": \"date_query\", \"comparator\": \"is_before\", \"timestamp\": 1427375693 }, "
				+ "{ \"queryType\": \"date_query\", \"comparator\": \"is_after\", \"timestamp\": 1427352427 }, "
				+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": \"praying\", \"comparator\": \"is\", \"min_confidence\": 0.8 }, "
				+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": \"030_info\", \"comparator\": \"is_not\" }, "
				+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": null, \"comparator\": \"has_confidence\", \"min_confidence\": 0.5 } ] }";
		
		Response response = persister4TaggerAPI.generateCSVFromLastestJSONFiltered(queryString, sampleCollectionCode, exportLimit, userName);
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
	}
	
	@Test(expected = NullPointerException.class)
	public void testNullPointerExceptionOnInvalidQueryStringInGenerateCSVFromLastestJSONFiltered() throws UnknownHostException {
		int exportLimit = 50;
			
		logger.info("Executing test for testNullPointerExceptionOnInvalidQueryStringInGenerateCSVFromLastestJSONFiltered in Persister4TaggerAPI with valid collection code, exportLimit & userName and queryString is null");
		String queryString = null;
		Response response = persister4TaggerAPI.generateCSVFromLastestJSONFiltered(queryString, existedCollectionCode, exportLimit, userName);
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
	}
	
	@Test
	public void testWithInvalidCollectionCodeInGenerateCSVFromLastestJSONFiltered() throws UnknownHostException {
		int exportLimit = 50;
		logger.info("Executing test for testWithInvalidCollectionCodeInGenerateCSVFromLastestJSONFiltered in Persister4TaggerAPI with invalid collection code and valid exportLimit");
		Response response = persister4TaggerAPI.generateCSVFromLastestJSONFiltered(sampleCollectionCode, exportLimit);
		
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=false, closed=false, buffered=false}", response.toString());
	}
	
	@Test
	public void testGenerateTweetsIDSCSVFromAllJSONFiltered() throws UnknownHostException {
		Boolean downloadLimited = true;
		
		logger.info("Executing test for testGenerateTweetsIDSCSVFromAllJSONFiltered in Persister4TaggerAPI with valid collection code, downloadLimited, userName and queryString contains date filter");
		String queryString = "{ \"constraints\": [ { \"queryType\": \"date_query\", \"comparator\": \"is_before\", \"timestamp\": 1427375693 },"
				+ " { \"queryType\": \"date_query\", \"comparator\": \"is_after\", \"timestamp\": 1427352427 } ] }";
		Response response = persister4TaggerAPI.generateTweetsIDSCSVFromAllJSONFiltered(queryString, existedCollectionCode, downloadLimited, userName);
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
		
		logger.info("Executing test for testGenerateTweetsIDSCSVFromAllJSONFiltered in Persister4TaggerAPI with valid collection code, downloadLimited, userName and queryString contains date filter & classifier_query both");
		queryString = "{ \"constraints\": [ { \"queryType\": \"date_query\", \"comparator\": \"is_before\", \"timestamp\": 1427375693 }, "
				+ "{ \"queryType\": \"date_query\", \"comparator\": \"is_after\", \"timestamp\": 1427352427 }, "
				+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": \"praying\", \"comparator\": \"is\", \"min_confidence\": 0.8 }, "
				+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": \"030_info\", \"comparator\": \"is_not\" }, "
				+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": null, \"comparator\": \"has_confidence\", \"min_confidence\": 0.5 } ] }";
		
		response = persister4TaggerAPI.generateTweetsIDSCSVFromAllJSONFiltered(queryString, existedCollectionCode, downloadLimited, userName);
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
		
	}
	
	@Test(expected = NullPointerException.class)
	public void testNullPointerExceptionOnInvalidCollectionCodeInGenerateTweetsIDSCSVFromAllJSONFiltered() throws UnknownHostException {
		Boolean downloadLimited = true;
		
		logger.info("Executing test for testNullPointerExceptionOnInvalidCollectionCodeInGenerateTweetsIDSCSVFromAllJSONFiltered in Persister4TaggerAPI with invalid collection code and valid downloadLimited, userName and queryString contains date filter & classifier_query both");
		String queryString = "{ \"constraints\": [ { \"queryType\": \"date_query\", \"comparator\": \"is_before\", \"timestamp\": 1427375693 }, "
				+ "{ \"queryType\": \"date_query\", \"comparator\": \"is_after\", \"timestamp\": 1427352427 }, "
				+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": \"praying\", \"comparator\": \"is\", \"min_confidence\": 0.8 }, "
				+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": \"030_info\", \"comparator\": \"is_not\" }, "
				+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": null, \"comparator\": \"has_confidence\", \"min_confidence\": 0.5 } ] }";
		
		Response response = persister4TaggerAPI.generateTweetsIDSCSVFromAllJSONFiltered(queryString, sampleCollectionCode, downloadLimited, userName);
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
		
	}
	
	@Test(expected = NullPointerException.class)
	public void testNullPointerExceptionOnInvalidQueryStringInGenerateTweetsIDSCSVFromAllJSONFiltered() throws UnknownHostException {
		Boolean downloadLimited = true;
		
		logger.info("Executing test for testNullPointerExceptionOnInvalidQueryStringInGenerateTweetsIDSCSVFromAllJSONFiltered in Persister4TaggerAPI with valid collection code, downloadLimited, userName and queryString is null");
		String queryString = null;
		Response response = persister4TaggerAPI.generateTweetsIDSCSVFromAllJSONFiltered(queryString, existedCollectionCode, downloadLimited, userName);
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
		
	}
	
	@Test
	public void testOverloadedMethodGenerateTweetsIDSCSVFromAllJSONFiltered() throws UnknownHostException {
		Boolean downloadLimited = true;
		logger.info("Executing test for testOverloadedMethodGenerateTweetsIDSCSVFromAllJSONFiltered in Persister4TaggerAPI with valid collection code and downloadLimited");
		
		Response response = persister4TaggerAPI.generateTweetsIDSCSVFromAllJSONFiltered(existedCollectionCode, downloadLimited);
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=false, closed=false, buffered=false}", response.toString());
	}
	
	@Test
	public void testInvalidCollectionCodeOnOverloadedMethodGenerateTweetsIDSCSVFromAllJSONFiltered() throws UnknownHostException {
		Boolean downloadLimited = true;
		logger.info("Executing test for testNullPointerExceptionOnInvalidCollectionCodeOnOverloadedMethodGenerateTweetsIDSCSVFromAllJSONFiltered in Persister4TaggerAPI with invalid collection code and valid downloadLimited");
		Response response= persister4TaggerAPI.generateTweetsIDSCSVFromAllJSONFiltered(sampleCollectionCode, downloadLimited);
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=false, closed=false, buffered=false}", response.toString());
	}
	
	@Test
	public void testGenerateJSONFromLastestJSONFiltered() throws UnknownHostException {
		int exportLimit = 50;
		String jsonType = "TEXT_JSON";
		logger.info("Executing test for testGenerateJSONFromLastestJSONFiltered in Persister4TaggerAPI with valid collection code, exportLimit,jsonType, userName and queryString contains date filter");
		String queryString = "{ \"constraints\": [ { \"queryType\": \"date_query\", \"comparator\": \"is_before\", \"timestamp\": 1427375693 },"
				+ " { \"queryType\": \"date_query\", \"comparator\": \"is_after\", \"timestamp\": 1427352427 } ] }";
		Response response = persister4TaggerAPI.generateJSONFromLastestJSONFiltered(queryString, existedCollectionCode, exportLimit, jsonType, userName);
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
		
		logger.info("Executing test for testGenerateJSONFromLastestJSONFiltered in Persister4TaggerAPI with valid collection code, exportLimit,jsonType, userName and queryString contains date filter & classifier_query both");
		queryString = "{ \"constraints\": [ { \"queryType\": \"date_query\", \"comparator\": \"is_before\", \"timestamp\": 1427375693 }, "
				+ "{ \"queryType\": \"date_query\", \"comparator\": \"is_after\", \"timestamp\": 1427352427 }, "
				+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": \"praying\", \"comparator\": \"is\", \"min_confidence\": 0.8 }, "
				+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": \"030_info\", \"comparator\": \"is_not\" }, "
				+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": null, \"comparator\": \"has_confidence\", \"min_confidence\": 0.5 } ] }";
	
		response = persister4TaggerAPI.generateJSONFromLastestJSONFiltered(queryString, existedCollectionCode, exportLimit, jsonType, userName);
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
	}
	
	@Test
	public void testWithInvalidCollectionCodeInGenerateJSONFromLastestJSONFiltered() throws UnknownHostException {
		int exportLimit = 50;
		String jsonType = "TEXT_JSON";
		logger.info("Executing test for testWithInvalidCollectionCodeInGenerateJSONFromLastestJSONFiltered in Persister4TaggerAPI with invalid collection code and valid exportLimit, jsonType, userName & queryString contains date filter & classifier_query both");
		String queryString = "{ \"constraints\": [ { \"queryType\": \"date_query\", \"comparator\": \"is_before\", \"timestamp\": 1427375693 }, "
				+ "{ \"queryType\": \"date_query\", \"comparator\": \"is_after\", \"timestamp\": 1427352427 }, "
				+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": \"praying\", \"comparator\": \"is\", \"min_confidence\": 0.8 }, "
				+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": \"030_info\", \"comparator\": \"is_not\" }, "
				+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": null, \"comparator\": \"has_confidence\", \"min_confidence\": 0.5 } ] }";
	
		Response response = persister4TaggerAPI.generateJSONFromLastestJSONFiltered(queryString, sampleCollectionCode, exportLimit, jsonType, userName);
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
	}
	
	@Test(expected = NullPointerException.class)
	public void testNullPointerExceptionOnInvalidQueryStringInGenerateJSONFromLastestJSONFiltered() throws UnknownHostException {
		int exportLimit = 50;
		String jsonType = "TEXT_JSON";
		logger.info("Executing test for testNullPointerExceptionOnInvalidQueryStringInGenerateJSONFromLastestJSONFiltered in Persister4TaggerAPI with valid collection code, exportLimit, jsonType, userName and queryString is null");
		String queryString = null;
		Response response = persister4TaggerAPI.generateJSONFromLastestJSONFiltered(queryString, existedCollectionCode, exportLimit, jsonType, userName);
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
	}
	
	@Test(expected = NullPointerException.class)
	public void testNullPointerExceptionOnInvalidJsonTypeInGenerateJSONFromLastestJSONFiltered() throws UnknownHostException {
		int exportLimit = 50;
		String jsonType = null;
		String queryString = "{ \"constraints\": [] }";
		logger.info("Executing test for testNullPointerExceptionOnInvalidJsonTypeInGenerateJSONFromLastestJSONFiltered in Persister4TaggerAPI with valid collection code, exportLimit, userName, queryString and jsonType is null");
		persister4TaggerAPI.generateJSONFromLastestJSONFiltered(queryString, existedCollectionCode, exportLimit, jsonType, userName);
	}
	
	@Test
	public void testGenerateTweetsIDSJSONFromAllJSONFiltered() throws UnknownHostException {
		Boolean downloadLimited = true;
		String jsonType = "TEXT_JSON";
		
		logger.info("Executing test for testGenerateTweetsIDSJSONFromAllJSONFiltered in Persister4TaggerAPI with valid collection code, downloadLimited, userName,jsonType and queryString contains date filter");
		String queryString = "{ \"constraints\": [ { \"queryType\": \"date_query\", \"comparator\": \"is_before\", \"timestamp\": 1427375693 },"
				+ " { \"queryType\": \"date_query\", \"comparator\": \"is_after\", \"timestamp\": 1427352427 } ] }";
		Response response = persister4TaggerAPI.generateTweetsIDSJSONFromAllJSONFiltered(queryString, existedCollectionCode , downloadLimited, jsonType, userName);
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
		
		logger.info("Executing test for testGenerateTweetsIDSJSONFromAllJSONFiltered in Persister4TaggerAPI with valid collection code, downloadLimited, userName,jsonType and queryString contains date filter & classifier_query both");
		queryString = "{ \"constraints\": [ { \"queryType\": \"date_query\", \"comparator\": \"is_before\", \"timestamp\": 1427375693 }, "
				+ "{ \"queryType\": \"date_query\", \"comparator\": \"is_after\", \"timestamp\": 1427352427 }, "
				+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": \"praying\", \"comparator\": \"is\", \"min_confidence\": 0.8 }, "
				+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": \"030_info\", \"comparator\": \"is_not\" }, "
				+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": null, \"comparator\": \"has_confidence\", \"min_confidence\": 0.5 } ] }";
		
		response = persister4TaggerAPI.generateTweetsIDSJSONFromAllJSONFiltered(queryString, existedCollectionCode , downloadLimited, jsonType, userName);
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
	}
	
	@Test(expected = NullPointerException.class)
	public void testNullPointerExceptionOnInvalidCollectionCodeInGenerateTweetsIDSJSONFromAllJSONFiltered() throws UnknownHostException {
		Boolean downloadLimited = true;
		String jsonType = "TEXT_JSON";
		String queryString =  "{ \"constraints\": [] }";
		logger.info("Executing test for testNullPointerExceptionOnInvalidCollectionCodeInGenerateTweetsIDSJSONFromAllJSONFiltered in Persister4TaggerAPI with invalid collection code and valid downloadLimited, userName, jsonType & queryString ");
		persister4TaggerAPI.generateTweetsIDSJSONFromAllJSONFiltered(queryString, sampleCollectionCode , downloadLimited, jsonType, userName);
	}
	
	@Test(expected = NullPointerException.class)
	public void testNullPointerExceptionOnInvalidJsonTypeInGenerateTweetsIDSJSONFromAllJSONFiltered() throws UnknownHostException {
		Boolean downloadLimited = true;
		String jsonType = null;
		String queryString =  "{ \"constraints\": [] }";
		logger.info("Executing test for testNullPointerExceptionOnInvalidJsonTypeInGenerateTweetsIDSJSONFromAllJSONFiltered in Persister4TaggerAPI with valid collection code, downloadLimited, userName & queryString and jsonType is null");
		Response response = persister4TaggerAPI.generateTweetsIDSJSONFromAllJSONFiltered(queryString, existedCollectionCode , downloadLimited, jsonType, userName);
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
	}
	
	@Test(expected = NullPointerException.class)
	public void testNullPointerExceptionOnInvalidDownloadLimitedInGenerateTweetsIDSJSONFromAllJSONFiltered() throws UnknownHostException {
		Boolean downloadLimited = null;
		String jsonType = "TEXT_JSON";
		String queryString =  "{ \"constraints\": [] }";
		logger.info("Executing test for testNullPointerExceptionOnInvalidDownloadLimitedInGenerateTweetsIDSJSONFromAllJSONFiltered in Persister4TaggerAPI with valid collection code, jsonType, userName & queryString and downloadLimited is null");
		Response response = persister4TaggerAPI.generateTweetsIDSJSONFromAllJSONFiltered(queryString, existedCollectionCode , downloadLimited, jsonType, userName);
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
	}
	
	@Test(expected = NullPointerException.class)
	public void testNullPointerExceptionOnInvalidQueryStringInGenerateTweetsIDSJSONFromAllJSONFiltered() throws UnknownHostException {
		Boolean downloadLimited = true;
		String jsonType = "TEXT_JSON";
		String queryString = null;
		logger.info("Executing test for testNullPointerExceptionOnInvalidQueryStringInGenerateTweetsIDSJSONFromAllJSONFiltered in Persister4TaggerAPI with valid collection code, jsonType, userName & downloadLimited and queryString is null");
		Response response = persister4TaggerAPI.generateTweetsIDSJSONFromAllJSONFiltered(queryString, existedCollectionCode , downloadLimited, jsonType, userName);
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
	}
}
