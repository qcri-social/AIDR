package qa.qcri.aidr.predict.common;

import java.io.File;

import org.apache.log4j.Logger;

import qa.qcri.aidr.common.code.Configurator;
import qa.qcri.aidr.common.code.impl.BaseConfigurator;
import qa.qcri.aidr.common.exception.DirectoryNotWritableException;

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
		
		this.directoryIsWritable(TaggerConfigurationProperty.MODEL_STORE_PATH);
	}

	public static Configurator getInstance() {
		return instance;
	}

	private void directoryIsWritable(TaggerConfigurationProperty propertyName) throws DirectoryNotWritableException{
		String directoryLocation = this.getProperty(propertyName);
		File f = new File(directoryLocation);
		if(!f.canWrite()) {
			LOGGER.info(propertyName.getName()+ " = " +directoryLocation+ " is not writable. Please verify if this is a valid writable directory.");
			throw new DirectoryNotWritableException(propertyName.getName(), directoryLocation);
		}
	}
}