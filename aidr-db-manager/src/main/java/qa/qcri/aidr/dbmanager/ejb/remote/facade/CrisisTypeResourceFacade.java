
package qa.qcri.aidr.dbmanager.ejb.remote.facade;

import java.util.List;

import javax.ejb.Remote;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.CrisisDTO;
import qa.qcri.aidr.dbmanager.dto.CrisisTypeDTO;
import qa.qcri.aidr.dbmanager.ejb.local.facade.CoreDBServiceFacade;
import qa.qcri.aidr.dbmanager.entities.misc.CrisisType;

@Remote
public interface CrisisTypeResourceFacade extends CoreDBServiceFacade<CrisisType, Long>{

	public CrisisTypeDTO addCrisisType(CrisisTypeDTO crisisType); 

	public CrisisTypeDTO editCrisisType(CrisisTypeDTO crisisType) throws PropertyNotSetException; 

	public List<CrisisTypeDTO> findByCriteria(String columnName, Object value) throws PropertyNotSetException;

	public CrisisTypeDTO findCrisisTypeByID(Long id) throws PropertyNotSetException;

	public boolean isCrisisTypeExists(Long id) throws PropertyNotSetException;

	public List<CrisisTypeDTO> getAllCrisisTypes() throws PropertyNotSetException;
	
	public List<CrisisDTO> getAllCrisisForCrisisTypeID(Long id) throws PropertyNotSetException;
	
	public Integer deleteCrisisType(Long id);
}
