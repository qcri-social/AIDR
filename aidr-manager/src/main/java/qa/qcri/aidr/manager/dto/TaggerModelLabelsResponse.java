package qa.qcri.aidr.manager.dto;

import java.util.ArrayList;
import java.util.List;

import qa.qcri.aidr.dbmanager.dto.ModelNominalLabelDTO;

public class TaggerModelLabelsResponse {

    private List<TaggerModelNominalLabel> modelNominalLabelsDTO;

    public List<TaggerModelNominalLabel> getModelNominalLabelsDTO() {
        return modelNominalLabelsDTO;
    }
    
    /*
    public void setModelNominalLabelsDTO(List<TaggerModelNominalLabel> modelNominalLabelsDTO) {
        this.modelNominalLabelsDTO = modelNominalLabelsDTO;
    }*/
    
    public void setModelNominalLabelsDTO(List<ModelNominalLabelDTO> dtoList) throws Exception {
    	if (dtoList != null) {
    		List<TaggerModelNominalLabel> list = new ArrayList<TaggerModelNominalLabel>();
    		for (ModelNominalLabelDTO dto: dtoList) {
    			TaggerModelNominalLabel t = new TaggerModelNominalLabel(dto);
    			list.add(t);
    		}
    		this.modelNominalLabelsDTO = list;
    	}
    }
}
