package qa.qcri.aidr.common.code;

import qa.qcri.aidr.common.exception.ConfigurationPropertyFileException;
import qa.qcri.aidr.common.exception.ConfigurationPropertyNotRecognizedException;
import qa.qcri.aidr.common.exception.ConfigurationPropertyNotSetException;
import qa.qcri.aidr.common.exception.DirectoryNotWritableException;

public interface Configurator {

	public void initProperties(String configLoadFileName,
			ConfigurationProperty[] configurationProperties)
			throws ConfigurationPropertyNotSetException,
			ConfigurationPropertyNotRecognizedException,
			ConfigurationPropertyFileException;

	public String getProperty(ConfigurationProperty property);
	public String getProperty(String propertyName);
	public void setProperty(String property, String newValue);
	public void directoryIsWritable(String propertyName) throws DirectoryNotWritableException;
}
