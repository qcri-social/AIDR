package qa.qcri.aidr.analysis.utils;

import java.util.Date;

import qa.qcri.aidr.common.values.ReturnCode;
import net.minidev.json.JSONObject;

public class JsonResponse {
	JSONObject json = new JSONObject();
	
	public JsonResponse() {
		json.put("status", ReturnCode.SUCCESS);
	}
	public JSONObject getNewJsonResponseObject(String crisisCode, String attributeCode, Long granularity, Long startTime, Long endTime) {
		
		json.put("crisis_code", crisisCode);
		json.put("attribute_code", attributeCode);
		json.put("granularity", granularity);

		if (startTime != null) {
			json.put("startTime", new Date(startTime));
		} else {
			json.put("startTime", null);
		}

		if (endTime != null) {
			json.put("endTime", new Date(endTime));
		} else {
			json.put("endTime", null);
		}
		return json;
	}
	
	public static JSONObject addError(JSONObject json) {
		json.put("status", ReturnCode.ERROR);
		return json;
	}
}
