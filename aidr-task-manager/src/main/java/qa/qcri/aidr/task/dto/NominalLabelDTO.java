package qa.qcri.aidr.task.dto;

import java.io.Serializable;
import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import qa.qcri.aidr.task.entities.Document;
import qa.qcri.aidr.task.entities.NominalLabel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
public class NominalLabelDTO  implements Serializable {
    @XmlElement
    private Integer nominalLabelID;

    @XmlElement
    private String nominalLabelCode;

    @XmlElement
    private String name;

    @XmlElement
    private String description;

    @XmlElement
    private Integer sequence;

    @JsonManagedReference
    @JsonIgnore
    private transient Collection<DocumentDTO> documentCollection;
    
    public NominalLabelDTO() {
    }

    public NominalLabelDTO(Integer nominalLabelID) {
        this.nominalLabelID = nominalLabelID;
    }

    public NominalLabelDTO(Integer nominalLabelID, String nominalLabelCode, String name, String description, Integer sequence) {
        this.nominalLabelID = nominalLabelID;
        this.nominalLabelCode = nominalLabelCode;
        this.name = name;
        this.description = description;
        this.sequence = sequence;
    }

    public NominalLabelDTO(Integer nominalLabelID, String nominalLabelCode, String name, String description) {
        this.nominalLabelID = nominalLabelID;
        this.nominalLabelCode = nominalLabelCode;
        this.name = name;
        this.description = description;
    }

    public Integer getNominalLabelID() {
        return nominalLabelID;
    }

    public void setNominalLabelID(Integer nominalLabelID) {
        this.nominalLabelID = nominalLabelID;
    }

    public String getNominalLabelCode() {
        return nominalLabelCode;
    }

    public void setNominalLabelCode(String nominalLabelCode) {
        this.nominalLabelCode = nominalLabelCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<DocumentDTO> getDocumentCollection() {
        return documentCollection;
    }

    public void setDocumentCollection(Collection<DocumentDTO> documentCollection) {
        this.documentCollection = documentCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nominalLabelID != null ? nominalLabelID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NominalLabel)) {
            return false;
        }
        NominalLabel other = (NominalLabel) object;
        if ((this.nominalLabelID == null && other.getNominalLabelID() != null) || (this.nominalLabelID != null && !this.nominalLabelID.equals(other.getNominalLabelID()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "qa.qcri.aidr.predictui.entities.NominalLabel[ nominalLabelID=" + nominalLabelID + " ]";
    }

    
}
