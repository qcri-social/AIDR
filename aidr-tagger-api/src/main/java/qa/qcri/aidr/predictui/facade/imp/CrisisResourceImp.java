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
import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.dbmanager.dto.CrisisDTO;
import qa.qcri.aidr.predictui.facade.CrisisResourceFacade;

/**
 *
 * @author Imran, Koushik
 */
@Stateless
public class CrisisResourceImp implements CrisisResourceFacade {
	//private static Logger logger = Logger.getLogger(CrisisResourceImp.class);
	private static Logger logger = Logger.getLogger(CrisisResourceImp.class);
	private static ErrorLog elog = new ErrorLog();

	//@PersistenceContext(unitName = "qa.qcri.aidr.predictui-EJBS")
	//private EntityManager em;

	@EJB
	private qa.qcri.aidr.dbmanager.ejb.remote.facade.CrisisResourceFacade remoteCrisisEJB;

	public CrisisDTO addCrisis(CrisisDTO crisis) {
		try {
			return remoteCrisisEJB.addCrisis(crisis);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public CrisisDTO getCrisisByID(Long id) {
		try {
			return remoteCrisisEJB.findCrisisByID(id);
		} catch (PropertyNotSetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public CrisisDTO getCrisisByCode(String code) {
		try {
			return remoteCrisisEJB.getCrisisByCode(code);
		} catch (PropertyNotSetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public CrisisDTO editCrisis(CrisisDTO crisis) {
		try {
			CrisisDTO editedDTO = remoteCrisisEJB.editCrisis(crisis);
			return editedDTO;
		} catch (PropertyNotSetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public List<CrisisDTO> getAllCrisis() {
		try {
			return remoteCrisisEJB.getAllCrisis();
		} catch (PropertyNotSetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public List<CrisisDTO> getAllCrisisByUserID(Long userID){
		try {
			return remoteCrisisEJB.getAllCrisisByUserID(userID);
		} catch (PropertyNotSetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Boolean isCrisisExists(String crisisCode) {
		try {
			return remoteCrisisEJB.isCrisisExists(crisisCode);
		} catch (PropertyNotSetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public HashMap<String, Integer> countClassifiersByCrisisCodes(List<String> codes) {
		return remoteCrisisEJB.countClassifiersByCrisisCodes(codes);
	}
}
