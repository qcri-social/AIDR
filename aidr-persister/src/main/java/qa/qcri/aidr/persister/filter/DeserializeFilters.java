package qa.qcri.aidr.persister.filter;


import org.apache.log4j.Logger;

import qa.qcri.aidr.common.logging.ErrorLog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class DeserializeFilters {
	
	private static Logger logger = Logger.getLogger(DeserializeFilters.class.getName());
	private static ErrorLog elog = new ErrorLog();
	
	public DeserializeFilters() {}
	
	public JsonQueryList deserializeConstraints(final String queryString) {
		Gson jsonObject = new GsonBuilder().serializeNulls().disableHtmlEscaping()
											.serializeSpecialFloatingPointValues()	
											.create();
		
		System.out.println("queryString = " + queryString);
		JsonParser parser = new JsonParser();
		JsonObject obj = (JsonObject) parser.parse(queryString);
		JsonArray constraintsArray = null;
		if (obj.has("constraints")) {					// should always be true
			constraintsArray = obj.has("constraints") ? obj.get("constraints").getAsJsonArray() : new JsonArray();
			//System.out.println("constraints: " + constraintsArray);
		}
		JsonQueryList queryList = new JsonQueryList();
		if (constraintsArray.size() > 0) {
			for (int i = 0;i < constraintsArray.size();i++) {
				try {
					JsonElement q = constraintsArray.get(i);
					//System.out.println("constraint " + i + ": " + q);
					GenericInputQuery constraint = jsonObject.fromJson(q, GenericInputQuery.class);
					queryList.createConstraint(constraint);
				} catch (Exception e) {
					logger.error("Error in deserializing received constraints");
					logger.error(elog.toStringException(e));
					return null;
				}
			}
			return queryList;
		}
		return null;
	}
}
