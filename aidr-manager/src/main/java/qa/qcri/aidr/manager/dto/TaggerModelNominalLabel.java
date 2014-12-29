package qa.qcri.aidr.manager.dto;

import qa.qcri.aidr.dbmanager.dto.ModelNominalLabelDTO;

public class TaggerModelNominalLabel {

	private Double labelPrecision;

	private Double labelRecall;

	private Double labelAuc;

	private Integer classifiedDocumentCount;

	private TaggerLabel nominalLabel;

	private TaggerModelNominalLabelPK modelNominalLabelPK;

	private int trainingDocuments;

	private String modelStatus;

	private Integer nominalAttributeId;

	public Double getLabelPrecision() {
		return labelPrecision;
	}

	public void setLabelPrecision(Double labelPrecision) {
		this.labelPrecision = labelPrecision;
	}

	public Double getLabelRecall() {
		return labelRecall;
	}

	public void setLabelRecall(Double labelRecall) {
		this.labelRecall = labelRecall;
	}

	public Double getLabelAuc() {
		return labelAuc;
	}

	public void setLabelAuc(Double labelAuc) {
		this.labelAuc = labelAuc;
	}

	public Integer getClassifiedDocumentCount() {
		return classifiedDocumentCount;
	}

	public void setClassifiedDocumentCount(Integer classifiedDocumentCount) {
		this.classifiedDocumentCount = classifiedDocumentCount;
	}

	public TaggerLabel getNominalLabel() {
		return nominalLabel;
	}

	public void setNominalLabel(TaggerLabel nominalLabel) {
		this.nominalLabel = nominalLabel;
	}

	public TaggerModelNominalLabelPK getModelNominalLabelPK() {
		return modelNominalLabelPK;
	}

	public void setModelNominalLabelPK(TaggerModelNominalLabelPK modelNominalLabelPK) {
		this.modelNominalLabelPK = modelNominalLabelPK;
	}

	public int getTrainingDocuments() {
		return trainingDocuments;
	}

	public void setTrainingDocuments(int trainingDocuments) {
		this.trainingDocuments = trainingDocuments;
	}

	public String getModelStatus() {
		return modelStatus;
	}

	public void setModelStatus(String modelStatus) {
		this.modelStatus = modelStatus;
	}

	public Integer getNominalAttributeId() {
		return nominalAttributeId;
	}

	public void setNominalAttributeId(Integer nominalAttributeId) {
		this.nominalAttributeId = nominalAttributeId;
	}

	public TaggerModelNominalLabel() {}

	public TaggerModelNominalLabel(ModelNominalLabelDTO dto) throws Exception {
		if (dto != null) {
			this.setClassifiedDocumentCount(dto.getClassifiedDocumentCount());
			this.setLabelAuc(dto.getLabelAuc());
			this.setLabelPrecision(dto.getLabelPrecision());
			this.setLabelRecall(dto.getLabelRecall());
			this.setModelStatus(dto.isModelStatus());
			if (dto.getNominalAttributeId() != null) {
				this.setNominalAttributeId(dto.getNominalAttributeId().intValue());
			}
			if (dto.getNominalLabelDTO() != null) {
				this.setNominalLabel(new TaggerLabel(dto.getNominalLabelDTO()));
			}
			this.setTrainingDocuments(dto.getTrainingDocuments());
			if (dto.getIdDTO() != null) {
				this.setModelNominalLabelPK(new TaggerModelNominalLabelPK(dto.getIdDTO()));
			}
		}
	}
	
	public ModelNominalLabelDTO toDTO() throws Exception {
		ModelNominalLabelDTO dto = new ModelNominalLabelDTO();
		dto.setClassifiedDocumentCount(this.getClassifiedDocumentCount());
		dto.setLabelAuc(this.getLabelAuc());
		dto.setLabelPrecision(this.getLabelPrecision());
		dto.setLabelRecall(this.getLabelRecall());
		dto.setModelStatus(this.getModelStatus());
		if (this.getNominalAttributeId() != null) {
			dto.setNominalAttributeId(new Long(this.getNominalAttributeId()));
		}
		if (this.getNominalLabel() != null) {
			dto.setNominalLabelDTO(this.getNominalLabel().toDTO());
		}
		dto.setTrainingDocuments(dto.getTrainingDocuments());
		if (this.getModelNominalLabelPK() != null) {
			dto.setIdDTO(this.getModelNominalLabelPK().toDTO());
		}
		return dto;
	}
}
