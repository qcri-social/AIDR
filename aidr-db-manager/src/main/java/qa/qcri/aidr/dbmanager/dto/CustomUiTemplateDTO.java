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
	private Long crisisID;
	
	@XmlElement
	private Long nominalAttributeID;
	
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
		this.setCrisisID(e.getCrisisID());
		this.setCustomUitemplateId(e.getCustomUitemplateId());
		this.setIsActive(e.isIsActive());
		this.setNominalAttributeID(e.getNominalAttributeID());
		this.setStatus(e.getStatus());
		this.setTemplateType(e.getTemplateType());
		this.setTemplateValue(e.getTemplateValue());
		this.setUpdated(e.getUpdated());
	}


	public CustomUiTemplateDTO(Long crisisId, Integer templateType,
			String templateValue, Boolean isActive, Date updated) {
		this.crisisID = crisisId;
		this.templateType = templateType;
		this.templateValue = templateValue;
		this.isActive = isActive;
		this.updated = updated;
	}

	public CustomUiTemplateDTO(Long crisisId, Long nominalAttributeId,
			Integer templateType, String templateValue, Integer status,
			Boolean isActive, Date updated) {
		this.crisisID = crisisId;
		this.nominalAttributeID = nominalAttributeId;
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

	public Long getCrisisID() {
		return this.crisisID;
	}

	public void setCrisisID(long crisisID) {
		this.crisisID = crisisID;
	}

	public Long getNominalAttributeID() {
		return this.nominalAttributeID;
	}

	public void setNominalAttributeID(Long nominalAttributeId) {
		this.nominalAttributeID = nominalAttributeId;
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
		CustomUiTemplate entity = new CustomUiTemplate(this.getCrisisID(), this.getNominalAttributeID(),
				templateType, templateValue, status, isActive, updated);
		entity.setCustomUitemplateId(customUitemplateId);
		return entity;
	}
}
