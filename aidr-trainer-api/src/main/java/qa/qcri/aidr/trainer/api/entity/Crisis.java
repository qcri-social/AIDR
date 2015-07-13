package qa.qcri.aidr.trainer.api.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;


@Entity
@Table(catalog = "aidr_predict", name = "crisis")
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
public class Crisis implements Serializable {

    private static final long serialVersionUID = -5527566248002296042L;

    public Crisis(){}

    public Crisis(Long crisisID, String name, String code, boolean isTrashed){
        this.crisisID = crisisID;
        this.name = name;
        this.code = code;
        this.isTrashed = isTrashed;
    }
    
    public Crisis(Long crisisID, String name, String code, boolean isTrashed, boolean isMicromapperEnabled){
        this.crisisID = crisisID;
        this.name = name;
        this.code = code;
        this.isTrashed = isTrashed;
        this.isMicromapperEnabled = isMicromapperEnabled;
    }

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

    public Long getCrisisTypeID() {
        return crisisTypeID;
    }

    public void setCrisisTypeID(Long crisisTypeID) {
        this.crisisTypeID = crisisTypeID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Boolean getTrashed() {
        return isTrashed;
    }

    public void setTrashed(Boolean trashed) {
        isTrashed = trashed;
    }

    public Set<ModelFamily> getModelFamilySet() {
        return modelFamilySet;
    }

    public void setModelFamilySet(Set<ModelFamily> modelFamilySet) {
        this.modelFamilySet = modelFamilySet;
    }
    
    public Boolean getMicromapperEnabled() {
		return isMicromapperEnabled;
	}

	public void setMicromapperEnabled(Boolean isMicromapperEnabled) {
		this.isMicromapperEnabled = isMicromapperEnabled;
	}

	@XmlElement
    @Id
    @Column(name = "crisisID")
    private Long crisisID;
    
    @XmlElement
    @Column (name = "name", nullable = false)
    private String name;
    
    @XmlElement
    @Column (name = "crisisTypeID", nullable = false)
    private Long crisisTypeID;
    
    @XmlElement
    @Column (name = "code", nullable = false)
    private String code;
    
    @XmlElement
    @Column (name = "userID", nullable = false)
    private Long userID;
    
    @XmlElement
    @Column (name = "isTrashed", nullable = false)
    private Boolean isTrashed;
    
    @XmlElement
    @Column (name = "isMicromapperEnabled", nullable = false)
    private Boolean isMicromapperEnabled;
    
    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumn(name="crisisID")
    private Set<ModelFamily> modelFamilySet;


}

