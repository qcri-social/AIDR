package qa.qcri.aidr.trainer.api.util;

import org.apache.log4j.Logger;

import qa.qcri.aidr.common.code.impl.BaseConfigurator;
import qa.qcri.aidr.common.exception.ConfigurationPropertyFileException;
import qa.qcri.aidr.common.exception.ConfigurationPropertyNotRecognizedException;
import qa.qcri.aidr.common.exception.ConfigurationPropertyNotSetException;

public class TrainerConfigurator extends BaseConfigurator {

	private static final Logger LOGGER = Logger
			.getLogger(TrainerConfigurator.class);

	public static final String configLoadFileName = "config.properties";

	private static final TrainerConfigurator instance = new TrainerConfigurator();

	private TrainerConfigurator() {
		LOGGER.info("Initializing Properties for aidr-manager.");
		this.initProperties(configLoadFileName, TrainerConfigurationProperty.values());
	}

	public static TrainerConfigurator getInstance()
			throws ConfigurationPropertyNotSetException,
			ConfigurationPropertyNotRecognizedException,
			ConfigurationPropertyFileException {
		return instance;
	}

}
