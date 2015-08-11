/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade;

import java.util.List;

import javax.ejb.Local;

import qa.qcri.aidr.dbmanager.dto.NominalLabelDTO;


/**
 *
 * @author Imran
 */
@Local
public interface NominalLabelResourceFacade {
    
   public NominalLabelDTO addNominalLabel(NominalLabelDTO label); 
   
   public NominalLabelDTO editNominalLabel(NominalLabelDTO label);
   
   public void deleteNominalLabel(Long labelID);
   
   public NominalLabelDTO getNominalLabelByID(Long id);
   
   public List<NominalLabelDTO> getAllNominalLabel(); 
   
   void deleteNominalLabelDataByAttribute(Long attributeID);
}
