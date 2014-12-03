package qa.qcri.aidr.dbmanager.dto;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.entities.model.ModelNominalLabel;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModelNominalLabelDTO {
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
			this.setModelDTO(new ModelDTO(modelNominalLabel.getModel()));
		} 
		if (modelNominalLabel.hasNominalLabel()) {
			this.setNominalLabelDTO(new NominalLabelDTO(modelNominalLabel.getNominalLabel()));
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
}
