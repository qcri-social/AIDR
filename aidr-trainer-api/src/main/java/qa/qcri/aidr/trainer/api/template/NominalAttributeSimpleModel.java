package qa.qcri.aidr.trainer.api.template;

import qa.qcri.aidr.trainer.api.entity.NominalLabel;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/23/13
 * Time: 8:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class NominalAttributeSimpleModel {
    public NominalAttributeSimpleModel() {}		// gf 3 way - attempting fix
	
	public Long getNominalAttributeID() {
        return nominalAttributeID;
    }

    public void setNominalAttributeID(Long nominalAttributeID) {
        this.nominalAttributeID = nominalAttributeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Set<NominalLabel> getNominalLabelSet() {
        return nominalLabelSet;
    }

    public void setNominalLabelSet(Set<NominalLabel> nominalLabelSet) {
        this.nominalLabelSet = nominalLabelSet;
    }

    private Set<NominalLabel> nominalLabelSet;
    private String description;
    private Long nominalAttributeID;
    private String name;
    private String code;

}
