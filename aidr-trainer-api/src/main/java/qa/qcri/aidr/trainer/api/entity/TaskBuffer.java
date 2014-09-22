package qa.qcri.aidr.trainer.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;


@Entity
@Table (catalog = "aidr_predict",name = "task_buffer")
@JsonIgnoreProperties(ignoreUnknown=true)
public class TaskBuffer implements Serializable {

    private static final long serialVersionUID = -5527566248002296042L;

    @Id
    @Column (name = "documentID")
    private Long documentID;

    @Column (name = "crisisID", nullable = false)
    private Long crisisID;

    @Column (name = "attributeInfo", nullable = false)
    private String attributeInfo;

    @Column (name = "language", nullable = false)
    private String language;

    @Column (name = "doctype", nullable = false)
    private String doctype;

    @Column (name = "data", nullable = false)
    private String data;

    @Column (name = "valueAsTrainingSample", nullable = false)
    private Double valueAsTrainingSample;

    @Column (name = "assignedCount", nullable = false)
    private Integer assignedCount;

    public TaskBuffer(){}

    public Long getDocumentID() {
        return documentID;
    }

    public void setDocumentID(Long documentID) {
        this.documentID = documentID;
    }

    public Long getCrisisID() {
        return crisisID;
    }

    public void setCrisisID(Long crisisID) {
        this.crisisID = crisisID;
    }

    public String getAttributeInfo() {
        return attributeInfo;
    }

    public void setAttributeInfo(String attributeInfo) {
        this.attributeInfo = attributeInfo;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDoctype() {
        return doctype;
    }

    public void setDoctype(String doctype) {
        this.doctype = doctype;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Double getValueAsTrainingSample() {
        return valueAsTrainingSample;
    }

    public void setValueAsTrainingSample(Double valueAsTrainingSample) {
        this.valueAsTrainingSample = valueAsTrainingSample;
    }

    public Integer getAssignedCount() {
        return assignedCount;
    }

    public void setAssignedCount(Integer assignedCount) {
        this.assignedCount = assignedCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskBuffer taskBuffer = (TaskBuffer) o;

        return documentID.equals(taskBuffer.documentID);
    }

}
