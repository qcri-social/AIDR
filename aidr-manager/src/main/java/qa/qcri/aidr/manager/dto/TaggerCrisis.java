package qa.qcri.aidr.manager.dto;

import java.util.List;

public class TaggerCrisis {

    private String code;

    private String name;

    private TaggerCrisisType crisisType;

    private TaggerUser users;

    private Integer crisisID;

    private List<TaggerModelFamilyCollection> modelFamilyCollection;

    public TaggerCrisis() {
    }

    public TaggerCrisis(Integer crisisID) {
        this.crisisID = crisisID;
    }

    public TaggerCrisis(String code, String name, TaggerCrisisType crisisType, TaggerUser users) {
        this.code = code;
        this.name = name;
        this.crisisType = crisisType;
        this.users = users;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TaggerCrisisType getCrisisType() {
        return crisisType;
    }

    public void setCrisisType(TaggerCrisisType crisisType) {
        this.crisisType = crisisType;
    }

    public TaggerUser getUsers() {
        return users;
    }

    public void setUsers(TaggerUser users) {
        this.users = users;
    }

    public Integer getCrisisID() {
        return crisisID;
    }

    public void setCrisisID(Integer crisisID) {
        this.crisisID = crisisID;
    }

    public List<TaggerModelFamilyCollection> getModelFamilyCollection() {
        return modelFamilyCollection;
    }

    public void setModelFamilyCollection(List<TaggerModelFamilyCollection> modelFamilyCollection) {
        this.modelFamilyCollection = modelFamilyCollection;
    }

}