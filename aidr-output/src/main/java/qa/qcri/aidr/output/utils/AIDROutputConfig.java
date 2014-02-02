/**
 * @author Koushik Sinha
 * 
 * Run this java application to set the configuration parameters for aidr-output
 */

package qa.qcri.aidr.output.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

public class AIDROutputConfig {

	private static String configsaveFileName = "src/main/resources/config.properties";		// default properties file
	private static String configloadFileName = "config.properties";		
	/**
	 * 
	 * @param propMap properties to store, in the form of a HashMap
	 */
	public void setConfigProperties(final HashMap<String, String> propMap) {
		Properties prop = new Properties();
		OutputStream output = null;

		Iterator<String>itr = propMap.keySet().iterator();
		try {
			output = new FileOutputStream(configsaveFileName);
			while (itr.hasNext()) {
				final String key = itr.next(); 
				prop.setProperty(key, propMap.get(key));
			}
			// save properties to project root folder
			prop.store(output, null);

		} catch (IOException e) {
			System.err.println("Error in saving to property file: " + configsaveFileName);
			e.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @return Map of properties from default properties file
	 */
	public HashMap<String, String> getConfigProperties() {
		return getConfigProperties(configloadFileName);	
	}
	
	/**
	 * 
	 * @param fileName properties filename
	 * @return HashMap of properties from specified properties file
	 */
	public HashMap<String, String> getConfigProperties(final String fileName) {
		Properties prop = new Properties();
		InputStream input = null;
		HashMap<String, String>properties = new HashMap<String, String>();
		try {
			//input = new FileInputStream(fileName); 
			input = this.getClass().getClassLoader().getResourceAsStream(configloadFileName);	
			
			// load a properties file
			prop.load(input);

			// get the property values
			Iterator<Object> itr = prop.keySet().iterator();
			while (itr.hasNext()) {
				String key = itr.next().toString();
				properties.put(key, prop.getProperty(key));
			}
			return properties;

		} catch (IOException e) {
			System.err.println("Error in reading properties file: " + configloadFileName);
			e.printStackTrace();
			return null;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void setConfigFile(String filename) {
		configsaveFileName = filename;
	}
	
	public String getConfigFile() {
		return configloadFileName;
	}

	public static void main(String args[]) {
		AIDROutputConfig config = new AIDROutputConfig();
		HashMap<String, String>propMap = new HashMap<String, String>();
		
		// Set properties here
		//propMap.put("host", "localhost");	
		//propMap.put("port", "1978");
		//propMap.put("logger", "slf4j");		// other option: "log4j"
		//config.setConfigProperties(propMap);
		
		// Test to see if properties were written correctly
		propMap.clear();
		propMap = null;
		propMap = config.getConfigProperties();
		System.out.println(propMap);
	}
}

