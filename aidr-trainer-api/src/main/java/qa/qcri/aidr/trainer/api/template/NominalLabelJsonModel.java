package qa.qcri.aidr.trainer.api.template;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/23/13
 * Time: 8:39 AM
 * To change this template use File | Settings | File Templates.
 */
public class NominalLabelJsonModel {

    public Long getNorminalLabelID() {
        return norminalLabelID;
    }

    public void setNorminalLabelID(Long norminalLabelID) {
        this.norminalLabelID = norminalLabelID;
    }

    public String getNorminalLabelCode() {
        return norminalLabelCode;
    }

    public void setNorminalLabelCode(String norminalLabelCode) {
        this.norminalLabelCode = norminalLabelCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String norminalLabelCode;
    private String name;
    private Long norminalLabelID;
    private String description;
}
