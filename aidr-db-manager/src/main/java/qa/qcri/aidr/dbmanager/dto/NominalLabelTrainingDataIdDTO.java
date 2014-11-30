package qa.qcri.aidr.dbmanager.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.entities.model.NominalLabelTrainingDataId;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
public class NominalLabelTrainingDataIdDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4397588498587349888L;

	@XmlElement
	private Long documentId;

	@XmlElement
	private Long crisisId;

	@XmlElement
	private Long nominalLabelId;

	@XmlElement
	private Long nominalAttributeId;

	@XmlElement
	private String wordFeatures;

	public NominalLabelTrainingDataIdDTO() {
	}

	public NominalLabelTrainingDataIdDTO(NominalLabelTrainingDataId id) throws PropertyNotSetException {
		if (id != null) {
			this.setDocumentId(id.getDocumentId());
			this.setCrisisId(id.getCrisisId());
			this.setNominalAttributeId(id.getNominalAttributeId());
			this.setNominalLabelId(id.getNominalLabelId());
			this.setWordFeatures(id.getWordFeatures());
		} else {
			throw new PropertyNotSetException("Primary key not set!");
		}
	}

	public NominalLabelTrainingDataIdDTO(Long documentId, Long crisisId,
			Long nominalLabelId, Long nominalAttributeId) {
		this.documentId = documentId;
		this.crisisId = crisisId;
		this.nominalLabelId = nominalLabelId;
		this.nominalAttributeId = nominalAttributeId;
	}

	public NominalLabelTrainingDataIdDTO(Long documentId, Long crisisId,
			Long nominalLabelId, Long nominalAttributeId, String wordFeatures) {
		this.documentId = documentId;
		this.crisisId = crisisId;
		this.nominalLabelId = nominalLabelId;
		this.nominalAttributeId = nominalAttributeId;
		this.wordFeatures = wordFeatures;
	}

	public Long getDocumentId() {
		return this.documentId;
	}

	public void setDocumentId(long documentId) {
		this.documentId = documentId;
	}

	public Long getCrisisId() {
		return this.crisisId;
	}

	public void setCrisisId(Long crisisId) {
		this.crisisId = crisisId;
	}

	public Long getNominalLabelId() {
		return this.nominalLabelId;
	}

	public void setNominalLabelId(Long nominalLabelId) {
		this.nominalLabelId = nominalLabelId;
	}

	public Long getNominalAttributeId() {
		return this.nominalAttributeId;
	}

	public void setNominalAttributeId(Long nominalAttributeId) {
		this.nominalAttributeId = nominalAttributeId;
	}

	public String getWordFeatures() {
		return this.wordFeatures;
	}

	public void setWordFeatures(String wordFeatures) {
		this.wordFeatures = wordFeatures;
	}

	public NominalLabelTrainingDataId toEntity() {
		NominalLabelTrainingDataId entity  = new NominalLabelTrainingDataId(this.documentId, this.crisisId,
				this.nominalLabelId, this.nominalAttributeId);
		if (this.getWordFeatures() != null) {
			entity.setWordFeatures(wordFeatures);
		}
		return entity;
	}
}
