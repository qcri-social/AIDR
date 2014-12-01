package qa.qcri.aidr.dbmanager.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.entities.misc.Crisis;
import qa.qcri.aidr.dbmanager.entities.misc.CrisisType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
public class CrisisTypeDTO implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8074463052776843105L;

	@XmlElement
	private Long crisisTypeId;

	@XmlElement
	private String name;

	@XmlElement
	private List<CrisisDTO> crisisesDTO = null;

	public CrisisTypeDTO() {
	}

	public CrisisTypeDTO(String name) {
		this.name = name;
	}

	public CrisisTypeDTO(Long crisisTypeId, String name) {
            //TO FIX: use setters and proper validation
		this.crisisTypeId = crisisTypeId;
		this.name = name;
	}

	public CrisisTypeDTO(Long crisisTypeId, String name, List<CrisisDTO> crisisesDTO) {
            //TO FIX: use setters and proper validation
		this.crisisTypeId = crisisTypeId;
		this.name = name;
		this.setCrisisesDTO(crisisesDTO);
	}

	public CrisisTypeDTO(CrisisType crisisType) throws PropertyNotSetException {
            //TO FIX: use setters and proper validation
		if (crisisType != null) {
			this.crisisTypeId = crisisType.getCrisisTypeId();
			this.name = crisisType.getName();
			if (crisisType.getCrisises() != null) {
				this.setCrisisesDTO(toCrisisDTOList(crisisType.getCrisises()));
			}
		}
	}
	
	public Long getCrisisTypeId() {
		return this.crisisTypeId;
	}

	public void setCrisisTypeId(Long crisisTypeId) {
		this.crisisTypeId = crisisTypeId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CrisisDTO> getCrisisesDTO() {
		return this.crisisesDTO;
	}

	public void setCrisisesDTO(List<CrisisDTO> crisisesDTO) {
		this.crisisesDTO = crisisesDTO;
	}

	private List<CrisisDTO> toCrisisDTOList(List<Crisis> list) throws PropertyNotSetException {
		if (list != null) {
			List<CrisisDTO> crisisesDTO = new ArrayList<CrisisDTO>();
			for (Crisis c: list) {
				crisisesDTO.add(new CrisisDTO(c));
			}
			return crisisesDTO;
		}
		return null;
	}

	private List<Crisis> toCrisisList(List<CrisisDTO> list) throws PropertyNotSetException {
		if (list != null) {
			List<Crisis> crisises = new ArrayList<Crisis>();
			for (CrisisDTO dto: list) {
				crisises.add(dto.toEntity());
			}
			return crisises;
		}
		return null;
	}

	public CrisisType toEntity() throws PropertyNotSetException {
		CrisisType cType = new CrisisType(this.getName());
		if (this.getCrisisTypeId() != null) {
			cType.setCrisisTypeId(this.getCrisisTypeId());
		}
		cType.setName(this.getName());
		cType.setCrisises(toCrisisList(this.getCrisisesDTO()));

		return cType;
	}
}
