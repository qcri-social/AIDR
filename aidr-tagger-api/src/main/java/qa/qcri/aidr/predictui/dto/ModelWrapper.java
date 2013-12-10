/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.dto;

/**
 *
 * @author Imran
 */
public class ModelWrapper {
    
    private Integer modelID;
    private Integer modelFamilyID;
    private String attribute;
    private String status;
    private long trainingExamples;
    private long classifiedDocuments;
    private double auc;

    /**
     * @return the attribute
     */
    public String getAttribute() {
        return attribute;
    }

    /**
     * @param attribute the attribute to set
     */
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the trainingExamples
     */
    public long getTrainingExamples() {
        return trainingExamples;
    }

    /**
     * @param trainingExamples the trainingExamples to set
     */
    public void setTrainingExamples(long trainingExamples) {
        this.trainingExamples = trainingExamples;
    }

    /**
     * @return the classifiedDocuments
     */
    public long getClassifiedDocuments() {
        return classifiedDocuments;
    }

    /**
     * @param classifiedDocuments the classifiedDocuments to set
     */
    public void setClassifiedDocuments(long classifiedDocuments) {
        this.classifiedDocuments = classifiedDocuments;
    }

    /**
     * @return the auc
     */
    public double getAuc() {
        return auc;
    }

    /**
     * @param auc the auc to set
     */
    public void setAuc(double auc) {
        this.auc = auc;
    }

    /**
     * @return the modelID
     */
    public Integer getModelID() {
        return modelID;
    }

    /**
     * @param modelID the modelID to set
     */
    public void setModelID(Integer modelID) {
        this.modelID = modelID;
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
    
    
            
    
}
