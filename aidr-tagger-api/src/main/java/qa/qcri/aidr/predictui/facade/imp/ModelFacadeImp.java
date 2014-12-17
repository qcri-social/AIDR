/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade.imp;

import java.util.ArrayList;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.ModelDTO;
import qa.qcri.aidr.predictui.facade.*;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import qa.qcri.aidr.dbmanager.dto.taggerapi.ModelDTOWrapper;


/**
 *
 * @author Imran
 *
 * Koushik: added try/catch
 */
@Stateless
public class ModelFacadeImp implements ModelFacade {

    @EJB
    private qa.qcri.aidr.dbmanager.ejb.remote.facade.ModelResourceFacade remoteModelEJB;

    @Override
    public List<ModelDTO> getAllModels() {
        try {
            return remoteModelEJB.getAllModels();
        } catch (PropertyNotSetException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public ModelDTO getModelByID(Long id) {
        try {
            return remoteModelEJB.getModelByID(id);
        } catch (PropertyNotSetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Integer getModelCountByModelFamilyID(Long modelFamilyID) {
        try {
            return remoteModelEJB.getModelCountByModelFamilyID(modelFamilyID);
        } catch (PropertyNotSetException pe) {
            pe.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ModelDTO> getModelByModelFamilyID(Long modelFamilyID, Integer start, Integer limit) {
        List<ModelDTO> modelDTOsList = new ArrayList<>();
        try {
            modelDTOsList = remoteModelEJB.getModelByModelFamilyID(modelFamilyID, start, limit);
        } catch (PropertyNotSetException pe) {
            pe.printStackTrace();
        }
        return modelDTOsList;
    }

    @Override
    public List<ModelDTOWrapper> getModelByCrisisID(Long crisisID) {
        try {
            return remoteModelEJB.getModelByCrisisID(crisisID);
        } catch (PropertyNotSetException pe) {
            pe.printStackTrace();
        }
        return null;
    }

}
