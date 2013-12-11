package qa.qcri.aidr.manager.dto;

import java.util.List;

public class TaggerModelLabelsResponse {

    private List<TaggerModelNominalLabel> modelNominalLabelsDTO;

    public List<TaggerModelNominalLabel> getModelNominalLabelsDTO() {
        return modelNominalLabelsDTO;
    }

    public void setModelNominalLabelsDTO(List<TaggerModelNominalLabel> modelNominalLabelsDTO) {
        this.modelNominalLabelsDTO = modelNominalLabelsDTO;
    }
}
