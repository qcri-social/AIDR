package qa.qcri.aidr.trainer.api.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 10/1/13
 * Time: 11:54 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(catalog = "aidr_predict",name = "document")
public class Document implements Serializable {

    private static final long serialVersionUID = -5527566248002296042L;

    @Id
    @Column(name = "documentID")
    private Long documentID;


    @Column (name = "hasHumanLabels", nullable = false)
    private boolean hasHumanLabels;

    @Column (name = "crisisID", nullable = false)
    private Long crisisID;

    @Column (name = "isEvaluationSet", nullable = false)
    private boolean isEvaluationSet;

    @Column (name = "sourceIP", nullable = false)
    private Long sourceIP;

    @Column (name = "valueAsTrainingSample", nullable = false)
    private Double valueAsTrainingSample;

    @Column (name = "receivedAt", nullable = false)
    private Date receivedAt;

    @Column (name = "language", nullable = false)
    private String language;

    @Column (name = "doctype", nullable = false)
    private String doctype;

    @Column (name = "data", nullable = false)
    private String data;

    @Column (name = "wordFeatures", nullable = false)
    private String wordFeatures;

    @Column (name = "geoFeatures", nullable = false)
    private String geoFeatures;

    @OneToOne(cascade=CascadeType.DETACH, fetch=FetchType.LAZY)
    @JoinColumn(name="documentID",insertable=true,
            updatable=true,nullable=true,unique=true)
    private TaskAssignment taskAssignment;


    public Document(){}

    public Document(Long documentID, boolean hasHumanLabels){
        this.documentID  = documentID;
        this.hasHumanLabels = hasHumanLabels;
    }

    public Long getDocumentID() {
        return documentID;
    }

    public void setDocumentID(Long documentID) {
        this.documentID = documentID;
    }

    public boolean isHasHumanLabels() {
        return hasHumanLabels;
    }

    public void setHasHumanLabels(boolean hasHumanLabels) {
        this.hasHumanLabels = hasHumanLabels;
    }

    public String getGeoFeatures() {
        return geoFeatures;
    }

    public void setGeoFeatures(String geoFeatures) {
        this.geoFeatures = geoFeatures;
    }

    public Long getCrisisID() {
        return crisisID;
    }

    public void setCrisisID(Long crisisID) {
        this.crisisID = crisisID;
    }

    public boolean isEvaluationSet() {
        return isEvaluationSet;
    }

    public void setEvaluationSet(boolean evaluationSet) {
        isEvaluationSet = evaluationSet;
    }

    public Long getSourceIP() {
        return sourceIP;
    }

    public void setSourceIP(Long sourceIP) {
        this.sourceIP = sourceIP;
    }

    public Double getValueAsTrainingSample() {
        return valueAsTrainingSample;
    }

    public void setValueAsTrainingSample(Double valueAsTrainingSample) {
        this.valueAsTrainingSample = valueAsTrainingSample;
    }

    public Date getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(Date receivedAt) {
        this.receivedAt = receivedAt;
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

    public String getWordFeatures() {
        return wordFeatures;
    }

    public void setWordFeatures(String wordFeatures) {
        this.wordFeatures = wordFeatures;
    }

    public TaskAssignment getTaskAssignment() {
        return taskAssignment;
    }

    public void setTaskAssignment(TaskAssignment taskAssignment) {
        this.taskAssignment = taskAssignment;
    }
}
