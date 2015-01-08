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
public class NominalAttribute implements Serializable {
    private static final long serialVersionUID = 1L;
   
    @XmlElement private Integer nominalAttributeID;
    
  
    @XmlElement private String name;
    
  
    @XmlElement private String description;
    
 
    @XmlElement private String code;
    
    @JsonManagedReference
    private Collection<ModelFamily> modelFamilyCollection;
   
    @JsonBackReference
    private Users users;
    
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
