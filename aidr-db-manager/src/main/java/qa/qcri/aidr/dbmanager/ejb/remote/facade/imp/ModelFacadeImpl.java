/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

import java.util.List;
import javax.ejb.Stateless;
import qa.qcri.aidr.dbmanager.dto.ModelDTO;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.ModelFacadeRemote;

/**
 *
 * @author Imran
 */
@Stateless
public class ModelFacadeImpl implements ModelFacadeRemote{

    public ModelDTO getModelByID() {
        //To implement: Hibernate code to get ModelDTO from database 
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    public List<ModelDTO> getModelByModelFamilyID(Integer modelFamilyID, Integer start, Integer limit) {
        //To implement: Hibernate code to list of ModelDTO from database
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    public List<ModelDTO> getModelByCrisisID(long crisisID) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
    
}
