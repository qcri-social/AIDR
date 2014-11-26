package qa.qcri.aidr.dbmanager.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
public class UsersDTO implements Serializable {

	@XmlElement
	private Long userID;

	@XmlElement
	private String name;

	@XmlElement
	private String role;

	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UsersDTO() {
	}

	public UsersDTO(Long userID) {
		this.userID = userID;
	}

	public UsersDTO(Long userID, String name, String role) {
		this.userID = userID;
		this.name = name;
		this.role = role;
	}
}
