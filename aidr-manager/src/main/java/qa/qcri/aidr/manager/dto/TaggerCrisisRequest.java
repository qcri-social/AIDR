package qa.qcri.aidr.manager.dto;

public class TaggerCrisisRequest {

    private String code;

    private String name;

    private TaggerCrisisType crisisType;

    private TaggerUserRequest users;

    public TaggerCrisisRequest() {
    }

    public TaggerCrisisRequest(String code, String name, TaggerCrisisType crisisType, TaggerUserRequest users) {
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

    public TaggerUserRequest getUsers() {
        return users;
    }

    public void setUsers(TaggerUserRequest users) {
        this.users = users;
    }

}
