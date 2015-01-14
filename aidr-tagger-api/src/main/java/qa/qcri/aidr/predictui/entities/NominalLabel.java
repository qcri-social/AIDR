/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 *
 * @author Imran
 */

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
public class NominalLabel implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @XmlElement private Integer nominalLabelID;
    
    
    @XmlElement private String nominalLabelCode;
    
    
    @XmlElement private String name;
    
 
    @XmlElement private String description;

  
    @XmlElement private Integer sequence;

    @Transient
    @JsonManagedReference
    private Collection<Document> documentCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nominalLabel")
    @JsonManagedReference
    private Collection<ModelNominalLabel> modelNominalLabelCollection;
    
 
    @JsonBackReference
    private NominalAttribute nominalAttribute;

    public NominalLabel() {
    }

    public NominalLabel(Integer nominalLabelID) {
        this.nominalLabelID = nominalLabelID;
    }

    public NominalLabel(Integer nominalLabelID, String nominalLabelCode, String name, String description) {
        this.nominalLabelID = nominalLabelID;
        this.nominalLabelCode = nominalLabelCode;
        this.name = name;
        this.description = description;
    }


    public NominalLabel(Integer nominalLabelID, String nominalLabelCode, String name, String description, Integer sequence) {
        this.nominalLabelID = nominalLabelID;
        this.nominalLabelCode = nominalLabelCode;
        this.name = name;
        this.description = description;
        this.sequence = sequence;
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
    public Collection<Document> getDocumentCollection() {
        return documentCollection;
    }

    public void setDocumentCollection(Collection<Document> documentCollection) {
        this.documentCollection = documentCollection;
    }
    
    @XmlTransient
    @JsonIgnore
    public Collection<ModelNominalLabel> getModelNominalLabelCollection() {
        return modelNominalLabelCollection;
    }

    public void setModelNominalLabelCollection(Collection<ModelNominalLabel> modelNominalLabelCollection) {
        this.modelNominalLabelCollection = modelNominalLabelCollection;
    }

    @XmlTransient
    @JsonIgnore
    public NominalAttribute getNominalAttribute() {
        return nominalAttribute;
    }

    public void setNominalAttribute(NominalAttribute nominalAttribute) {
        this.nominalAttribute = nominalAttribute;
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
        if ((this.nominalLabelID == null && other.nominalLabelID != null) || (this.nominalLabelID != null && !this.nominalLabelID.equals(other.nominalLabelID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "qa.qcri.aidr.predictui.entities.NominalLabel[ nominalLabelID=" + nominalLabelID + " ]";
    }
    
    /*
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
	*/
}
