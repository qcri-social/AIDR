package qa.qcri.aidr.dbmanager.dto;

import java.io.Serializable;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.log4j.Logger;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.entities.misc.Crisis;
import qa.qcri.aidr.dbmanager.entities.misc.CrisisType;
import qa.qcri.aidr.dbmanager.entities.misc.Users;
import qa.qcri.aidr.dbmanager.entities.model.ModelFamily;
import qa.qcri.aidr.dbmanager.entities.model.NominalAttribute;
import qa.qcri.aidr.dbmanager.entities.task.Document;
import qa.qcri.aidr.dbmanager.entities.task.DocumentNominalLabel;
import qa.qcri.aidr.dbmanager.entities.task.TaskAssignment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;



@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
public class CrisisDTO implements Serializable  {


	/**
	 * 
	 */
	private static final long serialVersionUID = -7825767671101319130L;

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
	private boolean isMicromapperEnabled;

	@XmlElement
	private List<NominalAttributeDTO> nominalAttributesDTO = null;

	@XmlElement
	private List<DocumentDTO> documentsDTO = null;

	@XmlElement
	private List<ModelFamilyDTO> modelFamiliesDTO = null;


	public CrisisDTO(){}

/*	public CrisisDTO(String name, String code, boolean isTrashed,
			CrisisTypeDTO crisisTypeDTO, UsersDTO usersDTO) {

		this.setName(name);
		this.setCode(code);
		this.setIsTrashed(isTrashed);
		this.setCrisisTypeDTO(crisisTypeDTO);
		this.setUsersDTO(usersDTO);
	}*/
	
	public CrisisDTO(String name, String code, boolean isTrashed, boolean isMicromapperEnabled,
			CrisisTypeDTO crisisTypeDTO, UsersDTO usersDTO) {

		this.setName(name);
		this.setCode(code);
		this.setIsTrashed(isTrashed);
		this.setIsMicromapperEnabled(isMicromapperEnabled);
		this.setCrisisTypeDTO(crisisTypeDTO);
		this.setUsersDTO(usersDTO);
	}
	

	public CrisisDTO(Long crisisID, String name, String code, boolean isTrashed, boolean isMicromapperEnabled,
			CrisisTypeDTO crisisTypeDTO, UsersDTO usersDTO) {

		this.setCrisisID(crisisID);
		this.setName(name);
		this.setCode(code);
		this.setIsTrashed(isTrashed);
		this.setIsMicromapperEnabled(isMicromapperEnabled);
		this.setCrisisTypeDTO(crisisTypeDTO);
		this.setUsersDTO(usersDTO);
	}

	public CrisisDTO(Crisis crisis) throws PropertyNotSetException {
		if (crisis != null) {
			
			this.setCrisisID(crisis.getCrisisId());
			this.setName(crisis.getName());
			this.setCode(crisis.getCode());
			this.setIsTrashed(crisis.isIsTrashed());
			this.setIsMicromapperEnabled(crisis.isIsMicromapperEnabled());
			if (crisis.hasCrisisType()) {
				CrisisType cType = new CrisisType(crisis.getCrisisType().getName());
				cType.setCrisisTypeId(crisis.getCrisisType().getCrisisTypeId());
				this.setCrisisTypeDTO(new CrisisTypeDTO(cType));
			}
			if (crisis.hasUsers()) {
				Users user = new Users(crisis.getUsers().getName(), crisis.getUsers().getRole());
				user.setUserId(crisis.getUsers().getUserId());
				this.setUsersDTO(new UsersDTO(user));
			}
			// Setting optional fields that were lazily initialized
			if (crisis.hasNominalAttributes()) {
				this.setNominalAttributesDTO(toNominalAttributeDTOList(crisis.getNominalAttributes()));
			}
			if (crisis.hasModelFamilies()) {
				this.setModelFamiliesDTO(toModelFamilyDTOList(crisis.getModelFamilies()));
			}
			if (crisis.hasDocuments()) {
				this.setDocumentsDTO(toDocumentDTOList(crisis.getDocuments()));
			}
		} else {
			logger.error("Entity = null in constructor");
		}

	}

	public Long getCrisisID() {
			return this.crisisID;
	}

	public void setCrisisID(Long crisisID) {
		if(crisisID.longValue() <= 0) {
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
		this.name = name;
	}

	public CrisisTypeDTO getCrisisTypeDTO() {
			return this.crisisTypeDTO;
	}

	public void setCrisisTypeDTO(CrisisTypeDTO crisisTypeDTO) {
		this.crisisTypeDTO = crisisTypeDTO;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean isIsTrashed() {
		return this.isTrashed;
	}

	public void setIsTrashed(boolean isTrashed) {
		this.isTrashed = isTrashed;
	}

	public boolean isIsMicromapperEnabled() {
		return isMicromapperEnabled;
	}

	public void setIsMicromapperEnabled(boolean isMicromapperEnabled) {
		this.isMicromapperEnabled = isMicromapperEnabled;
	}
	
	public UsersDTO getUsersDTO() {
		return this.usersDTO;
	}

	public void setUsersDTO(UsersDTO usersDTO) {
			this.usersDTO = usersDTO;
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
		return this.modelFamiliesDTO;
	}

	public void setModelFamiliesDTO(List<ModelFamilyDTO> modelFamiliesDTO) {
		this.modelFamiliesDTO = modelFamiliesDTO;
	}

	private List<DocumentDTO> toDocumentDTOList(List<Document> list) {
		if (list != null) {
			try {
				List<DocumentDTO> dtoList = new ArrayList<DocumentDTO>();
				for (Document d: list) {
					Document doc = new Document(d.getCrisis(), d.isIsEvaluationSet(),
											d.isHasHumanLabels(), d.getValueAsTrainingSample(),
											d.getReceivedAt(), d.getLanguage(), d.getDoctype(), d.getData(),
											d.getWordFeatures(), d.getGeoFeatures(), d.getTaskAssignments(),
											d.getDocumentNominalLabels());					
					doc.setDocumentId(d.getDocumentId());
					dtoList.add(new DocumentDTO(doc));
				}
				return dtoList;
			} catch (Exception e) {
				logger.warn("Unable to wrap Document to DocumentDTO.");
			}
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
		crisis.setIsMicromapperEnabled(this.isMicromapperEnabled);
		if (this.getUsersDTO() != null) {
			crisis.setUsers(this.getUsersDTO().toEntity());
		}
		if (this.getCrisisTypeDTO() != null) {
			crisis.setCrisisType(this.getCrisisTypeDTO().toEntity());
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

