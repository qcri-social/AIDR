package qa.qcri.aidr.manager.dto;

import qa.qcri.aidr.dbmanager.dto.UsersDTO;

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
		this.setName(name);
		this.setRole(role);
	}

	public TaggerUser(UsersDTO dto) throws Exception {
		if (dto != null) {
			this.setName(dto.getName());
			this.setRole(dto.getRole());
			this.setUserID(dto.getUserID() != null ? dto.getUserID().intValue() : null);
		}
	}

	public UsersDTO toDTO() throws Exception {
		UsersDTO dto = new UsersDTO();
		if (this.getUserID() != null) {
			dto.setUserID(new Long(this.getUserID()));
		}
		dto.setName(this.getName());
		dto.setRole(this.getRole());
		return dto;
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