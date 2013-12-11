package qa.qcri.aidr.manager.dto;

public class UpdateCrisisDTO {

    private String code;

    private String name;

    private Integer crisisTypeID;

    private String crisisTypeName;

    private Integer crisisID;

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

    public Integer getCrisisTypeID() {
        return crisisTypeID;
    }

    public void setCrisisTypeID(Integer crisisTypeID) {
        this.crisisTypeID = crisisTypeID;
    }

    public Integer getCrisisID() {
        return crisisID;
    }

    public void setCrisisID(Integer crisisID) {
        this.crisisID = crisisID;
    }

    public String getCrisisTypeName() {
        return crisisTypeName;
    }

    public void setCrisisTypeName(String crisisTypeName) {
        this.crisisTypeName = crisisTypeName;
    }
}