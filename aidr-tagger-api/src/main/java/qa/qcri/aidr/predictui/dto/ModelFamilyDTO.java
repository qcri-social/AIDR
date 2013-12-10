package qa.qcri.aidr.predictui.dto;

import qa.qcri.aidr.predictui.entities.Crisis;
import qa.qcri.aidr.predictui.entities.NominalAttribute;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ModelFamilyDTO {

    private Crisis crisis;

    private NominalAttribute nominalAttribute;

    private Boolean isActive;

    public Crisis getCrisis() {
        return crisis;
    }

    public void setCrisis(Crisis crisis) {
        this.crisis = crisis;
    }

    public NominalAttribute getNominalAttribute() {
        return nominalAttribute;
    }

    public void setNominalAttribute(NominalAttribute nominalAttribute) {
        this.nominalAttribute = nominalAttribute;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

}
