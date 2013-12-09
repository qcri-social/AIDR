package qa.qcri.aidr.trainer.api.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(catalog = "aidr_predict",name = "nominal_label")
public class NominalLabel implements Serializable {

    private static final long serialVersionUID = -5527566248002296042L;


    public Long getNorminalLabelID() {
        return norminalLabelID;
    }

    public void setNorminalLabelID(Long norminalLabelID) {
        this.norminalLabelID = norminalLabelID;
    }

    public String getNorminalLabelCode() {
        return norminalLabelCode;
    }

    public void setNorminalLabelCode(String norminalLabelCode) {
        this.norminalLabelCode = norminalLabelCode;
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

    @Id
    @Column(name = "nominalLabelID")
    private Long norminalLabelID;

    @Column (name = "nominalLabelCode", nullable = false)
    private String norminalLabelCode;

    @Column (name = "nominalAttributeID", nullable = false)
    private Long nominalAttributeID;

    @Column (name = "name", nullable = false)
    private String name;

    @Column (name = "description", nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name="nominalAttributeID" ,nullable = false, insertable = false, updatable = false)
    private NominalAttribute nominalAttribute;

}
