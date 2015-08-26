/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade.imp;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.ModelFamilyDTO;
import qa.qcri.aidr.dbmanager.dto.taggerapi.TaggersForCodes;
import qa.qcri.aidr.predictui.facade.ModelFacade;
import qa.qcri.aidr.predictui.facade.ModelFamilyFacade;


/**
 *
 * @author Imran
 *
 * Koushik: added try/catch
 */
@Stateless
public class ModelFamilyFacadeImp implements ModelFamilyFacade {

    private static Logger logger = Logger.getLogger(ModelFamilyFacadeImp.class);

    @EJB
    private qa.qcri.aidr.dbmanager.ejb.remote.facade.ModelFamilyResourceFacade remoteModelFamilyEJB;
    
    @EJB 
    private ModelFacade modelFacade;

    @Override
    public List<ModelFamilyDTO> getAllModelFamilies() {
        List<ModelFamilyDTO> modelFamilyDTOList = new ArrayList<>();
        try {
            modelFamilyDTOList = remoteModelFamilyEJB.getAllModelFamilies();
        } catch (PropertyNotSetException pe) {
            logger.error("Error in fetching all model families.", pe);
        }
        return modelFamilyDTOList;
    }

    @Override
    public ModelFamilyDTO getModelFamilyByID(Long id) {
        try {
            return remoteModelFamilyEJB.getModelFamilyByID(id);
        } catch (Exception e) {
            logger.error("exception", e);
        }
        return null;

    }

    @Override
    public List<ModelFamilyDTO> getAllModelFamiliesByCrisis(Long crisisID) {
        List<ModelFamilyDTO> modelFamilyDTOList = new ArrayList<>();
        try {
            modelFamilyDTOList = remoteModelFamilyEJB.getAllModelFamiliesByCrisis(crisisID);
        } catch (PropertyNotSetException pe) {
            logger.error("Error in fetching model familes for crisis : " + crisisID, pe);
        }
        return modelFamilyDTOList;

    }

    @Override
    public boolean addCrisisAttribute(ModelFamilyDTO modelFamily) {
        try {
            return remoteModelFamilyEJB.addCrisisAttribute(modelFamily);
        } catch (PropertyNotSetException pe) {
            logger.error("Error in adding crisis attribute.", pe);
        }
        return false;
    }

    @Override
    public boolean deleteModelFamily(Long modelFamilyID) {
        try {
            return remoteModelFamilyEJB.deleteModelFamily(modelFamilyID);
        } catch (PropertyNotSetException pe) {
            logger.error("Error in deleting model family for modelFamilyID : "+ modelFamilyID, pe);
        }
        return false;
    }

    public List<TaggersForCodes> getTaggersByCodes(final List<String> codes) {
        List<TaggersForCodes> result = remoteModelFamilyEJB.getTaggersByCodes(codes);
        return result;
    }
    
    @Override
    public boolean deleteModelFamilyData(Long modelFamilyID) {
    	
    	// delete model
    	
    	modelFacade.deleteModelDataByModelFamily(modelFamilyID);
        try {
            return remoteModelFamilyEJB.deleteModelFamily(modelFamilyID);
        } catch (PropertyNotSetException pe) {
        	logger.error("Error in deleting modelFamilyData with modelFamilyID : " + modelFamilyID);
        }
        return false;
    }

}
