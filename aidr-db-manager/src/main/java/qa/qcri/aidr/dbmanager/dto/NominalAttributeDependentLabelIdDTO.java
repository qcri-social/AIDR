package qa.qcri.aidr.dbmanager.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.entities.model.NominalAttributeDependentLabelId;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
public class NominalAttributeDependentLabelIdDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5910754106878087122L;
	private static final Logger logger = Logger.getLogger("db-manager-log");

	@XmlElement
	private Long nominalAttributeId;

	@XmlElement
	private Long nominalLabelId;

	public NominalAttributeDependentLabelIdDTO() {
	}

	public NominalAttributeDependentLabelIdDTO(Long nominalAttributeId,
			Long nominalLabelId) {
		this.nominalAttributeId = nominalAttributeId;
		this.nominalLabelId = nominalLabelId;
	}

	public NominalAttributeDependentLabelIdDTO(NominalAttributeDependentLabelId id) throws PropertyNotSetException {
		if (id != null) {
			this.nominalAttributeId = id.getNominalAttributeId();
			this.nominalLabelId = id.getNominalLabelId();
		} else {
			logger.error("Primary key not set!");
			throw new PropertyNotSetException("Primary key not set!");
		}
	}

	public Long getNominalAttributeId() {
		return this.nominalAttributeId;
	}

	public void setNominalAttributeId(Long nominalAttributeId) {
		this.nominalAttributeId = nominalAttributeId;
	}

	public Long getNominalLabelId() {
		return this.nominalLabelId;
	}

	public void setNominalLabelId(Long nominalLabelId) {
		this.nominalLabelId = nominalLabelId;
	}
	
	public NominalAttributeDependentLabelId toEntity() throws PropertyNotSetException {
		if (this.nominalAttributeId == null || this.nominalLabelId == null) {
			logger.error("Primary key not set!");
			throw new PropertyNotSetException("Primary key not set!");
		}
		NominalAttributeDependentLabelId id = new NominalAttributeDependentLabelId(this.nominalAttributeId, this.nominalLabelId);
		return id;
	}
}
