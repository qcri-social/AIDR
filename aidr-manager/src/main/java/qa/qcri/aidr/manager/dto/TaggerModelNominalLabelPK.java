package qa.qcri.aidr.manager.dto;

import qa.qcri.aidr.dbmanager.dto.ModelNominalLabelIdDTO;

public class TaggerModelNominalLabelPK {

	private Integer modelID;

	private Integer nominalLabelID;

	public Integer getModelID() {
		return modelID;
	}

	public void setModelID(Integer modelID) {
		this.modelID = modelID;
	}

	public Integer getNominalLabelID() {
		return nominalLabelID;
	}

	public void setNominalLabelID(Integer nominalLabelID) {
		this.nominalLabelID = nominalLabelID;
	}

	public TaggerModelNominalLabelPK() {}

	public TaggerModelNominalLabelPK(ModelNominalLabelIdDTO dto) {
		if (dto != null) {
			this.setModelID(dto.getModelId().intValue());
			this.setNominalLabelID(dto.getNominalLabelId().intValue());
		}
	}

	public ModelNominalLabelIdDTO toDTO() {
		ModelNominalLabelIdDTO id = new ModelNominalLabelIdDTO();
		if (this.getModelID() != null) {
			id.setModelId(new Long(this.getModelID()));
		}
		if (this.getNominalLabelID() != null) {
			id.setNominalLabelId(new Long(this.getNominalLabelID()));
		}
		return id;
	}
}
