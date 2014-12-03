package qa.qcri.aidr.dbmanager.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.entities.model.NominalLabelTrainingData;
import qa.qcri.aidr.dbmanager.entities.model.NominalLabelTrainingDataId;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
public class NominalLabelTrainingDataDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6001563823600724945L;
	@XmlElement
	private NominalLabelTrainingDataIdDTO idDTO;


	public NominalLabelTrainingDataDTO() {
	}

	public NominalLabelTrainingDataDTO(NominalLabelTrainingDataIdDTO idDTO) throws PropertyNotSetException {
		this.setIdDTO(idDTO);
	}

	public NominalLabelTrainingDataDTO(NominalLabelTrainingDataId id) throws PropertyNotSetException {
		this.setIdDTO(new NominalLabelTrainingDataIdDTO(id));
	}

	public NominalLabelTrainingDataIdDTO getIdDTO() {
		return this.idDTO;
	}

	public void setIdDTO(NominalLabelTrainingDataIdDTO idDTO) throws PropertyNotSetException {
		if (idDTO != null) {
			this.idDTO = idDTO;
		} else {
			throw new PropertyNotSetException("Primary key not set!");
		}
	}

	public NominalLabelTrainingData toEntity() {
		NominalLabelTrainingData entity = new NominalLabelTrainingData(this.getIdDTO().toEntity());
		return entity;
	}

}
