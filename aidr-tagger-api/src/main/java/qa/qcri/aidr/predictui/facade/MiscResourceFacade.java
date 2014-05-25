/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade;

import qa.qcri.aidr.predictui.dto.ItemToLabelDTO;
import qa.qcri.aidr.predictui.dto.TrainingDataDTO;

import javax.ejb.Local;
import java.util.List;

/**
 *
 * @author Imran
 */
@Local
public interface MiscResourceFacade {
    
   public List<TrainingDataDTO> getTraningDataByCrisisAndAttribute(long crisisID,
                                                                   int modelFamilyID,
                                                                   int fromRecord,
                                                                   int limit,
                                                                   String sortColumn,
                                                                   String sortDirection);

   public ItemToLabelDTO getItemToLabel(long crisisID, int attributeID);
   
}
