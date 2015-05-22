package qa.qcri.aidr.output.utils;

import org.apache.log4j.Logger;

import qa.qcri.aidr.common.code.impl.BaseConfigurator;
import qa.qcri.aidr.common.exception.ConfigurationPropertyFileException;
import qa.qcri.aidr.common.exception.ConfigurationPropertyNotRecognizedException;
import qa.qcri.aidr.common.exception.ConfigurationPropertyNotSetException;

/**
 * User: yakubenkova.elena@gmail.com Date: 29.09.14
 */

public class OutputConfigurator extends BaseConfigurator {

	private static final Logger LOGGER = Logger
			.getLogger(OutputConfigurator.class);

	public static final String configLoadFileName = "config.properties";

	private static final OutputConfigurator instance = new OutputConfigurator();

	private OutputConfigurator() {
		LOGGER.info("Initializing OutputConfigurator.");
		this.initProperties(configLoadFileName, OutputConfigurationProperty.values());	
	}

	public static OutputConfigurator getInstance()
			throws ConfigurationPropertyNotSetException,
			ConfigurationPropertyNotRecognizedException,
			ConfigurationPropertyFileException {
		return instance;
	}

}
