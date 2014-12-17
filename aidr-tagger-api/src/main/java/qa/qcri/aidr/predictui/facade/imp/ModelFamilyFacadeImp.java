/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade.imp;

import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.predictui.facade.*;

import java.util.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.ModelFamilyDTO;
import qa.qcri.aidr.dbmanager.dto.taggerapi.TaggersForCodes;


/**
 *
 * @author Imran
 *
 * Koushik: added try/catch
 */
@Stateless
public class ModelFamilyFacadeImp implements ModelFamilyFacade {

    private static Logger logger = LoggerFactory.getLogger(ModelFamilyFacadeImp.class);
    private static ErrorLog elog = new ErrorLog();

    @EJB
    private qa.qcri.aidr.dbmanager.ejb.remote.facade.ModelFamilyResourceFacade remoteModelFamilyEJB;

    @Override
    public List<ModelFamilyDTO> getAllModelFamilies() {
        List<ModelFamilyDTO> modelFamilyDTOList = new ArrayList<>();
        try {
            modelFamilyDTOList = remoteModelFamilyEJB.getAllModelFamilies();
        } catch (PropertyNotSetException pe) {
            pe.printStackTrace();
        }
        return modelFamilyDTOList;
    }

    @Override
    public ModelFamilyDTO getModelFamilyByID(Long id) {
        try {
            return remoteModelFamilyEJB.getModelFamilyByID(id);
        } catch (PropertyNotSetException pe) {
            pe.printStackTrace();
        }
        return null;

    }

    @Override
    public List<ModelFamilyDTO> getAllModelFamiliesByCrisis(Long crisisID) {
        List<ModelFamilyDTO> modelFamilyDTOList = new ArrayList<>();
        try {
            modelFamilyDTOList = remoteModelFamilyEJB.getAllModelFamiliesByCrisis(crisisID);
        } catch (PropertyNotSetException pe) {
            pe.printStackTrace();
        }
        return modelFamilyDTOList;

    }

    @Override
    public boolean addCrisisAttribute(ModelFamilyDTO modelFamily) {
        try {
            return remoteModelFamilyEJB.addCrisisAttribute(modelFamily);
        } catch (PropertyNotSetException pe) {
            pe.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteModelFamily(Long modelFamilyID) {
        try {
            return remoteModelFamilyEJB.deleteModelFamily(modelFamilyID);
        } catch (PropertyNotSetException pe) {
            pe.printStackTrace();
        }
        return false;
    }

    public List<TaggersForCodes> getTaggersByCodes(final List<String> codes) {
        List<TaggersForCodes> result = remoteModelFamilyEJB.getTaggersByCodes(codes);
        return result;
    }

}
