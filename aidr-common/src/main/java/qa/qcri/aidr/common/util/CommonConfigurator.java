package qa.qcri.aidr.common.util;

import qa.qcri.aidr.common.code.impl.BaseConfigurator;
import qa.qcri.aidr.common.exception.ConfigurationPropertyFileException;
import qa.qcri.aidr.common.exception.ConfigurationPropertyNotRecognizedException;
import qa.qcri.aidr.common.exception.ConfigurationPropertyNotSetException;

public class CommonConfigurator extends BaseConfigurator {
	
	public static final String configLoadFileName = "common_config.properties";

	private static final CommonConfigurator instance = new CommonConfigurator();

	private CommonConfigurator() {
		this.initProperties(
				CommonConfigurator.configLoadFileName,
				CommonConfigurationProperty.values());
	}

	public static CommonConfigurator getInstance()
			throws ConfigurationPropertyNotSetException,
			ConfigurationPropertyNotRecognizedException,
			ConfigurationPropertyFileException {
		return instance;
	}

}
