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
    
    public NominalAttributeDTO editAttribute(NominalAttributeDTO attribute) throws PropertyNotSetException;
    
    //client to fix: return type changed from void to boolean. And, attributeID parameter datatype is changed from int to Long
    public boolean deleteAttribute(Long attributeID) throws PropertyNotSetException;
    public NominalAttributeDTO getAttributeByID(Long attributeID) throws PropertyNotSetException;
    public List<NominalAttributeDTO> getAllAttributes() throws PropertyNotSetException;
    
    //TODO
    //public List<CrisisAttributesDTO> getAllAttributesExceptCrisis(int crisisID) throws PropertyNotSetException;
    
    //client to fix: return type changed from Integer to Long
    public Long isAttributeExists(String attributeCode) throws PropertyNotSetException;
    
}
