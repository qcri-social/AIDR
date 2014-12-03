/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package qa.qcri.aidr.dbmanager.ejb.remote.facade;

import java.util.List;
import javax.ejb.Remote;
import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.ModelDTO;
import qa.qcri.aidr.dbmanager.dto.taggerapi.ModelDTOWrapper;
import qa.qcri.aidr.dbmanager.ejb.local.facade.CoreDBServiceFacade;
import qa.qcri.aidr.dbmanager.entities.model.Model;

/**
 *
 * @author Imran
 */
@Remote
public interface ModelResourceFacade extends CoreDBServiceFacade<Model, Long>{
    
    public List<ModelDTO> getAllModels() throws PropertyNotSetException;

    public ModelDTO getModelByID(Long id) throws PropertyNotSetException;

    public Integer getModelCountByModelFamilyID(Long modelFamilyID) throws PropertyNotSetException;

    //Client fix needed: as this method was originally returning ModelHistoryWrapper (see this class in tagger_api) class
    public List<ModelDTO> getModelByModelFamilyID(Integer modelFamilyID, Integer start, Integer limit) throws PropertyNotSetException;

    //Client to fix: response changed from ModelWrapper -> ModelDTOWrapper. Only Class name is changed, all attributes are same.
    public List<ModelDTOWrapper> getModelByCrisisID(Long crisisID);
    
}
