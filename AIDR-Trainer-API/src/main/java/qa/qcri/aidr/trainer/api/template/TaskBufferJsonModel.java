package qa.qcri.aidr.trainer.api.template;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 11/10/13
 * Time: 1:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class TaskBufferJsonModel {
    private Long documentID;

    private Long crisisID;

    private Set<NominalAttributeJsonModel>  attributeInfo;

    private String language;

    private String doctype;

    private String data;

    private Double valueAsTrainingSample;

    private Integer assignedCount;

    public TaskBufferJsonModel(Long documentID, Long crisisID,Set<NominalAttributeJsonModel>  attributeInfo, String language,String doctype,String data,Double valueAsTrainingSample,Integer assignedCount){
        this.documentID = documentID;
        this.crisisID = crisisID;
        this.attributeInfo = attributeInfo;
        this.language = language;
        this.doctype = doctype;
        this.valueAsTrainingSample = valueAsTrainingSample;
        this.assignedCount = assignedCount;
        this.data = data;
    }

    public Long getDocumentID() {
        return documentID;
    }

    public void setDocumentID(Long documentID) {
        this.documentID = documentID;
    }

    public Long getCrisisID() {
        return crisisID;
    }

    public void setCrisisID(Long crisisID) {
        this.crisisID = crisisID;
    }

    public Set<NominalAttributeJsonModel> getAttributeInfo() {
        return attributeInfo;
    }

    public void setAttributeInfo(Set<NominalAttributeJsonModel> attributeInfo) {
        this.attributeInfo = attributeInfo;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDoctype() {
        return doctype;
    }

    public void setDoctype(String doctype) {
        this.doctype = doctype;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Double getValueAsTrainingSample() {
        return valueAsTrainingSample;
    }

    public void setValueAsTrainingSample(Double valueAsTrainingSample) {
        this.valueAsTrainingSample = valueAsTrainingSample;
    }

    public Integer getAssignedCount() {
        return assignedCount;
    }

    public void setAssignedCount(Integer assignedCount) {
        this.assignedCount = assignedCount;
    }

}
