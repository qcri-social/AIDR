package qa.qcri.aidr.common.code;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

public class JacksonWrapper {
	
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
