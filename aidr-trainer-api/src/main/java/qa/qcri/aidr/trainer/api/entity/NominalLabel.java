package qa.qcri.aidr.trainer.api.entity;

import javax.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(catalog = "aidr_predict",name = "nominal_label")
public class NominalLabel implements Serializable {

    private static final long serialVersionUID = -5527566248002296042L;

    public NominalLabel() {
    }

    public NominalLabel(Long nominalLabelID) {
        this.nominalLabelID = nominalLabelID;
    }

    public NominalLabel(Long nominalLabelID, String nominalLabelCode, String name, String description) {
        this.nominalLabelID = nominalLabelID;
        this.nominalLabelCode = nominalLabelCode;
        this.name = name;
        this.description = description;
    }
    
    
    public Long getNominalLabelID() {
        return nominalLabelID;
    }

    public void setNominalLabelID(Long nominalLabelID) {
        this.nominalLabelID = nominalLabelID;
    }

    public String getNominalLabelCode() {
        return nominalLabelCode;
    }

    public void setNominalLabelCode(String nominalLabelCode) {
        this.nominalLabelCode = nominalLabelCode;
    }

    public Long getNominalAttributeID() {
        return nominalAttributeID;
    }

    public void setNominalAttributeID(Long nominalAttributeID) {
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

    public NominalAttribute getNominalAttribute() {
        return nominalAttribute;
    }

    public void setNominalAttribute(NominalAttribute nominalAttribute) {
        this.nominalAttribute = nominalAttribute;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }
    
    @Id
    @Column(name = "nominalLabelID")
    private Long nominalLabelID;

    @Column (name = "nominalLabelCode", nullable = false)
    private String nominalLabelCode;

    @Column (name = "nominalAttributeID", nullable = false)
    private Long nominalAttributeID;

    @Column (name = "name", nullable = false)
    private String name;

    @Column (name = "description", nullable = false)
    private String description;

    @Column(name = "sequence", nullable = false)
	private Integer sequence;

    @ManyToOne
    @JoinColumn(name="nominalAttributeID" ,nullable = false, insertable = false, updatable = false)
    private NominalAttribute nominalAttribute;

}
