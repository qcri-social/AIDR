/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade;


import javax.ejb.Local;

import qa.qcri.aidr.dbmanager.dto.HumanLabeledDocumentDTO;
import qa.qcri.aidr.dbmanager.dto.HumanLabeledDocumentList;
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
   
   public List<HumanLabeledDocumentDTO> getHumanLabeledDocumentsByCrisisID(Long crisisID, Integer count);
   public List<HumanLabeledDocumentDTO> getHumanLabeledDocumentsByCrisisCode(String crisisCode, Integer count);
   public List<HumanLabeledDocumentDTO> getHumanLabeledDocumentsByCrisisIDUserID(Long crisisID, Long userID, Integer count);
   public List<HumanLabeledDocumentDTO> getHumanLabeledDocumentsByCrisisIDUserName(Long crisisID, String userName, Integer count);

   public String downloadItems(HumanLabeledDocumentList dtoList, String queryString, String crisisCode, String userName, Integer count, String fileType, String contentType);
   
}
