package qa.qcri.aidr.output.getdata;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;



//import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JsonOutputBuilder {
	private static Logger logger = LoggerFactory.getLogger(JsonOutputBuilder.class);
	
	public JsonOutputBuilder() {
		//BasicConfigurator.configure();
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
			
		// extract relevant fields from the aidr JSON object
		String crisisCode = aidrData.get("crisis_code").getAsString();
		String crisisName = aidrData.get("crisis_name").getAsString();
		String nominalLabels = aidrData.get("nominal_labels") != null ?
								aidrData.get("nominal_labels").toString() : new String("[{}]");
		
		JsonReturnClass jsonObj = new JsonReturnClass();
		jsonObj.text.append(tweetData.getAsString());
		jsonObj.aidr.crisis_code.append(crisisCode);
		jsonObj.aidr.crisis_name.append(crisisName);
		jsonObj.aidr.nominal_labels.append(nominalLabels);
		
		logger.debug("[JsonOutputBuilder] text : " + jsonObj.text);
		logger.debug("[JsonOutputBuilder] crisis code: " + jsonObj.aidr.crisis_code);
		logger.debug("[JsonOutputBuilder] crisis name: " + jsonObj.aidr.crisis_name);
		logger.debug("[JsonOutputBuilder] nominal labels: " + jsonObj.aidr.nominal_labels);
		
		return jsonObject.toJson(jsonObj);
				
	}
	
	// Json output data format
	private class AIDRData {
		private StringBuilder crisis_code;
		private StringBuilder crisis_name;
		private StringBuilder nominal_labels;
	
		public AIDRData() {
			crisis_code = new StringBuilder();
			crisis_name = new StringBuilder();
			nominal_labels = new StringBuilder();
		}
	}
	
	private class JsonReturnClass {
		private StringBuilder text;
		private AIDRData aidr;
		
		public JsonReturnClass() {
			text = new StringBuilder();
			aidr = new AIDRData();
		}
	}
}
