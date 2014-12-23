package qa.qcri.aidr.manager.dto;

public class CrisisRequest {

    private String code;

    private String name;

    private Long crisisTypeID;

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
}