package qa.qcri.aidr.manager.dto;

import qa.qcri.aidr.manager.hibernateEntities.AidrCollectionLog;

import java.io.Serializable;
import java.util.List;

public class CollectionLogDataResponse implements Serializable {

    public CollectionLogDataResponse() {}

    public CollectionLogDataResponse(List<AidrCollectionLog> data, Long total) {
        this.total = total;
        this.data = data;
    }

    private static final long serialVersionUID = 1L;
    private Long total;
    private List<AidrCollectionLog> data;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<AidrCollectionLog> getData() {
        return data;
    }

    public void setData(List<AidrCollectionLog> data) {
        this.data = data;
    }

}