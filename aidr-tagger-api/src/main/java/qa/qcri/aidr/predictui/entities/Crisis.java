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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
//import org.codehaus.jackson.annotate.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author Imran
 */
@Entity
@Table(name = "crisis")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Crisis.findAll", query = "SELECT c FROM Crisis c"),
    @NamedQuery(name = "Crisis.findByCrisisID", query = "SELECT c FROM Crisis c WHERE c.crisisID = :crisisID"),
    @NamedQuery(name = "Crisis.findByName", query = "SELECT c FROM Crisis c WHERE c.name = :name"),
    @NamedQuery(name = "Crisis.findByCode", query = "SELECT c FROM Crisis c WHERE c.code = :code"),
    @NamedQuery(name = "Crisis.findByUserID", query = "SELECT c FROM Crisis c WHERE c.users = :user")})
public class Crisis implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "crisisID")
    private Integer crisisID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 140)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "code")
    private String code;
//    @JoinTable(name = "crisis_nominal_attribute", joinColumns = {
//        @JoinColumn(name = "crisisID", referencedColumnName = "crisisID")}, inverseJoinColumns = {
//        @JoinColumn(name = "nominalAttributeID", referencedColumnName = "nominalAttributeID")})
//    @ManyToMany
//    private Collection<NominalAttribute> nominalAttributeCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "crisis")
    private Collection<Document> documentCollection;
    
    @OneToMany( cascade = CascadeType.ALL, mappedBy = "crisis")
    private Collection<ModelFamily> modelFamilyCollection;
    
    @JoinColumn(name = "crisisTypeID", referencedColumnName = "crisisTypeID")
    @ManyToOne(optional = false)
    private CrisisType crisisType;
    
    @JoinColumn(name = "userID", referencedColumnName = "userID")
    @ManyToOne(optional = false)
    private Users users;

    public Crisis() {
    }

    public Crisis(Integer crisisID) {
        this.crisisID = crisisID;
    }

    public Crisis(Integer crisisID, String name, String code) {
        this.crisisID = crisisID;
        this.name = name;
        this.code = code;
    }

    public Integer getCrisisID() {
        return crisisID;
    }

    public void setCrisisID(Integer crisisID) {
        this.crisisID = crisisID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

//    @XmlTransient
//    @JsonIgnore
//    public Collection<NominalAttribute> getNominalAttributeCollection() {
//        return nominalAttributeCollection;
//    }
//
//    public void setNominalAttributeCollection(Collection<NominalAttribute> nominalAttributeCollection) {
//        this.nominalAttributeCollection = nominalAttributeCollection;
//    }

    @XmlTransient
    @JsonIgnore
    public Collection<Document> getDocumentCollection() {
        return documentCollection;
    }

    public void setDocumentCollection(Collection<Document> documentCollection) {
        this.documentCollection = documentCollection;
    }

    //@XmlTransient
    @JsonIgnore
    public Collection<ModelFamily> getModelFamilyCollection() {
        return modelFamilyCollection;
    }

    public void setModelFamilyCollection(Collection<ModelFamily> modelFamilyCollection) {
        this.modelFamilyCollection = modelFamilyCollection;
    }

    public CrisisType getCrisisType() {
        return crisisType;
    }

    public void setCrisisType(CrisisType crisisType) {
        this.crisisType = crisisType;
    }
    
    //@XmlTransient
    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (crisisID != null ? crisisID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Crisis)) {
            return false;
        }
        Crisis other = (Crisis) object;
        if ((this.crisisID == null && other.crisisID != null) || (this.crisisID != null && !this.crisisID.equals(other.crisisID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "qa.qcri.aidr.predictui.entities.Crisis[ crisisID=" + crisisID + " ]";
    }
    
}
