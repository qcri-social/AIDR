/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.utils;

import java.io.Serializable;

import qa.qcri.aidr.dbmanager.dto.HumanLabeledDocumentDTO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 *
 * @author Imran
 */
@SuppressWarnings("serial")
public class Tweet  implements Document, Serializable{

	private String tweetID;
	private String message;
	private String crisisName;
	//private String reTweeted;
	//private String reTweetCount;
	private String createdAt;

	private String userID;
	private String userName;
	private String userURL;
	private String tweetURL;

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

	public String getCrisisName() {
		return crisisName;
	}

	public void setCrisisName(String crisisName) {
		this.crisisName = crisisName;
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
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
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
		return tweetID+","+message+","+createdAt+","+userID+","+userName+","+userURL+","+tweetURL;
	}

	public Tweet toTweet(String str) {
		if (str != null) {
			Tweet tweet = new Tweet();
			try {
				JsonParser parser = new JsonParser();
				JsonObject jsonObj = (JsonObject) parser.parse(str);


				if (jsonObj.get("id") != null) {
					tweet.setTweetID(jsonObj.get("id").getAsString());
				} 

				if (jsonObj.get("text") != null) {
					tweet.setMessage(jsonObj.get("text").getAsString());
				}

				if (jsonObj.get("created_at") != null) {
					tweet.setCreatedAt(jsonObj.get("created_at").getAsString());
				}


				JsonObject jsonUserObj = null;
				if (jsonObj.get("user") != null) {				
					jsonUserObj = jsonObj.get("user").getAsJsonObject();
					if (jsonUserObj.get("id") != null) {
						tweet.setUserID(jsonUserObj.get("id").getAsString());
					}

					if (jsonUserObj.get("screen_name") != null) {
						tweet.setUserName(jsonUserObj.get("screen_name").getAsString());
						tweet.setTweetURL("https://twitter.com/" + tweet.getUserName() + "/status/" + tweet.getTweetID());
					}
					if (jsonUserObj.get("url") != null) {
						tweet.setUserURL(jsonUserObj.get("url").toString());
					}
				}
				return tweet;
			} catch (Exception ex) {
				return null;
			}
		}
		return null;
	}
	
	public Tweet toTweetFromLabeledDoc(HumanLabeledDocumentDTO doc) {
		if (doc != null) {
			try {
				Tweet tweet = this.toTweet(doc.getDoc().getData());
				return tweet;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
  	}
}
