/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predict.dbentities;

/**
 *
 * @author Imran
 */
public class ModelFamilyEC {

    private int modelFamilyID;
    private int crisisID;
    private NominalAttributeEC nominalAttribute;
    private Integer currentModelID;
    private boolean isActive;
    private int trainingExampleCount;

    /**
     * @return the nominalAttribute
     */
    public NominalAttributeEC getNominalAttribute() {
        return nominalAttribute;
    }

    /**
     * @param nominalAttribute the nominalAttribute to set
     */
    public void setNominalAttribute(NominalAttributeEC nominalAttribute) {
        this.nominalAttribute = nominalAttribute;
    }

    /**
     * @return the trainingExampleCount
     */
    public int getTrainingExampleCount() {
        return trainingExampleCount;
    }

    /**
     * @param trainingExampleCount the trainingExampleCount to set
     */
    public void setTrainingExampleCount(int trainingExampleCount) {
        this.trainingExampleCount = trainingExampleCount;
    }

    public static enum State {

        NOT_ACTIVE, NOT_TRAINED, RUNNING
    }

    public State getState() {
        if (!isActive) {
            return State.NOT_ACTIVE;
        } else {
            if (currentModelID == null) {
                return State.NOT_TRAINED;
            } else {
                return State.RUNNING;
            }
        }
    }

    /**
     * @return the modelFamilyID
     */
    public int getModelFamilyID() {
        return modelFamilyID;
    }

    /**
     * @param modelFamilyID the modelFamilyID to set
     */
    public void setModelFamilyID(int modelFamilyID) {
        this.modelFamilyID = modelFamilyID;
    }

    /**
     * @return the crisisID
     */
    public int getCrisisID() {
        return crisisID;
    }

    /**
     * @param crisisID the crisisID to set
     */
    public void setCrisisID(int crisisID) {
        this.crisisID = crisisID;
    }

    /**
     * @return the currentModelID
     */
    public Integer getCurrentModelID() {
        return currentModelID;
    }

    /**
     * @param currentModelID the currentModelID to set
     */
    public void setCurrentModelID(Integer currentModelID) {
        this.currentModelID = currentModelID;
    }

    /**
     * @return the isActive
     */
    public boolean isIsActive() {
        return isActive;
    }

    /**
     * @param isActive the isActive to set
     */
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
