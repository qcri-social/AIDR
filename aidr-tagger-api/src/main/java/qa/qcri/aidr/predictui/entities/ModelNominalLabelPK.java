/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Imran
 */
@Embeddable
public class ModelNominalLabelPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "modelID")
    private int modelID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nominalLabelID")
    private int nominalLabelID;

    public ModelNominalLabelPK() {
    }

    public ModelNominalLabelPK(int modelID, int nominalLabelID) {
        this.modelID = modelID;
        this.nominalLabelID = nominalLabelID;
    }

    public int getModelID() {
        return modelID;
    }

    public void setModelID(int modelID) {
        this.modelID = modelID;
    }

    public int getNominalLabelID() {
        return nominalLabelID;
    }

    public void setNominalLabelID(int nominalLabelID) {
        this.nominalLabelID = nominalLabelID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) modelID;
        hash += (int) nominalLabelID;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ModelNominalLabelPK)) {
            return false;
        }
        ModelNominalLabelPK other = (ModelNominalLabelPK) object;
        if (this.modelID != other.modelID) {
            return false;
        }
        if (this.nominalLabelID != other.nominalLabelID) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "qa.qcri.aidr.predictui.entities.ModelNominalLabelPK[ modelID=" + modelID + ", nominalLabelID=" + nominalLabelID + " ]";
    }
    
}
