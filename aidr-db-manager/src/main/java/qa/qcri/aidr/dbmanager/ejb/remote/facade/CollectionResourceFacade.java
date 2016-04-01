
package qa.qcri.aidr.dbmanager.ejb.remote.facade;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.common.wrapper.CollectionBriefInfo;
import qa.qcri.aidr.dbmanager.dto.CollectionDTO;
import qa.qcri.aidr.dbmanager.ejb.local.facade.CoreDBServiceFacade;
import qa.qcri.aidr.dbmanager.entities.misc.Collection;

@Remote
public interface CollectionResourceFacade extends CoreDBServiceFacade<Collection, Long> {

	public Integer deleteCrisis(CollectionDTO crisis) throws PropertyNotSetException;
	
	public CollectionDTO addCrisis(CollectionDTO crisis); 

	public CollectionDTO editCrisis(CollectionDTO crisis) throws PropertyNotSetException; 

	public List<CollectionDTO> findByCriteria(String columnName, Object value) throws PropertyNotSetException;

	public CollectionDTO findCrisisByID(Long id) throws PropertyNotSetException;

	public CollectionDTO getCrisisWithAllFieldsByID(Long id) throws PropertyNotSetException;

	public CollectionDTO getCrisisByCode(String code) throws PropertyNotSetException;

	public boolean isCrisisExists(String crisisCode) throws PropertyNotSetException;

	public List<CollectionDTO> getAllCrisis() throws PropertyNotSetException; 

	public List<CollectionDTO> getAllCrisisWithModelFamilies() throws PropertyNotSetException;

	public List<CollectionDTO> getAllCrisisByUserID(Long userID) throws PropertyNotSetException; 

	HashMap<String, Integer> countClassifiersByCrisisCodes(List<String> codes);

	// aidr-trainer-api specific
	public List<CollectionDTO> getAllCrisisWithModelFamilyNominalAttribute() throws PropertyNotSetException;
	public CollectionDTO getWithModelFamilyNominalAttributeByCrisisID(Long crisisID) throws PropertyNotSetException;
	public List<CollectionDTO> findActiveCrisis() throws PropertyNotSetException;
	
	public int deleteCrisis(Long id);

	public List findAllCrisisIds();
	
	public List<CollectionBriefInfo> getCrisisForNominalAttributeById(Integer attributeID,Integer crisis_type,String lang_filters)  throws PropertyNotSetException;
}
