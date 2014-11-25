package qa.qcri.aidr.predictdb.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

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

	public CrisisTypeDTO(String name, List<CrisisDTO> crisisesDTO) {
		this.name = name;
		this.crisisesDTO = crisisesDTO;
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

}
