package qa.qcri.aidr.common.code;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * A wrapper around an {@link ObjectMapper}.
 * 
 */
public class JacksonWrapper {
	
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
			e.printStackTrace();
			return null;
		}
	}
}
