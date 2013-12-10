/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade;

import java.util.List;
import javax.ejb.Local;
import qa.qcri.aidr.predictui.entities.Model;
import qa.qcri.aidr.predictui.dto.ModelWrapper;

/**
 *
 * @author Imran
 */
@Local
public interface ModelFacade {
    
    public List<Model> getAllModels();
    public Model getModelByID(int id);
    public List<Model> getModelByModelFamilyID(int modelFamilyID);
    public List<ModelWrapper> getModelByCrisisID(int crisisID);
    
}
