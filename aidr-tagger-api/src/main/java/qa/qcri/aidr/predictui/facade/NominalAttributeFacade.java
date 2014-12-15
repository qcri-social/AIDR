/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade;

import java.util.List;
import javax.ejb.Local;
import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.CrisisAttributesDTO;
import qa.qcri.aidr.dbmanager.dto.NominalAttributeDTO;

/**
 *
 * @author Imran
 */
@Local
public interface NominalAttributeFacade {
    
    //client to fix: return type is chagned from NominalAttribute to boolean
    public boolean addAttribute(NominalAttributeDTO attribute) throws PropertyNotSetException;
    
    //client to fix: both return type and parameter is changed from NominalAttribute to NominalAttributeDTO
    public NominalAttributeDTO editAttribute(NominalAttributeDTO attribute) throws PropertyNotSetException;
    
    //client to fix: return type changed from void to boolean
    //client to fix: datatype of the parameter "attributeID" is changed from int to Long
    public boolean deleteAttribute(Long attributeID) throws PropertyNotSetException;
    
    //client to fix: return type changed from NominalAttribute to NominalAttributeDTO
    //client to fix: the parameter "attributeID" is changed from int to Long
    public NominalAttributeDTO getAttributeByID(Long attributeID) throws PropertyNotSetException;
    
    public List<NominalAttributeDTO> getAllAttributes() throws PropertyNotSetException;
    
    //1- client to fix: datatype of the parameter "crisisID" is changed from int to Long
    //2- client to fix: retrun type is changed from taggar-api local crisisAttributeDTO to db-manager crisisAttributeDTO. 
    public List<CrisisAttributesDTO> getAllAttributesExceptCrisis(Long crisisID) throws PropertyNotSetException;
    
    //client to fix: return type is changed from Integer to Long
    public Long isAttributeExists(String attributeCode) throws PropertyNotSetException;
    
}
