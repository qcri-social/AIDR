package qa.qcri.aidr.predict.common;

import org.apache.log4j.Logger;

import qa.qcri.aidr.common.code.Configurator;
import qa.qcri.aidr.common.code.impl.BaseConfigurator;

/**
 * User: yakubenkova.elena@gmail.com Date: 29.09.14
 */
public class TaggerConfigurator extends BaseConfigurator {

	private static final Logger LOGGER = Logger
			.getLogger(TaggerConfigurator.class);

	public static final String configLoadFileName = "config.properties";

	private static final Configurator instance = new TaggerConfigurator();

	private TaggerConfigurator() {
		LOGGER.info("Instantiating TaggerConfigurator,");
		this.initProperties(configLoadFileName, TaggerConfigurationProperty.values());
	}

	public static Configurator getInstance() {
		return instance;
	}

}