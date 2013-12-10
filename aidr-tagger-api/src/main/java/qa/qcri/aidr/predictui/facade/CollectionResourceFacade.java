/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade;

import java.util.List;
import javax.ejb.Local;
import qa.qcri.aidr.predictui.entities.AidrCollection;
import qa.qcri.aidr.predictui.entities.Crisis;

/**
 *
 * @author Imran
 */
@Local
public interface CollectionResourceFacade {
    
   public List<AidrCollection> getAllRunningCollectionsByUserID(int userID); 
   
    
}
