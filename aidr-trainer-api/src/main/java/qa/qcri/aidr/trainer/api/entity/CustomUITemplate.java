package qa.qcri.aidr.trainer.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 4/2/14
 * Time: 3:55 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(catalog = "aidr_predict",name = "custom_ui_template")
public class CustomUITemplate implements Serializable {

    private static final long serialVersionUID = -5527566248002296042L;

    @Id
    @Column(name = "customUITemplateID")
    Long customUITemplateID;

    @Column (name = "crisisID", nullable = false)
    Long crisisID;

    @Column (name = "nominalAttributeID", nullable = false)
    Long nominalAttributeID;

    @Column (name = "templateType", nullable = false)
    Integer templateType;

    @Column (name = "templateValue", nullable = false)
    String  templateValue;

    @Column (name = "status", nullable = false)
    Integer status;

    @Column (name = "isActive", nullable = false)
    boolean isActive;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
