/**
 * @author Koushik Sinha
 * Last modified: 11/01/2014
 * 
 * The TaggerJsonOutputAdpater class implements Tagger specific
 * json object format. Specifically, it returns null if the 
 * nominal_labels field in a json message is null. 
 *  
 */

package qa.qcri.aidr.output.getdata;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

//import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TaggerJsonOutputAdapter {
	private static Logger logger = LoggerFactory.getLogger(TaggerJsonOutputAdapter.class);
	
	public TaggerJsonOutputAdapter() {
		//BasicConfigurator.configure();		// configuration for log4j logging
		System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");		// set logging level for slf4j
	}
	
	public String buildJsonString(String rawJsonString) {
		Gson jsonObject = new GsonBuilder().serializeNulls()			//.disableHtmlEscaping()
				.serializeSpecialFloatingPointValues().setPrettyPrinting()
				.create();
		
		JsonParser parser = new JsonParser();
		JsonObject obj = (JsonObject) parser.parse(rawJsonString);
		
		JsonElement tweetData = (JsonElement) obj.get("text");		// get the tweet text string
		JsonObject aidrData = (JsonObject) obj.get("aidr");			// get the aidr JSON object
		JsonArray nominalLabels; 
		
		// extract relevant fields from the aidr JSON object
		String crisisCode = aidrData.get("crisis_code").getAsString();
		String crisisName = aidrData.get("crisis_name").getAsString();
		if (aidrData.has("nominal_labels")) {
			nominalLabels = aidrData.get("nominal_labels").getAsJsonArray();
		} else {
			nominalLabels = new JsonArray();
		}
		JsonReturnClass jsonObj = new JsonReturnClass();
		jsonObj.text.append(tweetData.getAsString());
		jsonObj.crisis_code.append(crisisCode);
		jsonObj.crisis_name.append(crisisName);
		
		for (int itr = 0;itr < nominalLabels.size();itr++) {
			//logger.debug("[buildJsonString] Label " + itr + ": " + nominalLabels.get(itr));
			jsonObj.nominal_labels.add(nominalLabels.get(itr));
		}
		
		//logger.debug("[JsonOutputBuilder] text : " + jsonObj.text);
		//logger.debug("[JsonOutputBuilder] crisis code: " + jsonObj.crisis_code);
		//logger.debug("[JsonOutputBuilder] crisis name: " + jsonObj.crisis_name);
		//logger.debug("[JsonOutputBuilder] nominal labels: " + jsonObj.nominal_labels);
		
		if (!isemptyNominalLabels(nominalLabels))
			return jsonObject.toJson(jsonObj);
		return null;			// returning "null" to meet Tagger's specific front-end requirements
	}
	
	private boolean isemptyNominalLabels(JsonArray nominalLabels) {
		if (nominalLabels.size() == 0) 
			return true;
		else
			return false;
	}
	
	private class JsonReturnClass {
		private StringBuilder text;
		private StringBuilder crisis_code;
		private StringBuilder crisis_name;
		private List<JsonElement> nominal_labels;
		
		public JsonReturnClass() {
			text = new StringBuilder();
			crisis_code = new StringBuilder();
			crisis_name = new StringBuilder();
			nominal_labels = new ArrayList<JsonElement>();
		}
	}
}