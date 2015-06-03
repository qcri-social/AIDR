package qa.qcri.aidr;

import static org.junit.Assert.assertNotSame;

import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import qa.qcri.aidr.common.code.Configurator;
import qa.qcri.aidr.utils.PersisterConfigurator;
import qa.qcri.aidr.utils.PersisterConfigurationProperty;

public class ConfigPropertiesTest {
	private static Logger logger = Logger.getLogger(ConfigPropertiesTest.class.getName());
	
	private static Configurator configurator = PersisterConfigurator.getInstance();
	
	@Before
	public void setUp() throws Exception {
		configurator.initProperties(
				PersisterConfigurator.configLoadFileName,
				PersisterConfigurationProperty.values());
	}

	
	@Test
	public void testDefaultPersisterFilePath() throws UnknownHostException {
		String response = configurator.getProperty(PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH);
		logger.info("Executing test for testDefaultPersisterFilePath in ConfigPropertiesTest");
		assertNotSame("Default persister file path not exists in the config file ",null, response.toString());
	}

	@Test
	public void testSCD1_URL() throws UnknownHostException {
		String response = configurator.getProperty(PersisterConfigurationProperty.SCD1_URL);
		logger.info("Executing test for testSCD1_URL in ConfigPropertiesTest");
		assertNotSame("SCD1_URL not exists in the config file ",null, response.toString());
	}

	@Test
	public void testDefaultFileVolumeLimit() throws UnknownHostException {
		String response = configurator.getProperty(PersisterConfigurationProperty.DEFAULT_FILE_VOLUMN_LIMIT);
		logger.info("Executing test for testDefaultFileVolumeLimit in ConfigPropertiesTest");
		assertNotSame("DEFAULT_FILE_VOLUMN_LIMIT not exists in the config file ",null, response.toString());
	}
	
	@Test
	public void testTweetsExportLimit100K() throws UnknownHostException {
		String response =configurator.getProperty(PersisterConfigurationProperty.TWEETS_EXPORT_LIMIT_100K);
		logger.info("Executing test for testTweetsExportLimit100K in ConfigPropertiesTest");
		assertNotSame("TWEETS_EXPORT_LIMIT_100K not exists in the config file ",null, response.toString());
	}
	
	@Test
	public void testDefaultFileBufferSize() throws UnknownHostException {
		String response = configurator.getProperty(PersisterConfigurationProperty.DEFAULT_FILE_WRITER_BUFFER_SIZE);
		logger.info("Executing test for testDefaultFileBufferSize in ConfigPropertiesTest");
		assertNotSame("DEFAULT_FILE_WRITER_BUFFER_SIZE not exists in the config file ",null, response.toString());
	}
	
	@Test
	public void testLogFileName() throws UnknownHostException {
		String response = configurator.getProperty(PersisterConfigurationProperty.LOG_FILE_NAME);
		logger.info("Executing test for testLogFileName in ConfigPropertiesTest");
		assertNotSame("LOG_FILE_NAME not exists in the config file ",null, response.toString());
	}
}
