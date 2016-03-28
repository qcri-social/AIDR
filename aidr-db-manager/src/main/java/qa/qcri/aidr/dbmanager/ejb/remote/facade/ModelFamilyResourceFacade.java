/**
 *
 * @author Imran
 */

package qa.qcri.aidr.dbmanager.ejb.remote.facade;

import java.util.List;

import javax.ejb.Remote;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.ModelFamilyDTO;
import qa.qcri.aidr.dbmanager.dto.taggerapi.TaggersForCodes;
import qa.qcri.aidr.dbmanager.ejb.local.facade.CoreDBServiceFacade;
import qa.qcri.aidr.dbmanager.entities.model.ModelFamily;


@Remote
public interface ModelFamilyResourceFacade extends CoreDBServiceFacade<ModelFamily, Long>{
    public List<ModelFamilyDTO> getAllModelFamilies() throws PropertyNotSetException;;
    public List<ModelFamilyDTO> getAllModelFamiliesByCrisis(Long crisisID) throws PropertyNotSetException;
    public ModelFamilyDTO getModelFamilyByID(Long id) throws PropertyNotSetException;
    
    //Client to fix: return type changed from ModelFamilyDTO to boolean
    public boolean addCrisisAttribute(ModelFamilyDTO modelFamily) throws PropertyNotSetException;
    
    //Clien to fix: return type chagned from void to boolean
    public boolean deleteModelFamily(Long modelFamilyID) throws PropertyNotSetException;
    
    //TODO for Koushik - convert to Hibernate query and remove TaggerForCodes & TaggerForCodesRequest DTOs from db-manager eventually
    public List<TaggersForCodes> getTaggersByCodes(List<String> codes);
}
