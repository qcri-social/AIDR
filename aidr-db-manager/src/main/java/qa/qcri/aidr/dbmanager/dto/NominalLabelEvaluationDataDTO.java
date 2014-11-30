package qa.qcri.aidr.dbmanager.dto;


import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.entities.model.NominalLabelEvaluationData;
import qa.qcri.aidr.dbmanager.entities.model.NominalLabelEvaluationDataId;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
public class NominalLabelEvaluationDataDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8978010233097606501L;
	@XmlElement
	private NominalLabelEvaluationDataIdDTO idDTO;

	public NominalLabelEvaluationDataDTO() {
	}

	public NominalLabelEvaluationDataDTO(NominalLabelEvaluationDataIdDTO idDTO) {
		this.idDTO = idDTO;
	}

	public NominalLabelEvaluationDataDTO(NominalLabelEvaluationDataId id) throws PropertyNotSetException {
		this.setIdDTO(new NominalLabelEvaluationDataIdDTO(id));
	}

	
	public NominalLabelEvaluationDataIdDTO getIdDTO() {
		return this.idDTO;
	}

	public void setIdDTO(NominalLabelEvaluationDataIdDTO idDTO) {
		this.idDTO = idDTO;
	}
	
	public NominalLabelEvaluationData toEntity() {
		NominalLabelEvaluationData entity  = new NominalLabelEvaluationData(idDTO.toEntity());
		return entity;
	}

}
