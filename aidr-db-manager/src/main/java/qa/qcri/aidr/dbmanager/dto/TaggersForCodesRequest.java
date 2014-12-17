package qa.qcri.aidr.dbmanager.dto;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class TaggersForCodesRequest {

    private List<String> codes;

    public TaggersForCodesRequest() {
    }

    public TaggersForCodesRequest(List<String> codes) {
        this.codes = codes;
    }

    public List<String> getCodes() {
        return codes;
    }

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }

}
