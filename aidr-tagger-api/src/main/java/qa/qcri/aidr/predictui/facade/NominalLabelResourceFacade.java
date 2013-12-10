/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade;

import java.util.List;
import javax.ejb.Local;
import qa.qcri.aidr.predictui.dto.NominalLabelDTO;
import qa.qcri.aidr.predictui.entities.NominalLabel;


/**
 *
 * @author Imran
 */
@Local
public interface NominalLabelResourceFacade {
    
   public NominalLabel addNominalLabel(NominalLabelDTO label); 
   
   public NominalLabel editNominalLabel(NominalLabelDTO label);
   
   public void deleteNominalLabel(int labelID);
   
   public NominalLabel getNominalLabelByID(int id);
   
   public List<NominalLabel> getAllNominalLabel(); 
   
    
}
