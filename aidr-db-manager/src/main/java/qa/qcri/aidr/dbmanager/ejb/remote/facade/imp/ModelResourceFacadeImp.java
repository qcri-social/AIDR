/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.ModelDTO;
import qa.qcri.aidr.dbmanager.ejb.local.facade.impl.CoreDBServiceFacadeImp;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.ModelResourceFacade;
import qa.qcri.aidr.dbmanager.entities.misc.Crisis;
import qa.qcri.aidr.dbmanager.entities.model.Model;

/**
 *
 * @author Imran
 */
public class ModelResourceFacadeImp extends CoreDBServiceFacadeImp<Model, Long> implements ModelResourceFacade {

    public List<ModelDTO> getAllModels() throws PropertyNotSetException {
        List<ModelDTO> modelDTOList = new ArrayList<ModelDTO>();
        List<Model> modelList = getAll();
        if (modelList != null) {
            for (Model model : modelList) {
                modelDTOList.add(new ModelDTO(model));
            }
        } 
        return modelDTOList;
    }

    public ModelDTO getModelByID(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Integer getModelCountByModelFamilyID(int modelFamilyID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
