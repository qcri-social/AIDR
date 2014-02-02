package qa.qcri.aidr.manager.dto;

import java.util.List;

public class TaggersForCollectionsResponse {

    private List<TaggersForCodes> taggersForCodes;

    public List<TaggersForCodes> getTaggersForCodes() {
        return taggersForCodes;
    }

    public void setTaggersForCodes(List<TaggersForCodes> taggersForCodes) {
        this.taggersForCodes = taggersForCodes;
    }
}
