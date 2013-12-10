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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author Imran
 */
@Entity
@Table(name = "model_family")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ModelFamily.findAll", query = "SELECT m FROM ModelFamily m"),
    @NamedQuery(name = "ModelFamily.findByModelFamilyID", query = "SELECT m FROM ModelFamily m WHERE m.modelFamilyID = :modelFamilyID"),
    @NamedQuery(name = "ModelFamily.findByIsActive", query = "SELECT m FROM ModelFamily m WHERE m.isActive = :isActive"),
    @NamedQuery(name = "ModelFamily.findByCrisis", query = "SELECT m FROM ModelFamily m WHERE m.crisis = :crisis")})
public class ModelFamily implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "modelFamilyID")
    private Integer modelFamilyID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "isActive")
    private boolean isActive;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "modelFamily")
    private Collection<Model> modelCollection;
    @JoinColumn(name = "nominalAttributeID", referencedColumnName = "nominalAttributeID")
    @ManyToOne(optional = false)
    private NominalAttribute nominalAttribute;
//    @JoinColumn(name = "currentModelID", referencedColumnName = "modelID")
//    @ManyToOne
//    private Model model;
    @JoinColumn(name = "crisisID", referencedColumnName = "crisisID")
    @ManyToOne(optional = false)
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
