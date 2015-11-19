/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade.imp;

import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.CollectionDTO;
import qa.qcri.aidr.predictui.facade.CrisisResourceFacade;

/**
 *
 * @author Imran, Koushik
 */
@Stateless
public class CrisisResourceImp implements CrisisResourceFacade {
	private Logger logger = Logger.getLogger(CrisisResourceImp.class);

	@EJB
	private qa.qcri.aidr.dbmanager.ejb.remote.facade.CollectionResourceFacade remoteCrisisEJB;

	public CollectionDTO addCrisis(CollectionDTO crisis) {
		try {
			return remoteCrisisEJB.addCrisis(crisis);
		} catch (Exception e) {
			logger.error("Error in addCrisis.", e);
		}
		return null;
	}

	public CollectionDTO getCrisisByID(Long id) {
		try {
			return remoteCrisisEJB.findCrisisByID(id);
		} catch (PropertyNotSetException e) {
			logger.error("Error in getCrisisByID for id : " + id, e);
		}
		return null;
	}

	public CollectionDTO getCrisisByCode(String code) {
		try {
			return remoteCrisisEJB.getCrisisByCode(code);
		} catch (PropertyNotSetException e) {
			logger.error("Error in getCrisisByCode for code : " + code, e);
		}
		return null;
	}

	public CollectionDTO editCrisis(CollectionDTO crisis) {
		try {
			CollectionDTO editedDTO = remoteCrisisEJB.editCrisis(crisis);
			return editedDTO;
		} catch (PropertyNotSetException e) {
			logger.error("Error in editCrisis.", e);
		}
		return null;
	}

	public List<CollectionDTO> getAllCrisis() {
		try {
			return remoteCrisisEJB.getAllCrisis();
		} catch (PropertyNotSetException e) {
			logger.error("Error in getAllCrisis.", e);
		}
		return null;
	}

	public List<CollectionDTO> getAllCrisisByUserID(Long userID){
		try {
			return remoteCrisisEJB.getAllCrisisByUserID(userID);
		} catch (PropertyNotSetException e) {
			logger.error("Error in getAllCrisisByUserID for userID : " + userID, e);
		}
		return null;
	}

	public Boolean isCrisisExists(String crisisCode) {
		try {
			return remoteCrisisEJB.isCrisisExists(crisisCode);
		} catch (PropertyNotSetException e) {
			logger.error("Error in isCrisisExists for code : " + crisisCode, e);
		}
		return false;
	}

	@Override
	public HashMap<String, Integer> countClassifiersByCrisisCodes(List<String> codes) {
		return remoteCrisisEJB.countClassifiersByCrisisCodes(codes);
	}
	
	@Override
	public int deleteCrisis(Long crisisID) {
		return remoteCrisisEJB.deleteCrisis(crisisID);
	}
}
