package qa.qcri.aidr.manager.dto;

public class TaggerCrisisType {

    private Long crisisTypeID;

    private String name;

    private int numberOfCrisisAssociated;
    
    public TaggerCrisisType() {
    }

    public TaggerCrisisType(Long crisisTypeID) {
        this.crisisTypeID = crisisTypeID;
    }

    public TaggerCrisisType(Long crisisTypeID, String name) {
        this.crisisTypeID = crisisTypeID;
        this.name = name;
    }

    public Long getCrisisTypeID() {
        return crisisTypeID;
    }

    public void setCrisisTypeID(Long crisisTypeID) {
        this.crisisTypeID = crisisTypeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfCrisisAssociated() {
        return numberOfCrisisAssociated;
    }

    public void setNumberOfCrisisAssociated(int numberOfCrisisAssociated) {
        this.numberOfCrisisAssociated = numberOfCrisisAssociated;
    }

    
}
