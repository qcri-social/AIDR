package qa.qcri.aidr.predictdb.dto;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import qa.qcri.aidr.dbmanager.entities.misc.CrisisType;
import qa.qcri.aidr.dbmanager.entities.model.ModelFamily;
import qa.qcri.aidr.dbmanager.entities.model.NominalAttribute;
import qa.qcri.aidr.dbmanager.entities.task.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
public class CrisisDTO implements Serializable {

	@XmlElement
	private Long crisisID;

	@XmlElement
	private String name;

	@XmlElement
	private CrisisTypeDTO crisisTypeDTO;

	@XmlElement
	private String code;

	@XmlElement
	private Long userID;
	
	@XmlElement
	private boolean isTrashed;
	
	@XmlElement
	private List<NominalAttributeDTO> nominalAttributesDTO;
	
	@XmlElement
	private List<DocumentDTO> documentsDTO;
	
	@XmlElement
	private List<ModelFamilyDTO> modelFamiliesDTO;

	
	public CrisisDTO(){}

	public Long getCrisisID() {
		return crisisID;
	}

	public void setCrisisID(Long crisisID) {
		this.crisisID = crisisID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CrisisTypeDTO getCrisisTypeDTO() {
		return crisisTypeDTO;
	}

	public void setCrisisTypeDTO(CrisisTypeDTO crisisTypeDTO) {
		this.crisisTypeDTO = crisisTypeDTO;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

	public boolean isIsTrashed() {
		return this.isTrashed;
	}

	public void setIsTrashed(boolean isTrashed) {
		this.isTrashed = isTrashed;
	}
	
	public List<NominalAttributeDTO> getNominalAttributesDTO() {
		return this.nominalAttributesDTO;
	}

	public void setNominalAttributesDTO(List<NominalAttributeDTO> nominalAttributesDTO) {
		this.nominalAttributesDTO = nominalAttributesDTO;
	}

}

