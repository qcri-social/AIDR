package qa.qcri.aidr.predictui.util;

import qa.qcri.aidr.common.code.Configurator;
import qa.qcri.aidr.common.code.impl.BaseConfigurator;

import org.apache.log4j.Logger;

/**
 * User: yakubenkova.elena@gmail.com Date: 29.09.14
 */
public class TaggerAPIConfigurator extends BaseConfigurator {

	private static final Logger LOGGER = Logger
			.getLogger(TaggerAPIConfigurator.class);

	public static final String configLoadFileName = "config.properties";

	private static final Configurator instance = new TaggerAPIConfigurator();

	private TaggerAPIConfigurator() {
		LOGGER.info("Instantiating TaggerConfigurator,");
	}

	public static Configurator getInstance() {
		return instance;
	}

}
