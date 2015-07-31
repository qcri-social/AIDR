package qa.qcri.aidr.common.code;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;


public class FasterXmlWrapper {
	private static Logger logger = Logger.getLogger(FasterXmlWrapper.class);
	
	public static ObjectMapper getObjectMapper() {
		try {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return objectMapper;
		} catch (Exception e) {
			logger.error("Exception in FasterXmlWrapper  " + e.getStackTrace());
			return null;
		}
	}
}
