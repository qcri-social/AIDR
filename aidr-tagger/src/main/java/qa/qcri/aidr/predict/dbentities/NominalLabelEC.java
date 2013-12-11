/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predict.dbentities;

/**
 *
 * @author Imran
 */
public class NominalLabelEC {
    
    private int nominalLabelID;
    private String nominalLabelCode;
    private NominalAttributeEC nominalAttribute;
    private String name;
    private String description;

    /**
     * @return the nominalLabelID
     */
    public int getNominalLabelID() {
        return nominalLabelID;
    }

    /**
     * @param nominalLabelID the nominalLabelID to set
     */
    public void setNominalLabelID(int nominalLabelID) {
        this.nominalLabelID = nominalLabelID;
    }

    /**
     * @return the nominalLabelCode
     */
    public String getNominalLabelCode() {
        return nominalLabelCode;
    }

    /**
     * @param nominalLabelCode the nominalLabelCode to set
     */
    public void setNominalLabelCode(String nominalLabelCode) {
        this.nominalLabelCode = nominalLabelCode;
    }

    

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the nominalAttribute
     */
    public NominalAttributeEC getNominalAttribute() {
        return nominalAttribute;
    }

    /**
     * @param nominalAttribute the nominalAttribute to set
     */
    public void setNominalAttribute(NominalAttributeEC nominalAttribute) {
        this.nominalAttribute = nominalAttribute;
    }
    
    
    
    
}
