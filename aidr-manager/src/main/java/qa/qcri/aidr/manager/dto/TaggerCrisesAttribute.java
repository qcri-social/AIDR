package qa.qcri.aidr.manager.dto;

import qa.qcri.aidr.dbmanager.dto.CrisisAttributesDTO;

public class TaggerCrisesAttribute {

    private String code;

    private String description;

    private String labelName;

    private Integer labelID;

    private String name;

    private Integer nominalAttributeID;

    private Integer userID;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNominalAttributeID() {
        return nominalAttributeID;
    }

    public void setNominalAttributeID(Integer nominalAttributeID) {
        this.nominalAttributeID = nominalAttributeID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public Integer getLabelID() {
        return labelID;
    }

    public void setLabelID(Integer labelID) {
        this.labelID = labelID;
    }
    
    public TaggerCrisesAttribute() {}
    
    public TaggerCrisesAttribute(CrisisAttributesDTO dto) {
    	if (dto != null) {
    		this.setCode(dto.getCode());
    		this.setDescription(dto.getDescription());
    		this.setLabelID(dto.getLabelID());
    		this.setLabelName(dto.getLabelName());
    		this.setName(dto.getName());
    		this.setNominalAttributeID(dto.getNominalAttributeID());
    		this.setUserID(dto.getUserID());
    	}
    }
    
    public CrisisAttributesDTO toDTO() {
    	CrisisAttributesDTO dto = new CrisisAttributesDTO();
    	dto.setCode(this.getCode());
    	dto.setDescription(this.getDescription());
    	dto.setLabelID(this.getLabelID());
    	dto.setLabelName(this.getLabelName());
    	dto.setName(this.getName());
    	dto.setNominalAttributeID(this.getNominalAttributeID());
    	dto.setUserID(this.getUserID());
    	
    	return dto;
    }
}
