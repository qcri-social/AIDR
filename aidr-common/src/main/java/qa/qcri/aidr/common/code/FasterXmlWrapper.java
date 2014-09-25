package qa.qcri.aidr.common.code;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;


public class FasterXmlWrapper {
	public static ObjectMapper getObjectMapper() {
		try {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return objectMapper;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
