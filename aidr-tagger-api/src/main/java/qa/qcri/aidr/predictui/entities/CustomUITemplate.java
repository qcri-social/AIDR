package qa.qcri.aidr.predictui.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 3/17/14
 * Time: 10:00 PM
 * To change this template use File | Settings | File Templates.
 */

@XmlRootElement
public class CustomUITemplate implements Serializable {
    private static final long serialVersionUID = 1L;

 
    @XmlElement private Long customUITemplateID;

    @Column(name = "crisisID")
    @XmlElement private Long crisisID;

  
    @XmlElement private Long nominalAttributeID;


    @XmlElement private Integer templateType;

  
    private String templateValue;

  
    @XmlElement private boolean isActive;

    public Long getCustomUITemplateID() {
        return customUITemplateID;
    }

    public void setCustomUITemplateID(Long customUITemplateID) {
        this.customUITemplateID = customUITemplateID;
    }

    public Long getCrisisID() {
        return crisisID;
    }

    public void setCrisisID(Long crisisID) {
        this.crisisID = crisisID;
    }

    public Long getNominalAttributeID() {
        return nominalAttributeID;
    }

    public void setNominalAttributeID(Long nominalAttributeID) {
        this.nominalAttributeID = nominalAttributeID;
    }

    public Integer getTemplateType() {
        return templateType;
    }

    public void setTemplateType(Integer templateType) {
        this.templateType = templateType;
    }

    public String getTemplateValue() {
        return templateValue;
    }

    public void setTemplateValue(String templateValue) {
        this.templateValue = templateValue;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }
}
