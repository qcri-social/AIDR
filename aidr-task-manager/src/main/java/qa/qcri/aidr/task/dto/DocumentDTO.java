package qa.qcri.aidr.task.dto;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import qa.qcri.aidr.task.entities.TaskAssignment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
@XmlRootElement
public class DocumentDTO implements Serializable {

	   @XmlElement
	    private Long documentID;

	    @XmlElement
	    private boolean hasHumanLabels;

	    @XmlElement
	    private Long crisisID;

	    @XmlElement
	    private boolean isEvaluationSet;
	    
	    @XmlElement
	    private Double valueAsTrainingSample;

	    @XmlElement
	    private Date receivedAt;

	    @XmlElement
	    private String language;

	    @XmlElement
	    private String doctype;

	    @XmlElement
	    private String data;

	    @XmlElement
	    private String wordFeatures;

	    @XmlElement
	    private String geoFeatures;

	    @XmlElement
	    @JsonBackReference
	    private TaskAssignmentDTO taskAssignment;
	    
	    @JsonBackReference
	    @JsonIgnore
	    @XmlTransient
		private Collection<NominalLabelDTO> nominalLabelCollection;
	    
	    public DocumentDTO(){}

	    public DocumentDTO(Long documentID, boolean hasHumanLabels){
	        this.documentID  = documentID;
	        this.hasHumanLabels = hasHumanLabels;
	    }

	    public Long getDocumentID() {
	        return documentID;
	    }

	    public void setDocumentID(Long documentID) {
	        this.documentID = documentID;
	    }

	    public boolean getHasHumanLabels() {
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

	    public boolean getIsEvaluationSet() {
	        return isEvaluationSet;
	    }

	    public void setIsEvaluationSet(boolean evaluationSet) {
	        isEvaluationSet = evaluationSet;
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
	    
	    public TaskAssignmentDTO getTaskAssignment() {
	        return taskAssignment;
	    }

	    public void setTaskAssignmentDTO(TaskAssignmentDTO taskAssignment) {
	        this.taskAssignment = taskAssignment;
	    }
	    
	    
	    @XmlTransient
	    @JsonIgnore
	    public Collection<NominalLabelDTO> getNominalLabelCollection() {
	        return nominalLabelCollection;
	    }

	    public void setNominalLabelCollection(Collection<NominalLabelDTO> nominalLabelCollection) {
	        this.nominalLabelCollection = nominalLabelCollection;
	    }
	    
}
