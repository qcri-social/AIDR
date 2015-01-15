package qa.qcri.aidr.manager.dto;

import qa.qcri.aidr.dbmanager.dto.ModelFamilyDTO;

public class TaggerModelFamily {

	private TaggerCrisis crisis;

	private TaggerAttribute nominalAttribute;

	private Boolean isActive;

	private Integer modelFamilyID;

	public TaggerModelFamily() {
	}

	public TaggerModelFamily(TaggerCrisis crisis, TaggerAttribute nominalAttribute, Boolean active) {
		this.crisis = crisis;
		this.nominalAttribute = nominalAttribute;
		isActive = active;
	}

	public TaggerModelFamily(ModelFamilyDTO dto) throws Exception {
		if (dto != null) {
			this.setIsActive(dto.isIsActive());
			this.setModelFamilyID(dto.getModelFamilyId() != null ? dto.getModelFamilyId().intValue() : null);
			if (dto.getCrisisDTO() != null) {
				this.setCrisis(new TaggerCrisis(dto.getCrisisDTO()));
			}
			if (dto.getNominalAttributeDTO() != null) {
				this.setNominalAttribute(new TaggerAttribute(dto.getNominalAttributeDTO()));
			}
		}
	}

	public ModelFamilyDTO toDTO() throws Exception {
		ModelFamilyDTO dto = new ModelFamilyDTO();
		dto.setIsActive(this.getIsActive());
		if (this.getModelFamilyID() != null) { 
			dto.setModelFamilyId(new Long(this.getModelFamilyID()));
		}
		if (this.getCrisis() != null) { 
			dto.setCrisisDTO(this.getCrisis().toDTO());
		}
		if (this.getNominalAttribute() != null) {
			dto.setNominalAttributeDTO(this.getNominalAttribute().toDTO());
		}

		return dto;
	}

	public TaggerCrisis getCrisis() {
		return crisis;
	}

	public void setCrisis(TaggerCrisis crisis) {
		this.crisis = crisis;
	}

	public TaggerAttribute getNominalAttribute() {
		return nominalAttribute;
	}

	public void setNominalAttribute(TaggerAttribute nominalAttribute) {
		this.nominalAttribute = nominalAttribute;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean active) {
		isActive = active;
	}

	public Integer getModelFamilyID() {
		return modelFamilyID;
	}

	public void setModelFamilyID(Integer modelFamilyID) {
		this.modelFamilyID = modelFamilyID;
	}
}
