package qa.qcri.aidr.analysis.utils;

import net.minidev.json.JSONObject;

public class JsonResponse {
	public static JSONObject getNewJsonResponseObject(String crisisCode, String attributeCode, Long granularity) {
		JSONObject json = new JSONObject();
		json.put("crisis_code", crisisCode);
		json.put("attribute_code", attributeCode);
		json.put("granularity", granularity);
		
		return json;
	}
	
}
