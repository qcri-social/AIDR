
package qa.qcri.aidr.dbmanager.ejb.remote.facade;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.CrisisDTO;
import qa.qcri.aidr.dbmanager.ejb.local.facade.CoreDBServiceFacade;
import qa.qcri.aidr.dbmanager.entities.misc.Crisis;

@Remote
public interface CrisisResourceFacade extends CoreDBServiceFacade<Crisis, Long> {
    
   public CrisisDTO addCrisis(CrisisDTO crisis) throws PropertyNotSetException; 
   
   public CrisisDTO editCrisis(CrisisDTO crisis) throws PropertyNotSetException; 
   
   public List<CrisisDTO> findByCriteria(String columnName, Long value) throws PropertyNotSetException;
	
   public CrisisDTO getCrisisByID(Long id) throws PropertyNotSetException;
   
   public CrisisDTO getCrisisWithAllFieldsByID(Long id) throws PropertyNotSetException;

   public CrisisDTO getCrisisByCode(String code) throws PropertyNotSetException;

   public boolean isCrisisExists(String crisisCode) throws PropertyNotSetException;
   
   public List<CrisisDTO> getAllCrisis() throws PropertyNotSetException; 
   
   public List<CrisisDTO> getAllCrisisWithModelFamilies() throws PropertyNotSetException;
   
   public List<CrisisDTO> getAllCrisisByUserID(Long userID) throws PropertyNotSetException; 

   HashMap<String, Integer> countClassifiersByCrisisCodes(List<String> codes);
   
   // aidr-trainer-api specific
   public List<CrisisDTO> getAllCrisisWithModelFamilyNominalAttribute() throws PropertyNotSetException;
   public CrisisDTO getWithModelFamilyNominalAttributeByCrisisID(Long crisisID) throws PropertyNotSetException;
   public List<CrisisDTO> findActiveCrisis() throws PropertyNotSetException;
}
