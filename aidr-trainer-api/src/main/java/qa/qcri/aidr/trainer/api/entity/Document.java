/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.trainer.api.entity;

import java.io.Serializable;

import java.util.Date;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;



/**
 *
 * @author Koushik
 */
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
 public class Document implements Serializable {

	private static final long serialVersionUID = -5527566248002296042L;

	@XmlElement private Long documentID;

	@XmlElement private boolean hasHumanLabels;

	@XmlElement private Long crisisID;

	@XmlElement private boolean isEvaluationSet;

	@XmlElement private Double valueAsTrainingSample;

	@XmlElement private Date receivedAt;

	@XmlElement private String language;

	@XmlElement private String doctype;

	@XmlElement private String data;

	@XmlElement private String wordFeatures;

	@XmlElement private String geoFeatures;

	@XmlElement 
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
	
	public boolean getIsEvaluationSet() {
        return isEvaluationSet;
    }

    public void setIsEvaluationSet(boolean isEvaluationSet) {
        this.isEvaluationSet = isEvaluationSet;
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






