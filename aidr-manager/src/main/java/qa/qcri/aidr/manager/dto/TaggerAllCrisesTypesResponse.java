package qa.qcri.aidr.manager.dto;

import java.util.List;

public class TaggerAllCrisesTypesResponse {

    private String message;

    private List<TaggerCrisisType> crisisTypes;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<TaggerCrisisType> getCrisisTypes() {
        return crisisTypes;
    }

    public void setCrisisTypes(List<TaggerCrisisType> crisisTypes) {
        this.crisisTypes = crisisTypes;
    }
}
