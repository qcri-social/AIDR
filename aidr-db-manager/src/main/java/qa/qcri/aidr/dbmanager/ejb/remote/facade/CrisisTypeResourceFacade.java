
package qa.qcri.aidr.dbmanager.ejb.remote.facade;

import java.util.List;

import javax.ejb.Remote;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.CrisisTypeDTO;
import qa.qcri.aidr.dbmanager.ejb.local.facade.CoreDBServiceFacade;
import qa.qcri.aidr.dbmanager.entities.misc.CrisisType;

@Remote
public interface CrisisTypeResourceFacade extends CoreDBServiceFacade<CrisisType, Long>{
    
   public CrisisTypeDTO addCrisisType(CrisisTypeDTO crisisType) throws PropertyNotSetException; 
   public CrisisTypeDTO editCrisisType(CrisisTypeDTO crisisType) throws PropertyNotSetException; 
   public CrisisTypeDTO getCrisisTypeByID(Long id) throws PropertyNotSetException;
   public List<CrisisTypeDTO> getCrisisTypes() throws PropertyNotSetException;
   public void deleteCrisisType(Long id);
   
    
}
