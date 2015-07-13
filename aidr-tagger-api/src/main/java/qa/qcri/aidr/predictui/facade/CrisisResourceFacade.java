/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Local;
import javax.persistence.PersistenceException;

import qa.qcri.aidr.dbmanager.dto.CrisisDTO;

/**
 *
 * @author Imran
 */
@Local
public interface CrisisResourceFacade {
    
   public CrisisDTO addCrisis(CrisisDTO crisis) throws PersistenceException; 
   
   public CrisisDTO editCrisis(CrisisDTO crisis); 
   
   public CrisisDTO getCrisisByID(Long id);

   public CrisisDTO getCrisisByCode(String code);

   public Boolean isCrisisExists(String crisisCode);
   
   public List<CrisisDTO> getAllCrisis(); 
   
   public List<CrisisDTO> getAllCrisisByUserID(Long userID); 

   HashMap<String, Integer> countClassifiersByCrisisCodes(List<String> codes);
   
   public int deleteCrisis(Long crisisID);
}
