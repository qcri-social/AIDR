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
	static Persister4TaggerAPI persister4TaggerAPI;
	static String existedCollectionCode = "test_collection_code";
	static String sampleCollectionCode = "sample_collection_ode";
	static String userName = "SinhaKoushik";
	private static Logger logger = Logger.getLogger(Persister4TaggerAPITest.class.getName());	
	private static Configurator configurator = PersisterConfigurator
			.getInstance();

	@BeforeClass
	public static void setUpBeforeClass() {
		configurator.initProperties(PersisterConfigurator.configLoadFileName,PersisterConfigurationProperty.values());	
		persister4TaggerAPI = new Persister4TaggerAPI();
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
		Response response = persister4TaggerAPI.generateCSVFromLastestJSON(existedCollectionCode, exportLimit);
		assertEquals(200, response.getStatus());
		assertEquals(true, response.hasEntity());
		assertEquals(false, response.bufferEntity());
	}

	@Test(expected = NullPointerException.class)
	public void testNullPointerExceptionOnInvalidCollectionCodeInGenerateCSVFromLastestJSON() throws UnknownHostException {
		int exportLimit = 100;
		persister4TaggerAPI.generateCSVFromLastestJSON(sampleCollectionCode, exportLimit);
	}

	@Test
	public void testGenerateTweetsIDSCSVFromAllJSON() throws UnknownHostException {
		Boolean downloadLimited = true;
		Response response = persister4TaggerAPI.generateTweetsIDSCSVFromAllJSON(existedCollectionCode, downloadLimited);
		assertEquals(200, response.getStatus());
		assertEquals(true, response.hasEntity());
		assertEquals(false, response.bufferEntity());
	}

	@Test(expected = NullPointerException.class)
	public void  testNullPointerExceptionOnInvalidCollectionCodeInGenerateTweetsIDSCSVFromAllJSON() throws UnknownHostException {
		Boolean downloadLimited = true;
		persister4TaggerAPI.generateTweetsIDSCSVFromAllJSON(sampleCollectionCode, downloadLimited);
	}

	@Test(expected = NullPointerException.class)
	public void testNullPointerExceptionOnInvalidDownloadLimitedInGenerateTweetsIDSCSVFromAllJSON() throws UnknownHostException {
		persister4TaggerAPI.generateTweetsIDSCSVFromAllJSON(existedCollectionCode, null);
	}

	@Test
	public void testGenerateJSONFromLastestJSON() throws UnknownHostException {
		int exportLimit = 50;
		String jsonType = "TEXT_JSON";
		Response response = persister4TaggerAPI.generateJSONFromLastestJSON(existedCollectionCode, exportLimit, jsonType);
		assertEquals(200, response.getStatus());
		assertEquals(true, response.hasEntity());
		assertEquals(false, response.bufferEntity());
	}

	@Test
	public void testWithInvalidCollectionCodeInGenerateJSONFromLastestJSON() throws UnknownHostException {
		int exportLimit = 50;
		String jsonType = "TEXT_JSON";
		Response response = persister4TaggerAPI.generateJSONFromLastestJSON(sampleCollectionCode, exportLimit, jsonType);
		assertEquals(200, response.getStatus());
		assertEquals(true, response.hasEntity());
		assertEquals(false, response.bufferEntity());
	}

	@Test(expected = NullPointerException.class)
	public void testNullPointerExceptionOnInvalidJsonTypeInGenerateJSONFromLastestJSON() throws UnknownHostException {
		int exportLimit = 50;
		persister4TaggerAPI.generateJSONFromLastestJSON(existedCollectionCode, exportLimit, null);

	}

	@Test
	public void testGenerateTweetsIDSJSONFromAllJSON() throws UnknownHostException {
		// this method returns NullPointerException because it's calling deprecated methods
		Boolean downloadLimited = true;
		String jsonType = "TEXT_JSON";
		Response response = persister4TaggerAPI.generateTweetsIDSJSONFromAllJSON(existedCollectionCode, downloadLimited, jsonType);
		assertEquals(200, response.getStatus());
		assertEquals(true, response.hasEntity());
		assertEquals(false, response.bufferEntity());
	}

	@Test(expected = NullPointerException.class)
	public void testNullPointerExceptionOnInvalidJsonTypeInGenerateTweetsIDSJSONFromAllJSON() throws UnknownHostException {
		Boolean downloadLimited = true;
		persister4TaggerAPI.generateTweetsIDSJSONFromAllJSON(existedCollectionCode, downloadLimited, null);
	}

	@Test(expected = NullPointerException.class)
	public void testNullPointerExceptionOnInvalidDownloadLimitedInGenerateTweetsIDSJSONFromAllJSON() throws UnknownHostException {
		Boolean downloadLimited = null;
		String jsonType = "TEXT_JSON";
		persister4TaggerAPI.generateTweetsIDSJSONFromAllJSON(existedCollectionCode, downloadLimited, jsonType);
	}


	@Test
	public void testGenerateCSVFromLastestJSONFiltered() throws UnknownHostException {
		int exportLimit = 50;

		String queryString = "{ \"constraints\": [ { \"queryType\": \"date_query\", \"comparator\": \"is_before\", \"timestamp\": 1427375693 },"
				+ " { \"queryType\": \"date_query\", \"comparator\": \"is_after\", \"timestamp\": 1427352427 } ] }";

		Response response = persister4TaggerAPI.generateCSVFromLastestJSONFiltered(queryString, existedCollectionCode, exportLimit, userName);
		assertEquals(200, response.getStatus());
		assertEquals(true, response.hasEntity());
		assertEquals(false, response.bufferEntity());

		queryString = "{ \"constraints\": [ { \"queryType\": \"date_query\", \"comparator\": \"is_before\", \"timestamp\": 1427375693 }, "
				+ "{ \"queryType\": \"date_query\", \"comparator\": \"is_after\", \"timestamp\": 1427352427 }, "
				+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": \"praying\", \"comparator\": \"is\", \"min_confidence\": 0.8 }, "
				+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": \"030_info\", \"comparator\": \"is_not\" }, "
				+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": null, \"comparator\": \"has_confidence\", \"min_confidence\": 0.5 } ] }";

		response = persister4TaggerAPI.generateCSVFromLastestJSONFiltered(queryString, existedCollectionCode, exportLimit, userName);
		assertEquals(200, response.getStatus());
		assertEquals(true, response.hasEntity());
		assertEquals(false, response.bufferEntity());
	}

	@Test
	public void testOverloadedMethodGenerateCSVFromLastestJSONFiltered() throws UnknownHostException {
		int exportLimit = 50;
		Response response = persister4TaggerAPI.generateCSVFromLastestJSONFiltered(existedCollectionCode, exportLimit);
		assertEquals(200, response.getStatus());
		assertEquals(false, response.hasEntity());
		assertEquals(false, response.bufferEntity());
	}

	@Test(expected = NullPointerException.class)
	public void testNullPointerExceptionOnInvalidCollectionCodeInGenerateCSVFromLastestJSONFiltered() throws UnknownHostException {
		int exportLimit = 50;
		String queryString = "{ \"constraints\": [ { \"queryType\": \"date_query\", \"comparator\": \"is_before\", \"timestamp\": 1427375693 }, "
				+ "{ \"queryType\": \"date_query\", \"comparator\": \"is_after\", \"timestamp\": 1427352427 }, "
				+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": \"praying\", \"comparator\": \"is\", \"min_confidence\": 0.8 }, "
				+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": \"030_info\", \"comparator\": \"is_not\" }, "
				+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": null, \"comparator\": \"has_confidence\", \"min_confidence\": 0.5 } ] }";

		Response response = persister4TaggerAPI.generateCSVFromLastestJSONFiltered(queryString, sampleCollectionCode, exportLimit, userName);
		assertEquals(200, response.getStatus());
		assertEquals(true, response.hasEntity());
		assertEquals(false, response.bufferEntity());
	}

	@Test(expected = NullPointerException.class)
	public void testNullPointerExceptionOnInvalidQueryStringInGenerateCSVFromLastestJSONFiltered() throws UnknownHostException {
		int exportLimit = 50;
		String queryString = null;
		Response response = persister4TaggerAPI.generateCSVFromLastestJSONFiltered(queryString, existedCollectionCode, exportLimit, userName);
		assertEquals(200, response.getStatus());
		assertEquals(true, response.hasEntity());
		assertEquals(false, response.bufferEntity());
	}

	@Test
	public void testWithInvalidCollectionCodeInGenerateCSVFromLastestJSONFiltered() throws UnknownHostException {
		int exportLimit = 50;
		Response response = persister4TaggerAPI.generateCSVFromLastestJSONFiltered(sampleCollectionCode, exportLimit);

		assertEquals(200, response.getStatus());
		assertEquals(false, response.hasEntity());
		assertEquals(false, response.bufferEntity());
	}

	@Test
	public void testGenerateTweetsIDSCSVFromAllJSONFiltered() throws UnknownHostException {
		Boolean downloadLimited = true;
		String queryString = "{ \"constraints\": [ { \"queryType\": \"date_query\", \"comparator\": \"is_before\", \"timestamp\": 1427375693 },"
				+ " { \"queryType\": \"date_query\", \"comparator\": \"is_after\", \"timestamp\": 1427352427 } ] }";
		Response response = persister4TaggerAPI.generateTweetsIDSCSVFromAllJSONFiltered(queryString, existedCollectionCode, downloadLimited, userName);
		assertEquals(200, response.getStatus());
		assertEquals(true, response.hasEntity());
		assertEquals(false, response.bufferEntity());

		queryString = "{ \"constraints\": [ { \"queryType\": \"date_query\", \"comparator\": \"is_before\", \"timestamp\": 1427375693 }, "
				+ "{ \"queryType\": \"date_query\", \"comparator\": \"is_after\", \"timestamp\": 1427352427 }, "
				+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": \"praying\", \"comparator\": \"is\", \"min_confidence\": 0.8 }, "
				+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": \"030_info\", \"comparator\": \"is_not\" }, "
				+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": null, \"comparator\": \"has_confidence\", \"min_confidence\": 0.5 } ] }";

		response = persister4TaggerAPI.generateTweetsIDSCSVFromAllJSONFiltered(queryString, existedCollectionCode, downloadLimited, userName);
		assertEquals(200, response.getStatus());
		assertEquals(true, response.hasEntity());
		assertEquals(false, response.bufferEntity());
	}

	@Test(expected = NullPointerException.class)
	public void testNullPointerExceptionOnInvalidCollectionCodeInGenerateTweetsIDSCSVFromAllJSONFiltered() throws UnknownHostException {
		Boolean downloadLimited = true;
		String queryString = "{ \"constraints\": [ { \"queryType\": \"date_query\", \"comparator\": \"is_before\", \"timestamp\": 1427375693 }, "
				+ "{ \"queryType\": \"date_query\", \"comparator\": \"is_after\", \"timestamp\": 1427352427 }, "
				+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": \"praying\", \"comparator\": \"is\", \"min_confidence\": 0.8 }, "
				+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": \"030_info\", \"comparator\": \"is_not\" }, "
				+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": null, \"comparator\": \"has_confidence\", \"min_confidence\": 0.5 } ] }";

		Response response = persister4TaggerAPI.generateTweetsIDSCSVFromAllJSONFiltered(queryString, sampleCollectionCode, downloadLimited, userName);
		assertEquals(200, response.getStatus());
		assertEquals(true, response.hasEntity());
		assertEquals(false, response.bufferEntity());
	}

	@Test(expected = NullPointerException.class)
	public void testNullPointerExceptionOnInvalidQueryStringInGenerateTweetsIDSCSVFromAllJSONFiltered() throws UnknownHostException {
		Boolean downloadLimited = true;
		String queryString = null;
		Response response = persister4TaggerAPI.generateTweetsIDSCSVFromAllJSONFiltered(queryString, existedCollectionCode, downloadLimited, userName);
		assertEquals(200, response.getStatus());
		assertEquals(true, response.hasEntity());
		assertEquals(false, response.bufferEntity());
	}

	@Test
	public void testOverloadedMethodGenerateTweetsIDSCSVFromAllJSONFiltered() throws UnknownHostException {
		Boolean downloadLimited = true;
		Response response = persister4TaggerAPI.generateTweetsIDSCSVFromAllJSONFiltered(existedCollectionCode, downloadLimited);
		assertEquals(200, response.getStatus());
		assertEquals(false, response.hasEntity());
		assertEquals(false, response.bufferEntity());
	}

	@Test
	public void testInvalidCollectionCodeOnOverloadedMethodGenerateTweetsIDSCSVFromAllJSONFiltered() throws UnknownHostException {
		Boolean downloadLimited = true;
		Response response= persister4TaggerAPI.generateTweetsIDSCSVFromAllJSONFiltered(sampleCollectionCode, downloadLimited);
		assertEquals(200, response.getStatus());
		assertEquals(false, response.hasEntity());
		assertEquals(false, response.bufferEntity());
	}

	@Test
	public void testGenerateJSONFromLastestJSONFiltered() throws UnknownHostException {
		int exportLimit = 50;
		String jsonType = "TEXT_JSON";
		String queryString = "{ \"constraints\": [ { \"queryType\": \"date_query\", \"comparator\": \"is_before\", \"timestamp\": 1427375693 },"
				+ " { \"queryType\": \"date_query\", \"comparator\": \"is_after\", \"timestamp\": 1427352427 } ] }";
		Response response = persister4TaggerAPI.generateJSONFromLastestJSONFiltered(queryString, existedCollectionCode, exportLimit, jsonType, userName);
		assertEquals(200, response.getStatus());
		assertEquals(true, response.hasEntity());
		assertEquals(false, response.bufferEntity());

		queryString = "{ \"constraints\": [ { \"queryType\": \"date_query\", \"comparator\": \"is_before\", \"timestamp\": 1427375693 }, "
				+ "{ \"queryType\": \"date_query\", \"comparator\": \"is_after\", \"timestamp\": 1427352427 }, "
				+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": \"praying\", \"comparator\": \"is\", \"min_confidence\": 0.8 }, "
				+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": \"030_info\", \"comparator\": \"is_not\" }, "
				+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": null, \"comparator\": \"has_confidence\", \"min_confidence\": 0.5 } ] }";

		response = persister4TaggerAPI.generateJSONFromLastestJSONFiltered(queryString, existedCollectionCode, exportLimit, jsonType, userName);
		assertEquals(200, response.getStatus());
		assertEquals(true, response.hasEntity());
		assertEquals(false, response.bufferEntity());
	}

	@Test
	public void testWithInvalidCollectionCodeInGenerateJSONFromLastestJSONFiltered() throws UnknownHostException {
		int exportLimit = 50;
		String jsonType = "TEXT_JSON";
		String queryString = "{ \"constraints\": [ { \"queryType\": \"date_query\", \"comparator\": \"is_before\", \"timestamp\": 1427375693 }, "
				+ "{ \"queryType\": \"date_query\", \"comparator\": \"is_after\", \"timestamp\": 1427352427 }, "
				+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": \"praying\", \"comparator\": \"is\", \"min_confidence\": 0.8 }, "
				+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": \"030_info\", \"comparator\": \"is_not\" }, "
				+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": null, \"comparator\": \"has_confidence\", \"min_confidence\": 0.5 } ] }";

		Response response = persister4TaggerAPI.generateJSONFromLastestJSONFiltered(queryString, sampleCollectionCode, exportLimit, jsonType, userName);
		assertEquals(200, response.getStatus());
		assertEquals(true, response.hasEntity());
		assertEquals(false, response.bufferEntity());
	}

	@Test(expected = NullPointerException.class)
	public void testNullPointerExceptionOnInvalidQueryStringInGenerateJSONFromLastestJSONFiltered() throws UnknownHostException {
		int exportLimit = 50;
		String jsonType = "TEXT_JSON";
		String queryString = null;
		Response response = persister4TaggerAPI.generateJSONFromLastestJSONFiltered(queryString, existedCollectionCode, exportLimit, jsonType, userName);
		assertEquals(200, response.getStatus());
		assertEquals(true, response.hasEntity());
		assertEquals(false, response.bufferEntity());
	}

	@Test(expected = NullPointerException.class)
	public void testNullPointerExceptionOnInvalidJsonTypeInGenerateJSONFromLastestJSONFiltered() throws UnknownHostException {
		int exportLimit = 50;
		String jsonType = null;
		String queryString = "{ \"constraints\": [] }";
		persister4TaggerAPI.generateJSONFromLastestJSONFiltered(queryString, existedCollectionCode, exportLimit, jsonType, userName);
	}

	@Test
	public void testGenerateTweetsIDSJSONFromAllJSONFiltered() throws UnknownHostException {
		Boolean downloadLimited = true;
		String jsonType = "TEXT_JSON";

		String queryString = "{ \"constraints\": [ { \"queryType\": \"date_query\", \"comparator\": \"is_before\", \"timestamp\": 1427375693 },"
				+ " { \"queryType\": \"date_query\", \"comparator\": \"is_after\", \"timestamp\": 1427352427 } ] }";
		Response response = persister4TaggerAPI.generateTweetsIDSJSONFromAllJSONFiltered(queryString, existedCollectionCode , downloadLimited, jsonType, userName);
		assertEquals(200, response.getStatus());
		assertEquals(true, response.hasEntity());
		assertEquals(false, response.bufferEntity());

		queryString = "{ \"constraints\": [ { \"queryType\": \"date_query\", \"comparator\": \"is_before\", \"timestamp\": 1427375693 }, "
				+ "{ \"queryType\": \"date_query\", \"comparator\": \"is_after\", \"timestamp\": 1427352427 }, "
				+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": \"praying\", \"comparator\": \"is\", \"min_confidence\": 0.8 }, "
				+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": \"030_info\", \"comparator\": \"is_not\" }, "
				+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": null, \"comparator\": \"has_confidence\", \"min_confidence\": 0.5 } ] }";

		response = persister4TaggerAPI.generateTweetsIDSJSONFromAllJSONFiltered(queryString, existedCollectionCode , downloadLimited, jsonType, userName);
		assertEquals(200, response.getStatus());
		assertEquals(true, response.hasEntity());
		assertEquals(false, response.bufferEntity());
	}

	@Test(expected = NullPointerException.class)
	public void testNullPointerExceptionOnInvalidCollectionCodeInGenerateTweetsIDSJSONFromAllJSONFiltered() throws UnknownHostException {
		Boolean downloadLimited = true;
		String jsonType = "TEXT_JSON";
		String queryString =  "{ \"constraints\": [] }";
		persister4TaggerAPI.generateTweetsIDSJSONFromAllJSONFiltered(queryString, sampleCollectionCode , downloadLimited, jsonType, userName);
	}

	@Test(expected = NullPointerException.class)
	public void testNullPointerExceptionOnInvalidJsonTypeInGenerateTweetsIDSJSONFromAllJSONFiltered() throws UnknownHostException {
		Boolean downloadLimited = true;
		String jsonType = null;
		String queryString =  "{ \"constraints\": [] }";
		Response response = persister4TaggerAPI.generateTweetsIDSJSONFromAllJSONFiltered(queryString, existedCollectionCode , downloadLimited, jsonType, userName);
		assertEquals(200, response.getStatus());
		assertEquals(true, response.hasEntity());
		assertEquals(false, response.bufferEntity());
	}

	@Test
	public void testNullPointerExceptionOnInvalidDownloadLimitedInGenerateTweetsIDSJSONFromAllJSONFiltered() throws UnknownHostException {
		Boolean downloadLimited = null;
		String jsonType = "TEXT_JSON";
		String queryString =  "{ \"constraints\": [] }";
		Response response = persister4TaggerAPI.generateTweetsIDSJSONFromAllJSONFiltered(queryString, existedCollectionCode , downloadLimited, jsonType, userName);
		assertEquals(200, response.getStatus());
		assertEquals(true, response.hasEntity());
		assertEquals(false, response.bufferEntity());
	}

	@Test(expected = NullPointerException.class)
	public void testNullPointerExceptionOnInvalidQueryStringInGenerateTweetsIDSJSONFromAllJSONFiltered() throws UnknownHostException {
		Boolean downloadLimited = true;
		String jsonType = "TEXT_JSON";
		String queryString = null;
		Response response = persister4TaggerAPI.generateTweetsIDSJSONFromAllJSONFiltered(queryString, existedCollectionCode , downloadLimited, jsonType, userName);
		assertEquals(200, response.getStatus());
		assertEquals(true, response.hasEntity());
		assertEquals(false, response.bufferEntity());
	}
}
