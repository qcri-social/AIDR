package qa.qcri.aidr.common.filter;


import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;



@SuppressWarnings("serial")
@XmlRootElement(name="ClassifiedFilteredTweet")
public class ClassifiedFilteredTweet implements Serializable {

	private static Logger logger = Logger.getLogger(ClassifiedFilteredTweet.class);

	private Date created_at = null;
	private String text = null;
	private String crisis_code = null;
	private String crisis_name = null;
	public List<NominalLabel> nominal_labels;

	private String id = null;
	private String screen_name = null;


	public ClassifiedFilteredTweet() {
		nominal_labels = new ArrayList<NominalLabel>();
	}

	public float getMaxConfidence() {
		float maxConfidence = 0;
		for (NominalLabel nLabel: nominal_labels) {
			if (nLabel.confidence > maxConfidence) {
				maxConfidence = nLabel.confidence;
			}
		}
		return maxConfidence;
	}

	public ClassifiedFilteredTweet deserialize(String rawJsonString) {

		created_at = null;
		text = null;
		crisis_code = null;
		crisis_name = null;
		id = null;
		screen_name = null;
		nominal_labels.clear();
		if (null == rawJsonString) {
			return null;
		}

		if (rawJsonString.startsWith("["))		// should never happen 
			rawJsonString = rawJsonString.substring(1, rawJsonString.length()-1);	

		try {
			JsonParser parser = new JsonParser();
			JsonObject obj = (JsonObject) parser.parse(rawJsonString);
			JsonElement tweetData = null;
			JsonElement timestamp = null;
			JsonObject aidrData = null;
			JsonArray nominalLabels; 

			JsonElement tweetId = null;
			JsonObject userData = null;
			JsonElement screenName = null;

			if (obj.has("text")) {					// should always be true
				tweetData = obj.get("text");		// get the tweet text string

				if (obj.has("created_at")) {
					timestamp = obj.get("created_at");
				}

				if (obj.has("id_str")) {
					tweetId = obj.get("id_str");
				}

				if (obj.has("user")) {
					userData = (JsonObject) obj.get("user");
					screenName = userData.get("screen_name");
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
					if (aidrData.has("nominal_labels") && !aidrData.get("nominal_labels").isJsonNull()) {			// if false, then something wrong in AIDR setup
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

					jsonObj.created_at = createDate(timestamp.getAsString()); 
					created_at = createDate(timestamp.getAsString());

					jsonObj.id = tweetId.getAsString();
					id = tweetId.getAsString();

					jsonObj.screen_name = screenName.getAsString();
					screen_name = screenName.getAsString();

					for (int i = 0;i < nominalLabels.size();i++) {
						NominalLabel nLabel = new NominalLabel();
						JsonObject temp = (JsonObject) nominalLabels.get(i);

						if (!temp.get("attribute_code").isJsonNull() && !temp.get("label_code").isJsonNull()) {
							nLabel.attribute_code = (temp.has("attribute_code") &&  !temp.get("attribute_code").isJsonNull()) ? temp.get("attribute_code").getAsString() : null;
							nLabel.label_code = (temp.has("label_code") && !temp.get("label_code").isJsonNull()) ? temp.get("label_code").getAsString() : null;
							nLabel.confidence = (temp.has("confidence") && !temp.get("confidence").isJsonNull()) ? temp.get("confidence").getAsFloat() : 0;

							nLabel.attribute_name = (temp.has("attribute_name") && !temp.get("attribute_name").isJsonNull()) ? temp.get("attribute_name").getAsString() : null;
							nLabel.label_name = (temp.has("label_name") && !temp.get("label_name").isJsonNull()) ? temp.get("label_name").getAsString() : null;
							nLabel.attribute_description = (temp.has("attribute_description") && !temp.get("attribute_description").isJsonNull()) ? temp.get("attribute_description").getAsString() : null;
							nLabel.label_description = (temp.has("label_description") && !temp.get("label_description").isJsonNull())? temp.get("label_description").getAsString() : null;
							nLabel.from_human = (temp.has("from_human") && !temp.get("from_human").isJsonNull()) ? temp.get("from_human").getAsBoolean() : false;

							jsonObj.nominal_labels.add(nLabel); 
							nominal_labels.add(nLabel);
						}
					}
					return jsonObj;
				}
			}
			return null;
		} catch (Exception e) {
			logger.error("Exception in json parsing for string: " + rawJsonString, e);
			return null;
		}
	}

	public void setCreateAt(final Date created_at) {
		this.created_at = created_at;
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
		if (null != timestamp) {
			try {
				return formatter.parse(timestamp);
			} catch (ParseException e) {
				logger.error("Error in parsing Date string");
			}
		}
		logger.error("Warning, tweet has no createdAt field!");
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

	public List<NominalLabel> getNominalLabels() {
		return nominal_labels!=null ? nominal_labels : new ArrayList<NominalLabel>();
	}

	public void setNominalLabels(List<NominalLabel> nLabels) {
		if (nominal_labels == null) {
			nominal_labels = new ArrayList<NominalLabel>();
		}
		nominal_labels.addAll(nLabels);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getScreenName() {
		return screen_name;
	}

	public void setScreenName(String screenName) {
		this.screen_name = screenName;
	}
}
