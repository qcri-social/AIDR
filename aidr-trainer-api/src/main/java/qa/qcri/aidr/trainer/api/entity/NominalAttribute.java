package qa.qcri.aidr.trainer.api.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/11/13
 * Time: 11:58 AM
 * To change this template use File | Settings | File Templates.
 */


@Entity
@Table(catalog = "aidr_predict",name = "nominal_attribute")
public class NominalAttribute implements Serializable {

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NominalAttribute nominalAttribute = (NominalAttribute) o;

        return nominalAttributeID.equals(nominalAttribute.nominalAttributeID);
    }

    @Id
    @Column(name = "nominalAttributeID")
    private Long nominalAttributeID;

    @Column (name = "name", nullable = false)
    private String name;

    @Column (name = "description", nullable = false)
    private String description;

    @Column (name = "code", nullable = false)
    private String code;

    @Column(name = "userID", nullable = false)
    private Long userID;

    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumn(name="nominalAttributeID")
    private Set<ModelFamily> modelFamilySet;

    public Set<NominalLabel> getNominalLabelSet() {
        return nominalLabelSet;
    }

    public void setNominalLabelSet(Set<NominalLabel> nominalLabelSet) {
        this.nominalLabelSet = nominalLabelSet;
    }

    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumn(name="nominalAttributeID")
    private Set<NominalLabel> nominalLabelSet;

}
