/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade.imp;

import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.dbmanager.dto.TaggersForCodes;
import qa.qcri.aidr.predictui.facade.*;

import java.math.BigInteger;
import java.util.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import qa.qcri.aidr.predictui.entities.Crisis;
import qa.qcri.aidr.predictui.entities.ModelFamily;

/**
 *
 * @author Imran
 * 
 * Koushik: added try/catch 
 */
@Stateless
public class ModelFamilyFacadeImp implements ModelFamilyFacade{

	@PersistenceContext(unitName = "qa.qcri.aidr.predictui-EJBS")
	private EntityManager em;
	
	private static Logger logger = LoggerFactory.getLogger(ModelFamilyFacadeImp.class);
	private static ErrorLog elog = new ErrorLog();
	
	@EJB
	private qa.qcri.aidr.dbmanager.ejb.remote.facade.ModelFamilyResourceFacade remoteModelFamilyEJB;
	
	public List<ModelFamily> getAllModelFamilies() {
		Query query = em.createNamedQuery("ModelFamily.findAll", ModelFamily.class);
		try {
			List<ModelFamily> modelFamilyList = query.getResultList();
			return modelFamilyList;
		} catch (NoResultException e) {
			return null;
		}

	}

	public ModelFamily getModelFamilyByID(Long id) {
		Query query = em.createNamedQuery("ModelFamily.findByModelFamilyID", ModelFamily.class);
		query.setParameter("modelFamilyID", id);
		try {
			ModelFamily modelFamily = (ModelFamily)query.getSingleResult();
			return modelFamily;
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<ModelFamily> getAllModelFamiliesByCrisis(Long crisisID) {
		Query query = em.createNamedQuery("Crisis.findByCrisisID", Crisis.class);
		query.setParameter("crisisID", crisisID);
		Crisis crisis = null;
		try {
			crisis = (Crisis)query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
		query = em.createNamedQuery("ModelFamily.findByCrisis", ModelFamily.class);
		query.setParameter("crisis", crisis);
		try {
			List<ModelFamily> modelFamilyList = query.getResultList();
			return modelFamilyList;
		} catch (NoResultException e) {
			return null;
		}
	}

	public ModelFamily addCrisisAttribute(ModelFamily modelFamily) {
		em.persist(modelFamily);
		return modelFamily;
	}

	public void deleteModelFamily(Long modelFamilyID){
		ModelFamily mf = em.find(ModelFamily.class, modelFamilyID);
		if (mf != null){
			em.remove(mf);
		}
	}

	public List<TaggersForCodes> getTaggersByCodes(final List<String> codes) {
		List<TaggersForCodes> result = remoteModelFamilyEJB.getTaggersByCodes(codes);
		return result;
	}

}
