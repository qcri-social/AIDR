package qa.qcri.aidr.manager.dto;

public class CrisisRequest {

    private String code;

    private String name;

    private Integer crisisTypeID;

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
}