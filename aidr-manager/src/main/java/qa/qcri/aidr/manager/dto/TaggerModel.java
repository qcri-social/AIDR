package qa.qcri.aidr.manager.dto;

import qa.qcri.aidr.dbmanager.dto.ModelDTO;

public class TaggerModel {

    private Integer modelID;

    private String attribute;

    private String status;

    private long trainingExamples;

    private long classifiedDocuments;

    private double auc;
    
    private Integer modelFamilyID;

    private Integer attributeID;

    private Integer retrainingThreshold;

    public Integer getModelID() {
        return modelID;
    }

    public void setModelID(Integer modelID) {
        this.modelID = modelID;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTrainingExamples() {
        return trainingExamples;
    }

    public void setTrainingExamples(long trainingExamples) {
        this.trainingExamples = trainingExamples;
    }

    public long getClassifiedDocuments() {
        return classifiedDocuments;
    }

    public void setClassifiedDocuments(long classifiedDocuments) {
        this.classifiedDocuments = classifiedDocuments;
    }

    public double getAuc() {
        return auc;
    }

    public void setAuc(double auc) {
        this.auc = auc;
    }

    /**
     * @return the modelFamilyID
     */
    public Integer getModelFamilyID() {
        return modelFamilyID;
    }

    /**
     * @param modelFamilyID the modelFamilyID to set
     */
    public void setModelFamilyID(Integer modelFamilyID) {
        this.modelFamilyID = modelFamilyID;
    }

    public Integer getAttributeID() {
        return attributeID;
    }

    public void setAttributeID(Integer attributeID) {
        this.attributeID = attributeID;
    }

    public Integer getRetrainingThreshold() {
        return retrainingThreshold;
    }

    public void setRetrainingThreshold(Integer retrainingThreshold) {
        this.retrainingThreshold = retrainingThreshold;
    }
    
    public TaggerModel() {}
    
}
