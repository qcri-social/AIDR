/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

//import org.codehaus.jackson.annotate.JsonBackReference;
//import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
//import org.codehaus.jackson.annotate.JsonManagedReference;

import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author Imran
 */

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
public class Crisis implements Serializable {


    private static final long serialVersionUID = -5527566248002296042L;

    @XmlElement 
    private Long crisisID;

 
    @XmlElement 
    private String name;

 
    @JoinColumn(name = "crisisTypeID", referencedColumnName = "crisisTypeID")
    @ManyToOne(optional = false)
    @JsonBackReference
    private CrisisType crisisType;
    
  
    @XmlElement 
    private String code;

 
    @JsonBackReference
    private Users users;

    
    @XmlTransient
    @JsonIgnore
    private Collection<ModelFamily> modelFamilyCollection;
    
 
    @XmlElement 
    private Boolean isTrashed;
    
    @XmlElement 
    private Boolean isMicromapperEnabled;
    
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
	
	public Boolean getIsMicromapperEnabled() {
		return isMicromapperEnabled;
	}

	public void setIsMicromapperEnabled(Boolean isMicromapperEnabled) {
		this.isMicromapperEnabled = isMicromapperEnabled;
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


