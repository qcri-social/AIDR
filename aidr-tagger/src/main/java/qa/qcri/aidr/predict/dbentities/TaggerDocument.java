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

	@JsonIgnore
	@XmlTransient
	private Collection<NominalLabel> nominalLabelCollection;

	public TaggerDocument(){
		super();
	}

	public TaggerDocument(qa.qcri.aidr.task.dto.DocumentDTO document) {
		this();
		if (document != null) {
			//Hibernate.initialize(document.getNominalLabelCollection());
			
			this.setDocumentID(document.getDocumentID());
			this.setCrisisID(document.getCrisisID());
			this.setDoctype(document.getDoctype());
			this.setData(document.getData());
			this.setIsEvaluationSet(document.getIsEvaluationSet());
			this.setGeoFeatures(document.getGeoFeatures());
			this.setLanguage(document.getLanguage());
			this.setHasHumanLabels(document.getHasHumanLabels());

			this.setReceivedAt(document.getReceivedAt());
			this.setWordFeatures(document.getWordFeatures());
			this.setValueAsTrainingSample(document.getValueAsTrainingSample());

			this.setNominalLabelCollection(NominalLabel.toNominalLabelCollection(document.getNominalLabelCollection()));
		}
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

	public static TaggerDocument toTaggerDocument(qa.qcri.aidr.task.dto.DocumentDTO document) {
		TaggerDocument doc = new TaggerDocument();
		if (document != null) {
			//Hibernate.initialize(document.getNominalLabelCollection());
			
			doc.setDocumentID(document.getDocumentID());
			doc.setCrisisID(document.getCrisisID());
			doc.setDoctype(document.getDoctype());
			doc.setData(document.getData());
			doc.setIsEvaluationSet(document.getIsEvaluationSet());
			doc.setGeoFeatures(document.getGeoFeatures());
			doc.setLanguage(document.getLanguage());
			doc.setHasHumanLabels(document.getHasHumanLabels());

			doc.setReceivedAt(document.getReceivedAt());
			doc.setWordFeatures(document.getWordFeatures());
			doc.setValueAsTrainingSample(document.getValueAsTrainingSample());

			doc.setNominalLabelCollection(NominalLabel.toNominalLabelCollection(document.getNominalLabelCollection()));
			return doc;
		} 
		return null;
	}

	public static qa.qcri.aidr.task.entities.Document toTaskManagerDocument(TaggerDocument document) {
		qa.qcri.aidr.task.entities.Document doc = new qa.qcri.aidr.task.entities.Document();
		if (document != null) {
			doc.setDocumentID(document.getDocumentID());
			doc.setCrisisID(document.getCrisisID());
			doc.setDoctype(document.getDoctype());
			doc.setData(document.getData());
			doc.setIsEvaluationSet(document.getIsEvaluationSet());
			doc.setGeoFeatures(document.getGeoFeatures());
			doc.setLanguage(document.getLanguage());
			doc.setHasHumanLabels(document.isHasHumanLabels());

			doc.setReceivedAt(document.getReceivedAt());
			doc.setWordFeatures(document.getWordFeatures());
			doc.setValueAsTrainingSample(document.getValueAsTrainingSample());

			doc.setNominalLabelCollection(NominalLabel.toTaskManagerNominalLabelCollection(document.getNominalLabelCollection()));
			return doc;
		} 
		return null;
	}

}



