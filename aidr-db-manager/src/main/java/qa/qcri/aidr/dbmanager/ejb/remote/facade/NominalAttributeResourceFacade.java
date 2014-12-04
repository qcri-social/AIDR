/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package qa.qcri.aidr.dbmanager.ejb.remote.facade;

import java.util.List;
import javax.ejb.Remote;
import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.NominalAttributeDTO;
import qa.qcri.aidr.dbmanager.ejb.local.facade.CoreDBServiceFacade;
import qa.qcri.aidr.dbmanager.entities.model.Model;
import qa.qcri.aidr.dbmanager.entities.model.NominalAttribute;

/**
 *
 * @author Imran
 */
@Remote
public interface NominalAttributeResourceFacade extends CoreDBServiceFacade<NominalAttribute, Long>{
    
    //client to fix: return type changed from NominalAttribute to boolean
    public boolean addAttribute(NominalAttributeDTO attribute) throws PropertyNotSetException;
    
    public NominalAttribute editAttribute(NominalAttribute attribute) throws PropertyNotSetException;
    public void deleteAttribute(int attributeID) throws PropertyNotSetException;
    public NominalAttribute getAttributeByID(int attributeID) throws PropertyNotSetException;
    public List<NominalAttribute> getAllAttributes() throws PropertyNotSetException;
    //public List<CrisisAttributesDTO> getAllAttributesExceptCrisis(int crisisID) throws PropertyNotSetException;
    public Integer isAttributeExists(String attributeCode) throws PropertyNotSetException;
    
}
