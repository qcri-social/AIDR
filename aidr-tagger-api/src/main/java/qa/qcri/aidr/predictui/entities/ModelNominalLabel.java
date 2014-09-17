/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author Imran
 */
@Entity
@Table(name = "model_nominal_label")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ModelNominalLabel.findAll", query = "SELECT m FROM ModelNominalLabel m"),
    @NamedQuery(name = "ModelNominalLabel.findByModelID", query = "SELECT m FROM ModelNominalLabel m WHERE m.modelNominalLabelPK.modelID = :modelID"),
    @NamedQuery(name = "ModelNominalLabel.findByNominalLabelID", query = "SELECT m FROM ModelNominalLabel m WHERE m.modelNominalLabelPK.nominalLabelID = :nominalLabelID"),
    @NamedQuery(name = "ModelNominalLabel.findByLabelPrecision", query = "SELECT m FROM ModelNominalLabel m WHERE m.labelPrecision = :labelPrecision"),
    @NamedQuery(name = "ModelNominalLabel.findByLabelRecall", query = "SELECT m FROM ModelNominalLabel m WHERE m.labelRecall = :labelRecall"),
    @NamedQuery(name = "ModelNominalLabel.findByLabelAuc", query = "SELECT m FROM ModelNominalLabel m WHERE m.labelAuc = :labelAuc"),
    @NamedQuery(name = "ModelNominalLabel.findByClassifiedDocumentCount", query = "SELECT m FROM ModelNominalLabel m WHERE m.classifiedDocumentCount = :classifiedDocumentCount"),
    @NamedQuery(name = "ModelNominalLabel.findByModel", query = "SELECT m FROM ModelNominalLabel m WHERE m.model = :model")})
public class ModelNominalLabel implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ModelNominalLabelPK modelNominalLabelPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "labelPrecision")
    @XmlElement private Double labelPrecision;
    
    @Column(name = "labelRecall")
    @XmlElement private Double labelRecall;
    
    @Column(name = "labelAuc")
    @XmlElement private Double labelAuc;
    
    @Column(name = "classifiedDocumentCount")
    @XmlElement private Integer classifiedDocumentCount;
    
    @JoinColumn(name = "modelID", referencedColumnName = "modelID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    @JsonBackReference
    private Model model;
    
    @JoinColumn(name = "nominalLabelID", referencedColumnName = "nominalLabelID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    @JsonBackReference
    private NominalLabel nominalLabel;

    public ModelNominalLabel() {
    }

    public ModelNominalLabel(ModelNominalLabelPK modelNominalLabelPK) {
        this.modelNominalLabelPK = modelNominalLabelPK;
    }

    public ModelNominalLabel(int modelID, int nominalLabelID) {
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
    @JsonIgnore
    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }
    
    @XmlTransient
    @JsonIgnore
    public NominalLabel getNominalLabel() {
        return nominalLabel;
    }

    public void setNominalLabel(NominalLabel nominalLabel) {
        this.nominalLabel = nominalLabel;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (modelNominalLabelPK != null ? modelNominalLabelPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ModelNominalLabel)) {
            return false;
        }
        ModelNominalLabel other = (ModelNominalLabel) object;
        if ((this.modelNominalLabelPK == null && other.modelNominalLabelPK != null) || (this.modelNominalLabelPK != null && !this.modelNominalLabelPK.equals(other.modelNominalLabelPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "qa.qcri.aidr.predictui.entities.ModelNominalLabel[ modelNominalLabelPK=" + modelNominalLabelPK + " ]";
    }
    
}
