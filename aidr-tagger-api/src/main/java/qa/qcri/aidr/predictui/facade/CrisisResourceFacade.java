/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Local;
import javax.persistence.PersistenceException;

import qa.qcri.aidr.dbmanager.dto.CollectionDTO;

/**
 *
 * @author Imran
 */
@Local
public interface CrisisResourceFacade {
    
   public CollectionDTO addCrisis(CollectionDTO crisis) throws PersistenceException; 
   
   public CollectionDTO editCrisis(CollectionDTO crisis); 
   
   public CollectionDTO getCrisisByID(Long id);

   public CollectionDTO getCrisisByCode(String code);

   public Boolean isCrisisExists(String crisisCode);
   
   public List<CollectionDTO> getAllCrisis(); 
   
   public List<CollectionDTO> getAllCrisisByUserID(Long userID); 

   HashMap<String, Integer> countClassifiersByCrisisCodes(List<String> codes);
   
   public int deleteCrisis(Long crisisID);
}
