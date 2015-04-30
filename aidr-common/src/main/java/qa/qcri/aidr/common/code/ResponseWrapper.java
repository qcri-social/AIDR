package qa.qcri.aidr.common.code;

import java.util.HashMap;
import java.util.Map;

/**
 * A response wrapper, possibly superceded by {@link ResponseWrapperNEW}.
 * 
 * TODO: DEPRECATE. Apparently this is deprecated, mark as deprecated.
 *
 */
public class ResponseWrapper {
	/**
	 * Creates a map with two keys, and one value on each key
	 * 
	 * @param key1 the first key
	 * @param value1 the first value
	 * @param key2 the second key
	 * @param value2 the second value
	 * @return a map with two keys
	 */
	public static Map<String, Object> getUIWrapper(String key1, Object value1, String key2, Object value2) {
		Map<String, Object> resultMap = new HashMap<String, Object>(2);
		resultMap.put(key1, value1);
		resultMap.put(key2, value2);
		return resultMap;
	}
	
	/**
	 * Creates a map with four keys: "code", "message", "url" and "success"
	 * 
	 * @param collectionCode the element to put in the "code" value
	 * @param message the element to put in the "message" value
	 * @param fileName the element to put in the "url" value
	 * @param success the element to put in the "success" value
	 * @return a map with four keys
	 */
	public static Map<String, Object> getUIWrapper(String collectionCode, String message, String fileName, Boolean success) {
		Map<String, Object> modelMap = new HashMap<String, Object>(4);
		modelMap.put("code", collectionCode);
		modelMap.put("message", message);
		modelMap.put("url", fileName);
		modelMap.put("success", success);
		
		return modelMap;
	}
}
