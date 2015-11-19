package qa.qcri.aidr.manager.dto;

import java.util.ArrayList;
import java.util.List;

import qa.qcri.aidr.dbmanager.dto.CollectionDTO;

public class TaggerAllCrisesResponse {

    private List<TaggerCrisis> crisises;

    public List<TaggerCrisis> getCrisises() {
        return crisises;
    }
    
    /*
    public void setCrisises(List<TaggerCrisis> crisises) {
        this.crisises = crisises;
    }*/
    
    public void setCrisises(List<CollectionDTO> dtoList) throws Exception {
    	if (dtoList != null) {
    		List<TaggerCrisis> list = new ArrayList<TaggerCrisis>();
    		for (CollectionDTO dto: dtoList) {
    			TaggerCrisis c = new TaggerCrisis(dto);
    			list.add(c);
    		}
    		this.crisises = list;
    	}
    }
}
