package qa.qcri.aidr.analysis.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import qa.qcri.aidr.common.code.DateFormatConfig;
import qa.qcri.aidr.common.logging.ErrorLog;

public class GranularityData {
	// Debugging
	private static Logger logger = Logger.getLogger(GranularityData.class);
	private static ErrorLog elog = new ErrorLog();

	private static String configloadFileName = "granularity.properties";
	
	public static List<Long> getGranularities() {
		List<Long> granularityList = new ArrayList<Long>();
		Map<String, String> granularities = getConfigProperties(configloadFileName);
		if (granularities != null) {
			String[] values = granularities.get("granularity").split(",");
			for (int i = 0;i < values.length;i++) {
				granularityList.add(DateFormatConfig.parseTime(values[i]));
			}
			Collections.sort(granularityList);
		} else {
			return null;
		}
		return granularityList;
	}

	public static HashMap<String, String> getConfigProperties(final String fileName) {
		Properties prop = new Properties();
		InputStream input = null;
		HashMap<String, String>properties = new HashMap<String, String>();
		try {
			//input = new FileInputStream(fileName); 
			input = GranularityData.class.getClassLoader().getResourceAsStream(fileName);	

			// load a properties file
			prop.load(input);

			// get the property values
			Iterator<Object> itr = prop.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String) itr.next();
				properties.put(key, prop.getProperty(key));
				System.out.println("Found property: " + key + ", value = " + properties.get(key));
			}
			return properties;

		} catch (IOException e) {
			logger.error("Error in reading properties file: " + fileName);
			System.err.println("Error in reading properties file: " + fileName);
			logger.error(e);
			return null;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					logger.error("Erorr in closing config file");
				}
			}
		}
	}

}
