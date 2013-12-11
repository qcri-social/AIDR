package qa.qcri.aidr.manager.dto;

import java.util.List;

public class TaggerCrisisAttributesResponse {

    private List<TaggerCrisesAttribute> crisisAttributes;

    public List<TaggerCrisesAttribute> getCrisisAttributes() {
        return crisisAttributes;
    }

    public void setCrisisAttributes(List<TaggerCrisesAttribute> crisisAttributes) {
        this.crisisAttributes = crisisAttributes;
    }
}
