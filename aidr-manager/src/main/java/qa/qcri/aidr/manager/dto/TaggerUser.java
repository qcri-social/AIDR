package qa.qcri.aidr.manager.dto;

public class TaggerUser {

    private Integer userID;

    private String name;

    private String role;

    public TaggerUser() {
    }

    public TaggerUser(Integer userID) {
        this.userID = userID;
    }

    public TaggerUser(String name, String role) {
        this.name = name;
        this.role = role;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}