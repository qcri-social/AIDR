package qa.qcri.aidr.manager.dto;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 3/23/14
 * Time: 1:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class UITemplateRequest {

    private Long customUITemplateID;
    private Long crisisID;
    private Long nominalAttributeID;
    private Integer templateType;
    private String templateValue;
    private boolean isActive;

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

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
