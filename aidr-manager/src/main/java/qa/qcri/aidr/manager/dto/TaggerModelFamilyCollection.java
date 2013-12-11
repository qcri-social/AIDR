package qa.qcri.aidr.manager.dto;

public class TaggerModelFamilyCollection {

    private String isActive;

    private Integer modelFamilyID;

    private TaggerAttribute nominalAttribute;

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String active) {
        isActive = active;
    }

    public Integer getModelFamilyID() {
        return modelFamilyID;
    }

    public void setModelFamilyID(Integer modelFamilyID) {
        this.modelFamilyID = modelFamilyID;
    }

    public TaggerAttribute getNominalAttribute() {
        return nominalAttribute;
    }

    public void setNominalAttribute(TaggerAttribute nominalAttribute) {
        this.nominalAttribute = nominalAttribute;
    }
}
