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

public class Persister4CollectorAPITest {
	static Persister4CollectorAPI persister4CollectorAPI;
	static String existedCollectionCode = "test_collection_code";
	static String sampleCollectionCode = "sample_collection_ode";
	private static Logger logger = Logger.getLogger(Persister4CollectorAPITest.class.getName());

	private static Configurator configurator = PersisterConfigurator
			.getInstance();

	@BeforeClass
	public static void setUpBeforeClass() {
		configurator.initProperties(PersisterConfigurator.configLoadFileName,PersisterConfigurationProperty.values());
		persister4CollectorAPI = new Persister4CollectorAPI();
		//Creating a sample persister directory
		File folderLocation = new File(configurator.getProperty(PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH) + existedCollectionCode);
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

	@AfterClass
	public static void tearDownAfterClass() {
		//Deleting the sample persister directory
		File folderLocation = new File(configurator.getProperty(PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH) + existedCollectionCode);

		try {
			FileUtils.deleteDirectory(folderLocation);
		} catch (IOException e) {
			logger.info("Unable to delete sample directory in unit test in Persister4CollectorAPITest");
			e.printStackTrace();
		}
	}

	@Test
	public void testGenerateCSVFromLastestJSON() throws UnknownHostException {
		int exportLimit= 50;
		Response response = persister4CollectorAPI.generateCSVFromLastestJSON(existedCollectionCode, exportLimit);
		assertEquals(200, response.getStatus());
		assertEquals(true, response.hasEntity());
		assertEquals(false, response.bufferEntity());

		response = persister4CollectorAPI.generateCSVFromLastestJSON(existedCollectionCode, 0);
		assertEquals(200, response.getStatus());
		assertEquals(true, response.hasEntity());
		assertEquals(false, response.bufferEntity());
	}

	@Test(expected = NullPointerException.class)
	public void testNullPointerExceptionOnInvalidCollectionCodeInGenerateCSVFromLastestJSON() throws UnknownHostException {
		int exportLimit= 50;
		persister4CollectorAPI.generateCSVFromLastestJSON(sampleCollectionCode, exportLimit);
	}

	@Test
	public void testGenerateTweetsIDSCSVFromAllJSON() throws UnknownHostException {
		boolean downloadLimited = false;
		Response response = persister4CollectorAPI.generateTweetsIDSCSVFromAllJSON(existedCollectionCode , downloadLimited);
		assertEquals(200, response.getStatus());
		assertEquals(true, response.hasEntity());
		assertEquals(false, response.bufferEntity());

		downloadLimited=true;
		response = persister4CollectorAPI.generateTweetsIDSCSVFromAllJSON(existedCollectionCode , downloadLimited);
		assertEquals(200, response.getStatus());
		assertEquals(true, response.hasEntity());
		assertEquals(false, response.bufferEntity());
	}


	@Test(expected = NullPointerException.class)
	public void testNullPointerExceptionOnInvalidCollectionCodeInGenerateTweetsIDSCSVFromAllJSON() throws UnknownHostException {
		boolean downloadLimited = false;
		persister4CollectorAPI.generateTweetsIDSCSVFromAllJSON(sampleCollectionCode , downloadLimited);
	}

	@Test
	public void testGenerateJSONFromLastestJSONTest() throws UnknownHostException {
		String jsonType = "TEXT_JSON";
		Response response = persister4CollectorAPI.generateJSONFromLastestJSON(existedCollectionCode, jsonType);
		assertEquals(200, response.getStatus());
		assertEquals(true, response.hasEntity());
		assertEquals(false, response.bufferEntity());

		jsonType = "JSON";
		response = persister4CollectorAPI.generateJSONFromLastestJSON(existedCollectionCode, jsonType);
		assertEquals(200, response.getStatus());
		assertEquals(true, response.hasEntity());
		assertEquals(false, response.bufferEntity());
	}

	@Test(expected = NullPointerException.class)
	public void testNullPointerExceptionOnInvalidCollectionCodeInGenerateJSONFromLastestJSON() throws UnknownHostException {
		String jsonType = "TEXT_JSON";
		persister4CollectorAPI.generateJSONFromLastestJSON(sampleCollectionCode, jsonType);
	}

	@Test(expected = NullPointerException.class)
	public void testNullPointerExceptionOnInvalidJsonTypeInGenerateJSONFromLastestJSON() throws UnknownHostException {
		String jsonType = null;
		persister4CollectorAPI.generateJSONFromLastestJSON(existedCollectionCode, jsonType);
	}

	@Test
	public void testGenerateTweetsIDSJSONFromAllJSON() throws UnknownHostException {
		Boolean downloadLimited = true;
		String jsonType = "json";
		Response response = persister4CollectorAPI.generateTweetsIDSJSONFromAllJSON(existedCollectionCode, downloadLimited, jsonType);
		assertEquals(200, response.getStatus());
		assertEquals(true, response.hasEntity());
		assertEquals(false, response.bufferEntity());

		downloadLimited = false;
		response = persister4CollectorAPI.generateTweetsIDSJSONFromAllJSON(existedCollectionCode, downloadLimited, jsonType);
		assertEquals(200, response.getStatus());
		assertEquals(true, response.hasEntity());
		assertEquals(false, response.bufferEntity());
	}

	@Test(expected = NullPointerException.class)
	public void testNullPointerExceptionOnInvalidCollectionCodeInGenerateTweetsIDSJSONFromAllJSON() throws UnknownHostException {
		Boolean downloadLimited = true;
		String jsonType = "json";
		Response response = persister4CollectorAPI.generateTweetsIDSJSONFromAllJSON(sampleCollectionCode, downloadLimited, jsonType);
		assertEquals(200, response.getStatus());
		assertEquals(true, response.hasEntity());
		assertEquals(false, response.bufferEntity());
	}

	@Test(expected = NullPointerException.class)
	public void testNullPointerExceptionOnInvalidJsonTypeInGenerateTweetsIDSJSONFromAllJSON() throws UnknownHostException {
		Boolean downloadLimited = true;
		String jsonType = null;
		Response response = persister4CollectorAPI.generateTweetsIDSJSONFromAllJSON(existedCollectionCode, downloadLimited, jsonType);
		assertEquals(200, response.getStatus());
		assertEquals(true, response.hasEntity());
		assertEquals(false, response.bufferEntity());
	}
}
