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

@Entity
@Table(catalog = "aidr_predict", name = "crisis")
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

    public Set<ModelFamily> getModelFamilySet() {
        return modelFamilySet;
    }

    public void setModelFamilySet(Set<ModelFamily> modelFamilySet) {
        this.modelFamilySet = modelFamilySet;
    }

    @Id
    @Column(name = "crisisID")
    private Long crisisID;

    @Column (name = "name", nullable = false)
    private String name;

    @Column (name = "crisisTypeID", nullable = false)
    private Long crisisTypeID;

    @Column (name = "code", nullable = false)
    private String code;

    @Column (name = "userID", nullable = false)
    private Long userID;

    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumn(name="crisisID")
    private Set<ModelFamily> modelFamilySet;


}

