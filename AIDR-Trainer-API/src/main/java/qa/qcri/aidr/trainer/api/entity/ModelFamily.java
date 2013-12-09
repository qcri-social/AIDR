package qa.qcri.aidr.trainer.api.entity;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(catalog = "aidr_predict",name = "model_family")
public class ModelFamily implements Serializable {
    private static final long serialVersionUID = -5527566248002296042L;

    public Long getModelFamilyID() {
        return modelFamilyID;
    }

    public void setModelFamilyID(Long modelFamilyID) {
        this.modelFamilyID = modelFamilyID;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Long getNominalAttributeID() {
        return nominalAttributeID;
    }

    public void setNominalAttributeID(Long nominalAttributeID) {
        this.nominalAttributeID = nominalAttributeID;
    }

    public Long getCrisisID() {
        return crisisID;
    }

    public void setCrisisID(Long crisisID) {
        this.crisisID = crisisID;
    }

    public Crisis getCrisis() {
        return crisis;
    }

    public void setCrisis(Crisis crisis) {
        this.crisis = crisis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ModelFamily modelFamily = (ModelFamily) o;

        return modelFamilyID.equals(modelFamily.modelFamilyID);
    }

    public NominalAttribute getNominalAttribute() {
        return nominalAttribute;
    }

    public void setNominalAttribute(NominalAttribute nominalAttribute) {
        this.nominalAttribute = nominalAttribute;
    }


    @Id
    @Column(name = "modelFamilyID")
    private Long modelFamilyID;

    @Column (name = "crisisID", nullable = false)
    private Long crisisID;

    @Column (name = "nominalAttributeID", nullable = false)
    private Long nominalAttributeID;

    @Column (name = "isActive", nullable = false)
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name="crisisID" ,nullable = false, insertable = false, updatable = false)
    private Crisis crisis;

    @ManyToOne
    @JoinColumn(name="nominalAttributeID" ,nullable = false, insertable = false, updatable = false)
    private NominalAttribute nominalAttribute;

}
