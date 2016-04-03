/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade;

import java.util.List;

import javax.ejb.Local;

import qa.qcri.aidr.common.wrapper.CollectionBriefInfo;
import qa.qcri.aidr.dbmanager.entities.misc.Collection;

/**
 *
 * @author Imran
 */
@Local
public interface CollectionResourceFacade {
    
   public List<Collection> getAllRunningCollectionsByUserID(int userID); 
   
   public List<CollectionBriefInfo> getCrisisForNominalAttributeById(Integer attributeID,Integer crisis_type,String lang_filters);
}
