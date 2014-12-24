package qa.qcri.aidr.manager.dto;

import qa.qcri.aidr.dbmanager.dto.ModelFamilyDTO;

public class TaggerModelFamilyCollection {

	private String isActive;

	private Integer modelFamilyID;

	private TaggerAttribute nominalAttribute;

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String active) {
		isActive = active;
	}

	public Integer getModelFamilyID() {
		return modelFamilyID;
	}

	public void setModelFamilyID(Integer modelFamilyID) {
		this.modelFamilyID = modelFamilyID;
	}

	public TaggerAttribute getNominalAttribute() {
		return nominalAttribute;
	}

	public void setNominalAttribute(TaggerAttribute nominalAttribute) {
		this.nominalAttribute = nominalAttribute;
	}

	public TaggerModelFamilyCollection() {}

	public TaggerModelFamilyCollection(ModelFamilyDTO dto) throws Exception {
		if (dto != null) {
			this.setModelFamilyID(dto.getModelFamilyId().intValue());
			this.setIsActive(dto.isIsActive() == true ? "true" : "false");
			this.setNominalAttribute(new TaggerAttribute(dto.getNominalAttributeDTO()));
		}
	}

	public ModelFamilyDTO toDTO() throws Exception {
		ModelFamilyDTO dto = new ModelFamilyDTO();
		dto.setIsActive(this.getIsActive().equalsIgnoreCase("true") ? true : false);
		dto.setModelFamilyId(new Long(this.getModelFamilyID()));
		dto.setNominalAttributeDTO(this.getNominalAttribute().toDTO());

		return dto;
	}
}
