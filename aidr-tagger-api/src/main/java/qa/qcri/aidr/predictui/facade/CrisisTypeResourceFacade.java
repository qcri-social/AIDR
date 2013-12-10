/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade;

import java.util.List;
import javax.ejb.Local;
import qa.qcri.aidr.predictui.dto.CrisisTypeDTO;
import qa.qcri.aidr.predictui.entities.CrisisType;

/**
 *
 * @author Imran
 */
@Local
public interface CrisisTypeResourceFacade {
    
   public CrisisType addCrisisType(CrisisType crisis); 
   public CrisisType editCrisisType(CrisisType crisis); 
   public CrisisType getCrisisTypeByID(int id);
   public List<CrisisTypeDTO> getCrisisTypes();
   public void deleteCrisisType(int id);
   
    
}
