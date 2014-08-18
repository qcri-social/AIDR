/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predict.dbentities;

import java.io.Serializable;
import java.util.Collection;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;


//import org.codehaus.jackson.annotate.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author Koushik
 */
public class NominalLabel implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement
    private Integer nominalLabelID;

    @XmlElement
    private String nominalLabelCode;

    @XmlElement
    private String name;

    @XmlElement
    private String description;

    @JsonIgnore
    private transient Collection<TaggerDocument> documentCollection;

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

    @XmlTransient
    @JsonIgnore
    public Collection<TaggerDocument> getDocumentCollection() {
        return documentCollection;
    }

    public void setDocumentCollection(Collection<TaggerDocument> documentCollection) {
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
        if ((this.nominalLabelID == null && other.nominalLabelID != null) || (this.nominalLabelID != null && !this.nominalLabelID.equals(other.nominalLabelID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "qa.qcri.aidr.predict.dbentities.NominalLabel[ nominalLabelID=" + nominalLabelID + " ]";
    }
    
}
