package qa.qcri.aidr.dbmanager.dto;



import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.entities.misc.Crisis;
import qa.qcri.aidr.dbmanager.entities.model.ModelFamily;
import qa.qcri.aidr.dbmanager.entities.model.NominalAttribute;
import qa.qcri.aidr.dbmanager.entities.model.NominalAttributeDependentLabel;
import qa.qcri.aidr.dbmanager.entities.model.NominalLabel;

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
	private List<NominalAttributeDependentLabelDTO> nominalAttributeDependentLabelsDTO = null;

	@XmlElement
	private List<CrisisDTO> crisisesDTO = null;

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

			if (nominalAttribute.hasCrisises()) {
				this.setCrisisesDTO(toCrisisDTOList(nominalAttribute.getCrisises()));
			}
			if (nominalAttribute.hasModelFamily()) {
				this.setModelFamiliesDTO(this.toModelFamilyDTOList(nominalAttribute.getModelFamilies()));
			}
			if (nominalAttribute.hasNominalAttributeDependentLabel()) {
				this.setNominalAttributeDependentLabelsDTO(
						this.toNominalAttributeDependentLabelDTOList(nominalAttribute.getNominalAttributeDependentLabels()));
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

	public List<NominalAttributeDependentLabelDTO> getNominalAttributeDependentLabelsDTO() {
		return this.nominalAttributeDependentLabelsDTO;
	}

	public void setNominalAttributeDependentLabelsDTO(
			List<NominalAttributeDependentLabelDTO> nominalAttributeDependentLabelsDTO) {
		this.nominalAttributeDependentLabelsDTO = nominalAttributeDependentLabelsDTO;
	}

	public List<CrisisDTO> getCrisisesDTO() {
		return this.crisisesDTO;
	}

	public void setCrisisesDTO(List<CrisisDTO> crisisesDTO) {
		this.crisisesDTO = crisisesDTO;
	}

	public List<NominalLabelDTO> getNominalLabelsDTO() {
		return this.nominalLabelsDTO;
	}

	public void setNominalLabelsDTO(List<NominalLabelDTO> nominalLabelsDTO) {
		this.nominalLabelsDTO = nominalLabelsDTO;
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

	private List<NominalAttributeDependentLabelDTO> toNominalAttributeDependentLabelDTOList(List<NominalAttributeDependentLabel> list) throws PropertyNotSetException {
		if (list != null) {
			List<NominalAttributeDependentLabelDTO> dtoList = new ArrayList<NominalAttributeDependentLabelDTO>();
			for (NominalAttributeDependentLabel d: list) {
				dtoList.add(new NominalAttributeDependentLabelDTO(d));
			}
			return dtoList;
		}
		return null;
	}


	private List<NominalAttributeDependentLabel> toNominalAttributeDependentLabelList(List<NominalAttributeDependentLabelDTO> list) throws PropertyNotSetException {
		if (list != null) {
			List<NominalAttributeDependentLabel> eList = new ArrayList<NominalAttributeDependentLabel>();
			for (NominalAttributeDependentLabelDTO dto: list) {
				eList.add(dto.toEntity());
			}
			return eList;
		}
		return null;
	}

	public NominalAttribute toEntity() throws PropertyNotSetException {
		NominalAttribute entity = new NominalAttribute(this.getUsersDTO().toEntity(), 
				this.getName(), this.getDescription(), this.getCode());
		if (this.getNominalAttributeId() != null) {
			entity.setNominalAttributeId(nominalAttributeId);
		}
		if (this.crisisesDTO != null) {
			entity.setCrisises(this.toCrisisList(this.getCrisisesDTO()));
		}
		if (this.nominalLabelsDTO != null) {
			entity.setNominalLabels(this.toNominalLabelList(this.getNominalLabelsDTO()));
		}
		if (this.modelFamiliesDTO != null) {
			entity.setModelFamilies(this.toModelFamilyList(this.getModelFamiliesDTO()));
		}
		if (this.nominalAttributeDependentLabelsDTO != null) {
			entity.setNominalAttributeDependentLabels(
					this.toNominalAttributeDependentLabelList(this.getNominalAttributeDependentLabelsDTO()));
		}
		return entity;
	}

}

