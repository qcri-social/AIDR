package qa.qcri.aidr.dbmanager.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.entities.model.NominalLabelEvaluationDataId;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
public class NominalLabelEvaluationDataIdDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3487603334742448796L;
	
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

	public NominalLabelEvaluationDataIdDTO() {
	}

	public NominalLabelEvaluationDataIdDTO(NominalLabelEvaluationDataId id) throws PropertyNotSetException {
		if (id != null) {
		this.documentId = id.getDocumentId();
		this.crisisId = id.getCrisisId();
		this.nominalLabelId = id.getNominalLabelId();
		this.nominalAttributeId = id.getNominalAttributeId();
		this.wordFeatures = id.getWordFeatures();
		} else {
			throw new PropertyNotSetException("primary key not set!");
		}
	}
	
	public NominalLabelEvaluationDataIdDTO(Long documentId, Long crisisId,
			Long nominalLabelId, Long nominalAttributeId) {
		this.documentId = documentId;
		this.crisisId = crisisId;
		this.nominalLabelId = nominalLabelId;
		this.nominalAttributeId = nominalAttributeId;
	}

	public NominalLabelEvaluationDataIdDTO(Long documentId, Long crisisId,
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
	
	public NominalLabelEvaluationDataId toEntity() {
		NominalLabelEvaluationDataId id = new NominalLabelEvaluationDataId(documentId, crisisId,
			nominalLabelId, nominalAttributeId, wordFeatures);
		
		return id;
	}
}
