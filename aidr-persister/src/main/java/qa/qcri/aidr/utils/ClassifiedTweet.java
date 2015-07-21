/**
 * POJO for (de)serialization of tweets coming on the REDIS aidr_predict.* channels
 * 
 * @author Koushik
 */


package qa.qcri.aidr.utils;

import java.io.Serializable;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import qa.qcri.aidr.common.code.DateFormatConfig;
import qa.qcri.aidr.common.filter.ClassifiedFilteredTweet;
import qa.qcri.aidr.common.filter.NominalLabel;
import qa.qcri.aidr.dbmanager.dto.DocumentNominalLabelDTO;
import qa.qcri.aidr.dbmanager.dto.HumanLabeledDocumentDTO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;


public class ClassifiedTweet  extends ClassifiedFilteredTweet implements Document, Serializable {

	private static final long serialVersionUID = 3780215910578547404L;

	private static Logger logger = Logger.getLogger(ClassifiedTweet.class);

	/**
	 * 
	 */
	private String tweetID;
	private String message;
	private String createdAt;
	private long timestamp;

	private String userID;
	private String userName;
	private String userURL;
	private String tweetURL;

	private String crisisName;
	private String crisisCode;

	private String attributeName_1;
	private String attributeCode_1;
	private String labelName_1;
	private String labelCode_1;
	private String labelDescription_1;
	private String confidence_1;
	private String humanLabeled_1;

	private AidrObject aidr;

	public ClassifiedTweet() {
		nominal_labels = new ArrayList<NominalLabel>();
		aidr = new AidrObject();
	}

	/**
	 * @return the userURL
	 */
	public String getUserURL() {
		return userURL;
	}

	/**
	 * @param userURL the userURL to set
	 */
	public void setUserURL(String userURL) {
		this.userURL = userURL;
	}

	/**
	 * @return the tweetID
	 */
	public String getTweetID() {
		return tweetID;
	}

	/**
	 * @param tweetID the tweetID to set
	 */
	public void setTweetID(String tweetID) {
		this.tweetID = tweetID;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the userID
	 */
	public String getUserID() {
		return userID;
	}

	/**
	 * @param userID the userID to set
	 */
	public void setUserID(String userID) {
		this.userID = userID;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}


	/**
	 * @return the createdAt
	 */
	public String getCreatedAtString() {
		return createdAt;
	}

	/**
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAtString(String createdAtString) {
		this.createdAt = setDateString(createdAtString);
	}

	/**
	 * @return the tweetURL
	 */
	public String getTweetURL() {
		return tweetURL;
	}

	/**
	 * @param tweetURL the tweetURL to set
	 */
	public void setTweetURL(String tweetURL) {
		this.tweetURL = tweetURL;
	}

	public String toString(){
		StringBuffer tweetString = new StringBuffer();
		tweetString.append(tweetID).append(",")
		.append(message).append(",")
		.append(createdAt).append(",")
		.append(userID).append(",")
		.append(userName).append(",")
		.append(userURL).append(",")
		.append(tweetURL).append(",")
		.append(crisisName).append(",")
		.append(attributeName_1).append(",")
		.append(attributeCode_1).append(",")
		.append(labelName_1).append(",")
		.append(labelDescription_1).append(",")
		.append(confidence_1).append(",")
		.append(humanLabeled_1);

		return tweetString.toString();	

	}


	/**
	 * @return the crisisName
	 */
	public String getCrisisName() {
		return crisisName;
	}

	/**
	 * @param crisisName the crisisName to set
	 */
	public void setCrisisName(String crisisName) {
		this.crisisName = crisisName;
	}

	public String getAttributeName_1() {
		return attributeName_1;
	}

	public void setAttributeName_1(String attributeName) {
		this.attributeName_1 = attributeName;
	}

	public String getAttributeCode_1() {
		return attributeCode_1;
	}

	public void setAttributeCode_1(String attributeCode) {
		this.attributeCode_1 = attributeCode;
	}

	/**
	 * @return the labelName
	 */
	public String getLabelName_1() {
		return labelName_1;
	}

	/**
	 * @param labelName the labelName to set
	 */
	public void setLabelName_1(String labelName) {
		this.labelName_1 = labelName;
	}

	/**
	 * @return the labelDescription
	 */
	public String getLabelDescription_1() {
		return labelDescription_1;
	}

	/**
	 * @param labelDescription the labelDescription to set
	 */
	public void setLabelDescription_1(String labelDescription) {
		this.labelDescription_1 = labelDescription;
	}

	public String getLabelCode_1() {
		return labelCode_1;

	}

	public void setLabelCode_1(String labelCode) {
		this.labelCode_1 = labelCode;
	}

	/**
	 * @return the confidence
	 */
	public String getConfidence_1() {
		return confidence_1;
	}

	/**
	 * @param confidence the confidence to set
	 */
	public void setConfidence_1(String confidence) {
		this.confidence_1 = confidence;
	}

	/**
	 * @return the humanLabeled
	 */
	public String getHumanLabeled_1() {
		return humanLabeled_1;
	}

	/**
	 * @param humanLabeled the humanLabeled to set
	 */
	public void setHumanLabeled_1(String humanLabeled) {
		this.humanLabeled_1 = humanLabeled;
	}


	public Date getDate(String timeString) {
		//SimpleDateFormat formatter = new SimpleDateFormat(StandardDateFormat);
		DateFormat formatter = new SimpleDateFormat(DateFormatConfig.ISODateFormat);
		formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		if (timeString != null) {
			try {
				Date newDate = formatter.parse(timeString);
				//System.out.println("[getDate] Converted date: " + newDate.toString());
				return newDate;
			} catch (ParseException e) {
				logger.error("Parse Error in getting Date string = " + timeString);
				logger.error("exception", e);
			}
		}
		logger.warn("[getDate] Warning! returning Date = null for time String = " + timeString);
		return null;
	} 

	public String setDateString(String timeString) {
		//System.out.println("[setDateString] Received time string: " + timeString);

		DateFormat dateFormatISO = new SimpleDateFormat(DateFormatConfig.ISODateFormat);
		dateFormatISO.setTimeZone(TimeZone.getTimeZone("GMT"));
		if (timeString != null) {
			try {
				SimpleDateFormat formatter = new SimpleDateFormat(DateFormatConfig.StandardDateFormat);
				Date newDate = formatter.parse(timeString);
				if (newDate != null) setTimestamp(newDate.getTime());
				//System.out.println("[setDateString] Converted date: " + newDate.toString());
				return dateFormatISO.format(newDate);
			} catch (ParseException e) {
				logger.error("Error in setting createdAt field = " + timeString);
				logger.error("exception", e);
			}
		}
		setTimestamp(0);
		return timeString;
	}

	public long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getCrisisCode() {
		return this.crisisCode;
	}

	public void setCrisisCode(String crisisCode) {
		this.crisisCode = crisisCode;
	}

	public Map<String, Object> prettyPrint() {
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("id", this.getTweetID());
		obj.put("crisisName", this.getCrisisName());
		obj.put("crisisCode", this.getCrisisCode());
		obj.put("tweet", this.getMessage());
		for (int i = 0; i < this.getNominalLabels().size();i++) {
			NominalLabel nb = this.getNominalLabels().get(i);
			obj.put("attribute_name_"+i, nb.attribute_name);
			obj.put("attribute_code_"+i, nb.attribute_code);
			obj.put("label_name_"+i, nb.label_name);
			obj.put("label_description_"+i, nb.label_description);
			obj.put("label_code_"+i, nb.label_code);
			obj.put("confidence_"+i, nb.confidence);
			obj.put("humanLabeled_"+i, nb.from_human);
		}
		return obj;
	}

	public NominalLabel createDummyNominalLabels(final String crisisCode) {
		//System.out.println("Creating dummy nominal labels array");

		NominalLabel nLabel = new NominalLabel();
		nLabel.attribute_code = "null";
		nLabel.label_code = "null";
		nLabel.confidence = 0;

		nLabel.attribute_name = "null";
		nLabel.label_name = "null";
		nLabel.attribute_description = "null";
		nLabel.label_description = "null";
		nLabel.from_human = false;

		this.nominal_labels.add(nLabel);
		return nLabel;
	}

	public void toClassifiedTweet(String data) {
		this.toClassifiedTweet(data, null);
	}
	
	public void toClassifiedTweet(String data, String collectionCode) {
		//System.out.println("Received string to deserialize: " + data);
		if (data != null) {
			try {
				StringReader reader = new StringReader(data.trim());
				JsonReader jsonReader = new JsonReader(reader);
				jsonReader.setLenient(true);

				JsonParser parser = new JsonParser();
				JsonObject jsonObj = (JsonObject) parser.parse(jsonReader);

				//System.out.println("Unparsed tweet data: " + jsonObj.get("id") + ", " + jsonObj.get("created_at") + ", " + jsonObj.get("user")  + ", " + jsonObj.get("aidr"));

				if (!jsonObj.get("id").isJsonNull()) {
					this.setTweetID(jsonObj.get("id").getAsString());
				} 

				if (!jsonObj.get("text").isJsonNull()) {
					this.setMessage(jsonObj.get("text").getAsString());
				}

				if (!jsonObj.get("created_at").isJsonNull()) {
					this.setCreatedAtString(jsonObj.get("created_at").getAsString());
				}

				JsonObject jsonUserObj = null;
				if (!jsonObj.get("user").isJsonNull()) {				
					jsonUserObj = jsonObj.get("user").getAsJsonObject();
					if (jsonUserObj.get("id") != null) {
						this.setUserID(jsonUserObj.get("id").getAsString());
					}

					if (!jsonUserObj.get("screen_name").isJsonNull()) {
						this.setUserName(jsonUserObj.get("screen_name").getAsString());
						this.setTweetURL("https://twitter.com/" + this.getUserName() + "/status/" + this.getTweetID());
					}
					if (jsonUserObj.get("url") != null) {
						this.setUserURL(jsonUserObj.get("url").toString());
					}
				}

				JsonObject aidrObject = null;
				if (jsonObj.has("aidr") && !jsonObj.get("aidr").isJsonNull()) {
					aidrObject = jsonObj.get("aidr").getAsJsonObject();
					if (!aidrObject.get("crisis_name").isJsonNull()) {
						this.setCrisisName(aidrObject.get("crisis_name").getAsString());
						this.getAidr().setCrisisName(this.getCrisisName());
					}
					if (!aidrObject.get("crisis_code").isJsonNull()) {
						this.setCrisisCode(aidrObject.get("crisis_code").getAsString());
						this.getAidr().setCrisisCode(this.getCrisisCode());
					}
					if (aidrObject.has("nominal_labels") && !aidrObject.get("nominal_labels").isJsonNull()) {
						//JSONArray nominalLabels = (JSONArray) aidrObject.get("nominal_labels");
						JsonArray nominalLabels = aidrObject.get("nominal_labels").getAsJsonArray();
						StringBuffer allAttributeNames = new StringBuffer();
						StringBuffer allAttributeCodes = new StringBuffer();
						StringBuffer allLabelNames = new StringBuffer();
						StringBuffer allLabelCodes = new StringBuffer();
						StringBuffer allLabelDescriptions = new StringBuffer();
						StringBuffer allConfidences = new StringBuffer();
						StringBuffer humanLabeled = new StringBuffer();
						for (int i = 0; i < nominalLabels.size(); i++) {
							//JSONObject label = (JSONObject) nominalLabels.get(i);
							JsonObject label = nominalLabels.get(i).getAsJsonObject();
							allAttributeNames.append((label.has("attribute_name") && !label.get("attribute_name").isJsonNull()) ? label.get("attribute_name").getAsString() : "null");
							allAttributeCodes.append((label.has("attribute_code") && !label.get("attribute_code").isJsonNull()) ? label.get("attribute_code").getAsString() : "null");
							allLabelNames.append((label.has("label_name") && !label.get("label_name").isJsonNull()) ? label.get("label_name").getAsString() : "null");
							allLabelCodes.append((label.has("label_code") && !label.get("label_code").isJsonNull()) ? label.get("label_code").getAsString() : "null");
							allLabelDescriptions.append((label.has("label_description") && !label.get("label_description").isJsonNull()) ? label.get("label_description").getAsString() : "null");
							allConfidences.append((label.has("confidence") && !label.get("confidence").isJsonNull()) ? label.get("confidence").getAsFloat() : 0);
							humanLabeled.append((label.has("from_human") && !label.get("from_human").isJsonNull()) ? label.get("from_human").getAsBoolean() : false);

							NominalLabel nLabel = new NominalLabel();
							nLabel.attribute_code = (label.has("attribute_code") && !label.get("attribute_code").isJsonNull()) ?  label.get("attribute_code").getAsString() : "null";
							nLabel.label_code = (label.has("label_code") && !label.get("label_code").isJsonNull()) ?   label.get("label_code").getAsString() : "null";
							nLabel.confidence = (label.has("confidence") && !label.get("confidence").isJsonNull()) ? Float.parseFloat(label.get("confidence").getAsString()) : 0;

							nLabel.attribute_name = (label.has("attribute_name") && !label.get("attribute_name").isJsonNull()) ?  label.get("attribute_name").getAsString() : "null";
							nLabel.label_name = (label.has("label_name") && !label.get("label_name").isJsonNull()) ?  label.get("label_name").getAsString() : "null";
							nLabel.attribute_description = (label.has("attribute_description") && !label.get("attribute_description").isJsonNull()) ?   label.get("attribute_description").getAsString() : "null";
							nLabel.label_description = (label.has("label_description") && !label.get("label_description").isJsonNull()) ?   label.get("label_description").getAsString() : "null";
							nLabel.from_human = (label.has("from_human") && !label.get("from_human").isJsonNull()) ?  Boolean.parseBoolean(label.get("from_human").getAsString()): false;

							this.nominal_labels.add(nLabel);

							// remove the ugly ';' from end-of-list
							if (i < nominalLabels.size() - 1) {
								allAttributeNames.append(";");
								allAttributeCodes.append(";");
								allLabelNames.append(";");
								allLabelDescriptions.append(";");
								allConfidences.append(";");
								humanLabeled.append(";");
							}
						}
						this.getAidr().setNominalLabels(this.getNominalLabels());
						
						this.setAttributeName_1(allAttributeNames.toString());
						this.setAttributeCode_1(allAttributeCodes.toString());
						this.setLabelName_1(allLabelNames.toString());
						this.setLabelDescription_1(allLabelDescriptions.toString());
						this.setConfidence_1(allConfidences.toString());
						this.setHumanLabeled_1(humanLabeled.toString());

					} else {
						//System.out.println("Creating dummy nominal labels");
						this.createDummyNominalLabels(collectionCode);        	
					}
				} else {
					//System.out.println("Creating dummy nominal labels");
					this.createDummyAIDRField(collectionCode);
				}
			} catch (Exception ex) {
				logger.error("Exception in deserialization, returning null");
				logger.error("exception", ex);
				ex.printStackTrace();
			}
		}
	}

	public void createDummyAIDRField(String collectionCode) {
		this.getAidr().setCrisisCode(collectionCode);
		this.getAidr().getNominalLabels().add(this.createDummyNominalLabels(collectionCode));
		this.createDummyNominalLabels(collectionCode);
	}

	public void toClassifiedTweetFromLabeledDoc(HumanLabeledDocumentDTO doc, String collectionCode) {
		if (doc != null) {
			try {
				this.toClassifiedTweet(doc.getDoc().getData(), collectionCode);

				// Now fill up the nominal_label field
				if (doc.getLabelData() != null && !doc.getLabelData().isEmpty()) {
					// first remove the dummy nominal_label list
					this.nominal_labels.clear();
					this.getAidr().getNominalLabels().clear();
					
					// Now add the actual nominal_label data
					for (DocumentNominalLabelDTO label: doc.getLabelData()) {
						NominalLabel nb = new NominalLabel();
						nb.from_human = doc.getDoc().getHasHumanLabels();
						nb.attribute_code = label.getNominalLabelDTO().getNominalAttributeDTO().getCode();
						nb.attribute_description = label.getNominalLabelDTO().getNominalAttributeDTO().getDescription();
						nb.attribute_name = label.getNominalLabelDTO().getNominalAttributeDTO().getName();
						nb.confidence = 1;		// default confidence for human labelers = 1.0
						nb.label_code = label.getNominalLabelDTO().getNominalLabelCode();
						nb.label_description = label.getNominalLabelDTO().getDescription();
						nb.label_name = label.getNominalLabelDTO().getName();
						this.nominal_labels.add(nb);
						this.getAidr().getNominalLabels().add(nb);
					}
				}
				//System.out.println(this.toJsonString());

			} catch (Exception e) {
				System.out.println("Exception in parsing labeled document");
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @return serialized JSON string without pretty printing (default behavior)
	 */
	public String toJsonString() {
		return this.toJsonString(false);
	}
	
	/**
	 * 
	 * @param isPrettyPrinting turn PrettyPrinting on/off
	 * @return serialized JSON string
	 */
	public String toJsonString(boolean isPrettyPrinting) {
		Gson jsonObject = null;
		
		if (isPrettyPrinting) {
		jsonObject = new GsonBuilder().serializeNulls().disableHtmlEscaping()
				.serializeSpecialFloatingPointValues().setPrettyPrinting()
				.create();
		} else {
			jsonObject = new GsonBuilder().serializeNulls().disableHtmlEscaping()
					.serializeSpecialFloatingPointValues()
					.create();
		}
		
		try {
			String jsonString = jsonObject.toJson(this, ClassifiedTweet.class);
			return jsonString;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public AidrObject getAidr() {
		return this.aidr;
	}


	public void setAidr(AidrObject aidr) {
		this.aidr = aidr;
	}
}