package qa.qcri.aidr.predictui.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CrisisTypeDTO {

    private Integer crisisTypeID;

    private String name;
    
    private int numberOfCrisisAssociated;

    public CrisisTypeDTO() {
    }

    public CrisisTypeDTO(Integer crisisTypeID) {
        this.crisisTypeID = crisisTypeID;
    }

    public CrisisTypeDTO(Integer crisisTypeID, String name) {
        this.crisisTypeID = crisisTypeID;
        this.name = name;
    }

    public Integer getCrisisTypeID() {
        return crisisTypeID;
    }

    public void setCrisisTypeID(Integer crisisTypeID) {
        this.crisisTypeID = crisisTypeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the numberOfCrisisAssociated
     */
    public int getNumberOfCrisisAssociated() {
        return numberOfCrisisAssociated;
    }

    /**
     * @param numberOfCrisisAssociated the numberOfCrisisAssociated to set
     */
    public void setNumberOfCrisisAssociated(int numberOfCrisisAssociated) {
        this.numberOfCrisisAssociated = numberOfCrisisAssociated;
    }

}
