/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.task.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 *
 * @author Koushik
 */
@Entity
@Table(catalog = "aidr_predict",name = "crisis")
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
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

