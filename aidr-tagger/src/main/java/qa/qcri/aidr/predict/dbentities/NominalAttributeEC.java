/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predict.dbentities;

import java.util.ArrayList;

/**
 *
 * @author Imran
 */
public class NominalAttributeEC {
    
    private int nominalAttributeID;
    private String name;
    private String description;
    private String code;
    private int userID;
    ArrayList<NominalLabelEC> labels = new ArrayList<>();

    /**
     * @return the nominalAttributeID
     */
    public int getNominalAttributeID() {
        return nominalAttributeID;
    }

    /**
     * @param nominalAttributeID the nominalAttributeID to set
     */
    public void setNominalAttributeID(int nominalAttributeID) {
        this.nominalAttributeID = nominalAttributeID;
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
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the userID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * @param userID the userID to set
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }
    
    public NominalLabelEC[] getNominalLabels() {
        return labels.toArray(new NominalLabelEC[labels.size()]);
    }
    
    public NominalLabelEC getNominalLabel(String nominalLabelCode) {
        for (NominalLabelEC label : labels) {
            if (label.getNominalLabelCode().equals(nominalLabelCode))
                return label;
        }
        return null;
    }

    public NominalLabelEC getNominalLabel(int nominalLabelID) {
        for (NominalLabelEC label : labels) {
            if (label.getNominalLabelID() == nominalLabelID)
                return label;
        }
        return null;
    }
    
    public void addNominalLabel(NominalLabelEC label) {
        if (!labels.contains(label))
            labels.add(label);
    }
    
    public void resetNominalLabels() {
       labels = new ArrayList<NominalLabelEC>();
    }
}
