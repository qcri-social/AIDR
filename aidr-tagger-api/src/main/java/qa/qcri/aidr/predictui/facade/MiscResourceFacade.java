/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade;


import javax.ejb.Local;

import qa.qcri.aidr.dbmanager.dto.taggerapi.ItemToLabelDTO;
import qa.qcri.aidr.dbmanager.dto.taggerapi.TrainingDataDTO;

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
