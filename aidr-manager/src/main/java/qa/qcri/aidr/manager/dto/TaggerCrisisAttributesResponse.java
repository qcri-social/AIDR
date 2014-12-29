package qa.qcri.aidr.manager.dto;

import java.util.ArrayList;
import java.util.List;

import qa.qcri.aidr.dbmanager.dto.CrisisAttributesDTO;

public class TaggerCrisisAttributesResponse {

    private List<TaggerCrisesAttribute> crisisAttributes;

    public List<TaggerCrisesAttribute> getCrisisAttributes() {
        return crisisAttributes;
    }
    
    /*
    public void setCrisisAttributes(List<TaggerCrisesAttribute> crisisAttributes) {
        this.crisisAttributes = crisisAttributes;
    }
    */
    
    public void setCrisisAttributes(List<CrisisAttributesDTO> dtoList) {
    	if (dtoList != null) {
    		List<TaggerCrisesAttribute> list = new ArrayList<TaggerCrisesAttribute>();
    		for (CrisisAttributesDTO dto: dtoList) {
    			TaggerCrisesAttribute attribute = new TaggerCrisesAttribute(dto);
    			list.add(attribute);
    		}
    		this.crisisAttributes = list;
    	}
    }
}
