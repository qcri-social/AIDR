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
import java.util.TimeZone;

import org.codehaus.jackson.annotate.JsonIgnore;

import qa.qcri.aidr.persister.filter.NominalLabel;

/**
 *
 * @author Imran, modified by Koushik
 */
public class ClassifiedTweet  implements Document, Serializable{

	private String tweetID;
	private String message;
	//private String reTweeted;
	//private String reTweetCount;
	private String createdAt;

	private String userID;
	private String userName;
	private String userURL;
	private String tweetURL;

	private String crisisName;
	private String labelName;
	private String labelDescription;
	private String confidence;
	private String humanLabeled;
	
	// Added by koushik
	//@JsonIgnore
	
	public transient ArrayList<NominalLabel> nominal_labels;

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
				   .append(labelName).append(",")
				   .append(labelDescription).append(",")
				   .append(confidence).append(",")
				   .append(humanLabeled);
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

	/**
	 * @return the labelName
	 */
	public String getLabelName() {
		return labelName;
	}

	/**
	 * @param labelName the labelName to set
	 */
	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	/**
	 * @return the labelDescription
	 */
	public String getLabelDescription() {
		return labelDescription;
	}

	/**
	 * @param labelDescription the labelDescription to set
	 */
	public void setLabelDescription(String labelDescription) {
		this.labelDescription = labelDescription;
	}

	/**
	 * @return the confidence
	 */
	public String getConfidence() {
		return confidence;
	}

	/**
	 * @param confidence the confidence to set
	 */
	public void setConfidence(String confidence) {
		this.confidence = confidence;
	}

	/**
	 * @return the humanLabeled
	 */
	public String getHumanLabeled() {
		return humanLabeled;
	}

	/**
	 * @param humanLabeled the humanLabeled to set
	 */
	public void setHumanLabeled(String humanLabeled) {
		this.humanLabeled = humanLabeled;
	}


	// Added by koushik
	public Date getDate(String timeString) {
		//SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZ yyyy");
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
		formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		if (timeString != null) {
			try {
				Date newDate = formatter.parse(timeString);
				//System.out.println("[getDate] Converted date: " + newDate.toString());
				return newDate;
			} catch (ParseException e) {
				System.err.println("[getDate] Error in parsing Date string = " + timeString);
			}
		}
		System.out.println("[getDate] Warning! returning Date = null for time String = " + timeString);
		return null;
	} 

	public String setDateString(String timeString) {
		//System.out.println("[setDateString] Received time string: " + timeString);

		DateFormat dateFormatISO = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
		dateFormatISO.setTimeZone(TimeZone.getTimeZone("GMT"));
		if (timeString != null) {
			try {
				SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZ yyyy");
				Date newDate = formatter.parse(timeString);
				//System.out.println("[setDateString] Converted date: " + newDate.toString());
				return dateFormatISO.format(newDate);
			} catch (ParseException e) {
				System.err.println("[setDateString] Error in parsing Date string = " + timeString);
			}
		}
		System.out.println("[setDateString] Null createdAt Warning! time String = " + timeString);
		return null;
	}

	public ArrayList<NominalLabel> getNominalLabels() {
		return nominal_labels;
	}

	public void setNominalLabels(ArrayList<NominalLabel> nLabels) {
		if (this.nominal_labels != null) {
			this.nominal_labels = nLabels;
		}
	}
 }
