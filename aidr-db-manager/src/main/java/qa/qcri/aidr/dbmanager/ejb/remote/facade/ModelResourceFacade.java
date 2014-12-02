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
import qa.qcri.aidr.dbmanager.ejb.local.facade.CoreDBServiceFacade;
import qa.qcri.aidr.dbmanager.entities.model.Model;

/**
 *
 * @author Imran
 */
@Remote
public interface ModelResourceFacade extends CoreDBServiceFacade<Model, Long>{
    
    public List<ModelDTO> getAllModels() throws PropertyNotSetException;

    public ModelDTO getModelByID(int id);

    public Integer getModelCountByModelFamilyID(int modelFamilyID);

    //public List<ModelHistoryWrapper> getModelByModelFamilyID(int modelFamilyID, Integer start, Integer limit);

    //public List<ModelWrapper> getModelByCrisisID(long crisisID);
    
}
