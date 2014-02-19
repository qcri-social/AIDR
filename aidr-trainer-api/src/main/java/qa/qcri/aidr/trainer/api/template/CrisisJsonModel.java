package qa.qcri.aidr.trainer.api.template;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/23/13
 * Time: 8:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class CrisisJsonModel {

    private Long crisisID;
    private String name;
    private String code;
    private Set<NominalAttributeJsonModel> nominalAttributeJsonModelSet;

    public CrisisJsonModel() {}		// gf 3 way - attempting fix
    public Set<NominalAttributeJsonModel> getNominalAttributeJsonModelSet() {
        return nominalAttributeJsonModelSet;
    }

    public void setNominalAttributeJsonModelSet(Set<NominalAttributeJsonModel> nominalAttributeJsonModelSet) {
        this.nominalAttributeJsonModelSet = nominalAttributeJsonModelSet;
    }

    public Long getCrisisID() {
        return crisisID;
    }

    public void setCrisisID(Long crisisID) {
        this.crisisID = crisisID;
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


}
