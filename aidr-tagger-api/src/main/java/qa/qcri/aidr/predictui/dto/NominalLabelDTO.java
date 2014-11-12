/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.dto;

import qa.qcri.aidr.predictui.entities.*;
import java.io.Serializable;
import java.util.Collection;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
//import org.codehaus.jackson.annotate.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author Imran
 */
@XmlRootElement
public class NominalLabelDTO implements Serializable {
    private Integer nominalLabelID;
    private String nominalLabelCode;
    private String name;
    private String description;
    private Collection<Document> documentCollection;
    private Collection<ModelNominalLabel> modelNominalLabelCollection;
    private Integer nominalAttributeID;
    private Integer sequence;

    public NominalLabelDTO() {
    }

    public NominalLabelDTO(Integer nominalLabelID) {
        this.nominalLabelID = nominalLabelID;
    }

    public NominalLabelDTO(Integer nominalLabelID, String nominalLabelCode, String name, String description) {
        this.nominalLabelID = nominalLabelID;
        this.nominalLabelCode = nominalLabelCode;
        this.name = name;
        this.description = description;
    }

    public NominalLabelDTO(Integer nominalLabelID, String nominalLabelCode, String name, String description, Integer sequence) {
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


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nominalLabelID != null ? nominalLabelID.hashCode() : 0);
        return hash;
    }

  

    @Override
    public String toString() {
        return "qa.qcri.aidr.predictui.entities.NominalLabel[ nominalLabelID=" + nominalLabelID + " ]";
    }

    /**
     * @return the nominalAttributeID
     */
    public Integer getNominalAttributeID() {
        return nominalAttributeID;
    }

    /**
     * @param nominalAttributeID the nominalAttributeID to set
     */
    public void setNominalAttributeID(Integer nominalAttributeID) {
        this.nominalAttributeID = nominalAttributeID;
    }
    
}
