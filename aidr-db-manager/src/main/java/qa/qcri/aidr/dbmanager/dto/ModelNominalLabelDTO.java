package qa.qcri.aidr.dbmanager.dto;




import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.entities.model.Model;
import qa.qcri.aidr.dbmanager.entities.model.ModelNominalLabel;
import qa.qcri.aidr.dbmanager.entities.model.NominalLabel;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModelNominalLabelDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6522772017803560098L;

	@XmlElement
	private ModelNominalLabelIdDTO idDTO;

	@XmlElement
	private NominalLabelDTO nominalLabelDTO;

	@XmlElement
	private ModelDTO modelDTO;

	@XmlElement
	private Double labelPrecision;

	@XmlElement
	private Double labelRecall;

	@XmlElement
	private Double labelAuc;

	@XmlElement
	private Integer classifiedDocumentCount;

	@XmlElement 
	private String modelStatus;

	@XmlElement 
	private Long nominalAttributeId;

	@XmlElement 
	private Integer trainingDocuments;

	public ModelNominalLabelDTO() {
	}

	public ModelNominalLabelDTO(ModelNominalLabelIdDTO idDTO, NominalLabelDTO nominalLabelDTO,
			ModelDTO modelDTO) throws PropertyNotSetException {
		this.setIdDTO(idDTO);
		this.setNominalLabelDTO(nominalLabelDTO);
		this.setModelDTO(modelDTO);
	}

	public ModelNominalLabelDTO(ModelNominalLabelIdDTO idDTO, NominalLabelDTO nominalLabelDTO,
			ModelDTO modelDTO, Double labelPrecision, Double labelRecall,
			Double labelAuc, Integer classifiedDocumentCount) throws PropertyNotSetException {
		this.setIdDTO(idDTO);
		this.setNominalLabelDTO(nominalLabelDTO);
		this.setModelDTO(modelDTO);
		this.labelPrecision = labelPrecision;
		this.labelRecall = labelRecall;
		this.labelAuc = labelAuc;
		this.classifiedDocumentCount = classifiedDocumentCount;
	}

	public ModelNominalLabelDTO(ModelNominalLabel modelNominalLabel) throws PropertyNotSetException {
		if (modelNominalLabel != null) {
			this.setLabelPrecision(modelNominalLabel.getLabelPrecision()); 
			this.setLabelRecall(modelNominalLabel.getLabelRecall());
			this.setLabelAuc(modelNominalLabel.getLabelAuc());
			this.setClassifiedDocumentCount(modelNominalLabel.getClassifiedDocumentCount());

			if (modelNominalLabel.getId() != null) {
				this.setIdDTO(new ModelNominalLabelIdDTO(modelNominalLabel.getId()));
			} else {
				throw new PropertyNotSetException("Primary key not set!");
			}
			if (modelNominalLabel.hasModel()) {
				Model m = new Model(modelNominalLabel.getModel().getModelFamily(), modelNominalLabel.getModel().getAvgPrecision(), 
						modelNominalLabel.getModel().getAvgRecall(), modelNominalLabel.getModel().getAvgAuc(), 
						modelNominalLabel.getModel().getTrainingCount(), modelNominalLabel.getModel().getTrainingTime(),
						modelNominalLabel.getModel().isIsCurrentModel());
			
				Long modelID = new Long(modelNominalLabel.getModel().getModelId());
				m.setModelId(modelID);
				this.setModelDTO(new ModelDTO(m));
			} 
			if (modelNominalLabel.hasNominalLabel()) {
				NominalLabel nb = new NominalLabel(modelNominalLabel.getNominalLabel().getNominalAttribute(),
						modelNominalLabel.getNominalLabel().getNominalLabelCode(), modelNominalLabel.getNominalLabel().getName(),
						modelNominalLabel.getNominalLabel().getDescription(),
						modelNominalLabel.getNominalLabel().getSequence());
				nb.setNominalLabelId(modelNominalLabel.getNominalLabel().getNominalLabelId());
				this.setNominalLabelDTO(new NominalLabelDTO(nb));
			} 
		}
	}


	public ModelNominalLabelIdDTO getIdDTO() {
		return this.idDTO;
	}


	public void setIdDTO(ModelNominalLabelIdDTO idDTO) {
		this.idDTO = idDTO;
	}

	public NominalLabelDTO getNominalLabelDTO() {
		return this.nominalLabelDTO;
	}

	public void setNominalLabelDTO(NominalLabelDTO nominalLabelDTO) {
		this.nominalLabelDTO = nominalLabelDTO;
	}

	public ModelDTO getModelDTO() {
		return this.modelDTO;
	}

	public void setModelDTO(ModelDTO modelDTO) throws PropertyNotSetException {
		this.modelDTO = modelDTO;
	}

	public Double getLabelPrecision() {
		return this.labelPrecision;
	}

	public void setLabelPrecision(Double labelPrecision) {
		this.labelPrecision = labelPrecision;
	}

	public Double getLabelRecall() {
		return this.labelRecall;
	}

	public void setLabelRecall(Double labelRecall) {
		this.labelRecall = labelRecall;
	}

	public Double getLabelAuc() {
		return this.labelAuc;
	}

	public void setLabelAuc(Double labelAuc) {
		this.labelAuc = labelAuc;
	}

	public Integer getClassifiedDocumentCount() {
		return this.classifiedDocumentCount;
	}

	public void setClassifiedDocumentCount(Integer classifiedDocumentCount) {
		this.classifiedDocumentCount = classifiedDocumentCount;
	}

	public ModelNominalLabel toEntity() throws PropertyNotSetException {
		ModelNominalLabel entity = new ModelNominalLabel(this.idDTO.toEntity(), this.nominalLabelDTO.toEntity(),
				this.modelDTO.toEntity(), this.labelPrecision, this.labelRecall,
				this.labelAuc, this.classifiedDocumentCount);

		return entity;
	}

	public Long getNominalAttributeId() {
		return nominalAttributeId;
	}

	public void setNominalAttributeId(Long nominalAttributeId) {
		this.nominalAttributeId = nominalAttributeId;
	}

	/**
	 * @return the modelStatus
	 */
	public String isModelStatus() {
		return modelStatus;
	}

	/**
	 * @param modelStatus the modelStatus to set
	 */
	public void setModelStatus(String modelStatus) {
		this.modelStatus = modelStatus;
	}

	public Integer getTrainingDocuments() {
		return trainingDocuments;
	}

	/**
	 * @param trainingDocuments the trainingDocuments to set
	 */
	public void setTrainingDocuments(Integer trainingDocuments) {
		this.trainingDocuments = trainingDocuments;
	}
}
