/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;






//import org.codehaus.jackson.annotate.JsonBackReference;
//import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.Hibernate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author Imran
 */

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
public class Document implements Serializable {
    private static final long serialVersionUID = 1L;
 
    @XmlElement private Long documentID;
    
 
    @XmlElement private boolean isEvaluationSet;
    
 
    @XmlElement private boolean hasHumanLabels;
    
 
    @XmlElement private double valueAsTrainingSample;
    
  
    @XmlElement private Date receivedAt;
    
 
    @XmlElement private String language;
    
 
    @XmlElement private String doctype;
    
  
    @XmlElement private String data;
    
 
    @XmlElement private String wordFeatures;
    
  
    @XmlElement private String geoFeatures;
    
    @Transient
    @JsonBackReference
    private Collection<NominalLabel> nominalLabelCollection;
    
    // Added by Koushik instead of Crisis
 	@XmlElement private Long crisisID;
    
    public Document() {
    }

    public Document(Long documentID) {
        this.documentID = documentID;
    }
    
    // Change by Koushik - removed sourceIP, added crisisID
    public Document(Long documentID, Long crisisID, boolean isEvaluationSet, boolean hasHumanLabels, double valueAsTrainingSample, Date receivedAt, String language, String doctype, String data) {
        this.documentID = documentID;
        this.crisisID = crisisID;
        this.isEvaluationSet = isEvaluationSet;
        this.hasHumanLabels = hasHumanLabels;
        this.valueAsTrainingSample = valueAsTrainingSample;
        this.receivedAt = receivedAt;
        this.language = language;
        this.doctype = doctype;
        this.data = data;
    }
   
    public Document(qa.qcri.aidr.task.dto.DocumentDTO document) {
		this();
    	if (document != null) {
			Hibernate.initialize(document.getNominalLabelCollection());
			
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
	
			this.setNominalLabelCollection(toLocalNominalLabelCollection(document.getNominalLabelCollection()));
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

    public double getValueAsTrainingSample() {
        return valueAsTrainingSample;
    }

    public void setValueAsTrainingSample(double valueAsTrainingSample) {
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

    public String getGeoFeatures() {
        return geoFeatures;
    }

    public void setGeoFeatures(String geoFeatures) {
        this.geoFeatures = geoFeatures;
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
    public int hashCode() {
        int hash = 0;
        hash += (documentID != null ? documentID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Document)) {
            return false;
        }
        Document other = (Document) object;
        if ((this.documentID == null && other.documentID != null) || (this.documentID != null && !this.documentID.equals(other.documentID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "qa.qcri.aidr.predictui.entities.Document[ documentID=" + documentID + " ]";
    }
    
    public Long getCrisisID() {
		return crisisID;
	}

	public void setCrisisID(Long crisisID) {
		this.crisisID = crisisID;
	}

	
	public static Document toLocalDocument(qa.qcri.aidr.task.dto.DocumentDTO document) {
		Document doc = new Document();
		if (document != null) {
			Hibernate.initialize(document.getNominalLabelCollection());
			
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
	
			doc.setNominalLabelCollection(toLocalNominalLabelCollection(document.getNominalLabelCollection()));
			return doc;
		} 
		return null;
	}

	public static List<Document> toLocalDocumentList(List<qa.qcri.aidr.task.dto.DocumentDTO> documentList) {
		List<Document> docList = null;
		if (documentList != null) {
			docList = new ArrayList<Document>(documentList.size());
			for (qa.qcri.aidr.task.dto.DocumentDTO document: documentList) {
					docList.add(toLocalDocument(document));
			}
		} 
		return docList;
	}

	public static qa.qcri.aidr.task.dto.DocumentDTO toTaskManagerDocumentDTO(Document document) {
		qa.qcri.aidr.task.dto.DocumentDTO doc = new qa.qcri.aidr.task.dto.DocumentDTO();
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
	
			doc.setNominalLabelCollection(toTaskManagerNominalLabelDTOCollection(document.getNominalLabelCollection()));
			return doc;
		} 
		return null;
	}

	public static List<qa.qcri.aidr.task.dto.DocumentDTO> toTaskManagerDocumentDTOList(List<Document> documentList) {
		List<qa.qcri.aidr.task.dto.DocumentDTO> docList = null;
		if (documentList != null) {
			docList = new ArrayList<qa.qcri.aidr.task.dto.DocumentDTO>(documentList.size());
			for (Document document: documentList) {
					docList.add(toTaskManagerDocumentDTO(document));
			}
		}
		return docList;
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
	
			//doc.setNominalLabelCollection(toTaskManagerNominalLabelCollection(document.getNominalLabelCollection()));
			return doc;
		} 
		return null;
	}

	public static List<qa.qcri.aidr.task.entities.Document> toTaskManagerDocumentList(List<Document> documentList) {
		List<qa.qcri.aidr.task.entities.Document> docList = null;
		if (documentList != null) {
			docList = new ArrayList<qa.qcri.aidr.task.entities.Document>(documentList.size());
			for (Document document: documentList) {
					docList.add(toTaskManagerDocument(document));
			}
		}
		return docList;
	}
    
    
	public static Collection<NominalLabel> toLocalNominalLabelCollection(Collection<qa.qcri.aidr.task.dto.NominalLabelDTO> list) {
		if (list != null) {
			Collection<NominalLabel> nominalLabelList = new ArrayList<NominalLabel>();
			for (qa.qcri.aidr.task.dto.NominalLabelDTO t: list) {
				if (t != null) {
					NominalLabel nominalLabel  = new NominalLabel(t.getNominalLabelID(), t.getNominalLabelCode(), t.getName(), t.getDescription());
					nominalLabelList.add(nominalLabel);
				}
			}
			return nominalLabelList;
		}
		return null;
	}

	public static Collection<qa.qcri.aidr.task.dto.NominalLabelDTO> toTaskManagerNominalLabelDTOCollection(Collection<NominalLabel> list) {
		if (list != null) {
			Collection<qa.qcri.aidr.task.dto.NominalLabelDTO> nominalLabelDTOList = new ArrayList<qa.qcri.aidr.task.dto.NominalLabelDTO>();
			for (NominalLabel t: list) {
				if (t != null) {
					qa.qcri.aidr.task.dto.NominalLabelDTO nominalLabelDTO = new qa.qcri.aidr.task.dto.NominalLabelDTO(t.getNominalLabelID(), t.getNominalLabelCode(), t.getName(), t.getDescription());
					nominalLabelDTOList.add(nominalLabelDTO);
				}
			}
			return nominalLabelDTOList;
		}
		return null;
	}
	
	public static Collection<qa.qcri.aidr.task.entities.NominalLabel> toTaskManagerNominalLabelCollection(Collection<NominalLabel> list) {
		if (list != null) {
			Collection<qa.qcri.aidr.task.entities.NominalLabel> nominalLabelList = new ArrayList<qa.qcri.aidr.task.entities.NominalLabel>();
			for (NominalLabel t: list) {
				if (t != null) {
					qa.qcri.aidr.task.entities.NominalLabel nominalLabel = new qa.qcri.aidr.task.entities.NominalLabel(t.getNominalLabelID(), t.getNominalLabelCode(), t.getName(), t.getDescription());
					nominalLabelList.add(nominalLabel);
				}
			}
			return nominalLabelList;
		}
		return null;
	}
}
