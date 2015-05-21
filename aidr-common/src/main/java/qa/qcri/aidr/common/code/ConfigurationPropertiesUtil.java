package qa.qcri.aidr.common.code;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.log4j.Logger;

import qa.qcri.aidr.common.exception.ConfigurationPropertyFileException;
import qa.qcri.aidr.common.exception.ConfigurationPropertyNotRecognizedException;
import qa.qcri.aidr.common.exception.ConfigurationPropertyNotSetException;

/**
 * 
 * @author dhruv-sharma
 * 
 *         Contains utility methods to deal with configuration property files in
 *         various modules.
 *
 */
public class ConfigurationPropertiesUtil {

	private static final Logger logger = Logger
			.getLogger(ConfigurationPropertiesUtil.class);

	private static Map<String, Boolean> getValidPropertyMap(
			ConfigurationProperty[] configProperties) {
		Map<String, Boolean> validPropertyMap = new HashMap<String, Boolean>();
		for (int index = 0; index < configProperties.length; index++) {
			String currentProperty = configProperties[index].getName();
			validPropertyMap.put(currentProperty, false);
		}
		return validPropertyMap;
	}

	public static HashMap<String, String> readConfigurations(
			ConfigurationProperty[] configProperties, String filePath)
			throws ConfigurationPropertyNotSetException,
			ConfigurationPropertyNotRecognizedException,
			ConfigurationPropertyFileException {
		Map<String, Boolean> validPropertyMap = getValidPropertyMap(configProperties);
		Properties prop = new Properties();
		InputStream input = null;
		HashMap<String, String> properties = new HashMap<String, String>();
		String currentPropertyName = null;

		try {
			// input = new FileInputStream(fileName);
			input = ConfigurationPropertiesUtil.class.getClassLoader()
					.getResourceAsStream(filePath);

			// load the properties file
			prop.load(input);

			// get the property values
			Iterator<Object> itr = prop.keySet().iterator();

			while (itr.hasNext()) {
				// Will go to the catch block in case of an entity which is not
				// expected.
				currentPropertyName = (String) itr.next();

				if (validPropertyMap.containsKey(currentPropertyName)) {
					properties.put(currentPropertyName,
							prop.getProperty(currentPropertyName));
					validPropertyMap.put(currentPropertyName, true);
				} else {
					logger.error("Encountered an unexpected property:'"
							+ currentPropertyName + "' in the file: "
							+ filePath);
					throw new ConfigurationPropertyNotRecognizedException(
							currentPropertyName, filePath);
				}
			}
			// Checking if all the properties required were available or not.
			for (Entry<String, Boolean> propertyMarker : validPropertyMap
					.entrySet()) {
				if (!propertyMarker.getValue()) {
					logger.error("All the required properties have not been set. Please verify the property file : "
							+ filePath);
					throw new ConfigurationPropertyNotSetException(
							propertyMarker.getKey(), filePath);
				}
			}
			return properties;
		} catch (IOException e) {
			logger.error("Error in reading properties file: " + filePath);
			System.err.println("Error in reading properties file: " + filePath);
			throw new ConfigurationPropertyFileException(filePath, e);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					logger.error("Erorr in closing config file: " + filePath);
				}
			}
		}

	}

}
