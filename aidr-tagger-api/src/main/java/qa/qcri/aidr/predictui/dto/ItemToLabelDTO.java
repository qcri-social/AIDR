/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.dto;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlRootElement;
import qa.qcri.aidr.predictui.entities.NominalAttribute;

/**
 *
 * @author Imran
 */
@XmlRootElement
public class ItemToLabelDTO {
    
    private BigInteger itemID;
    private String itemText;
    private NominalAttributeDTO attribute;

    /**
     * @return the itemText
     */
    public String getItemText() {
        return itemText;
    }

    /**
     * @param itemText the itemText to set
     */
    public void setItemText(String itemText) {
        this.itemText = itemText;
    }

    /**
     * @return the attribute
     */
    public NominalAttributeDTO getAttribute() {
        return attribute;
    }

    /**
     * @param attribute the attribute to set
     */
    public void setAttribute(NominalAttributeDTO attribute) {
        this.attribute = attribute;
    }

    /**
     * @return the itemID
     */
    public BigInteger getItemID() {
        return itemID;
    }

    /**
     * @param itemID the itemID to set
     */
    public void setItemID(BigInteger itemID) {
        this.itemID = itemID;
    }
    
    
    
    
}
