/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predict.dbentities;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import qa.qcri.aidr.predict.data.Document;
import qa.qcri.aidr.predict.dbentities.NominalLabel;
import qa.qcri.aidr.predict.dbentities.TaskAssignment;

import com.google.common.net.InetAddresses;

/**
 *
 * @author Koushik
 */

public class TaggerDocument extends Document implements Serializable {

    private static final long serialVersionUID = -5527566248002296042L;
    
    @XmlElement
    private boolean isEvaluationSet;

    
    @XmlElement
    private String doctype;
	
    
    //@XmlElement
    //private Long sourceIP;

    @XmlElement
    private boolean hasHumanLabels;

    @XmlElement
    private Date receivedAt;
    
    @XmlElement
    private String data;

    @XmlElement
    private String wordFeatures;

    @XmlElement
    private String geoFeatures;

    //@XmlElement
    //private TaskAssignment taskAssignment;
    
    @JsonIgnore
    @XmlTransient
	private Collection<NominalLabel> nominalLabelCollection;
    
    public TaggerDocument(){
    	super();
    }

    public TaggerDocument(Long documentID, boolean hasHumanLabels){
        super();
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

    public boolean getIsEvaluationSet() {
        return isEvaluationSet;
    }

    public void setIsEvaluationSet(boolean evaluationSet) {
        isEvaluationSet = evaluationSet;
    }
    
    /*
    @JsonProperty("sourceIP")
    public Long getSourceIPAsLong() {
        return sourceIP;
    }
    
    @XmlTransient
    @JsonIgnore
    public InetAddress getSourceIP() {
    	return InetAddresses.fromInteger(sourceIP.intValue());
    }
    
    @XmlTransient
    @JsonIgnore
    public void setSourceIP(InetAddress source) {
        if (source != null) {
        	sourceIP = new Long(InetAddresses.coerceToInteger(source));
        } else {
        	sourceIP = 0L;
        }
    }
    
    @JsonSetter("sourceIP")
    public void setSourceIP(Long sourceIP) {
        if (sourceIP != null) { 
        	this.sourceIP = sourceIP;
        } else {
        	this.sourceIP = 0L;
        }
    }
    
    @XmlTransient
    @JsonIgnore
    public void setSourceIP(Integer sourceIP) {
    	if (sourceIP != null) { 
        	this.sourceIP = new Long(sourceIP);
        } else {
        	this.sourceIP = 0L;
        }
    }
	*/
    
    public double getValueAsTrainingSample() {
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

    /*
    public TaskAssignment getTaskAssignment() {
        return taskAssignment;
    }

    public void setTaskAssignment(TaskAssignment taskAssignment) {
        this.taskAssignment = taskAssignment;
    }
    */
    
    @XmlTransient
    @JsonIgnore
    public Collection<NominalLabel> getNominalLabelCollection() {
        return nominalLabelCollection;
    }

    public void setNominalLabelCollection(Collection<NominalLabel> nominalLabelCollection) {
        this.nominalLabelCollection = nominalLabelCollection;
    }

	@Override
	public boolean isNovel() {
		return true;
	}
    
}



