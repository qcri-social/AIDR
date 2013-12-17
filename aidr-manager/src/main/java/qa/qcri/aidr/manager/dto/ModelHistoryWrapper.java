package qa.qcri.aidr.manager.dto;

import java.util.List;

/**
 *
 * @author Alex Mikhashchuk
 */
public class ModelHistoryWrapper {

    private List<ModelHistory> modelHistoryWrapper;
    
    private Long total;

    public List<ModelHistory> getModelHistoryWrapper() {
        return modelHistoryWrapper;
    }

    public void setModelHistoryWrapper(List<ModelHistory> modelHistoryWrapper) {
        this.modelHistoryWrapper = modelHistoryWrapper;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
    
}