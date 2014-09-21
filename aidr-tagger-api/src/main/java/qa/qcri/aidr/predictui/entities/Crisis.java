/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Collection;

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


    private static final long serialVersionUID = -5527566248002296042L;

    public Crisis(){}

    public Long getCrisisID() {
        return crisisID;
    }

    public void setCrisisID(Long crisisID) {
        this.crisisID = crisisID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public CrisisType getCrisisType() {
        return crisisType;
    }

    public void setCrisisType(CrisisType crisisType) {
        this.crisisType = crisisType;
    }
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "crisisID")
    @XmlElement 
    private Long crisisID;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 140)
    @Column(name = "name")
    @XmlElement 
    private String name;

    //@Column (name = "crisisTypeID", nullable = false)
    //private Long crisisTypeID;
    
    @JoinColumn(name = "crisisTypeID", referencedColumnName = "crisisTypeID")
    @ManyToOne(optional = false)
    @JsonBackReference
    private CrisisType crisisType;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @XmlElement 
    private String code;

    //@Column (name = "userID", nullable = false)
    //private Long userID;
    @JoinColumn(name = "userID", referencedColumnName = "userID")
    @ManyToOne(optional = false)
    @JsonBackReference
    private Users users;

    
    @OneToMany( cascade = CascadeType.ALL, mappedBy = "crisis")
    @JsonManagedReference
    @XmlElement 
    private Collection<ModelFamily> modelFamilyCollection;
    
    @Basic(optional = false)
    @NotNull
    @Column(name="isTrashed")
    @XmlElement 
    private Boolean isTrashed;
    
    @XmlTransient
    @JsonIgnore
    public Collection<ModelFamily> getModelFamilyCollection() {
        return modelFamilyCollection;
    }

    public void setModelFamilyCollection(Collection<ModelFamily> modelFamilyCollection) {
        this.modelFamilyCollection = modelFamilyCollection;
    }
    
    public Boolean getIsTrashed() {
		return isTrashed;
	}
	public void setIsTrashed(Boolean isTrashed) {
		this.isTrashed = isTrashed;
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


