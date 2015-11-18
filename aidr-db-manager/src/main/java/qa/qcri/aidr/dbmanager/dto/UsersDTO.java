package qa.qcri.aidr.dbmanager.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.entities.misc.Collection;
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
	private List<CollectionDTO> crisisesDTO = null;


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

	public List<CollectionDTO> getCrisisesDTO() {
		return this.crisisesDTO;
	}

	public void setCrisisesDTO(List<CollectionDTO> crisisesDTO) {
		this.crisisesDTO = crisisesDTO;
	}

	private List<CollectionDTO> toCrisisDTOList(List<Collection> list) throws PropertyNotSetException {
		if (list != null) {
			List<CollectionDTO> dtoList = new ArrayList<CollectionDTO>();
			for (Collection d: list) {
				dtoList.add(new CollectionDTO(d));
			}
			return dtoList;
		}
		return null;
	}


	private List<Collection> toCrisisList(List<CollectionDTO> list) throws PropertyNotSetException {
		if (list != null) {
			List<Collection> eList = new ArrayList<Collection>();
			for (CollectionDTO dto: list) {
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

	public UsersDTO(Users user) {
		if (user != null) {
			this.name = user.getUserName();
			this.setUserID(user.getId());
		}
	}

	public Users toEntity() {
		Users user = new Users();
		user.setUserName(this.getName());
		if (this.getUserID() != null) {
			user.setId(this.getUserID());
		}

		return user;
	}
}
