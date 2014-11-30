package qa.qcri.aidr.dbmanager.dto;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.entities.model.Model;
import qa.qcri.aidr.dbmanager.entities.model.ModelFamily;


@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
public class ModelFamilyDTO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7377627785888285732L;
	private Long modelFamilyId;
    private NominalAttributeDTO nominalAttributeDTO;
    private CrisisDTO crisisDTO;
    private boolean isActive;

  
    private List<ModelDTO> modelCollectionDTO;

    public ModelFamilyDTO() {
    }

    public ModelFamilyDTO(ModelFamily model) throws PropertyNotSetException {
        this.setModelFamilyId(model.getModelFamilyId());
    	this.nominalAttributeDTO = new NominalAttributeDTO(model.getNominalAttribute());
        this.crisisDTO = new CrisisDTO(model.getCrisis());
        this.isActive = model.isIsActive();
        this.modelFamilyId = model.getModelFamilyId();
        if (model.hasModelCollection()) {
        	this.setModelCollectionDTO(this.toModelDTOList(model.getModelCollection()));
        }
    }
    
    public ModelFamilyDTO(NominalAttributeDTO nominalAttributeDTO, CrisisDTO crisisDTO,
            boolean isActive) throws PropertyNotSetException {
        this.setNominalAttributeDTO(nominalAttributeDTO);
        this.setCrisisDTO(crisisDTO);
        this.isActive = isActive;
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

    public void setNominalAttributeDTO(NominalAttributeDTO nominalAttributeDTO) throws PropertyNotSetException {
        if (nominalAttributeDTO != null) {
    	this.nominalAttributeDTO = nominalAttributeDTO;
        } else {
        	throw new PropertyNotSetException();
        }
    }

    public CrisisDTO getCrisisDTO() {
        return this.crisisDTO;
    }

    public void setCrisisDTO(CrisisDTO crisisDTO) throws PropertyNotSetException {
        if (crisisDTO != null) {
    	this.crisisDTO = crisisDTO;
        } else {
        	throw new PropertyNotSetException();
        }
    }

    public boolean isIsActive() {
        return this.isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public List<ModelDTO> getModelCollectionDTO() {
        return modelCollectionDTO;
    }

    public void setModelCollectionDTO(List<ModelDTO> modelCollectionDTO) {
        this.modelCollectionDTO = modelCollectionDTO;
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
		ModelFamily entity = new ModelFamily(this.getNominalAttributeDTO().toEntity(), 
									this.getCrisisDTO().toEntity(), this.isIsActive());
		if (this.getModelFamilyId() != null) {
			entity.setModelFamilyId(modelFamilyId);
		}
		if (this.modelCollectionDTO != null) {
			entity.setModelCollection(this.toModelList(this.getModelCollectionDTO()));
		}
		return entity;
	}
}
