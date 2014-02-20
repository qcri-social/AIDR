/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.entities;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
//import org.codehaus.jackson.annotate.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author Imran
 */
@Entity
@Table(name = "nominal_label")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NominalLabel.findAll", query = "SELECT n FROM NominalLabel n"),
    @NamedQuery(name = "NominalLabel.findByNominalLabelID", query = "SELECT n FROM NominalLabel n WHERE n.nominalLabelID = :nominalLabelID"),
    @NamedQuery(name = "NominalLabel.findByNominalLabelCode", query = "SELECT n FROM NominalLabel n WHERE n.nominalLabelCode = :nominalLabelCode"),
    @NamedQuery(name = "NominalLabel.findByName", query = "SELECT n FROM NominalLabel n WHERE n.name = :name"),
    @NamedQuery(name = "NominalLabel.findByDescription", query = "SELECT n FROM NominalLabel n WHERE n.description = :description"),
    @NamedQuery(name = "NominalLabel.findByNominalAttribute", query = "SELECT n FROM NominalLabel n WHERE n.nominalAttribute = :nominalAttribute")})
public class NominalLabel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "nominalLabelID")
    private Integer nominalLabelID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "nominalLabelCode")
    private String nominalLabelCode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 600)
    @Column(name = "description")
    private String description;
    @ManyToMany(mappedBy = "nominalLabelCollection")
    private Collection<Document> documentCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nominalLabel")
    private Collection<ModelNominalLabel> modelNominalLabelCollection;
    
    @JoinColumn(name = "nominalAttributeID", referencedColumnName = "nominalAttributeID")
    @ManyToOne(optional = false)
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
    
}
