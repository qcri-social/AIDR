/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.dbmanager.dto.taggerapi;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Imran
 */
@XmlRootElement
public class TrainingDataDTO implements Serializable {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 2392073534449776415L;
	@XmlElement private Integer labelID;
	@XmlElement private String labelName;
	@XmlElement private String tweetJSON;
    
	@XmlElement private Integer labelerID; 
	@XmlElement private String labelerName;
	@XmlElement private Date labeledTime;
	@XmlElement private Integer totalRows;

	@XmlElement private Long documentID;

    /**
     * @return the labelID
     */
    public Integer getLabelID() {
        return labelID;
    }

    /**
     * @param labelID the labelID to set
     */
    public void setLabelID(Integer labelID) {
        this.labelID = labelID;
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
     * @return the tweetJSON
     */
    public String getTweetJSON() {
        return tweetJSON;
    }

    /**
     * @param tweetJSON the tweetJSON to set
     */
    public void setTweetJSON(String tweetJSON) {
        this.tweetJSON = tweetJSON;
    }

    /**
     * @return the labelerID
     */
    public Integer getLabelerID() {
        return labelerID;
    }

    /**
     * @param labelerID the labelerID to set
     */
    public void setLabelerID(Integer labelerID) {
        this.labelerID = labelerID;
    }

    /**
     * @return the labelerName
     */
    public String getLabelerName() {
        return labelerName;
    }

    /**
     * @param labelerName the labelerName to set
     */
    public void setLabelerName(String labelerName) {
        this.labelerName = labelerName;
    }

    /**
     * @return the labeledTime
     */
    public Date getLabeledTime() {
        return labeledTime;
    }

    /**
     * @param labeledTime the labeledTime to set
     */
    public void setLabeledTime(Date labeledTime) {
        this.labeledTime = labeledTime;
    }

    /**
     * @return the totalRows
     */
    public Integer getTotalRows() {
        return totalRows;
    }

    /**
     * @param totalRows the totalRows to set
     */
    public void setTotalRows(Integer totalRows) {
        this.totalRows = totalRows;
    }

    public Long getDocumentID() {
        return documentID;
    }

    public void setDocumentID(Long documentID) {
        this.documentID = documentID;
    }
}
