package qa.qcri.aidr.collector.utils;

import org.apache.log4j.Logger;

import qa.qcri.aidr.common.code.impl.BaseConfigurator;
import qa.qcri.aidr.common.exception.ConfigurationPropertyFileException;
import qa.qcri.aidr.common.exception.ConfigurationPropertyNotRecognizedException;
import qa.qcri.aidr.common.exception.ConfigurationPropertyNotSetException;

/**
 * User: yakubenkova.elena@gmail.com Date: 29.09.14
 */
public class CollectorConfigurator extends BaseConfigurator {

	private static final Logger LOGGER = Logger
			.getLogger(CollectorConfigurator.class);

	public static final String configLoadFileName = "config.properties";

	private static final CollectorConfigurator instance = new CollectorConfigurator();

	private CollectorConfigurator() {
		LOGGER.info("Creating instance.");
		this.initProperties(
				CollectorConfigurator.configLoadFileName,
				CollectorConfigurationProperty.values());
	
	}

	public static CollectorConfigurator getInstance()
			throws ConfigurationPropertyNotSetException,
			ConfigurationPropertyNotRecognizedException,
			ConfigurationPropertyFileException {
		return instance;
	}
}
