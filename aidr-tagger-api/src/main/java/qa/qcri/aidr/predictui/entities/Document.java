/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author Imran
 */
@Entity
@Table(name = "document")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Document.findAll", query = "SELECT d FROM Document d"),
    @NamedQuery(name = "Document.findByDocumentID", query = "SELECT d FROM Document d WHERE d.documentID = :documentID"),
    @NamedQuery(name = "Document.findByIsEvaluationSet", query = "SELECT d FROM Document d WHERE d.isEvaluationSet = :isEvaluationSet"),
    @NamedQuery(name = "Document.findByHasHumanLabels", query = "SELECT d FROM Document d WHERE d.hasHumanLabels = :hasHumanLabels"),
    @NamedQuery(name = "Document.findBySourceIP", query = "SELECT d FROM Document d WHERE d.sourceIP = :sourceIP"),
    @NamedQuery(name = "Document.findByValueAsTrainingSample", query = "SELECT d FROM Document d WHERE d.valueAsTrainingSample = :valueAsTrainingSample"),
    @NamedQuery(name = "Document.findByReceivedAt", query = "SELECT d FROM Document d WHERE d.receivedAt = :receivedAt"),
    @NamedQuery(name = "Document.findByLanguage", query = "SELECT d FROM Document d WHERE d.language = :language"),
    @NamedQuery(name = "Document.findByDoctype", query = "SELECT d FROM Document d WHERE d.doctype = :doctype")})
public class Document implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "documentID")
    private Long documentID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "isEvaluationSet")
    private boolean isEvaluationSet;
    @Basic(optional = false)
    @NotNull
    @Column(name = "hasHumanLabels")
    private boolean hasHumanLabels;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sourceIP")
    private int sourceIP;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valueAsTrainingSample")
    private double valueAsTrainingSample;
    @Basic(optional = false)
    @NotNull
    @Column(name = "receivedAt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date receivedAt;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "language")
    private String language;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "doctype")
    private String doctype;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "data")
    private String data;
    @Lob
    @Size(max = 65535)
    @Column(name = "wordFeatures")
    private String wordFeatures;
    @Lob
    @Size(max = 65535)
    @Column(name = "geoFeatures")
    private String geoFeatures;
    @JoinTable(name = "document_nominal_label", joinColumns = {
        @JoinColumn(name = "documentID", referencedColumnName = "documentID")}, inverseJoinColumns = {
        @JoinColumn(name = "nominalLabelID", referencedColumnName = "nominalLabelID")})
    @ManyToMany
    private Collection<NominalLabel> nominalLabelCollection;
    @JoinColumn(name = "crisisID", referencedColumnName = "crisisID")
    @ManyToOne(optional = false)
    private Crisis crisis;

    public Document() {
    }

    public Document(Long documentID) {
        this.documentID = documentID;
    }

    public Document(Long documentID, boolean isEvaluationSet, boolean hasHumanLabels, int sourceIP, double valueAsTrainingSample, Date receivedAt, String language, String doctype, String data) {
        this.documentID = documentID;
        this.isEvaluationSet = isEvaluationSet;
        this.hasHumanLabels = hasHumanLabels;
        this.sourceIP = sourceIP;
        this.valueAsTrainingSample = valueAsTrainingSample;
        this.receivedAt = receivedAt;
        this.language = language;
        this.doctype = doctype;
        this.data = data;
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

    public int getSourceIP() {
        return sourceIP;
    }

    public void setSourceIP(int sourceIP) {
        this.sourceIP = sourceIP;
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

    public Crisis getCrisis() {
        return crisis;
    }

    public void setCrisis(Crisis crisis) {
        this.crisis = crisis;
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
    
}
