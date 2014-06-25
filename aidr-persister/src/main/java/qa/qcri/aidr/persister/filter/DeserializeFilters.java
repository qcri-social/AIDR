package qa.qcri.aidr.persister.filter;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class DeserializeFilters {
	
	public DeserializeFilters() {}
	
	public JsonQueryList deserializeConstraints(final String queryString) {
		Gson jsonObject = new GsonBuilder().serializeNulls().disableHtmlEscaping()
											.serializeSpecialFloatingPointValues()	
											.create();
		JsonParser parser = new JsonParser();
		JsonObject obj = (JsonObject) parser.parse(queryString);
		JsonArray constraintsArray = null;
		if (obj.has("constraints")) {					// should always be true
			constraintsArray = obj.get("constraints") != null ? obj.get("constraints").getAsJsonArray() : new JsonArray();
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
					System.err.println("[deserializeConstraints] Persister: Error in deserializing received constraints");
					e.printStackTrace();
					return null;
				}
			}
			return queryList;
		}
		return null;
	}
}
