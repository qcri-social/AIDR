package qa.qcri.aidr.common.code;

import java.util.HashMap;
import java.util.Map;

public class ResponseWrapper {
	public static Map<String, Object> getUIWrapper(String key1, Object value1, String key2, Object value2) {
		Map<String, Object> resultMap = new HashMap<String, Object>(2);
		resultMap.put(key1, value1);
		resultMap.put(key2, value2);
		return resultMap;
	}
	
	public static Map<String, Object> getUIWrapper(String collectionCode, String message, String fileName, Boolean success) {
		Map<String, Object> modelMap = new HashMap<String, Object>(4);
		modelMap.put("code", collectionCode);
		modelMap.put("message", message);
		modelMap.put("url", fileName);
		modelMap.put("success", success);
		
		return modelMap;
	}
}
