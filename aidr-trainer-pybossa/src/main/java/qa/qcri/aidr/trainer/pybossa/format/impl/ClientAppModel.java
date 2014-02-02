package qa.qcri.aidr.trainer.pybossa.format.impl;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/28/13
 * Time: 9:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientAppModel {

    private Long cririsID;
    private Long nominalAttributeID;

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
