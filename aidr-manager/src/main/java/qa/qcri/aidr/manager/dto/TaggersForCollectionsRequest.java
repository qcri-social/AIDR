package qa.qcri.aidr.manager.dto;

import java.util.List;

public class TaggersForCollectionsRequest {

    private List<String> codes;

    public TaggersForCollectionsRequest() {
    }

    public TaggersForCollectionsRequest(List<String> codes) {
        this.codes = codes;
    }

    public List<String> getCodes() {
        return codes;
    }

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }
}
