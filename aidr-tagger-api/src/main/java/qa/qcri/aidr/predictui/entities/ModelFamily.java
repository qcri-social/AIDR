/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.entities;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

//import org.codehaus.jackson.annotate.JsonBackReference;
//import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
//import org.codehaus.jackson.annotate.JsonManagedReference;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 *
 * @author Imran
 */

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
public class ModelFamily implements Serializable {
    private static final long serialVersionUID = 1L;
 
    @XmlElement private Integer modelFamilyID;
    
  
    @XmlElement private boolean isActive;
    
    @JsonManagedReference
    private Collection<Model> modelCollection;
    
    @JsonBackReference
    private NominalAttribute nominalAttribute;

    @JsonBackReference
    private Crisis crisis;

    public ModelFamily() {
    }

    public ModelFamily(Integer modelFamilyID) {
        this.modelFamilyID = modelFamilyID;
    }

    public ModelFamily(Integer modelFamilyID, boolean isActive) {
        this.modelFamilyID = modelFamilyID;
        this.isActive = isActive;
    }

    public Integer getModelFamilyID() {
        return modelFamilyID;
    }

    public void setModelFamilyID(Integer modelFamilyID) {
        this.modelFamilyID = modelFamilyID;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<Model> getModelCollection() {
        return modelCollection;
    }

    public void setModelCollection(Collection<Model> modelCollection) {
        this.modelCollection = modelCollection;
    }

    public NominalAttribute getNominalAttribute() {
        return nominalAttribute;
    }

    public void setNominalAttribute(NominalAttribute nominalAttribute) {
        this.nominalAttribute = nominalAttribute;
    }

//    public Model getModel() {
//        return model;
//    }
//
//    public void setModel(Model model) {
//        this.model = model;
//    }

    @XmlTransient
    @JsonIgnore
    public Crisis getCrisis() {
        return crisis;
    }

    public void setCrisis(Crisis crisis) {
        this.crisis = crisis;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (modelFamilyID != null ? modelFamilyID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ModelFamily)) {
            return false;
        }
        ModelFamily other = (ModelFamily) object;
        if ((this.modelFamilyID == null && other.modelFamilyID != null) || (this.modelFamilyID != null && !this.modelFamilyID.equals(other.modelFamilyID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "qa.qcri.aidr.predictui.entities.ModelFamily[ modelFamilyID=" + modelFamilyID + " ]";
    }
    
}
