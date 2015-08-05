
package qa.qcri.aidr.analysis.facade.imp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import qa.qcri.aidr.analysis.entity.TagData;
import qa.qcri.aidr.analysis.entity.TagDataPK;
import qa.qcri.aidr.analysis.facade.TagDataStatisticsResourceFacade;
import qa.qcri.aidr.common.values.ReturnCode;

/**
 * Implements the operations for the interface TagDataStatisticsResourceFacade
 */
@Stateless(name = "TagDataStatisticsResourceFacadeImp")
public class TagDataStatisticsResourceFacadeImp implements TagDataStatisticsResourceFacade {

	private static Logger logger = Logger.getLogger(TagDataStatisticsResourceFacadeImp.class);

	@PersistenceContext(unitName = "qa.qcri.aidr.analysis-EJBS")
	private EntityManager em;

	@Override
	public EntityManager getEntityManager() {
		try {
			return em; 
		} catch (Exception e) {
			logger.error("getEntityManager failed");
			throw new HibernateException("getEntityManager failed");
		}
	}

	@Override
	public int setEntityManager(EntityManager em) {
		try {
			if (null == this.em) { 
				this.em = em;
				logger.info("EntityManager set to new value: " + this.em);
				return 1;
			} else 
				logger.info("Skipping setter, since EntityManager already initialized to :" + this.em);
			return 0;
		} catch (Exception e) {
			logger.error("EntityManager setting exception : " + em, e);
			throw new HibernateException("setEntityManager failed");
		}
	}

	@Override
	public Session getCurrentSession() {
		try { 
			return em.unwrap(Session.class);
		} catch (Exception e) {
			logger.error("exception: ", e);
			throw new HibernateException("getCurrentSession failed");
		}
	}


	@Override
	public ReturnCode writeData(TagData tagData) {
		try {
			em.persist(tagData);
			logger.info("Success in persisting tag data for: " + tagData.getCrisisCode() + ", " + tagData.getAttributeCode() 
					+ ", " + tagData.getLabelCode() + ", " + tagData.getTimestamp() + ", " + tagData.getGranularity() + ": " + tagData.getCount());
			return ReturnCode.SUCCESS;
		} catch (Exception e) {
			logger.error("Failure in persisting tag data for: " + tagData.getCrisisCode() + ", " + tagData.getAttributeCode() 
					+ ", " + tagData.getLabelCode() + ", " + tagData.getTimestamp() + ", " + tagData.getGranularity() + ": " + tagData.getCount());
			logger.error("exception: ", e);
			return ReturnCode.ERROR;
		}
	}

	@Override
	public TagData getSingleDataByPK(TagDataPK tagDataPK) {
		Criteria criteria = getCurrentSession().createCriteria(TagData.class);
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisisCode", tagDataPK.getCrisisCode()))
				.add(Restrictions.eq("timestamp", tagDataPK.getTimestamp()))
				.add(Restrictions.eq("granularity", tagDataPK.getGranularity()))
				.add(Restrictions.eq("attributeCode", tagDataPK.getAttributeCode()))
				.add(Restrictions.eq("labelCode", tagDataPK.getLabelCode()));
		criteria.add(criterion); 		
		try {
			TagData obj = (TagData) criteria.uniqueResult();
			return obj;
		} catch (HibernateException e) {
			logger.error("error in getSingleDataByPK for crisisCode : " + tagDataPK.getCrisisCode()
					+ " attributeCode : " + tagDataPK.getAttributeCode() 
					+ " labelCode : " +tagDataPK.getLabelCode() + "timestamp : " + tagDataPK.getTimestamp());
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TagData> getDataByCrisis(String crisisCode) {
		Criteria criteria = getCurrentSession().createCriteria(TagData.class);
		criteria.add(Restrictions.eq("crisisCode", crisisCode)); 		
		try {
			List<TagData> objList = (List<TagData>) criteria.list();
			return objList;
		} catch (HibernateException e) {
			logger.error("Error in getDataByCrisis for crisis : " + crisisCode);
		}
		return null;
	}
	
	// TODO
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Long> getTagCountByCrisisGranularity(String crisisCode, Long timestamp) {
		try {
			Criterion criterion = Restrictions.conjunction()
									.add(Restrictions.eq("crisisCode", crisisCode))
									.add(Restrictions.ge("timestamp", timestamp));
			
			List<Object> countList = getCurrentSession().createCriteria(TagData.class)
									.add(criterion)
									.setProjection(Projections.distinct(Projections.projectionList()
											.add(Projections.groupProperty("granularity").as("granularity"))
											.add(Projections.sum("count").as("count")))
									).list();
		
			Map<String, Long> data = new HashMap<String, Long>();
			for (int i = 0; i < countList.size();i++)  {
				Object[] temp = (Object[]) countList.get(i);
				Long g = ((Number) temp[0]).longValue();
				Long count = ((Number) temp[1]).longValue();
				data.put(g.toString(), count);
				
			}
			return data;
		} catch (HibernateException e) {
			logger.error("Error in getTagCountByCrisisGranularity for crisisCode : " + crisisCode
					+ " and timestamp : " + timestamp, e);
		}
		return null;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAttributesForCrisis(String crisisCode) {
		try {
			List<String> attributesList = (List<String>) getCurrentSession().createCriteria(TagData.class)
					.add(Restrictions.eq("crisisCode", crisisCode))
					.setProjection(Projections.distinct(Projections.property("attributeCode"))).list();
			return attributesList;
		} catch (Exception e) {
			logger.error("exception in getAttributesForCrisis for crisisCode : " + crisisCode, e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getGranularitiesForCrisis(String crisisCode) {
		try {
			List<Long> gList = (List<Long>) getCurrentSession().createCriteria(TagData.class)
					.add(Restrictions.eq("crisisCode", crisisCode))
					.setProjection(Projections.distinct(Projections.property("granularity"))).list();
			return gList;
		} catch (Exception e) {
			logger.error("exception in getGranularitiesForCrisis for crisisCode : " + crisisCode, e);
			return null;
		}

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TagData> getDataByCrisisGranularity(String crisisCode, Long timestamp, Long granularity) {
		Criteria criteria = getCurrentSession().createCriteria(TagData.class);
		Criterion criterion = Restrictions.conjunction()
								.add(Restrictions.eq("crisisCode", crisisCode))
								.add(Restrictions.ge("timestamp", timestamp));
		if (granularity != null) {
			criterion = Restrictions.conjunction()
					.add(criterion)
					.add(Restrictions.eq("granularity", granularity));
		}
		criteria.add(criterion); 		
		try {
			List<TagData> objList = (List<TagData>) criteria.list();
			return objList;
		} catch (HibernateException e) {
			logger.error("exception in getDataByCrisisGranularity for crisisCode : " + crisisCode
					+ " granularity : " + granularity, e);
		}
		return null;
	}


	
	@SuppressWarnings("unchecked")
	@Override
	public List<TagData> getDataByCrisisAttributeLabel(String crisisCode, String attributeCode, String labelCode) {
		Criteria criteria = getCurrentSession().createCriteria(TagData.class);
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisisCode", crisisCode))
				.add(Restrictions.eq("attributeCode", attributeCode));
		if (labelCode != null) {
			criterion = Restrictions.conjunction()
					.add(criterion)
					.add(Restrictions.eq("labelCode", labelCode));
		}
		criteria.add(criterion); 		
		try {
			List<TagData> objList = (List<TagData>) criteria.list();
			return objList;
		} catch (HibernateException e) {
			logger.error("exception in getDataByCrisisAttributeLabel for crisisCode : " + crisisCode
					+ " attributeCode : " + attributeCode + " labelCode : " + labelCode, e);
		}
		return null;
	}

	@Override
	public List<TagData> getDataByCrisisAttributeLabelGranularity(String crisisCode, String attributeCode, String labelCode, Long granularity) {
		Criteria criteria = getCurrentSession().createCriteria(TagData.class);
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisisCode", crisisCode))
				.add(Restrictions.eq("attributeCode", attributeCode))
				.add(Restrictions.eq("labelCode", labelCode))
				.add(Restrictions.eq("granularity", granularity));
		criteria.add(criterion); 		
		try {
			List<TagData> objList = (List<TagData>) criteria.list();
			return objList;
		} catch (HibernateException e) {
			logger.error("exception in getDataByCrisisAttributeLabelGranularity for crisisCode : " + crisisCode
					+ " attributeCode : " + attributeCode + " labelCode : " + labelCode
					+ " granularity : " + granularity, e);
		}
		return null;
	}

	public List<TagData> getDataByGranularityInTimeWindow(String crisisCode, String attributeCode, String labelCode, Long timestamp, Long granularity) {
		Criteria criteria = getCurrentSession().createCriteria(TagData.class);
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisisCode", crisisCode))
				.add(Restrictions.eq("attributeCode", attributeCode))
				.add(Restrictions.eq("granularity", granularity))
				.add(Restrictions.eq("timestamp", timestamp));
		if (labelCode != null) {
			criterion = Restrictions.conjunction()
					.add(criterion)
					.add(Restrictions.eq("labelCode", labelCode));
		}

		criteria.add(criterion); 		
		try {
			List<TagData> objList = (List<TagData>) criteria.list();
			return objList;
		} catch (HibernateException e) {
			logger.error("exception in getDataByGranularityInTimeWindow for crisisCode : " + crisisCode
					+ " attributeCode : " + attributeCode + " timestamp : " + timestamp
					+ " granularity : " + granularity, e);
		}
		return null;
	}

	@Override
	public List<TagData> getDataAfterTimestamp(String crisisCode, String attributeCode, String labelCode, Long timestamp) {
		Criteria criteria = getCurrentSession().createCriteria(TagData.class);
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisisCode", crisisCode))
				.add(Restrictions.eq("attributeCode", attributeCode))
				.add(Restrictions.ge("timestamp", timestamp));
		if (labelCode != null) {
			criterion = Restrictions.conjunction()
					.add(criterion)
					.add(Restrictions.eq("labelCode", labelCode));
		}

		criteria.add(criterion); 
		try {
			List<TagData> objList = (List<TagData>) criteria.list();
			return objList;
		} catch (HibernateException e) {
			logger.error("exception in getDataAfterTimestampGranularity for crisisCode : " + crisisCode
					+ " attributeCode : " + attributeCode + " timestamp : " + timestamp, e);
		}
		return null;
	}

	@Override
	public List<TagData> getDataAfterTimestampGranularity(String crisisCode, String attributeCode, String labelCode, Long timestamp, Long granularity) {
		Criteria criteria = getCurrentSession().createCriteria(TagData.class);
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisisCode", crisisCode))
				.add(Restrictions.eq("attributeCode", attributeCode))
				.add(Restrictions.ge("timestamp", timestamp))
				.add(Restrictions.eq("granularity", granularity));
		if (labelCode != null) {
			criterion = Restrictions.conjunction()
					.add(criterion)
					.add(Restrictions.eq("labelCode", labelCode));
		}
		criteria.add(criterion); 
		try {
			List<TagData> objList = (List<TagData>) criteria.list();
			return objList;
		} catch (HibernateException e) {
			logger.error("exception in getDataAfterTimestampGranularity for crisisCode : " + crisisCode
					+ " attributeCode : " + attributeCode + " timestamp : " + timestamp
					+ " granularity : " + granularity, e);
		}
		return null;
	}

	@Override
	public List<TagData> getDataBeforeTimestamp(String crisisCode, String attributeCode, String labelCode, Long timestamp) {
		Criteria criteria = getCurrentSession().createCriteria(TagData.class);
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisisCode", crisisCode))
				.add(Restrictions.eq("attributeCode", attributeCode))
				.add(Restrictions.le("timestamp", timestamp));
		if (labelCode != null) {
			criterion = Restrictions.conjunction()
					.add(criterion)
					.add(Restrictions.eq("labelCode", labelCode));
		}

		criteria.add(criterion); 		
		try {
			List<TagData> objList = (List<TagData>) criteria.list();
			return objList;
		} catch (HibernateException e) {
			logger.error("exception in getDataBeforeTimestamp for crisisCode : " + crisisCode
					+ " attributeCode : " + attributeCode + " timestamp : " + timestamp, e);
		}
		return null;
	}

	@Override
	public List<TagData> getDataBeforeTimestampGranularity(String crisisCode, String attributeCode, String labelCode, 
			Long timestamp, Long granularity) {
		Criteria criteria = getCurrentSession().createCriteria(TagData.class);
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisisCode", crisisCode))
				.add(Restrictions.eq("attributeCode", attributeCode))
				.add(Restrictions.le("timestamp", timestamp))
				.add(Restrictions.eq("granularity", granularity));
		if (labelCode != null) {
			criterion = Restrictions.conjunction()
					.add(criterion)
					.add(Restrictions.eq("labelCode", labelCode));
		}

		criteria.add(criterion);
		try {
			List<TagData> objList = (List<TagData>) criteria.list();
			return objList;
		} catch (HibernateException e) {
			logger.error("exception in getDataBeforeTimestampGranularity for crisisCode : " + crisisCode
					+ " attributeCode : " + attributeCode + " timestamp : " + timestamp
					+ " granularity : " + granularity, e);
		}
		return null;
	}


	@Override
	public List<TagData> getDataInInterval(String crisisCode, String attributeCode, String labelCode, Long timestamp1, Long timestamp2) {
		Criteria criteria = getCurrentSession().createCriteria(TagData.class);
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisisCode", crisisCode))
				.add(Restrictions.eq("attributeCode", attributeCode))
				.add(Restrictions.ge("timestamp", timestamp1))
				.add(Restrictions.le("timestamp", timestamp2));
		if (labelCode != null) {
			criterion = Restrictions.conjunction()
					.add(criterion)
					.add(Restrictions.eq("labelCode", labelCode));
		}
		criteria.add(criterion); 
		try {
			List<TagData> objList = (List<TagData>) criteria.list();
			return objList;
		} catch (HibernateException e) {
			logger.error("exception in getDataInInterval for crisisCode : " + crisisCode
					+ " attributeCode : " + attributeCode + " timestamp1 : " + timestamp1
					+ " timestamp2 : " + timestamp2, e);
		}
		return null;
	}

	@Override
	public List<TagData> getDataInIntervalWithGranularity(String crisisCode, String attributeCode, String labelCode, 
			Long timestamp1, Long timestamp2, Long granularity) {
		Criteria criteria = getCurrentSession().createCriteria(TagData.class);
		Criterion criterion = Restrictions.eq("crisisCode", crisisCode);

		// Now add the optional non-null criteria
		if (attributeCode != null) {
			criterion = Restrictions.conjunction()
					.add(criterion)
					.add(Restrictions.eq("attributeCode", attributeCode));
		}
		if (labelCode != null) {
			criterion = Restrictions.conjunction()
					.add(criterion)
					.add(Restrictions.eq("labelCode", labelCode));
		}
		if (granularity != null) {
			criterion = Restrictions.conjunction()
					.add(criterion)
					.add(Restrictions.eq("granularity", granularity));
		}
		if (timestamp1 != null) {
			criterion = Restrictions.conjunction()
					.add(criterion)
					.add(Restrictions.ge("timestamp", timestamp1));
		}
		if (timestamp2 != null) {
			criterion = Restrictions.conjunction()
					.add(criterion)
					.add(Restrictions.le("timestamp", timestamp2));
		}	
		criteria.add(criterion); 
		try {
			List<TagData> objList = (List<TagData>) criteria.list();
			return objList;
		} catch (HibernateException e) {
			logger.error("exception in getDataInIntervalWithGranularity for crisisCode : " + crisisCode
					+ " attributeCode : " + attributeCode + " timestamp1 : " + timestamp1
					+ " timestamp2 : " + timestamp2 + " granularity : " + granularity, e);
		}
		return null;
	}
}
