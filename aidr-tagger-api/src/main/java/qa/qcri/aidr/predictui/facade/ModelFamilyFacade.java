/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade;

import java.util.List;
import javax.ejb.Local;
import qa.qcri.aidr.predictui.entities.ModelFamily;

/**
 *
 * @author Imran
 */
@Local
public interface ModelFamilyFacade {
    
    public List<ModelFamily> getAllModelFamilies();
    public List<ModelFamily> getAllModelFamiliesByCrisis(int crisisID);
    public ModelFamily getModelFamilyByID(int id);
    public ModelFamily addCrisisAttribute(ModelFamily modelFamily);
    public void deleteModelFamily(int modelFamilyID);
    
}
