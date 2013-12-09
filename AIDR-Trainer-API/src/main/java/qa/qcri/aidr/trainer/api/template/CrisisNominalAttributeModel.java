package qa.qcri.aidr.trainer.api.template;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/28/13
 * Time: 6:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrisisNominalAttributeModel {

    private Long cririsID;
    private Long nominalAttributeID;

    public CrisisNominalAttributeModel(Long cririsID, Long nominalAttributeID){
        this.cririsID = cririsID;
        this.nominalAttributeID = nominalAttributeID;
    }

    public Long getNominalAttributeID() {
        return nominalAttributeID;
    }

    public void setNominalAttributeID(Long nominalAttributeID) {
        this.nominalAttributeID = nominalAttributeID;
    }

    public Long getCririsID() {
        return cririsID;
    }

    public void setCririsID(Long cririsID) {
        this.cririsID = cririsID;
    }
}


