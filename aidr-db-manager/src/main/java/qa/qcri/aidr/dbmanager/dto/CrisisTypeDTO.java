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
		this.setCrisisTypeId(crisisTypeId);
		this.setName(name);
	}

	public CrisisTypeDTO(Long crisisTypeId, String name, List<CrisisDTO> crisisesDTO) {
		this.setCrisisTypeId(crisisTypeId);
		this.setName(name);
		this.setCrisisesDTO(crisisesDTO);
	}

	public CrisisTypeDTO(CrisisType crisisType) throws PropertyNotSetException {
		if (crisisType != null) {
			this.setCrisisTypeId(crisisType.getCrisisTypeId());
			this.setName(crisisType.getName());
			if (crisisType.hasCrisises()) {
				this.setCrisisesDTO(toCrisisDTOList(crisisType.getCrisises()));
			}
		} else {
			throw new IllegalArgumentException("Cannot be null");
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
		if (crisisesDTO != null) {
		this.crisisesDTO = crisisesDTO;
		} else {
			throw new IllegalArgumentException("cannot be null");
		}
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
		} else {
			throw new IllegalArgumentException("Cannot be null");
		}
	}

	public CrisisType toEntity() throws PropertyNotSetException {
		CrisisType cType = new CrisisType(this.getName());
		if (this.getCrisisTypeId() != null) {
			cType.setCrisisTypeId(this.getCrisisTypeId());
		} else {
			throw new PropertyNotSetException();
		}
		cType.setName(this.getName());
		cType.setCrisises(toCrisisList(this.getCrisisesDTO()));

		return cType;
	}
}
