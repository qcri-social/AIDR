package qa.qcri.aidr.dbmanager.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.entities.model.ModelNominalLabelId;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModelNominalLabelIdDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7966215874963999322L;
	private Logger logger = Logger.getLogger(ModelNominalLabelIdDTO.class);

	@XmlElement
	private Long modelId;
	
	@XmlElement
	private Long nominalLabelId;

	public ModelNominalLabelIdDTO() {
	}

	public ModelNominalLabelIdDTO(ModelNominalLabelId id) {
		this.setModelId(id.getModelId());
		this.setNominalLabelId(id.getNominalLabelId());
	}
	
	public ModelNominalLabelIdDTO(Long modelId, Long nominalLabelId) {
		this.modelId = modelId;
		this.nominalLabelId = nominalLabelId;
	}

	public Long getModelId() {
		return this.modelId;
	}

	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}

	public Long getNominalLabelId() {
		return this.nominalLabelId;
	}

	public void setNominalLabelId(Long nominalLabelId) {
		this.nominalLabelId = nominalLabelId;
	}
	
	public ModelNominalLabelId toEntity() throws PropertyNotSetException {
		if (this.modelId == null || this.nominalLabelId == null) {
			logger.error("Primary key not set!");
			throw new PropertyNotSetException("Primary key not set!");
		}
		
		ModelNominalLabelId id = new ModelNominalLabelId(this.getModelId(), this.getNominalLabelId());
		return id;
		
	}
}
