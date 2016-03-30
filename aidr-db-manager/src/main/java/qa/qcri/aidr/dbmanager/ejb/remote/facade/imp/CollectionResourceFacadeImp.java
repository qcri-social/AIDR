/**
 * Implements operations for managing the collection table of the aidr_predict DB
 * 
 * @author Koushik
 */
package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.common.wrapper.CollectionBriefInfo;
import qa.qcri.aidr.dbmanager.dto.CollectionDTO;
import qa.qcri.aidr.dbmanager.ejb.local.facade.impl.CoreDBServiceFacadeImp;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.CollectionResourceFacade;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.UsersResourceFacade;
import qa.qcri.aidr.dbmanager.entities.misc.Collection;
import qa.qcri.aidr.dbmanager.entities.model.ModelFamily;

@Stateless(name="CollectionResourceFacadeImp")
public class CollectionResourceFacadeImp extends CoreDBServiceFacadeImp<Collection, Long> implements CollectionResourceFacade {

	private static final Logger logger = Logger.getLogger("db-manager-log");

	private static final String SELECT_COLLECTION_FOR_ATTRIBUTE_CRISIS_TYPE = "SELECT c.name as name,"+
			" a.user_name as owner,"+
			" c.code as code,"+
			" c.lang_filters as langFilter,"+
			" count(1) as trainingCount"+
			" FROM collection c "+
			" JOIN document doc " +
			" ON doc.crisisID = c.id "+ 
			" JOIN document_nominal_label dnl"+
			" ON doc.documentID = dnl.documentID"+
			" JOIN nominal_label nl"+
			" ON dnl.nominalLabelID = nl.nominalLabelID"+
			" JOIN account a "+
			" ON c.owner_id = a.id"+
			" WHERE nl.nominalAttributeID = :nominalAttributeID"+
			" AND c.crisis_type = :crisis_type ";
	
	@EJB
	private UsersResourceFacade userLocalEJB;

	protected CollectionResourceFacadeImp(){
		super(Collection.class);
	}

	@Override
	public List<CollectionDTO> findByCriteria(String columnName, Object value) throws PropertyNotSetException {
		List<Collection> list = getAllByCriteria(Restrictions.eq(columnName,value));
		List<CollectionDTO> dtoList = new ArrayList<CollectionDTO>();
		if (list != null && !list.isEmpty()) {
			for (Collection c: list) {
				dtoList.add(new CollectionDTO(c));
			}
		}
		return dtoList;
	}
	
	@Override
	public List findAllCrisisIds() {
		Criteria criteria = getCurrentSession().createCriteria(Collection.class);
		criteria.setProjection(Projections.distinct(Projections.property("id")));
		try {
			List result = criteria.list();
			return result;
		} catch (HibernateException e) {
			logger.error("Error in findAllCrisisIds(). ",e);
			return null;
		}
	}

	@Override 
	public Integer deleteCrisis(CollectionDTO crisis) throws PropertyNotSetException {
		try {
			Collection managed = em.merge(crisis.toEntity());
			em.remove(managed); 
		} catch (Exception e) {
			return 0;
		}
		return 1;
	}

	@Override
	public CollectionDTO addCrisis(CollectionDTO crisis) {
		try {
			Collection c = crisis.toEntity();
			em.persist(c);
			em.flush();
			em.refresh(c);
			return new CollectionDTO(c);
		} catch (Exception e) {
			logger.error("Error in addCrisis for crisis code : " + crisis.getCode(), e);
			return null;
		}
	}

	@Override
	public CollectionDTO findCrisisByID(Long id) throws PropertyNotSetException {
		Collection collection = getById(id);
		if (collection != null) {
			CollectionDTO dto = new CollectionDTO(collection);
			return dto;
		} else {
			return null;
		}
	}

	@Override
	public CollectionDTO getCrisisWithAllFieldsByID(Long id) throws PropertyNotSetException {
		Collection collection = getById(id);
		if (collection != null) {
			Hibernate.initialize(collection.getModelFamilies());
			Hibernate.initialize(collection.getDocuments());

			CollectionDTO dto = new CollectionDTO(collection);
			return dto;
		} else {
			return null;
		}
	}

	@Override
	public CollectionDTO getCrisisByCode(String code) throws PropertyNotSetException {
		Criterion criterion = Restrictions.eq("code", code);
		Collection collection = (Collection) getByCriteria(criterion);
		return collection != null ? new CollectionDTO(collection) : null;
	}

	@Override
	public CollectionDTO editCrisis(CollectionDTO crisis) throws PropertyNotSetException {
		try {
			Collection c = crisis.toEntity();
			Collection cr = getById(c.getCrisisId()); 
			if (cr != null) {
				cr = em.merge(c);
				em.flush();
				em.refresh(cr);
				return cr != null ? new CollectionDTO(cr) : null;
			} else {
				logger.error("Not found");
				throw new RuntimeException("Not found");
			}
		} catch (Exception e) {
			logger.error("Exception in merging/updating crisis: " + crisis.getCrisisID(), e);
		}
		return null;

	}

	@Override
	public List<CollectionDTO> getAllCrisis() throws PropertyNotSetException {
		logger.info("Received request for fetching all crisis!!!");
		List<CollectionDTO> dtoList = new ArrayList<CollectionDTO>();
		List<Collection> collections = getAll();
		if (collections != null && !collections.isEmpty()) {
			for (Collection collection : collections) {
				logger.info("Converting to DTO crisis: " + collection.getCode() + ", " + collection.getName() + ", " + collection.getCrisisId());

				CollectionDTO dto = new CollectionDTO(collection);
				dtoList.add(dto);
			}
		}
		logger.info("Done creating DTO list, size = " + dtoList.size());
		return dtoList;
	}

	@Override
	public List<CollectionDTO> getAllCrisisWithModelFamilies() throws PropertyNotSetException {
		List<CollectionDTO> dtoList = new ArrayList<CollectionDTO>();
		List<Collection> crisisList = getAll();
		if (crisisList != null && !crisisList.isEmpty()) {
			for (Collection crisis : crisisList) {
				try {
					Hibernate.initialize(crisis.getModelFamilies());		// fetching lazily loaded data
					CollectionDTO dto = new CollectionDTO(crisis);
					dtoList.add(dto);
				} catch (HibernateException e) {
					logger.error("Hibernate initialization error for lazy objects in : " + crisis.getCrisisId());
				}
			}
		} 
		return dtoList;
	}

	@Override
	public List<CollectionDTO> getAllCrisisByUserID(Long userID) throws PropertyNotSetException{
		List<CollectionDTO> dtoList = userLocalEJB.findAllCrisisByUserID(userID);
		return dtoList;
	}

	@Override
	public boolean isCrisisExists(String crisisCode) throws PropertyNotSetException {
		CollectionDTO dto = getCrisisByCode(crisisCode); 
		return dto != null ? true : false;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public HashMap<String, Integer> countClassifiersByCrisisCodes(List<String> codes) {
		// TODO: convert native query to Hibernate/JPA
		String sqlQuery = "select cr.code, " +
				"       (select count(*) from model_family mf where mf.crisisID = cr.id) as mf_amount " +
				" from collection cr " +
				" where cr.code in (:codes) and classifier_enabled = 1;";
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
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public List<CollectionDTO> getAllCrisisWithModelFamilyNominalAttribute() throws PropertyNotSetException {
		List<CollectionDTO> dtoList = new ArrayList<CollectionDTO>();
		List<Collection> list = getAll();
		if (list != null && !list.isEmpty()) {
			for (Collection c: list) {
				try {
					Hibernate.initialize(c.getModelFamilies());
					for (ModelFamily mf : c.getModelFamilies()) {
						if(mf.hasNominalAttribute()){
							Hibernate.initialize(mf.getNominalAttribute().getNominalLabels());
						}
					}
					dtoList.add(new CollectionDTO(c));
				} catch (HibernateException e) {
					logger.error("Hibernate initialization error for lazy objects in : " + c.getCrisisId());
				}
			}
		}
		return dtoList;
	}

	@Override
	public CollectionDTO getWithModelFamilyNominalAttributeByCrisisID(Long crisisID) throws PropertyNotSetException {
		Collection crisis = getById(crisisID);
		if (crisis != null) {
			try {
				Hibernate.initialize(crisis.getModelFamilies());
				for (ModelFamily mf : crisis.getModelFamilies()) {
					if(mf.hasNominalAttribute()){
						Hibernate.initialize(mf.getNominalAttribute().getNominalLabels());
					}
				}
				return new CollectionDTO(crisis);
			} catch (HibernateException e) {
				logger.error("Hibernate initialization error for lazy objects in : " + crisis.getCrisisId());
			}
		}
		return null;
	}

	@Override
	public List<CollectionDTO> findActiveCrisis() throws PropertyNotSetException {
		List<CollectionDTO> dtoList = new ArrayList<CollectionDTO>();
		List<Collection> list = getAllByCriteria(Restrictions.eq("isTrashed", false));
		if (list != null && !list.isEmpty()) {
			for (Collection c: list) {
				try {
					Hibernate.initialize(c.getModelFamilies());
					for(ModelFamily mf : c.getModelFamilies()){
						if(mf.isIsActive()){
							dtoList.add(new CollectionDTO(c));
						}
					}
				} catch (HibernateException e) {
					logger.error("Hibernate initialization error for lazy objects in : " + c.getCrisisId());
				}
			}
		}
		return dtoList;
	}
	
	@Override 
	public int deleteCrisis(Long id) {
		Collection collection = getById(id);
		if (collection != null) {
			delete(collection);
			em.flush();
			return 1;
		} 
		return 0;
	}
	
	@Override
	public List<CollectionBriefInfo> getCrisisForNominalAttributeById(Integer attributeID, Integer crisis_type, String lang_filters) throws PropertyNotSetException {
		List<CollectionBriefInfo> result = new ArrayList<CollectionBriefInfo>();
		
		String finalSQLQuery = SELECT_COLLECTION_FOR_ATTRIBUTE_CRISIS_TYPE;
		
		String intermediateCriteria = "";
		if(lang_filters != null && !lang_filters.isEmpty()) {
			String[] languageList = lang_filters.split(",");
			for(int index = 0; index < languageList.length; index++) {
				if(index == 0) {
					intermediateCriteria += " AND ";
				}
				intermediateCriteria += " FIND_IN_SET('" + languageList[index] + "' , c.lang_filters )";
				if(index != languageList.length - 1) {
					intermediateCriteria += " OR";
				}
			}
		}
		
		finalSQLQuery = finalSQLQuery + intermediateCriteria + " GROUP BY doc.crisisID";;
		
		Query query = em.createNativeQuery(finalSQLQuery);
		query.setParameter("nominalAttributeID", attributeID);
		query.setParameter("crisis_type", crisis_type);
	
		List<Object[]> rows = null;
		try {
			rows = query.getResultList();
			for (Object[] row : rows) {
				CollectionBriefInfo collectionBriefInfo = new CollectionBriefInfo();
				collectionBriefInfo.setName((String) row[0]);
				collectionBriefInfo.setOwner((String) row[1]);
				collectionBriefInfo.setCode((String) row[2]);
				collectionBriefInfo.setLanguage((String) row[3]);
				collectionBriefInfo.setTrainingCount(((BigInteger) row[4]).intValue());
				result.add(collectionBriefInfo);
			}
			
		} catch (Exception e) {
			logger.error("Error in fetching collections for attribute : " + attributeID, e);
		}
		
		return result;
	}

}
