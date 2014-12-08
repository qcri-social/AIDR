package qa.qcri.aidr.dbmanager.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.entities.misc.Crisis;
import qa.qcri.aidr.dbmanager.entities.misc.Users;
import qa.qcri.aidr.dbmanager.entities.model.NominalAttribute;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
public class UsersDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3558944427272012856L;

	@XmlElement
	private Long userID;

	@XmlElement
	private String name;

	@XmlElement
	private String role;

	@XmlElement
	private List<NominalAttributeDTO> nominalAttributesDTO = null;

	@XmlElement
	private List<CrisisDTO> crisisesDTO = null;


	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		if (userID != null) {
			this.userID = userID;
		}
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

	public List<NominalAttributeDTO> getNominalAttributesDTO() {
		return this.nominalAttributesDTO;
	}

	public void setNominalAttributesDTO(List<NominalAttributeDTO> nominalAttributesDTO) {
		this.nominalAttributesDTO = nominalAttributesDTO;
	}

	public List<CrisisDTO> getCrisisesDTO() {
		return this.crisisesDTO;
	}

	public void setCrisisesDTO(List<CrisisDTO> crisisesDTO) {
		this.crisisesDTO = crisisesDTO;
	}

	private List<CrisisDTO> toCrisisDTOList(List<Crisis> list) throws PropertyNotSetException {
		if (list != null) {
			List<CrisisDTO> dtoList = new ArrayList<CrisisDTO>();
			for (Crisis d: list) {
				dtoList.add(new CrisisDTO(d));
			}
			return dtoList;
		}
		return null;
	}


	private List<Crisis> toCrisisList(List<CrisisDTO> list) throws PropertyNotSetException {
		if (list != null) {
			List<Crisis> eList = new ArrayList<Crisis>();
			for (CrisisDTO dto: list) {
				eList.add(dto.toEntity());
			}
			return eList;
		}
		return null;
	}

	private List<NominalAttributeDTO> toNominalAttributeDTOList(List<NominalAttribute> list) throws PropertyNotSetException {
		if (list != null) {
			List<NominalAttributeDTO> dtoList = new ArrayList<NominalAttributeDTO>();
			for (NominalAttribute d: list) {
				dtoList.add(new NominalAttributeDTO(d));
			}
			return dtoList;
		}
		return null;
	}


	private List<NominalAttribute> toNominalAttributeList(List<NominalAttributeDTO> list) throws PropertyNotSetException {
		if (list != null) {
			List<NominalAttribute> eList = new ArrayList<NominalAttribute>();
			for (NominalAttributeDTO dto: list) {
				eList.add(dto.toEntity());
			}
			return eList;
		}
		return null;
	}


	public UsersDTO() {
	}

	public UsersDTO(Long userID, String name, String role) {
		this.userID = userID;
		this.name = name;
		this.role = role;
	}

	public UsersDTO(String name, String role) {
		this.name = name;
		this.role = role;
	}

	public UsersDTO(Users user) throws PropertyNotSetException {
		if (user != null) {
			System.out.println("Users Hash code: " + user.hashCode());
			
			this.name = user.getName();
			this.role = user.getRole();
			this.setUserID(user.getUserId());

			if (user.hasCrisises()) {
				this.setCrisisesDTO(toCrisisDTOList(user.getCrisises()));
			}
			if (user.hasNominalAttributes()) {
				this.setNominalAttributesDTO(toNominalAttributeDTOList(user.getNominalAttributes()));
			}
		}
	}

	public Users toEntity() throws PropertyNotSetException {
		Users user = new Users(this.getName(), this.getRole());
		if (this.getUserID() != null) {
			user.setUserId(this.getUserID());
		}

		// optional parameters
		if (this.getCrisisesDTO() != null) {
			user.setCrisises(this.toCrisisList(this.crisisesDTO));
		}
		if (this.getNominalAttributesDTO() != null) {
			user.setNominalAttributes(this.toNominalAttributeList(this.nominalAttributesDTO));
		}
		return user;
	}
}
