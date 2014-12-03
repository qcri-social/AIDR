/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.dbmanager.dto.taggerapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Imran
 */
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModelDTOWrapper implements Serializable {

    @XmlElement
    private Long modelID;
    @XmlElement
    private Long modelFamilyID;
    @XmlElement
    private Long attributeID;
    @XmlElement
    private String attribute;
    @XmlElement
    private String status;
    @XmlElement
    private long trainingExamples;
    @XmlElement
    private long classifiedDocuments;
    @XmlElement
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
    public Long getModelID() {
        return modelID;
    }

    /**
     * @param modelID the modelID to set
     */
    public void setModelID(Long modelID) {
        this.modelID = modelID;
    }

    /**
     * @return the modelFamilyID
     */
    public Long getModelFamilyID() {
        return modelFamilyID;
    }

    /**
     * @param modelFamilyID the modelFamilyID to set
     */
    public void setModelFamilyID(Long modelFamilyID) {
        this.modelFamilyID = modelFamilyID;
    }

    public Long getAttributeID() {
        return attributeID;
    }

    public void setAttributeID(Long attributeID) {
        this.attributeID = attributeID;
    }
}
