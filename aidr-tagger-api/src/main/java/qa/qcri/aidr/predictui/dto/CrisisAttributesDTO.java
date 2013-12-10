/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.dto;

/**
 *
 * @author Imran
 */
public class CrisisAttributesDTO {
    
    private Integer nominalAttributeID;
    private String name;
    private String description;
    private String code;
    private Integer crisisID;
    private Integer userID;
    private Integer labelID;
    private String labelName;

    /**
     * @return the nominalAttributeID
     */
    public Integer getNominalAttributeID() {
        return nominalAttributeID;
    }

    /**
     * @param nominalAttributeID the nominalAttributeID to set
     */
    public void setNominalAttributeID(Integer nominalAttributeID) {
        this.nominalAttributeID = nominalAttributeID;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the crisisID
     */
    public Integer getCrisisID() {
        return crisisID;
    }

    /**
     * @param crisisID the crisisID to set
     */
    public void setCrisisID(Integer crisisID) {
        this.crisisID = crisisID;
    }

    /**
     * @return the userID
     */
    public Integer getUserID() {
        return userID;
    }

    /**
     * @param userID the userID to set
     */
    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    /**
     * @return the labelName
     */
    public String getLabelName() {
        return labelName;
    }

    /**
     * @param labelName the labelName to set
     */
    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    /**
     * @return the labelID
     */
    public Integer getLabelID() {
        return labelID;
    }

    /**
     * @param labelID the labelID to set
     */
    public void setLabelID(Integer labelID) {
        this.labelID = labelID;
    }
    
    
    
}
