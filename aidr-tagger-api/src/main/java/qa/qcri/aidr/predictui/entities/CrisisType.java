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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 *
 * @author Imran
 */
@Entity
@Table(name = "crisis_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CrisisType.findAll", query = "SELECT c FROM CrisisType c"),
    @NamedQuery(name = "CrisisType.findByCrisisTypeID", query = "SELECT c FROM CrisisType c WHERE c.crisisTypeID = :crisisTypeID"),
    @NamedQuery(name = "CrisisType.findByName", query = "SELECT c FROM CrisisType c WHERE c.name = :name")})
public class CrisisType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "crisisTypeID")
    @XmlElement private Integer crisisTypeID;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 140)
    @Column(name = "name")
    @XmlElement private String name;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "crisisType")
    @JsonManagedReference
    private Collection<Crisis> crisisCollection;

    public CrisisType() {
    }

    public CrisisType(Integer crisisTypeID) {
        this.crisisTypeID = crisisTypeID;
    }

    public CrisisType(Integer crisisTypeID, String name) {
        this.crisisTypeID = crisisTypeID;
        this.name = name;
    }

    public Integer getCrisisTypeID() {
        return crisisTypeID;
    }

    public void setCrisisTypeID(Integer crisisTypeID) {
        this.crisisTypeID = crisisTypeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<Crisis> getCrisisCollection() {
        return crisisCollection;
    }

    public void setCrisisCollection(Collection<Crisis> crisisCollection) {
        this.crisisCollection = crisisCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (crisisTypeID != null ? crisisTypeID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CrisisType)) {
            return false;
        }
        CrisisType other = (CrisisType) object;
        if ((this.crisisTypeID == null && other.crisisTypeID != null) || (this.crisisTypeID != null && !this.crisisTypeID.equals(other.crisisTypeID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "qa.qcri.aidr.predictui.entities.CrisisType[ crisisTypeID=" + crisisTypeID + " ]";
    }
    
}
