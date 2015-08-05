package qa.qcri.aidr.common.code;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * A wrapper around an {@link ObjectMapper}.
 * 
 */
public class JacksonWrapper {
	private static Logger logger = Logger.getLogger(JacksonWrapper.class);
	/**
	 * Generates an {@link ObjectMapper} and configures it so that it does not fail on unknown properties.
	 * 
	 * @return an object mapper.
	 */
	public static ObjectMapper getObjectMapper() {
		try {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return objectMapper;
		} catch (Exception e) {
			logger.error("Exception in JacksonWrapper  "+e.getStackTrace());
			return null;
		}
	}
}
