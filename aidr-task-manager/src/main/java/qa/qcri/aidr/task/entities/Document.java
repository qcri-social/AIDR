/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.task.entities;

import java.io.Serializable;
import java.util.Collection;
//import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
//import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
//import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import qa.qcri.aidr.task.entities.NominalLabel;
import qa.qcri.aidr.task.entities.TaskAssignment;

/**
 *
 * @author Koushik
 */


@Entity
@Table(catalog = "aidr_predict",name = "document")
@XmlRootElement
public class Document implements Serializable {

    private static final long serialVersionUID = -5527566248002296042L;

    @XmlElement
    @Id
    @Column(name = "documentID")
    private Long documentID;

    @XmlElement
    @Column (name = "hasHumanLabels", nullable = false)
    private boolean hasHumanLabels;

    @XmlElement
    @Column (name = "crisisID", nullable = false)
    private Long crisisID;

    @XmlElement
    @Column (name = "isEvaluationSet", nullable = false)
    private boolean isEvaluationSet;

    //@XmlElement
    //@Column (name = "sourceIP", nullable = false)
    //private Integer sourceIP;

    @XmlElement
    @Column (name = "valueAsTrainingSample", nullable = false)
    private Double valueAsTrainingSample;

    @XmlElement
    @Column (name = "receivedAt", nullable = false)
    private Date receivedAt;

    @XmlElement
    @Column (name = "language", nullable = false)
    private String language;

    @XmlElement
    @Column (name = "doctype", nullable = false)
    private String doctype;

    @XmlElement
    @Column (name = "data", nullable = false)
    private String data;

    @XmlElement
    @Column (name = "wordFeatures", nullable = false)
    private String wordFeatures;

    @XmlElement
    @Column (name = "geoFeatures", nullable = false)
    private String geoFeatures;

    @XmlElement
    @OneToOne(cascade=CascadeType.DETACH, fetch=FetchType.LAZY)
    @JoinColumn(name="documentID",insertable=true,
            updatable=true,nullable=true,unique=true)
    private TaskAssignment taskAssignment;
    
    
    //@XmlElement
    @JoinTable(name = "document_nominal_label", joinColumns = {
			@JoinColumn(name = "documentID", referencedColumnName = "documentID")}, inverseJoinColumns = {
			@JoinColumn(name = "nominalLabelID", referencedColumnName = "nominalLabelID")})
	@ManyToMany(cascade=CascadeType.MERGE, fetch=FetchType.LAZY)
    @JsonIgnore
    @XmlTransient
	private Collection<NominalLabel> nominalLabelCollection;
    
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

    /*
    public Integer getSourceIP() {
        return sourceIP;
    }

    public void setSourceIP(Integer sourceIP) {
        this.sourceIP = sourceIP;
    }
	*/
    
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
    
    
    @XmlTransient
    @JsonIgnore
    public Collection<NominalLabel> getNominalLabelCollection() {
        return nominalLabelCollection;
    }

    public void setNominalLabelCollection(Collection<NominalLabel> nominalLabelCollection) {
        this.nominalLabelCollection = nominalLabelCollection;
    }
    
}



