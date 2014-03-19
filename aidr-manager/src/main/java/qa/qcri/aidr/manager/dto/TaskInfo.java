package qa.qcri.aidr.manager.dto;

public class TaskInfo {

    private Integer documentID;

    private String text;

    private String category;

    private Integer aidrID;

    private String tweetid;

    private Integer crisisID;

    private Integer attributeID;

    public Integer getDocumentID() {
        return documentID;
    }

    public void setDocumentID(Integer documentID) {
        this.documentID = documentID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getAidrID() {
        return aidrID;
    }

    public void setAidrID(Integer aidrID) {
        this.aidrID = aidrID;
    }

    public String getTweetid() {
        return tweetid;
    }

    public void setTweetid(String tweetid) {
        this.tweetid = tweetid;
    }

    public Integer getCrisisID() {
        return crisisID;
    }

    public void setCrisisID(Integer crisisID) {
        this.crisisID = crisisID;
    }

    public Integer getAttributeID() {
        return attributeID;
    }

    public void setAttributeID(Integer attributeID) {
        this.attributeID = attributeID;
    }
}
