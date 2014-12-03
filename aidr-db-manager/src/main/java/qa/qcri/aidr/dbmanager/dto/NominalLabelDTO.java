package qa.qcri.aidr.dbmanager.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.entities.model.ModelNominalLabel;
import qa.qcri.aidr.dbmanager.entities.model.NominalAttributeDependentLabel;
import qa.qcri.aidr.dbmanager.entities.model.NominalLabel;
import qa.qcri.aidr.dbmanager.entities.task.DocumentNominalLabel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
public class NominalLabelDTO  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 70272893149042961L;

	@XmlElement
	private Long nominalLabelId;

	@XmlElement
	private NominalAttributeDTO nominalAttributeDTO;

	@XmlElement
	private String nominalLabelCode;

	@XmlElement
	private String name;

	@XmlElement
	private String description;

	@XmlElement
	private Integer sequence;

	@XmlElement
	private List<ModelNominalLabelDTO> modelNominalLabelsDTO = null;

	@XmlElement
	private List<NominalAttributeDependentLabelDTO> nominalAttributeDependentLabelsDTO = null;

	@XmlElement
	private List<DocumentNominalLabelDTO> documentNominalLabelsDTO = null;

	public NominalLabelDTO() {
	}

	public NominalLabelDTO(NominalLabel nominalLabel) throws PropertyNotSetException {
		if (nominalLabel.hasNominalAttribute()) {
			this.setNominalAttributeDTO(new NominalAttributeDTO(nominalLabel.getNominalAttribute()));
		}
		this.setNominalLabelId(nominalLabel.getNominalLabelId());
		this.setNominalLabelCode(nominalLabel.getNominalLabelCode());
		this.setName(nominalLabel.getName());
		this.setDescription(nominalLabel.getDescription());
		this.setSequence(nominalLabel.getSequence());

		if (nominalLabel.hasDocumentNominalLabels()) {
			this.setDocumentNominalLabelsDTO(
					this.toDocumentNominalLabelDTOList(nominalLabel.getDocumentNominalLabels()));
		}
		if (nominalLabel.hasModelNominalLabels()) {
			this.setModelNominalLabelsDTO(this.toModelNominalLabelDTOList(nominalLabel.getModelNominalLabels()));
		}
		if (nominalLabel.hasNominalAttributeDependentLabels()) {
			this.setNominalAttributeDependentLabelsDTO(
					this.toNominalAttributeDependentLabelDTOList(nominalLabel.getNominalAttributeDependentLabels()));
		}

	}

	public NominalLabelDTO(NominalAttributeDTO nominalAttributeDTO,
			String nominalLabelCode, String name, String description,
			Integer sequence) throws PropertyNotSetException {
		this.setNominalAttributeDTO(nominalAttributeDTO);
		this.setNominalLabelCode(nominalLabelCode);
		this.setName(name);
		this.setDescription(description);
		this.setSequence(sequence);
	}

	public NominalLabelDTO(NominalAttributeDTO nominalAttributeDTO,
			String nominalLabelCode, String name, String description,
			Integer sequence, List<ModelNominalLabelDTO> modelNominalLabelsDTO,
			List<NominalAttributeDependentLabelDTO> nominalAttributeDependentLabelsDTO, 
			List<DocumentNominalLabelDTO> documentNominalLabelsDTO) throws PropertyNotSetException {
		this.setNominalAttributeDTO(nominalAttributeDTO);
		this.setNominalLabelCode(nominalLabelCode);
		this.setName(name);
		this.setDescription(description);
		this.setSequence(sequence);
		this.setModelNominalLabelsDTO(modelNominalLabelsDTO);
		this.setNominalAttributeDependentLabelsDTO(nominalAttributeDependentLabelsDTO);
		this.setDocumentNominalLabelsDTO(documentNominalLabelsDTO);
	}

	public Long getNominalLabelId() {
		return this.nominalLabelId;
	}

	public void setNominalLabelId(Long nominalLabelId) {
		this.nominalLabelId = nominalLabelId;
	}

	public NominalAttributeDTO getNominalAttributeDTO() {
		return this.nominalAttributeDTO;
	}

	public void setNominalAttributeDTO(NominalAttributeDTO nominalAttributeDTO) throws PropertyNotSetException {
		this.nominalAttributeDTO = nominalAttributeDTO;
	}

	public String getNominalLabelCode() {
		return this.nominalLabelCode;
	}

	public void setNominalLabelCode(String nominalLabelCode) {
		this.nominalLabelCode = nominalLabelCode;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getSequence() {
		return this.sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public List<ModelNominalLabelDTO> getModelNominalLabelsDTO() {
		return this.modelNominalLabelsDTO;
	}

	public void setModelNominalLabelsDTO(List<ModelNominalLabelDTO> modelNominalLabelsDTO) {
		this.modelNominalLabelsDTO = modelNominalLabelsDTO;
	}

	public List<NominalAttributeDependentLabelDTO> getNominalAttributeDependentLabelsDTO() {
		return this.nominalAttributeDependentLabelsDTO;
	}

	public void setNominalAttributeDependentLabelsDTO(List<NominalAttributeDependentLabelDTO> nominalAttributeDependentLabelsDTO) {
		this.nominalAttributeDependentLabelsDTO = nominalAttributeDependentLabelsDTO;
	}

	public List<DocumentNominalLabelDTO> getDocumentNominalLabelsDTO() {
		return this.documentNominalLabelsDTO;
	}

	public void setDocumentNominalLabelsDTO(List<DocumentNominalLabelDTO> documentNominalLabelsDTO) {
		if (documentNominalLabelsDTO != null) {
			this.documentNominalLabelsDTO = documentNominalLabelsDTO;
		} else {
			throw new IllegalArgumentException("Argument cannot be null!");
		}
	}


	private List<DocumentNominalLabelDTO> toDocumentNominalLabelDTOList(List<DocumentNominalLabel> list) throws PropertyNotSetException {
		if (list != null) {
			List<DocumentNominalLabelDTO> dtoList = new ArrayList<DocumentNominalLabelDTO>();
			for (DocumentNominalLabel d: list) {
				dtoList.add(new DocumentNominalLabelDTO(d));
			}
			return dtoList;
		}
		return null;
	}


	private List<DocumentNominalLabel> toDocumentNominalLabelList(List<DocumentNominalLabelDTO> list) throws PropertyNotSetException {
		if (list != null) {
			List<DocumentNominalLabel> eList = new ArrayList<DocumentNominalLabel>();
			for (DocumentNominalLabelDTO dto: list) {
				eList.add(dto.toEntity());
			}
			return eList;
		}
		return null;
	} 

	private List<ModelNominalLabelDTO> toModelNominalLabelDTOList(List<ModelNominalLabel> list) throws PropertyNotSetException {
		if (list != null) {
			List<ModelNominalLabelDTO> dtoList = new ArrayList<ModelNominalLabelDTO>();
			for (ModelNominalLabel d: list) {
				dtoList.add(new ModelNominalLabelDTO(d));
			}
			return dtoList;
		}
		return null;
	}


	private List<ModelNominalLabel> toModelNominalLabelList(List<ModelNominalLabelDTO> list) throws PropertyNotSetException {
		if (list != null) {
			List<ModelNominalLabel> eList = new ArrayList<ModelNominalLabel>();
			for (ModelNominalLabelDTO dto: list) {
				eList.add(dto.toEntity());
			}
			return eList;
		}
		return null;
	} 

	private List<NominalAttributeDependentLabelDTO> toNominalAttributeDependentLabelDTOList(List<NominalAttributeDependentLabel> list) throws PropertyNotSetException {
		if (list != null) {
			List<NominalAttributeDependentLabelDTO> dtoList = new ArrayList<NominalAttributeDependentLabelDTO>();
			for (NominalAttributeDependentLabel d: list) {
				dtoList.add(new NominalAttributeDependentLabelDTO(d));
			}
			return dtoList;
		}
		return null;
	}


	private List<NominalAttributeDependentLabel> toNominalAttributeDependentLabelList(List<NominalAttributeDependentLabelDTO> list) throws PropertyNotSetException {
		if (list != null) {
			List<NominalAttributeDependentLabel> eList = new ArrayList<NominalAttributeDependentLabel>();
			for (NominalAttributeDependentLabelDTO dto: list) {
				eList.add(dto.toEntity());
			}
			return eList;
		}
		return null;
	}


	public NominalLabel toEntity() throws PropertyNotSetException {
		NominalLabel entity  = new NominalLabel(this.getNominalAttributeDTO().toEntity(),
				this.getNominalLabelCode(), this.getName(), this.getDescription(), this.getSequence());

		if (this.getNominalLabelId() != null) {
			entity.setNominalLabelId(this.getNominalLabelId());
		}
		if (this.getDocumentNominalLabelsDTO() != null) {
			entity.setDocumentNominalLabels(this.toDocumentNominalLabelList(this.documentNominalLabelsDTO));
		}
		if (this.getModelNominalLabelsDTO() != null) {
			entity.setModelNominalLabels(this.toModelNominalLabelList(this.modelNominalLabelsDTO));
		}
		if (this.getNominalAttributeDependentLabelsDTO() != null) {
			entity.setNominalAttributeDependentLabels(this.toNominalAttributeDependentLabelList(this.nominalAttributeDependentLabelsDTO));
		}
		return entity;
	}

}
