/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade;

import java.util.List;

import javax.ejb.Local;

import qa.qcri.aidr.dbmanager.dto.TaggersForCodes;
import qa.qcri.aidr.predictui.entities.ModelFamily;

/**
 *
 * @author Imran
 */
@Local
public interface ModelFamilyFacade {
    
    public List<ModelFamily> getAllModelFamilies();
    public List<ModelFamily> getAllModelFamiliesByCrisis(Long crisisID);
    public ModelFamily getModelFamilyByID(Long id);
    public ModelFamily addCrisisAttribute(ModelFamily modelFamily);
    public void deleteModelFamily(Long modelFamilyID);
    public List<TaggersForCodes> getTaggersByCodes(List<String> codes);

}
