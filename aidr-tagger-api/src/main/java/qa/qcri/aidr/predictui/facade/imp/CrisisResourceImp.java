/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade.imp;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

//import org.apache.log4j.Logger;






import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.dbmanager.dto.CrisisDTO;
import qa.qcri.aidr.predictui.entities.ModelFamily;
import qa.qcri.aidr.predictui.entities.NominalLabel;
import qa.qcri.aidr.predictui.facade.CrisisResourceFacade;

/**
 *
 * @author Imran, Koushik
 */
@Stateless
public class CrisisResourceImp implements CrisisResourceFacade {
	//private static Logger logger = Logger.getLogger(CrisisResourceImp.class);
	private static Logger logger = LoggerFactory.getLogger(CrisisResourceImp.class);
	private static ErrorLog elog = new ErrorLog();

	//@PersistenceContext(unitName = "qa.qcri.aidr.predictui-EJBS")
	//private EntityManager em;

	@EJB
	private qa.qcri.aidr.dbmanager.ejb.remote.facade.CrisisResourceFacade remoteCrisisEJB;

	public CrisisDTO addCrisis(CrisisDTO crisis) {
        try {
			return remoteCrisisEJB.addCrisis(crisis);
		} catch (PropertyNotSetException e) {
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
			return remoteCrisisEJB.editCrisis(crisis);
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
