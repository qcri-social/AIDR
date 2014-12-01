/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.dbmanager.dto.CrisisDTO;
import qa.qcri.aidr.dbmanager.ejb.local.facade.impl.CoreDBServiceFacadeImp;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.CrisisResourceFacade;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.UsersResourceFacade;
import qa.qcri.aidr.dbmanager.entities.misc.Crisis;


/**
 *
 * @author Koushik
 */
@Stateless(name="CrisisResourceFacadeImp")
public class CrisisResourceFacadeImp extends CoreDBServiceFacadeImp<Crisis, Long> implements CrisisResourceFacade {

	private static Logger logger = Logger.getLogger("db-manager-log");
	private static ErrorLog elog = new ErrorLog();
	
	@EJB
	private UsersResourceFacade userLocalEJB;
	
	protected CrisisResourceFacadeImp(){
		super(Crisis.class);
	}

	@Override
	public List<CrisisDTO> findByCriteria(String columnName, Long value) throws PropertyNotSetException {
		List<Crisis> list = getAllByCriteria(Restrictions.eq(columnName,value));
		List<CrisisDTO> dtoList = new ArrayList<CrisisDTO>();
		if (list != null) {
			for (Crisis c: list) {
				dtoList.add(new CrisisDTO(c));
			}
		}
		return dtoList;
	}

	@Override
	public CrisisDTO addCrisis(CrisisDTO crisis) throws PropertyNotSetException {
		em.persist(crisis.toEntity());
		return crisis;
	}

	@Override
	public CrisisDTO getCrisisByID(Long id) throws PropertyNotSetException {
		Crisis crisis = getById(id);
		if (crisis != null) {
			CrisisDTO dto = new CrisisDTO(crisis);
			return dto;
		} else {
			return null;
		}
	}

	@Override
	public CrisisDTO getCrisisByCode(String code) throws PropertyNotSetException {
		Criterion criterion = Restrictions.eq("code", code);
		Crisis crisis = (Crisis) getByCriteria(criterion);
		return crisis != null ? new CrisisDTO(crisis) : null;
	}

	@Override
	public CrisisDTO editCrisis(CrisisDTO crisis) throws PropertyNotSetException {
		Crisis cr = getById(crisis.getCrisisID()); 
		if (cr != null) {
			cr = em.merge(cr);
			if (crisis.getCrisisTypeDTO() != null) {
				cr.setCrisisType(crisis.getCrisisTypeDTO().toEntity());
			}
			cr.setCode(crisis.getCode());
			cr.setName(crisis.getName());
			
			return new CrisisDTO(cr);
		} else {
			throw new RuntimeException("Not found");
		}
	}

	@Override
	public List<CrisisDTO> getAllCrisis() throws PropertyNotSetException {
		List<CrisisDTO> dtoList = new ArrayList<CrisisDTO>();
		List<Crisis> crisisList = getAll();
		if (crisisList != null) {
			for (Crisis crisis : crisisList) {
				CrisisDTO dto = new CrisisDTO(crisis);
				dtoList.add(dto);
			}
		} 
		return dtoList;
	}
	
	@Override
	public List<CrisisDTO> getAllCrisisWithModelFamilies() throws PropertyNotSetException {
		List<CrisisDTO> dtoList = new ArrayList<CrisisDTO>();
		List<Crisis> crisisList = getAll();
		if (crisisList != null) {
			for (Crisis crisis : crisisList) {
				Hibernate.initialize(crisis.getModelFamilies());		// fetching lazily loaded data
				CrisisDTO dto = new CrisisDTO(crisis);
				dtoList.add(dto);
			}
		} 
		return dtoList;
	}

	@Override
	public List<CrisisDTO> getAllCrisisByUserID(Long userID) throws PropertyNotSetException{
		List<CrisisDTO> dtoList = userLocalEJB.findAllCrisisByUserID(userID);
		return dtoList;
	}

	@Override
	public boolean isCrisisExists(String crisisCode) throws PropertyNotSetException {
		CrisisDTO dto = getCrisisByCode(crisisCode); 
		return dto != null ? true : false;
	}

	@Override
	public HashMap<String, Integer> countClassifiersByCrisisCodes(List<String> codes) {
		String sqlQuery = "select cr.code, " +
				"       (select count(*) from model_family mf where mf.crisisID = cr.crisisID) as mf_amount " +
				" from crisis cr " +
				" where cr.code in (:codes)";
		try {
			Query nativeQuery = em.createNativeQuery(sqlQuery);
			nativeQuery.setParameter("codes", codes);
			List resultList = nativeQuery.getResultList();
			HashMap<String, Integer> rv = new HashMap<String, Integer>();
			for(Object obj : resultList){
				Object[] objs = ((Object[])obj);
				rv.put((String)objs[0], ((BigInteger)objs[1]).intValue());
			}
			return rv;
		} catch (Exception e) {
			logger.error(elog.toStringException(e));
		}
		return null;
	}
}
