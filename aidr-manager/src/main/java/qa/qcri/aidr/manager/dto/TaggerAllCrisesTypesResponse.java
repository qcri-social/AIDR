package qa.qcri.aidr.manager.dto;

import java.util.ArrayList;
import java.util.List;

import qa.qcri.aidr.dbmanager.dto.CrisisTypeDTO;

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

    /*
    public void setCrisisTypes(List<TaggerCrisisType> crisisTypes) {
        this.crisisTypes = crisisTypes;
    }
    */
    
    public void setCrisisTypes(List<CrisisTypeDTO> dtoList) throws Exception {
    	if (dtoList != null) {
    		List<TaggerCrisisType> list= new ArrayList<TaggerCrisisType>();
    		for (CrisisTypeDTO d: dtoList) {
    			TaggerCrisisType t = new TaggerCrisisType(d);
    			list.add(t);
    		}
    		this.crisisTypes = list;
    	}
    }
    
    public TaggerAllCrisesTypesResponse() {}
    
}
