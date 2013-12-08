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
public class FetcherResponseWrapper implements Serializable{
    
    private String crisisId;
    private String crisisName;
    private String tweetJson;

    public FetcherResponseWrapper() {
    }

   
    /**
     * @return the tweetJson
     */
    public String getTweetJson() {
        return tweetJson;
    }

    /**
     * @param tweetJson the tweetJson to set
     */
    public void setTweetJson(String tweetJson) {
        this.tweetJson = tweetJson;
    }

    /**
     * @return the crisisId
     */
    public String getCrisisId() {
        return crisisId;
    }

    /**
     * @param crisisId the crisisId to set
     */
    public void setCrisisId(String crisisId) {
        this.crisisId = crisisId;
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

    
    
}
