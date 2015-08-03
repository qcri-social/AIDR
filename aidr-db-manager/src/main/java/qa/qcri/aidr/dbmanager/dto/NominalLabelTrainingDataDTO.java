package qa.qcri.aidr.dbmanager.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;

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
	private static final Logger logger = Logger.getLogger("db-manager-log");
	
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

	public NominalLabelTrainingDataDTO(NominalLabelTrainingData data) throws PropertyNotSetException {
		this.setIdDTO(new NominalLabelTrainingDataIdDTO(data.getId()));
	}
	
	public NominalLabelTrainingDataIdDTO getIdDTO() {
		return this.idDTO;
	}

	public void setIdDTO(NominalLabelTrainingDataIdDTO idDTO) throws PropertyNotSetException {
		if (idDTO != null) {
			this.idDTO = idDTO;
		} else {
			logger.error("Primary key not set!");
			throw new PropertyNotSetException("Primary key not set!");
		}
	}

	public NominalLabelTrainingData toEntity() {
		NominalLabelTrainingData entity = new NominalLabelTrainingData(this.getIdDTO().toEntity());
		return entity;
	}

}
