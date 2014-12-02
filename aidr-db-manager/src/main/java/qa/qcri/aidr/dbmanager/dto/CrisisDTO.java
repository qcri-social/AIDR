package qa.qcri.aidr.dbmanager.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.entities.misc.Crisis;
import qa.qcri.aidr.dbmanager.entities.model.ModelFamily;
import qa.qcri.aidr.dbmanager.entities.model.NominalAttribute;
import qa.qcri.aidr.dbmanager.entities.task.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
public class CrisisDTO implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4168326869582663472L;

	static final Logger logger = Logger.getLogger("db-manager-log");

	@XmlElement
	private Long crisisID;

	@XmlElement
	private  String name;

	@XmlElement
	private CrisisTypeDTO crisisTypeDTO = null;

	@XmlElement
	private  String code;

	@XmlElement
	private UsersDTO usersDTO = null;

	@XmlElement
	private boolean isTrashed;

	@XmlElement
	private List<NominalAttributeDTO> nominalAttributesDTO = null;

	@XmlElement
	private List<DocumentDTO> documentsDTO = null;

	@XmlElement
	private List<ModelFamilyDTO> modelFamiliesDTO = null;


	public CrisisDTO(){}

	public CrisisDTO(String name, String code, boolean isTrashed, 
			CrisisTypeDTO crisisTypeDTO, UsersDTO usersDTO) {
            
		this.setName(name);
		this.setCode(code);
		this.setIsTrashed(isTrashed);
		this.setCrisisTypeDTO(crisisTypeDTO);
		this.setUsersDTO(usersDTO);
	}

	public CrisisDTO(Long crisisID, String name, String code, boolean isTrashed, 
			CrisisTypeDTO crisisTypeDTO, UsersDTO usersDTO) {
           
		this.setCrisisID(crisisID);
		this.setName(name);
		this.setCode(code);
		this.setIsTrashed(isTrashed);
		this.setCrisisTypeDTO(crisisTypeDTO);
		this.setUsersDTO(usersDTO);
	}

	public CrisisDTO(Crisis crisis) throws PropertyNotSetException {
		this.setCrisisID(crisis.getCrisisId());
		this.setName(crisis.getName());
		this.setCode(crisis.getCode());
		this.setIsTrashed(crisis.isIsTrashed());
		if (crisis.hasCrisisType()) {
			this.setCrisisTypeDTO(new CrisisTypeDTO(crisis.getCrisisType()));
		}
		if (crisis.hasUsers()) {
			this.setUsersDTO(new UsersDTO(crisis.getUsers()));
		}
		// Setting optional fields that were lazily initialized
		if (crisis.hasDocuments()) {
			this.setDocumentsDTO(toDocumentDTOList(crisis.getDocuments()));
		}
		if (crisis.hasNominalAttributes()) {
			this.setNominalAttributesDTO(toNominalAttributeDTOList(crisis.getNominalAttributes()));
		}
		if (crisis.hasModelFamilies()) {
			this.setModelFamiliesDTO(toModelFamilyDTOList(crisis.getModelFamilies()));
		}
	}

	public Long getCrisisID() {
		return this.crisisID;
	}

	public void setCrisisID(Long crisisID) {
		if (crisisID == null) {
			logger.error( "Attempt to set a crisisID to null" );
			throw new IllegalArgumentException("crisisID cannot be null");
		} else if(crisisID.longValue() <= 0) {
			logger.error( "Attempt to set a crisisID to zero or a negative number" );
			throw new IllegalArgumentException("crisisID cannot be zero or a negative number");
		} else {
			this.crisisID = crisisID;
		}
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		if (null == name) {
			logger.error( "Attempt to set a name to null" );
			throw new IllegalArgumentException("name cannot be null");
		}
		this.name = name;
	}

	public CrisisTypeDTO getCrisisTypeDTO() {
		/*
		if (null == this.crisisTypeDTO) {
			logger.error("Attempt to access unset property");
			throw new PropertyNotSetException();
		} else {
			return this.crisisTypeDTO;
		}*/
		return this.crisisTypeDTO;
	}

	public void setCrisisTypeDTO(CrisisTypeDTO crisisTypeDTO) {
		if (crisisTypeDTO == null) {
			logger.error( "Attempt to set a crisisType to null" );
			throw new IllegalArgumentException("crisisType cannot be null");
		} else {
			this.crisisTypeDTO = crisisTypeDTO;
		}
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		if (null == code) {
			logger.error( "Attempt to set code to null" );
			throw new IllegalArgumentException("code cannot be null");
		}
		this.code = code;
	}
	
	public boolean isIsTrashed() {
		return this.isTrashed;
	}

	public void setIsTrashed(boolean isTrashed) {
		this.isTrashed = isTrashed;
	}

	public UsersDTO getUsersDTO() {
		/*
		if (this.usersDTO == null ) {
			logger.error( "Attempt to access unset property" );
			throw new PropertyNotSetException();
		} else {
			return this.usersDTO;
		}*/
		return this.usersDTO;
	}

	public void setUsersDTO(UsersDTO usersDTO) {
		if (usersDTO == null) {
			logger.error( "Attempt to set a user to null" );
			throw new IllegalArgumentException("user cannot be null");
		} else {
			this.usersDTO = usersDTO;
		}
	}
	
	public List<NominalAttributeDTO> getNominalAttributesDTO() {
		return this.nominalAttributesDTO;
	}

	public void setNominalAttributesDTO(List<NominalAttributeDTO> nominalAttributesDTO) {
		this.nominalAttributesDTO = nominalAttributesDTO;
	}

	public List<DocumentDTO> getDocumentsDTO() {
		return this.documentsDTO;
	}

	public void setDocumentsDTO(List<DocumentDTO> documentsDTO) {
		this.documentsDTO = documentsDTO;
	}
	
	public List<ModelFamilyDTO> getModelFamiliesDTO() {
		/*
		if(this.modelFamiliesDTO == null ) {
			logger.error( "Attempt to access unset property" );
			throw new PropertyNotSetException();
		} else {
			return this.modelFamiliesDTO;
		}*/
		return this.modelFamiliesDTO;
	}

	public void setModelFamiliesDTO(List<ModelFamilyDTO> modelFamiliesDTO) {
		/*
		if (null == modelFamiliesDTO) {
			logger.error( "Attempt to set a model family to null" );
			throw new IllegalArgumentException("Model family cannot be null");
		}*/
		this.modelFamiliesDTO = modelFamiliesDTO;
	}

	private List<DocumentDTO> toDocumentDTOList(List<Document> list) throws PropertyNotSetException {
		if (list != null) {
			List<DocumentDTO> dtoList = new ArrayList<DocumentDTO>();
			for (Document d: list) {
				dtoList.add(new DocumentDTO(d));
			}
			return dtoList;
		}
		return null;
	}

	private List<Document> toDocumentList(List<DocumentDTO> list) throws PropertyNotSetException {
		if (list != null) {
			List<Document> eList = new ArrayList<Document>();
			for (DocumentDTO dto: list) {
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
	
	
	/* Mapping to entity */
	public Crisis toEntity() throws PropertyNotSetException {
		Crisis crisis = new Crisis();
		if (this.getCrisisID() != null) {
			crisis.setCrisisId(this.getCrisisID());
		}
		crisis.setName(getName());
		crisis.setCode(this.getCode());
		crisis.setIsTrashed(this.isTrashed);
		if (this.getUsersDTO() != null) {
			crisis.setUsers(this.getUsersDTO().toEntity());
		} else {
			throw new PropertyNotSetException("Unset users property");
		}
		if (this.getCrisisTypeDTO() != null) {
			crisis.setCrisisType(this.getCrisisTypeDTO().toEntity());
		} else {
			throw new PropertyNotSetException("Unset crisisType property");
		}
		
		// Optional fields conversion
		if (this.getDocumentsDTO() != null) {
			crisis.setDocuments(this.toDocumentList(this.getDocumentsDTO()));
		} 
		if (this.getModelFamiliesDTO() != null) {
			crisis.setModelFamilies(this.toModelFamilyList(this.getModelFamiliesDTO()));
		} 
		if (this.getNominalAttributesDTO() != null) {
			crisis.setNominalAttributes(this.toNominalAttributeList(this.getNominalAttributesDTO()));
		} 
		return crisis;
	}


}

