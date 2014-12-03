/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package qa.qcri.aidr.dbmanager.ejb.remote.facade;

import java.util.List;
import javax.ejb.Remote;
import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.ModelFamilyDTO;

/**
 *
 * @author Imran
 */
@Remote
public interface ModelFamilyResourceFacade {
    public List<ModelFamilyDTO> getAllModelFamilies() throws PropertyNotSetException;;
    public List<ModelFamilyDTO> getAllModelFamiliesByCrisis(Long crisisID) throws PropertyNotSetException;
    public ModelFamilyDTO getModelFamilyByID(Long id) throws PropertyNotSetException;
    public ModelFamilyDTO addCrisisAttribute(ModelFamilyDTO modelFamily) throws PropertyNotSetException;
    public void deleteModelFamily(Integer modelFamilyID) throws PropertyNotSetException;
    
    //TODO
    //public List<TaggersForCodes> getTaggersByCodes(List<String> codes);
    
}
