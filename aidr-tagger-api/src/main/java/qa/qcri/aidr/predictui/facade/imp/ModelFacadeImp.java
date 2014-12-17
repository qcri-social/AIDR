/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade.imp;

import java.util.ArrayList;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.DocumentDTO;
import qa.qcri.aidr.dbmanager.dto.ModelDTO;
import qa.qcri.aidr.predictui.facade.*;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import qa.qcri.aidr.dbmanager.dto.taggerapi.ModelDTOWrapper;

import qa.qcri.aidr.task.ejb.TaskManagerRemote;

/**
 *
 * @author Imran
 *
 * Koushik: added try/catch
 */
@Stateless
public class ModelFacadeImp implements ModelFacade {

    @EJB
    private TaskManagerRemote<DocumentDTO, Long> taskManager;

    @EJB
    private qa.qcri.aidr.dbmanager.ejb.remote.facade.ModelResourceFacade remoteModelEJB;

    @PersistenceContext(unitName = "qa.qcri.aidr.predictui-EJBS")
    private EntityManager em;

    public List<ModelDTO> getAllModels() {
        try {
            return remoteModelEJB.getAllModels();
        } catch (PropertyNotSetException e) {
            e.printStackTrace();
        }
        return null;

    }

    public ModelDTO getModelByID(Long id) {
        try {
            return remoteModelEJB.getModelByID(id);
        } catch (PropertyNotSetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public Integer getModelCountByModelFamilyID(Long modelFamilyID) {
        try {
            return remoteModelEJB.getModelCountByModelFamilyID(modelFamilyID);
        } catch (PropertyNotSetException pe) {
            pe.printStackTrace();
        }
        return null;
    }

    public List<ModelDTO> getModelByModelFamilyID(Long modelFamilyID, Integer start, Integer limit) {
        List<ModelDTO> modelDTOsList = new ArrayList<>();
        try {
            modelDTOsList = remoteModelEJB.getModelByModelFamilyID(modelFamilyID, start, limit);
        } catch (PropertyNotSetException pe) {
            pe.printStackTrace();
        }
        return modelDTOsList;
    }

    public List<ModelDTOWrapper> getModelByCrisisID(Long crisisID) {
        try {
            return remoteModelEJB.getModelByCrisisID(crisisID);
        } catch (PropertyNotSetException pe) {
            pe.printStackTrace();
        }
        return null;
    }

}
