package qa.qcri.aidr.manager.dto;

import java.util.List;

public class TaggerCrisis {

    private String code;

    private String name;

    private TaggerCrisisType crisisType;

    private TaggerUser users;

    private Integer crisisID;
    
    private Boolean isTrashed;
    
    private List<TaggerModelFamilyCollection> modelFamilyCollection;

    public TaggerCrisis() {
    	this.isTrashed = false;
    }

    public TaggerCrisis(Integer crisisID) {
        this.crisisID = crisisID;
        this.isTrashed = false;
    }

    public TaggerCrisis(String code, String name, TaggerCrisisType crisisType, TaggerUser users, Boolean isTrashed) {
        this.code = code;
        this.name = name;
        this.crisisType = crisisType;
        this.users = users;
        this.isTrashed = isTrashed;
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
    
    public Boolean getIsTrashed() {
    	return isTrashed;
    }
    
    public void setIsTrashed(Boolean isTrashed) {
    	this.isTrashed = (isTrashed == null) ? false : isTrashed; 
    }

}