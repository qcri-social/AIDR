package qa.qcri.aidr.manager.dto;

public class TaskAnswerRequest {

    private Integer documentID;

    private Integer crisisID;

    private String category;

    private String taskcreated;

    private String taskcompleted;

    private Integer attributeID;

    public Integer getCrisisID() {
        return crisisID;
    }

    public void setCrisisID(Integer crisisID) {
        this.crisisID = crisisID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getDocumentID() {
        return documentID;
    }

    public void setDocumentID(Integer documentID) {
        this.documentID = documentID;
    }

    public String getTaskcreated() {
        return taskcreated;
    }

    public void setTaskcreated(String taskcreated) {
        this.taskcreated = taskcreated;
    }

    public String getTaskcompleted() {
        return taskcompleted;
    }

    public void setTaskcompleted(String taskcompleted) {
        this.taskcompleted = taskcompleted;
    }

    public Integer getAttributeID() {
        return attributeID;
    }

    public void setAttributeID(Integer attributeID) {
        this.attributeID = attributeID;
    }
}
