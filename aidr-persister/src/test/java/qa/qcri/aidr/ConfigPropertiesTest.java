package qa.qcri.aidr;

import static org.junit.Assert.assertNotSame;
import static qa.qcri.aidr.utils.ConfigProperties.getProperty;

import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.junit.Test;

public class ConfigPropertiesTest {
	private static Logger logger = Logger.getLogger(ConfigPropertiesTest.class.getName());
	
	@Test
	public void testDefaultPersisterFilePath() throws UnknownHostException {
		String response = getProperty("DEFAULT_PERSISTER_FILE_PATH");
		logger.info("Executing test for testDefaultPersisterFilePath in ConfigPropertiesTest");
		assertNotSame("Default persister file path not exists in the config file ",null, response.toString());
	}

	@Test
	public void testSCD1_URL() throws UnknownHostException {
		String response = getProperty("SCD1_URL");
		logger.info("Executing test for testSCD1_URL in ConfigPropertiesTest");
		assertNotSame("SCD1_URL not exists in the config file ",null, response.toString());
	}

	@Test
	public void testDefaultFileVolumeLimit() throws UnknownHostException {
		String response = getProperty("DEFAULT_FILE_VOLUMN_LIMIT");
		logger.info("Executing test for testDefaultFileVolumeLimit in ConfigPropertiesTest");
		assertNotSame("DEFAULT_FILE_VOLUMN_LIMIT not exists in the config file ",null, response.toString());
	}
	
	@Test
	public void testTweetsExportLimit100K() throws UnknownHostException {
		String response = getProperty("TWEETS_EXPORT_LIMIT_100K");
		logger.info("Executing test for testTweetsExportLimit100K in ConfigPropertiesTest");
		assertNotSame("TWEETS_EXPORT_LIMIT_100K not exists in the config file ",null, response.toString());
	}
	
	@Test
	public void testDefaultFileBufferSize() throws UnknownHostException {
		String response = getProperty("DEFAULT_FILE_WRITER_BUFFER_SIZE");
		logger.info("Executing test for testDefaultFileBufferSize in ConfigPropertiesTest");
		assertNotSame("DEFAULT_FILE_WRITER_BUFFER_SIZE not exists in the config file ",null, response.toString());
	}
	
	@Test
	public void testLogFileName() throws UnknownHostException {
		String response = getProperty("LOG_FILE_NAME");
		logger.info("Executing test for testLogFileName in ConfigPropertiesTest");
		assertNotSame("LOG_FILE_NAME not exists in the config file ",null, response.toString());
	}
}
