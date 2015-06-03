package qa.qcri.aidr.manager.util;

import org.apache.log4j.Logger;

import qa.qcri.aidr.common.code.impl.BaseConfigurator;
import qa.qcri.aidr.common.exception.ConfigurationPropertyFileException;
import qa.qcri.aidr.common.exception.ConfigurationPropertyNotRecognizedException;
import qa.qcri.aidr.common.exception.ConfigurationPropertyNotSetException;

public class ManagerConfigurator extends BaseConfigurator {

	private static final Logger LOGGER = Logger
			.getLogger(ManagerConfigurator.class);

	public static final String configLoadFileName = "system.properties";

	private static final ManagerConfigurator instance = new ManagerConfigurator();

	private ManagerConfigurator() {
		LOGGER.info("Initializing Properties for aidr-manager.");
		this.initProperties(configLoadFileName, ManagerConfigurationProperty.values());
	}

	public static ManagerConfigurator getInstance()
			throws ConfigurationPropertyNotSetException,
			ConfigurationPropertyNotRecognizedException,
			ConfigurationPropertyFileException {
		return instance;
	}

}
