package qa.qcri.aidr.trainer.api.template;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/23/13
 * Time: 8:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class ModelFamilyJsonModel {
    public Long getModelFamilyID() {
        return modelFamilyID;
    }

    public void setModelFamilyID(Long modelFamilyID) {
        this.modelFamilyID = modelFamilyID;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public CrisisJsonModel getCrisisJsonModel() {
        return crisisJsonModel;
    }

    public void setCrisisJsonModel(CrisisJsonModel crisisJsonModel) {
        this.crisisJsonModel = crisisJsonModel;
    }

    public NominalAttributeJsonModel getNominalAttributeJsonModel() {
        return nominalAttributeJsonModel;
    }

    public void setNominalAttributeJsonModel(NominalAttributeJsonModel nominalAttributeJsonModel) {
        this.nominalAttributeJsonModel = nominalAttributeJsonModel;
    }

    private Long modelFamilyID;
    private boolean isActive;
    private CrisisJsonModel crisisJsonModel;
    private NominalAttributeJsonModel nominalAttributeJsonModel;
}
