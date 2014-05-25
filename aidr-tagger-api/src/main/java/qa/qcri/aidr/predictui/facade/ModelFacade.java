/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade;

import java.util.List;
import javax.ejb.Local;

import qa.qcri.aidr.predictui.dto.ModelHistoryWrapper;
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

    public Integer getModelCountByModelFamilyID(int modelFamilyID);

    public List<ModelHistoryWrapper> getModelByModelFamilyID(int modelFamilyID, Integer start, Integer limit);

    public List<ModelWrapper> getModelByCrisisID(long crisisID);

}
