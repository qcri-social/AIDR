/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.collector.beans;

import java.io.Serializable;

/**
 *
 * @author Imran
 */
public class Tweet  implements Document, Serializable{
    
    private Long tweetID;
    private String message;
    private String user;
    private boolean isRT;
    private String userURL;

   

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the isRT
     */
    public boolean isIsRT() {
        return isRT;
    }

    /**
     * @param isRT the isRT to set
     */
    public void setIsRT(boolean isRT) {
        this.isRT = isRT;
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
    public Long getTweetID() {
        return tweetID;
    }

    /**
     * @param tweetID the tweetID to set
     */
    public void setTweetID(Long tweetID) {
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
    
    
}
