package qa.qcri.aidr.dbmanager.dto;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.entities.misc.Crisis;
import qa.qcri.aidr.dbmanager.entities.model.Model;
import qa.qcri.aidr.dbmanager.entities.model.ModelFamily;
import qa.qcri.aidr.dbmanager.entities.model.NominalAttribute;


@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
public class ModelFamilyDTO implements Serializable {

	/**
	 * 
	 */

	private static final long serialVersionUID = 7377627785888285732L;

	@XmlElement
	private Long modelFamilyId;

	@XmlElement
	private NominalAttributeDTO nominalAttributeDTO = null;

	@XmlElement
	private CrisisDTO crisisDTO = null;

	@XmlElement
	private boolean isActive;

	@XmlElement
	private List<ModelDTO> modelsDTO = null;

	public ModelFamilyDTO() {
	}

	public ModelFamilyDTO(ModelFamily model) throws PropertyNotSetException {
		if (model != null) {
			//System.out.println("ModelFamily Hash code: " + model.hashCode());
			this.setModelFamilyId(model.getModelFamilyId());
			if (model.hasNominalAttribute()) {
				NominalAttribute na = new NominalAttribute(model.getNominalAttribute().getUsers(), 
						model.getNominalAttribute().getName(), model.getNominalAttribute().getDescription(), model.getNominalAttribute().getCode());
				na.setNominalAttributeId(model.getNominalAttribute().getNominalAttributeId());
				this.setNominalAttributeDTO(new NominalAttributeDTO(na));
			}
			if (model.hasCrisis()) {
				Crisis c = new Crisis(model.getCrisis().getUsers(), model.getCrisis().getCrisisType(), model.getCrisis().getName(), model.getCrisis().getCode(),
						model.getCrisis().isIsTrashed());
				c.setCrisisId(model.getCrisis().getCrisisId());
				this.setCrisisDTO(new CrisisDTO(c));
			}
			this.setIsActive(model.isIsActive());
			this.setModelFamilyId(model.getModelFamilyId());
			if (model.hasModels()) {
				this.setModelsDTO(this.toModelDTOList(model.getModels()));
			} 
		}

	}

	public ModelFamilyDTO(NominalAttributeDTO nominalAttributeDTO, CrisisDTO crisisDTO,
			boolean isActive) {
		this.setNominalAttributeDTO(nominalAttributeDTO);
		this.setCrisisDTO(crisisDTO);
		this.setIsActive(isIsActive());
	}

	public Long getModelFamilyId() {
		return this.modelFamilyId;
	}

	public void setModelFamilyId(Long modelFamilyId) {
		this.modelFamilyId = modelFamilyId;
	}

	public NominalAttributeDTO getNominalAttributeDTO() {
		return this.nominalAttributeDTO;
	}

	public void setNominalAttributeDTO(NominalAttributeDTO nominalAttributeDTO) {
			this.nominalAttributeDTO = nominalAttributeDTO;
	}

	public CrisisDTO getCrisisDTO() {
		return this.crisisDTO;
	}

	public void setCrisisDTO(CrisisDTO crisisDTO) {
			this.crisisDTO = crisisDTO;
	}

	public boolean isIsActive() {
		return this.isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public List<ModelDTO> getModelsDTO() {
		return modelsDTO;
	}

	public void setModelsDTO(List<ModelDTO> modelsDTO) {
		this.modelsDTO = modelsDTO;
	}

	private List<ModelDTO> toModelDTOList(List<Model> list) throws PropertyNotSetException {
		if (list != null) {
			List<ModelDTO> dtoList = new ArrayList<ModelDTO>();
			for (Model d: list) {
				dtoList.add(new ModelDTO(d));
			}
			return dtoList;
		}
		return null;
	}


	private List<Model> toModelList(List<ModelDTO> list) throws PropertyNotSetException {
		if (list != null) {
			List<Model> eList = new ArrayList<Model>();
			for (ModelDTO dto: list) {
				eList.add(dto.toEntity());
			}
			return eList;
		}
		return null;
	}

	public ModelFamily toEntity() throws PropertyNotSetException {
		ModelFamily entity = new ModelFamily();
		if (this.getNominalAttributeDTO() != null) {
			entity.setNominalAttribute(this.getNominalAttributeDTO().toEntity());
		}
		if (this.getCrisisDTO() != null) {
				entity.setCrisis(this.getCrisisDTO().toEntity());
		}
		entity.setIsActive(this.isIsActive());
		if (this.getModelFamilyId() != null) {
			entity.setModelFamilyId(modelFamilyId);
		}
		if (this.modelsDTO != null) {
			entity.setModels(this.toModelList(this.getModelsDTO()));
		}
		return entity;
	}
}
