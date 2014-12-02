package qa.qcri.aidr.dbmanager.dto;


import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.entities.model.NominalAttributeDependentLabel;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
public class NominalAttributeDependentLabelDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4706537541293765081L;

	@XmlElement
	private NominalAttributeDependentLabelIdDTO idDTO;

	@XmlElement
	private NominalLabelDTO nominalLabelDTO;

	@XmlElement
	private NominalAttributeDTO nominalAttributeDTO;

	@XmlElement
	private Float threshold;

	public NominalAttributeDependentLabelDTO() {
	}

	public NominalAttributeDependentLabelDTO(NominalAttributeDependentLabelIdDTO idDTO,
			NominalLabelDTO nominalLabelDTO, NominalAttributeDTO nominalAttributeDTO) throws PropertyNotSetException {
		this.setIdDTO(idDTO);
		this.setNominalLabelDTO(nominalLabelDTO);
		this.setNominalAttributeDTO(nominalAttributeDTO);
	}

	public NominalAttributeDependentLabelDTO(NominalAttributeDependentLabelIdDTO idDTO,
			NominalLabelDTO nominalLabelDTO, NominalAttributeDTO nominalAttributeDTO,
			Float threshold) throws PropertyNotSetException {
		this.setIdDTO(idDTO);
		this.setNominalLabelDTO(nominalLabelDTO);
		this.setNominalAttributeDTO(nominalAttributeDTO);
		this.threshold = threshold;
	}

	public NominalAttributeDependentLabelDTO(NominalAttributeDependentLabel t) throws PropertyNotSetException {
		this.setIdDTO(new NominalAttributeDependentLabelIdDTO(t.getId()));
		if (t.hasNominalLabel()) {
			this.setNominalLabelDTO(new NominalLabelDTO(t.getNominalLabel()));
		}
		if (t.hasNominalAttribute()) {
			this.setNominalAttributeDTO(new NominalAttributeDTO(t.getNominalAttribute()));
		}
		this.threshold = t.getThreshold();
	}

	public NominalAttributeDependentLabelIdDTO getIdDTO() {
		return this.idDTO;
	}

	public void setIdDTO(NominalAttributeDependentLabelIdDTO idDTO) throws PropertyNotSetException {
		if (idDTO != null) {
			this.idDTO = idDTO;
		} else {
			throw new PropertyNotSetException("Primary key not set!");
		}
	}

	public NominalLabelDTO getNominalLabelDTO() {
		return this.nominalLabelDTO;
	}

	public void setNominalLabelDTO(NominalLabelDTO nominalLabelDTO) throws PropertyNotSetException {
		if (nominalLabelDTO != null) {
			this.nominalLabelDTO = nominalLabelDTO;
		} else {
			throw new PropertyNotSetException();
		}
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

	public Float getThreshold() {
		return this.threshold;
	}

	public void setThreshold(Float threshold) {
		this.threshold = threshold;
	}

	public NominalAttributeDependentLabel toEntity() throws PropertyNotSetException {
		NominalAttributeDependentLabel entity = new NominalAttributeDependentLabel(this.getIdDTO().toEntity(),
				this.getNominalLabelDTO().toEntity(), this.getNominalAttributeDTO().toEntity());
		entity.setThreshold(this.getThreshold());

		return entity;
	}
}
