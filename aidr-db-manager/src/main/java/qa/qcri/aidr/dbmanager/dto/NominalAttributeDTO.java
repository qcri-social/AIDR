package qa.qcri.aidr.dbmanager.dto;



import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.entities.misc.Collection;
import qa.qcri.aidr.dbmanager.entities.misc.Users;
import qa.qcri.aidr.dbmanager.entities.model.ModelFamily;
import qa.qcri.aidr.dbmanager.entities.model.NominalAttribute;
import qa.qcri.aidr.dbmanager.entities.model.NominalLabel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
public class NominalAttributeDTO implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 766829027153899876L;

	@XmlElement	
	private Long nominalAttributeId;

	@XmlElement
	private UsersDTO usersDTO = null;

	@XmlElement
	private String name;

	@XmlElement
	private String description;

	@XmlElement
	private String code;

	@XmlElement
	private List<ModelFamilyDTO> modelFamiliesDTO = null;

	@XmlElement
	private List<CollectionDTO> crisisesDTO = null;

	@XmlElement
	private List<NominalLabelDTO> nominalLabelsDTO = null;

	public NominalAttributeDTO() {
	}

	public NominalAttributeDTO(String name, String description, String code) {
		this.name = name;
		this.description = description;
		this.code = code;
	}


	public NominalAttributeDTO(Long nominalAttributeId, String name, String description, String code) {
		this.nominalAttributeId = nominalAttributeId;
		this.name = name;
		this.description = description;
		this.code = code;
	}

	public NominalAttributeDTO(NominalAttribute nominalAttribute) throws PropertyNotSetException {
		if (nominalAttribute != null) {
			this.setNominalAttributeId(nominalAttribute.getNominalAttributeId());
			this.nominalAttributeId = nominalAttribute.getNominalAttributeId();
			this.name = nominalAttribute.getName();
			this.description = nominalAttribute.getDescription();
			this.code = nominalAttribute.getCode();

			if (nominalAttribute.hasUsers() && nominalAttribute.getUsers() != null) {
				Users user = new Users();
				user.setUserName(nominalAttribute.getUsers().getUserName());
				user.setId(nominalAttribute.getUsers().getId());

				this.setUsersDTO(new UsersDTO(user));
			}
			if (nominalAttribute.hasModelFamily()) {
				this.setModelFamiliesDTO(this.toModelFamilyDTOList(nominalAttribute.getModelFamilies()));
			}
			if (nominalAttribute.hasNominalLabels()) {
				this.setNominalLabelsDTO(this.toNominalLabelDTOList(nominalAttribute.getNominalLabels()));
			}
		}
	}

	public Long getNominalAttributeId() {
		return this.nominalAttributeId;
	}

	public void setNominalAttributeId(Long nominalAttributeId) {
		this.nominalAttributeId = nominalAttributeId;
	}

	public UsersDTO getUsersDTO() {
		return this.usersDTO;
	}

	public void setUsersDTO(UsersDTO usersDTO) {
		this.usersDTO = usersDTO;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<ModelFamilyDTO> getModelFamiliesDTO() {
		return this.modelFamiliesDTO;
	}

	public void setModelFamiliesDTO(List<ModelFamilyDTO> modelFamiliesDTO) {
		this.modelFamiliesDTO = modelFamiliesDTO;
	}

	public List<CollectionDTO> getCrisisesDTO() {
		return this.crisisesDTO;
	}

	public void setCrisisesDTO(List<CollectionDTO> crisisesDTO) {
		this.crisisesDTO = crisisesDTO;
	}

	public List<NominalLabelDTO> getNominalLabelsDTO() {
		return this.nominalLabelsDTO;
	}

	public void setNominalLabelsDTO(List<NominalLabelDTO> nominalLabelsDTO) {
		this.nominalLabelsDTO = nominalLabelsDTO;
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

	private List<NominalLabelDTO> toNominalLabelDTOList(List<NominalLabel> list) throws PropertyNotSetException {
		if (list != null) {
			List<NominalLabelDTO> dtoList = new ArrayList<NominalLabelDTO>();
			for (NominalLabel d: list) {
				dtoList.add(new NominalLabelDTO(d));
			}
			return dtoList;
		}
		return null;
	}


	private List<NominalLabel> toNominalLabelList(List<NominalLabelDTO> list) throws PropertyNotSetException {
		if (list != null) {
			List<NominalLabel> eList = new ArrayList<NominalLabel>();
			for (NominalLabelDTO dto: list) {
				eList.add(dto.toEntity());
			}
			return eList;
		}
		return null;
	} 

	private List<ModelFamilyDTO> toModelFamilyDTOList(List<ModelFamily> list) throws PropertyNotSetException {
		if (list != null) {
			List<ModelFamilyDTO> dtoList = new ArrayList<ModelFamilyDTO>();
			for (ModelFamily d: list) {
				dtoList.add(new ModelFamilyDTO(d));
			}
			return dtoList;
		}
		return null;
	}


	private List<ModelFamily> toModelFamilyList(List<ModelFamilyDTO> list) throws PropertyNotSetException {
		if (list != null) {
			List<ModelFamily> eList = new ArrayList<ModelFamily>();
			for (ModelFamilyDTO dto: list) {
				eList.add(dto.toEntity());
			}
			return eList;
		}
		return null;
	}

	public NominalAttribute toEntity() throws PropertyNotSetException {
		NominalAttribute entity = new NominalAttribute();
		if (this.getUsersDTO() != null) {
			entity.setUsers(this.getUsersDTO().toEntity());
		}
		entity.setName(this.getName());
		entity.setDescription(this.getDescription());
		entity.setCode(this.getCode());
		if (this.getNominalAttributeId() != null) {
			entity.setNominalAttributeId(this.getNominalAttributeId());
		}
		if (this.nominalLabelsDTO != null) {
			entity.setNominalLabels(this.toNominalLabelList(this.getNominalLabelsDTO()));
		}
		if (this.modelFamiliesDTO != null) {
			entity.setModelFamilies(this.toModelFamilyList(this.getModelFamiliesDTO()));
		}
		return entity;
	}

}

