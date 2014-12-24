package qa.qcri.aidr.manager.dto;

import qa.qcri.aidr.dbmanager.dto.UsersDTO;

public class TaggerUserRequest {

    private Integer userID;

    public TaggerUserRequest() {
    }

	public UsersDTO toDTO() throws Exception {
		UsersDTO dto = new UsersDTO();
		dto.setUserID(new Long(this.getUserID()));
		
		return dto;
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
