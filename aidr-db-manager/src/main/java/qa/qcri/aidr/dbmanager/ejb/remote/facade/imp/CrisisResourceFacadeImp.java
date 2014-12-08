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
import org.hibernate.HibernateException;
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
	public List<CrisisDTO> findByCriteria(String columnName, Object value) throws PropertyNotSetException {
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
	public Integer deleteCrisis(CrisisDTO crisis) throws PropertyNotSetException {
		try {
			em.remove(crisis.toEntity()); 
		} catch (Exception e) {
			return 0;
		}
		return 1;
	}

	@Override
	public CrisisDTO addCrisis(CrisisDTO crisis) throws PropertyNotSetException {
		em.persist(crisis.toEntity());
		return crisis;
	}

	@Override
	public CrisisDTO findCrisisByID(Long id) throws PropertyNotSetException {
		Crisis crisis = getById(id);
		if (crisis != null) {
			CrisisDTO dto = new CrisisDTO(crisis);
			return dto;
		} else {
			return null;
		}
	}

	@Override
	public CrisisDTO getCrisisWithAllFieldsByID(Long id) throws PropertyNotSetException {
		Crisis crisis = getById(id);
		if (crisis != null) {
			Hibernate.initialize(crisis.getModelFamilies());
			Hibernate.initialize(crisis.getNominalAttributes());
			Hibernate.initialize(crisis.getDocuments());

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
		System.out.println("Received request for: " + crisis.getCrisisID() + ", " + crisis.getCode());
		try {
			Crisis c = crisis.toEntity();
			Crisis cr = getById(c.getCrisisId()); 
			if (cr != null) {
				cr = em.merge(c);
				return cr != null ? new CrisisDTO(cr) : null;
			} else {
				throw new RuntimeException("Not found");
			}
		} catch (Exception e) {
			System.out.println("Exception in merging/updating crisis: " + crisis.getCrisisID());
			e.printStackTrace();	
		}
		return null;

	}

	@Override
	public List<CrisisDTO> getAllCrisis() throws PropertyNotSetException {
		System.out.println("Received request for fetching all crisis!!!");
		List<CrisisDTO> dtoList = new ArrayList<CrisisDTO>();
		List<Crisis> crisisList = getAll();
		if (crisisList != null) {
			for (Crisis crisis : crisisList) {
				System.out.println("Converting to DTO crisis: " + crisis.getCode() + ", " + crisis.getName() + ", " + crisis.getCrisisId()
						+ ", " + crisis.getUsers().getUserId() + ":" + crisis.getUsers().getName());

				CrisisDTO dto = new CrisisDTO(crisis);
				dtoList.add(dto);
			}
		}
		System.out.println("Done creating DTO list, size = " + dtoList.size());
		return dtoList;
	}

	@Override
	public List<CrisisDTO> getAllCrisisWithModelFamilies() throws PropertyNotSetException {
		List<CrisisDTO> dtoList = new ArrayList<CrisisDTO>();
		List<Crisis> crisisList = getAll();
		if (crisisList != null) {
			for (Crisis crisis : crisisList) {
				try {
					Hibernate.initialize(crisis.getModelFamilies());		// fetching lazily loaded data
					CrisisDTO dto = new CrisisDTO(crisis);
					dtoList.add(dto);
				} catch (HibernateException e) {
					logger.error("Hibernate initialization error for lazy objects in : " + crisis.getCrisisId());
				}
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

	@SuppressWarnings("rawtypes")
	@Override
	public HashMap<String, Integer> countClassifiersByCrisisCodes(List<String> codes) {
		// TODO: convert native query to Hibernate/JPA
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

	@Override
	public List<CrisisDTO> getAllCrisisWithModelFamilyNominalAttribute() throws PropertyNotSetException {
		List<CrisisDTO> dtoList = new ArrayList<CrisisDTO>();
		List<Crisis> list = getAll();
		if (list != null) {
			for (Crisis c: list) {
				try {
					Hibernate.initialize(c.getModelFamilies());
					Hibernate.initialize(c.getNominalAttributes());
					dtoList.add(new CrisisDTO(c));
				} catch (HibernateException e) {
					logger.error("Hibernate initialization error for lazy objects in : " + c.getCrisisId());
				}
			}
		}
		return dtoList;
	}

	@Override
	public CrisisDTO getWithModelFamilyNominalAttributeByCrisisID(Long crisisID) throws PropertyNotSetException {
		Crisis crisis = getById(crisisID);
		if (crisis != null) {
			try {
				Hibernate.initialize(crisis.getModelFamilies());
				Hibernate.initialize(crisis.getNominalAttributes());
				return new CrisisDTO(crisis);
			} catch (HibernateException e) {
				logger.error("Hibernate initialization error for lazy objects in : " + crisis.getCrisisId());
			}
		}
		return null;
	}

	@Override
	public List<CrisisDTO> findActiveCrisis() throws PropertyNotSetException {
		List<CrisisDTO> dtoList = new ArrayList<CrisisDTO>();
		List<Crisis> list = getAllByCriteria(Restrictions.eq("isTrashed", false));
		if (list != null) {
			for (Crisis c: list) {
				try {
					Hibernate.initialize(c.getModelFamilies());
					dtoList.add(new CrisisDTO(c));
				} catch (HibernateException e) {
					logger.error("Hibernate initialization error for lazy objects in : " + c.getCrisisId());
				}
			}
		}
		return dtoList;
	}
}
