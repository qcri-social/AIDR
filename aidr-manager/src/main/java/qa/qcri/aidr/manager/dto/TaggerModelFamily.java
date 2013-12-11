package qa.qcri.aidr.manager.dto;

public class TaggerModelFamily {

    private TaggerCrisis crisis;

    private TaggerAttribute nominalAttribute;

    private Boolean isActive;

    private Integer modelFamilyID;

    public TaggerModelFamily() {
    }

    public TaggerModelFamily(TaggerCrisis crisis, TaggerAttribute nominalAttribute, Boolean active) {
        this.crisis = crisis;
        this.nominalAttribute = nominalAttribute;
        isActive = active;
    }

    public TaggerCrisis getCrisis() {
        return crisis;
    }

    public void setCrisis(TaggerCrisis crisis) {
        this.crisis = crisis;
    }

    public TaggerAttribute getNominalAttribute() {
        return nominalAttribute;
    }

    public void setNominalAttribute(TaggerAttribute nominalAttribute) {
        this.nominalAttribute = nominalAttribute;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public Integer getModelFamilyID() {
        return modelFamilyID;
    }

    public void setModelFamilyID(Integer modelFamilyID) {
        this.modelFamilyID = modelFamilyID;
    }
}
