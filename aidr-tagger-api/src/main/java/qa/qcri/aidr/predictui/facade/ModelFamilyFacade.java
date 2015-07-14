/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade;

import java.util.List;

import javax.ejb.Local;

import qa.qcri.aidr.dbmanager.dto.ModelFamilyDTO;
import qa.qcri.aidr.dbmanager.dto.taggerapi.TaggersForCodes;

/**
 *
 * @author Imran
 */
@Local
public interface ModelFamilyFacade {
    
    public List<ModelFamilyDTO> getAllModelFamilies();
    public List<ModelFamilyDTO> getAllModelFamiliesByCrisis(Long crisisID);
    public ModelFamilyDTO getModelFamilyByID(Long id);
    public boolean addCrisisAttribute(ModelFamilyDTO modelFamily);
    public boolean deleteModelFamily(Long modelFamilyID);
    public List<TaggersForCodes> getTaggersByCodes(List<String> codes);
    public boolean deleteModelFamilyData(Long modelFamilyID);

}
