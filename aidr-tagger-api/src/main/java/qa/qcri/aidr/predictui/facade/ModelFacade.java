/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade;

import java.util.List;

import javax.ejb.Local;

import qa.qcri.aidr.dbmanager.dto.ModelDTO;
import qa.qcri.aidr.dbmanager.dto.taggerapi.ModelHistoryWrapper;
import qa.qcri.aidr.dbmanager.dto.taggerapi.ModelWrapper;

/**
 *
 * @author Imran
 */
@Local
public interface ModelFacade {

    public List<ModelDTO> getAllModels();

    public ModelDTO getModelByID(Long id);

    public Integer getModelCountByModelFamilyID(Long modelFamilyID);

    public List<ModelHistoryWrapper> getModelByModelFamilyID(Long modelFamilyID, Integer start, Integer limit);

    public List<ModelWrapper> getModelByCrisisID(Long crisisID);

}
