/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.dto;

import java.io.Serializable;
import java.util.Collection;
import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author Imran, Koushik
 */
@XmlRootElement
public class TaggerAPINominalAttributeDTO implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -8044878750148294318L;
	private Integer nominalAttributeID;
    private String name;
    private String description;
    private String code;
    private Collection<TaggerAPINominalLabelDTO> nominalLabelCollection;

    public TaggerAPINominalAttributeDTO() {
    }

    public TaggerAPINominalAttributeDTO(Integer nominalAttributeID) {
        this.nominalAttributeID = nominalAttributeID;
    }

    public TaggerAPINominalAttributeDTO(Integer nominalAttributeID, String name, String description, String code) {
        this.nominalAttributeID = nominalAttributeID;
        this.name = name;
        this.description = description;
        this.code = code;
    }

    public Integer getNominalAttributeID() {
        return nominalAttributeID;
    }

    public void setNominalAttributeID(Integer nominalAttributeID) {
        this.nominalAttributeID = nominalAttributeID;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    //@XmlTransient
    @JsonIgnore
    public Collection<TaggerAPINominalLabelDTO> getNominalLabelCollection() {
        return nominalLabelCollection;
    }

    public void setNominalLabelCollection(Collection<TaggerAPINominalLabelDTO> nominalLabelCollection) {
        this.nominalLabelCollection = nominalLabelCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nominalAttributeID != null ? nominalAttributeID.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "qa.qcri.aidr.predictui.entities.NominalAttribute[ nominalAttributeID=" + nominalAttributeID + " ]";
    }
    
}
