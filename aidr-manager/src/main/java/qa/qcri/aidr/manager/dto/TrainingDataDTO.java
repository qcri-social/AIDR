package qa.qcri.aidr.manager.dto;

import java.util.Date;

public class TrainingDataDTO {

    private Integer labelID;
    private String labelName;
    private String tweetJSON;

    private Integer labelerID;
    private String labelerName;
    private Date labeledTime;
    private Integer totalRows;

    private Long documentID;

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

    public Integer getTotalRows() {
        return totalRows;
    }

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