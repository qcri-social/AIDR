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
@Entity
@Table(name = "custom_ui_template")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "CustomUITemplate.findByCrisisD", query = "SELECT c FROM CustomUITemplate c WHERE c.crisisID = :crisisID"),
        @NamedQuery(name = "CustomUITemplate.findBasedOnTypeByCrisisD", query = "SELECT c FROM CustomUITemplate c WHERE c.crisisID = :crisisID and c.templateType = :templateType"),
        @NamedQuery(name = "CustomUITemplate.findByCrisisIDAndAttributeID", query = "SELECT c FROM CustomUITemplate c WHERE c.crisisID = :crisisID and c.nominalAttributeID = :nominalAttributeID"),
        @NamedQuery(name = "CustomUITemplate.findBasedOnTypeByCrisisIDAndAttributeID", query = "SELECT c FROM CustomUITemplate c WHERE c.crisisID = :crisisID and c.nominalAttributeID = :nominalAttributeID and c.templateType = :templateType")})
public class CustomUITemplate implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "customUITemplateID")
    @XmlElement private Long customUITemplateID;

    @Column(name = "crisisID")
    @XmlElement private Long crisisID;

    @Column(name = "nominalAttributeID")
    @XmlElement private Long nominalAttributeID;

    @Column(name = "templateType")
    @XmlElement private Integer templateType;

    @Column(name = "templateValue")
    private String templateValue;

    @Column(name = "isActive")
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
