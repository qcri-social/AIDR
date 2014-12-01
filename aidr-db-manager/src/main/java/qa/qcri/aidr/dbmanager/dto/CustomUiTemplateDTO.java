package qa.qcri.aidr.dbmanager.dto;


import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import qa.qcri.aidr.dbmanager.entities.misc.CustomUiTemplate;

@JsonIgnoreProperties(ignoreUnknown=true)
@XmlRootElement
public class CustomUiTemplateDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7750032143570941469L;

	@XmlElement
	private Long customUitemplateId;
	
	@XmlElement
	private Long crisisId;
	
	@XmlElement
	private Long nominalAttributeId;
	
	@XmlElement
	private Integer templateType;
	
	@XmlElement
	private String templateValue;
	
	@XmlElement
	private Integer status;
	
	@XmlElement
	private Boolean isActive;
	
	@XmlElement
	private Date updated;

	public CustomUiTemplateDTO() {
	}
	
	public CustomUiTemplateDTO(CustomUiTemplate e) {
		this.setCrisisId(e.getCrisisId());
		this.setCustomUitemplateId(e.getCustomUitemplateId());
		this.setIsActive(e.isIsActive());
		this.setNominalAttributeId(e.getNominalAttributeId());
		this.setStatus(e.getStatus());
		this.setTemplateType(e.getTemplateType());
		this.setTemplateValue(e.getTemplateValue());
		this.setUpdated(e.getUpdated());
	}


	public CustomUiTemplateDTO(Long crisisId, Integer templateType,
			String templateValue, Boolean isActive, Date updated) {
		this.crisisId = crisisId;
		this.templateType = templateType;
		this.templateValue = templateValue;
		this.isActive = isActive;
		this.updated = updated;
	}

	public CustomUiTemplateDTO(Long crisisId, Long nominalAttributeId,
			Integer templateType, String templateValue, Integer status,
			Boolean isActive, Date updated) {
		this.crisisId = crisisId;
		this.nominalAttributeId = nominalAttributeId;
		this.templateType = templateType;
		this.templateValue = templateValue;
		this.status = status;
		this.isActive = isActive;
		this.updated = updated;
	}

	public Long getCustomUitemplateId() {
		return this.customUitemplateId;
	}

	public void setCustomUitemplateId(Long customUitemplateId) {
		this.customUitemplateId = customUitemplateId;
	}

	public Long getCrisisId() {
		return this.crisisId;
	}

	public void setCrisisId(long crisisId) {
		this.crisisId = crisisId;
	}

	public Long getNominalAttributeId() {
		return this.nominalAttributeId;
	}

	public void setNominalAttributeId(Long nominalAttributeId) {
		this.nominalAttributeId = nominalAttributeId;
	}

	public Integer getTemplateType() {
		return this.templateType;
	}

	public void setTemplateType(int templateType) {
		this.templateType = templateType;
	}

	public String getTemplateValue() {
		return this.templateValue;
	}

	public void setTemplateValue(String templateValue) {
		this.templateValue = templateValue;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Boolean isIsActive() {
		return this.isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Date getUpdated() {
		return this.updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	
	public CustomUiTemplate toEntity() {
		CustomUiTemplate entity = new CustomUiTemplate(crisisId, nominalAttributeId,
				templateType, templateValue, status, isActive, updated);
		entity.setCustomUitemplateId(customUitemplateId);
		return entity;
	}
}
