/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.dto;

import qa.qcri.aidr.predictui.entities.*;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Imran
 */
@XmlRootElement
public class ModelNominalLabelDTO implements Serializable {

    protected ModelNominalLabelPK modelNominalLabelPK;
    private Double labelPrecision;
    private Double labelRecall;
    private Double labelAuc;
    private Integer classifiedDocumentCount;
    private Model model;
    private NominalLabel nominalLabel;
    private int trainingDocuments;
    private String modelStatus;
    private Integer nominalAttributeId;

    public ModelNominalLabelDTO() {
    }

    public ModelNominalLabelDTO(ModelNominalLabelPK modelNominalLabelPK) {
        this.modelNominalLabelPK = modelNominalLabelPK;
    }

    public ModelNominalLabelDTO(int modelID, int nominalLabelID) {
        this.modelNominalLabelPK = new ModelNominalLabelPK(modelID, nominalLabelID);
    }

    public ModelNominalLabelPK getModelNominalLabelPK() {
        return modelNominalLabelPK;
    }

    public void setModelNominalLabelPK(ModelNominalLabelPK modelNominalLabelPK) {
        this.modelNominalLabelPK = modelNominalLabelPK;
    }

    public Double getLabelPrecision() {
        return labelPrecision;
    }

    public void setLabelPrecision(Double labelPrecision) {
        this.labelPrecision = labelPrecision;
    }

    public Double getLabelRecall() {
        return labelRecall;
    }

    public void setLabelRecall(Double labelRecall) {
        this.labelRecall = labelRecall;
    }

    public Double getLabelAuc() {
        return labelAuc;
    }

    public void setLabelAuc(Double labelAuc) {
        this.labelAuc = labelAuc;
    }

    public Integer getClassifiedDocumentCount() {
        return classifiedDocumentCount;
    }

    public void setClassifiedDocumentCount(Integer classifiedDocumentCount) {
        this.classifiedDocumentCount = classifiedDocumentCount;
    }

    @XmlTransient
    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public NominalLabel getNominalLabel() {
        return nominalLabel;
    }

    public void setNominalLabel(NominalLabel nominalLabel) {
        this.nominalLabel = nominalLabel;
    }

    public Integer getNominalAttributeId() {
        return nominalAttributeId;
    }

    public void setNominalAttributeId(Integer nominalAttributeId) {
        this.nominalAttributeId = nominalAttributeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (modelNominalLabelPK != null ? modelNominalLabelPK.hashCode() : 0);
        return hash;
    }

    

    @Override
    public String toString() {
        return "qa.qcri.aidr.predictui.entities.ModelNominalLabel[ modelNominalLabelPK=" + modelNominalLabelPK + " ]";
    }

    /**
     * @return the trainingDocuments
     */
    public int getTrainingDocuments() {
        return trainingDocuments;
    }

    /**
     * @param trainingDocuments the trainingDocuments to set
     */
    public void setTrainingDocuments(int trainingDocuments) {
        this.trainingDocuments = trainingDocuments;
    }

    /**
     * @return the modelStatus
     */
    public String isModelStatus() {
        return modelStatus;
    }

    /**
     * @param modelStatus the modelStatus to set
     */
    public void setModelStatus(String modelStatus) {
        this.modelStatus = modelStatus;
    }
    
}
