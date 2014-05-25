/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.task.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author Imran
 */
@Entity
@Table(catalog = "aidr_predict",name = "crisis")
@XmlRootElement
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

}

