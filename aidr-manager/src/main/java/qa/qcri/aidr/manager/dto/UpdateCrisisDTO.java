package qa.qcri.aidr.manager.dto;

public class UpdateCrisisDTO {

    private String code;

    private String name;

    private Long crisisTypeID;

    private String crisisTypeName;

    private Long crisisID;

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

    public Long getCrisisTypeID() {
        return crisisTypeID;
    }

    public void setCrisisTypeID(Long crisisTypeID) {
        this.crisisTypeID = crisisTypeID;
    }

    public Long getCrisisID() {
        return crisisID;
    }

    public void setCrisisID(Long crisisID) {
        this.crisisID = crisisID;
    }

    public String getCrisisTypeName() {
        return crisisTypeName;
    }

    public void setCrisisTypeName(String crisisTypeName) {
        this.crisisTypeName = crisisTypeName;
    }
}