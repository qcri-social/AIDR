package qa.qcri.aidr.manager.dto;

public class TaggerCrisisType {

    private Integer crisisTypeId;

    private String name;

    private int numberOfCrisisAssociated;
    
    public TaggerCrisisType() {
    }

    public TaggerCrisisType(Integer crisisTypeId) {
        this.crisisTypeId = crisisTypeId;
    }

    public TaggerCrisisType(Integer crisisTypeId, String name) {
        this.crisisTypeId = crisisTypeId;
        this.name = name;
    }

    public Integer getCrisisTypeId() {
        return crisisTypeId;
    }

    public void setCrisisTypeId(Integer crisisTypeId) {
        this.crisisTypeId = crisisTypeId;
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
