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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 *
 * @author Imran
 */
@Entity
@Table(name = "nominal_attribute")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NominalAttribute.findAll", query = "SELECT n FROM NominalAttribute n"),
    @NamedQuery(name = "NominalAttribute.findByNominalAttributeID", query = "SELECT n FROM NominalAttribute n WHERE n.nominalAttributeID = :nominalAttributeID"),
    @NamedQuery(name = "NominalAttribute.findByName", query = "SELECT n FROM NominalAttribute n WHERE n.name = :name"),
    @NamedQuery(name = "NominalAttribute.findByDescription", query = "SELECT n FROM NominalAttribute n WHERE n.description = :description"),
    @NamedQuery(name = "NominalAttribute.findByCode", query = "SELECT n FROM NominalAttribute n WHERE n.code = :code")})
public class NominalAttribute implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "nominalAttributeID")
    @XmlElement private Integer nominalAttributeID;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 140)
    @Column(name = "name")
    @XmlElement private String name;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 600)
    @Column(name = "description")
    @XmlElement private String description;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "code")
    @XmlElement private String code;
    
//    @ManyToMany(mappedBy = "nominalAttributeCollection")
//    private Collection<Crisis> crisisCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nominalAttribute")
    @JsonManagedReference
    private Collection<ModelFamily> modelFamilyCollection;
    
    @JoinColumn(name = "userID", referencedColumnName = "userID")
    @ManyToOne(optional = false)
    @JsonBackReference
    private Users users;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nominalAttribute")
    @JsonManagedReference
    private Collection<NominalLabel> nominalLabelCollection;

    public NominalAttribute() {
    }

    public NominalAttribute(Integer nominalAttributeID) {
        this.nominalAttributeID = nominalAttributeID;
    }

    public NominalAttribute(Integer nominalAttributeID, String name, String description, String code) {
        this.nominalAttributeID = nominalAttributeID;
        this.name = name;
        this.description = description;
        this.code = code;
    }

    public Integer getNominalAttributeID() {
        return nominalAttributeID;
    }

    public void setNominalAttributeID(Integer nominalAttributeID) {
        this.nominalAttributeID = nominalAttributeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

//    @XmlTransient
//    @JsonIgnore
//    public Collection<Crisis> getCrisisCollection() {
//        return crisisCollection;
//    }
//
//    public void setCrisisCollection(Collection<Crisis> crisisCollection) {
//        this.crisisCollection = crisisCollection;
//    }

    @XmlTransient
    @JsonIgnore
    public Collection<ModelFamily> getModelFamilyCollection() {
        return modelFamilyCollection;
    }

    public void setModelFamilyCollection(Collection<ModelFamily> modelFamilyCollection) {
        this.modelFamilyCollection = modelFamilyCollection;
    }
    
    //@XmlTransient
    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<NominalLabel> getNominalLabelCollection() {
        return nominalLabelCollection;
    }

    public void setNominalLabelCollection(Collection<NominalLabel> nominalLabelCollection) {
        this.nominalLabelCollection = nominalLabelCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nominalAttributeID != null ? nominalAttributeID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NominalAttribute)) {
            return false;
        }
        NominalAttribute other = (NominalAttribute) object;
        if ((this.nominalAttributeID == null && other.nominalAttributeID != null) || (this.nominalAttributeID != null && !this.nominalAttributeID.equals(other.nominalAttributeID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "qa.qcri.aidr.predictui.entities.NominalAttribute[ nominalAttributeID=" + nominalAttributeID + " ]";
    }
    
}
