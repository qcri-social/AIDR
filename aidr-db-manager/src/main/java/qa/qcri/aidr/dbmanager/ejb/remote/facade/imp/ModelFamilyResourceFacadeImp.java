/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

import java.util.ArrayList;
import java.util.List;
import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.ModelFamilyDTO;
import qa.qcri.aidr.dbmanager.ejb.local.facade.impl.CoreDBServiceFacadeImp;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.ModelFamilyResourceFacade;
import qa.qcri.aidr.dbmanager.entities.misc.Crisis;
import qa.qcri.aidr.dbmanager.entities.model.ModelFamily;

/**
 *
 * @author Imran
 */
public class ModelFamilyResourceFacadeImp extends CoreDBServiceFacadeImp<ModelFamily, Long> implements ModelFamilyResourceFacade {

    public List<ModelFamilyDTO> getAllModelFamilies() throws PropertyNotSetException {
        List<ModelFamilyDTO> modelFamilyDTOList = new ArrayList<ModelFamilyDTO>();
        List<ModelFamily> modelFamilyList = getAll();
        for (ModelFamily modelFamily : modelFamilyList) {
            modelFamilyDTOList.add(new ModelFamilyDTO(modelFamily));
        }
        return modelFamilyDTOList;
    }

    public List<ModelFamilyDTO> getAllModelFamiliesByCrisis(Long crisisID) throws PropertyNotSetException {
        List<ModelFamilyDTO> modelFamilyDTOList = new ArrayList<ModelFamilyDTO>();
        Crisis crisis = getEntityManager().find(Crisis.class, crisisID);
        List<ModelFamily> modelFamilyList = crisis.getModelFamilies();
        for (ModelFamily modelFamily : modelFamilyList) {
            modelFamilyDTOList.add(new ModelFamilyDTO(modelFamily));
        }
        return modelFamilyDTOList;
    }

    public ModelFamilyDTO getModelFamilyByID(Long id) throws PropertyNotSetException {
        return new ModelFamilyDTO(getById(id));
    }

    public ModelFamilyDTO addCrisisAttribute(ModelFamilyDTO modelFamily) throws PropertyNotSetException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void deleteModelFamily(Integer modelFamilyID) throws PropertyNotSetException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
