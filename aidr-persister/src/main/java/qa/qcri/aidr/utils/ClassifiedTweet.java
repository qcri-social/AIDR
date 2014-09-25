/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.utils;

import java.io.Serializable;
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
import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.persister.filter.NominalLabel;

/**
 *
 * @author Imran, modified by Koushik
 */
@SuppressWarnings("serial")
public class ClassifiedTweet  implements Document, Serializable {
	
	private static Logger logger = Logger.getLogger(ClassifiedTweet.class);
	private static ErrorLog elog = new ErrorLog();
	

	/**
	 * 
	 */
	private String tweetID;
	private String message;
	//private String reTweeted;
	//private String reTweetCount;
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
	
	// Added by koushik
	//@JsonIgnore
	public ArrayList<NominalLabel> nominal_labels;		// transient?

	public ClassifiedTweet() {
		nominal_labels = new ArrayList<NominalLabel>();
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
	public String getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(String createdAtString) {
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
		if (tweetString != null) {
			return tweetString.toString();	
		} 
		return null;
		//return tweetID+","+message+","+createdAt+","+userID+","+userName+","+userURL+","+tweetURL;
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


	// Added by koushik
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
				logger.error(elog.toStringException(e));
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
				logger.error(elog.toStringException(e));
			}
		}
		setTimestamp(0);
		return timeString;
	}

	public ArrayList<NominalLabel> getNominalLabels() {
		return nominal_labels;
	}

	public void setNominalLabels(ArrayList<NominalLabel> nLabels) {
		if (null == this.nominal_labels) {
			this.nominal_labels = nLabels;
		} else {
			this.nominal_labels.addAll(nLabels);
		}
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
	
	public static void main(String args[]) {
		
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

	public void createDummyNominalLabels(final String crisisCode) {
		//System.out.println("Creating dummy nominal labels array");
		
		this.crisisCode = crisisCode;
		this.crisisName = crisisCode;
		this.nominal_labels = new ArrayList<NominalLabel>();
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
	}
 }
