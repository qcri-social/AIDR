package qa.qcri.aidr.manager.dto;

import java.io.Serializable;
import java.util.List;

import qa.qcri.aidr.manager.persistence.entities.CollectionLog;

public class CollectionLogDataResponse implements Serializable {

    public CollectionLogDataResponse() {}

    public CollectionLogDataResponse(List<CollectionLog> data, Long total) {
        this.total = total;
        this.data = data;
    }

    private static final long serialVersionUID = 1L;
    private Long total;
    private List<CollectionLog> data;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<CollectionLog> getData() {
        return data;
    }

    public void setData(List<CollectionLog> data) {
        this.data = data;
    }

}