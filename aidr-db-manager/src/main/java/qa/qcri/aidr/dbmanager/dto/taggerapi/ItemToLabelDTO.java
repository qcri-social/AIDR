/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.dbmanager.dto.taggerapi;

import java.io.Serializable;
import java.math.BigInteger;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import qa.qcri.aidr.dbmanager.dto.NominalAttributeDTO;


/**
 *
 * @author Imran, koushik
 */
@XmlRootElement
public class ItemToLabelDTO implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -4435113285927401893L;
	
	@XmlElement private BigInteger itemID;
    
	@XmlElement private String itemText;
    
	@XmlElement private NominalAttributeDTO attribute;

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
