package qa.qcri.aidr.output.filter;


import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import qa.qcri.aidr.output.filter.NominalLabel;

@SuppressWarnings("serial")
@XmlRootElement(name="ClassifiedFilteredTweet")
public class ClassifiedFilteredTweet implements Serializable {

	private Date created_at = null;
	private String text = null;
	private String crisis_code = null;
	private String crisis_name = null;
	private ArrayList<NominalLabel> nominal_labels;


	public ClassifiedFilteredTweet() {
		nominal_labels = new ArrayList<NominalLabel>();
	}

	public ClassifiedFilteredTweet deserialize(String rawJsonString) {
		
		created_at = null;
		text = null;
		crisis_code = null;
		crisis_name = null;
		nominal_labels.clear();
		
		if (rawJsonString.startsWith("["))		// should never happen 
			rawJsonString = rawJsonString.substring(1, rawJsonString.length()-1);	

		JsonParser parser = new JsonParser();
		JsonObject obj = (JsonObject) parser.parse(rawJsonString);
		JsonElement tweetData = null;
		JsonElement timestamp = null;
		JsonObject aidrData = null;
		JsonArray nominalLabels; 

		if (obj.has("text")) {					// should always be true
			tweetData = obj.get("text");		// get the tweet text string

			if (obj.has("created_at")) {
				timestamp = obj.get("created_at");
			}

			if (obj.has("aidr")) {								// should always be true
				aidrData = (JsonObject) obj.get("aidr");		// get the aidr JSON object

				// extract relevant fields from the aidr JSON object
				JsonElement crisisCode = null;
				if (aidrData.has("crisis_code"))				// should always be true
					crisisCode = aidrData.get("crisis_code");
				JsonElement crisisName = null;					
				if (aidrData.has("crisis_name"))				// should always be true
					crisisName = aidrData.get("crisis_name");	
				if (aidrData.has("nominal_labels")) {			// if false, then something wrong in AIDR setup
					nominalLabels = aidrData.get("nominal_labels").getAsJsonArray();
				} else {
					nominalLabels = new JsonArray();
				}

				ClassifiedFilteredTweet jsonObj = new ClassifiedFilteredTweet();
				
				jsonObj.text = tweetData.getAsString(); 
				text = tweetData.getAsString(); 
				
				jsonObj.crisis_code = crisisCode.getAsString(); 
				crisis_code = crisisCode.getAsString();
				
				jsonObj.crisis_name = crisisName.getAsString(); 
				crisis_name = crisisName.getAsString();
				
				for (int i = 0;i < nominalLabels.size();i++) {
					NominalLabel nLabel = new NominalLabel();
					JsonObject temp = (JsonObject) nominalLabels.get(i);
					nLabel.attibute_code = temp.get("attribute_code").getAsString();
					nLabel.label_code = temp.get("label_code").getAsString();
					nLabel.confidence = temp.get("confidence").getAsFloat();

					nLabel.attribute_name = temp.get("attribute_name").getAsString();
					nLabel.label_name = temp.get("label_name").getAsString();
					nLabel.attribute_description = temp.get("attribute_description").getAsString();
					nLabel.label_description = temp.get("label_description").getAsString();
					nLabel.from_human = temp.get("from_human").getAsBoolean();

					jsonObj.nominal_labels.add(nLabel); 
					nominal_labels.add(nLabel);
					
					jsonObj.created_at = createDate(timestamp.getAsString()); 
					created_at = createDate(timestamp.getAsString());
				}
				return jsonObj;
			}
		}
		return null;
	}

	public Date getCreatedAt() {
		return created_at;
	}
	
	public String getCreatedAtAsString() {
		SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZ yyyy");
		formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		return formatter.format(created_at);
	}
	
	protected Date createDate(String timestamp) {
		SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZ yyyy");
		formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		try {
			return formatter.parse(timestamp);
		} catch (ParseException e) {
			System.err.println("[createDate] Error in parsing Date string");
		}
		return null;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getCrisisCode() {
		return crisis_code;
	}
	
	public void setCrisisCode(String crisisCode) {
		crisis_code = crisisCode;
	}
	
	public String getCrisisName() {
		return crisis_name;
	}
	
	public void setCrisisName(String crisisName) {
		crisis_name = crisisName;
	}
	
	public ArrayList<NominalLabel> getNominalLabels() {
		ArrayList<NominalLabel> arr = new ArrayList<NominalLabel>();
		if (nominal_labels != null) 
			arr.addAll(nominal_labels);
		return arr;
	}
	
	public void setNominalLabels(ArrayList<NominalLabel> nLabels) {
		if (nominal_labels != null) {
			nominal_labels.addAll(nLabels);
		}
	}
}
