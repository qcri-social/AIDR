package qa.qcri.aidr.manager.dto;

public class TaggerUserRequest {

    private Integer userID;

    public TaggerUserRequest() {
    }

    public TaggerUserRequest(Integer userID) {
        this.userID = userID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

}
