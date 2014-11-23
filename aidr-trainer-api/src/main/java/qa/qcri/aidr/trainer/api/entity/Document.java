/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.trainer.api.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.Hibernate;


/**
 *
 * @author Imran
 */
@Entity
@Table(catalog = "aidr_predict", name = "document")
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
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

	/*
	@Column (name = "sourceIP", nullable = false)
    private Long sourceIP;
    */
	//private Integer sourceIP;

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

	@JoinTable(name = "document_nominal_label", joinColumns = {
			@JoinColumn(name = "documentID", referencedColumnName = "documentID")}, inverseJoinColumns = {
			@JoinColumn(name = "nominalLabelID", referencedColumnName = "nominalLabelID")})
	@ManyToMany
	private Collection<NominalLabel> nominalLabelCollection;

	public Document(){}

	public Document(Long documentID, boolean hasHumanLabels){
		this.documentID  = documentID;
		this.hasHumanLabels = hasHumanLabels;
	}
	
	public Document(qa.qcri.aidr.task.dto.DocumentDTO document) {
		this();
		if (document != null) {
			//Hibernate.initialize(document.getNominalLabelCollection());
			//Hibernate.initialize(document.getTaskAssignment());
			
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
			this.setTaskAssignment(TaskAssignment.toLocalTaskAssignment(document.getTaskAssignment()));
		} 
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
	
	@XmlTransient
    @JsonIgnore
    public Collection<NominalLabel> getNominalLabelCollection() {
        return nominalLabelCollection;
    }

    public void setNominalLabelCollection(Collection<NominalLabel> nominalLabelCollection) {
        this.nominalLabelCollection = nominalLabelCollection;
    }

    
	public static Document toLocalDocument(qa.qcri.aidr.task.dto.DocumentDTO document) {
		Document doc = new Document();
		if (document != null) {
			//Hibernate.initialize(document.getNominalLabelCollection());
			//Hibernate.initialize(document.getTaskAssignment());
			
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
			doc.setTaskAssignment(TaskAssignment.toLocalTaskAssignment(document.getTaskAssignment()));

			return doc;
		} 
		return null;
	}

	public static qa.qcri.aidr.task.entities.Document toTaskManagerDocument(Document document) {
		qa.qcri.aidr.task.entities.Document doc = new qa.qcri.aidr.task.entities.Document();
		if (document != null) {
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
			doc.setTaskAssignment(TaskAssignment.toTaskManagerTaskAssignment(document.getTaskAssignment()));

			return doc;
		} 
		return null;
	}
	
	public static List<Document> toLocalDocumentList(List<qa.qcri.aidr.task.dto.DocumentDTO> list) {
		if (list != null) {
			List<Document> docList = new ArrayList<Document>();
			for (qa.qcri.aidr.task.dto.DocumentDTO dto: list) {
				docList.add(toLocalDocument(dto));
			}
			return docList;
		}
		return null;
	}
	
	public static List<qa.qcri.aidr.task.entities.Document> toTaskManagerDocumentList(List<Document> list) {
		if (list != null) {
			List<qa.qcri.aidr.task.entities.Document> docList = new ArrayList<qa.qcri.aidr.task.entities.Document>();
			for (Document doc: list) {
				docList.add(toTaskManagerDocument(doc));
			}
			return docList;
		}
		return null;
	}
}






