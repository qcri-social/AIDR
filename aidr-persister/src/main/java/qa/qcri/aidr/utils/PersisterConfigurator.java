package qa.qcri.aidr.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import qa.qcri.aidr.common.code.impl.BaseConfigurator;

/**
 * User: yakubenkova.elena@gmail.com Date: 29.09.14
 */
public class PersisterConfigurator extends BaseConfigurator {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PersisterConfigurator.class);

	public static final String configLoadFileName = "config.properties";

	private static final PersisterConfigurator instance = new PersisterConfigurator();

	private PersisterConfigurator() {
		LOGGER.info("Instantiating PersisterConfigurator,");
	}

	public static PersisterConfigurator getInstance() {
		return instance;
	}

}
