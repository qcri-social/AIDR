package qa.qcri.aidr.analysis.facade.imp;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import qa.qcri.aidr.analysis.entity.FrequencyData;
import qa.qcri.aidr.analysis.entity.FrequencyDataPK;
import qa.qcri.aidr.analysis.facade.FrequencyStatisticsResourceFacade;
import qa.qcri.aidr.analysis.utils.CommonOperations;
import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.common.values.ReturnCode;

@Stateless
public class FrequencyStatisticsResourceFacadeImp extends CommonOperations implements FrequencyStatisticsResourceFacade {
	
	private static Logger logger = LoggerFactory.getLogger(FrequencyStatisticsResourceFacadeImp.class);
	private static ErrorLog elog = new ErrorLog();
	
	@PersistenceContext(unitName = "qa.qcri.aidr.analysis-EJBS")
	private EntityManager em;
	
	@Override
	public ReturnCode writeData(FrequencyData freqData) {
		try {
			em.persist(freqData);
			//System.out.println("Success in persisting data for: " + freqData.getCrisisCode() + ", " + freqData.getAttributeCode() 
			//				+ ", " + freqData.getLabelCode() + ", " + freqData.getTimestamp() + ", " + freqData.getGranularity() 
			//				+ ", " + freqData.getBin() + ": " + freqData.getCount());
			return ReturnCode.SUCCESS;
		} catch (Exception e) {
			System.out.println("Failure in persisting data for: " + freqData.getCrisisCode() + ", " + freqData.getAttributeCode() 
					+ ", " + freqData.getLabelCode() + ", " + freqData.getTimestamp() + ", " + freqData.getGranularity() 
					+ ", " + freqData.getBin() + ": " + freqData.getCount());
			e.printStackTrace();
			logger.error(elog.toStringException(e));
			return ReturnCode.FAIL;
		}
	}

	@Override
	public FrequencyData getSingleDataByPK(FrequencyDataPK freqDataPK) {
		Criteria criteria = getCurrentSession(em).createCriteria(this.getClass());
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisis_code", freqDataPK.getCrisisCode()))
				.add(Restrictions.eq("timestamp", freqDataPK.getTimestamp()))
				.add(Restrictions.eq("granularity", freqDataPK.getGranularity()))
				.add(Restrictions.eq("attribute_code", freqDataPK.getAttributeCode()))
				.add(Restrictions.eq("label_code", freqDataPK.getLabelCode()))
				.add(Restrictions.eq("bin", freqDataPK.getBin()));
		criteria.add(criterion); 		
		try {
			FrequencyData obj = (FrequencyData) criteria.uniqueResult();
			return obj;
		} catch (HibernateException e) {
			logger.error(elog.toStringException(e));
		}
		return null;
	}
	
	@Override
	public List<FrequencyData> getDataByCrisis(String crisisCode) {
		Criteria criteria = getCurrentSession(em).createCriteria(this.getClass());
		criteria.add(Restrictions.eq("crisis_code", crisisCode)); 		
		try {
			List<FrequencyData> objList = (List<FrequencyData>) criteria.list();
			return objList;
		} catch (HibernateException e) {
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	@Override
	public List<FrequencyData> getDataByCrisisAttributeLabel(String crisisCode, String attributeCode, String labelCode) {
		Criteria criteria = getCurrentSession(em).createCriteria(this.getClass());
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisis_code", crisisCode))
				.add(Restrictions.eq("attribute_code", attributeCode))
				.add(Restrictions.eq("label_code", labelCode));
		criteria.add(criterion); 		
		try {
			List<FrequencyData> objList = (List<FrequencyData>) criteria.list();
			return objList;
		} catch (HibernateException e) {
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	@Override
	public List<FrequencyData> getDataByCrisisAttributeLabelGranularity(String crisisCode, String attributeCode, String labelCode, Long granularity) {
		Criteria criteria = getCurrentSession(em).createCriteria(this.getClass());
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisis_code", crisisCode))
				.add(Restrictions.eq("attribute_code", attributeCode))
				.add(Restrictions.eq("label_code", labelCode))
				.add(Restrictions.eq("granularity", granularity));
		criteria.add(criterion); 		
		try {
			List<FrequencyData> objList = (List<FrequencyData>) criteria.list();
			return objList;
		} catch (HibernateException e) {
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	@Override
	public List<FrequencyData> getDataAfterTimestamp(String crisisCode, String attributeCode, String labelCode, Long timestamp) {
		Criteria criteria = getCurrentSession(em).createCriteria(this.getClass());
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisis_code", crisisCode))
				.add(Restrictions.eq("attribute_code", attributeCode))
				.add(Restrictions.eq("label_code", labelCode))
				.add(Restrictions.ge("timestamp", timestamp));
				
		criteria.add(criterion); 		
		try {
			List<FrequencyData> objList = (List<FrequencyData>) criteria.list();
			return objList;
		} catch (HibernateException e) {
			logger.error(elog.toStringException(e));
		}
		return null;	
	}

	@Override
	public List<FrequencyData> getDataAfterTimestampGranularity(String crisisCode, String attributeCode, String labelCode,
																Long timestamp, Long granularity) {
		Criteria criteria = getCurrentSession(em).createCriteria(this.getClass());
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisis_code", crisisCode))
				.add(Restrictions.eq("attribute_code", attributeCode))
				.add(Restrictions.eq("label_code", labelCode))
				.add(Restrictions.ge("timestamp", timestamp))
				.add(Restrictions.eq("granularity", granularity));
				
		criteria.add(criterion); 		
		try {
			List<FrequencyData> objList = (List<FrequencyData>) criteria.list();
			return objList;
		} catch (HibernateException e) {
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	@Override
	public List<FrequencyData> getDataBeforeTimestamp(String crisisCode, String attributeCode, String labelCode, Long timestamp) {
		Criteria criteria = getCurrentSession(em).createCriteria(this.getClass());
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisis_code", crisisCode))
				.add(Restrictions.eq("attribute_code", attributeCode))
				.add(Restrictions.eq("label_code", labelCode))
				.add(Restrictions.le("timestamp", timestamp));
				
		criteria.add(criterion); 		
		try {
			List<FrequencyData> objList = (List<FrequencyData>) criteria.list();
			return objList;
		} catch (HibernateException e) {
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	@Override
	public List<FrequencyData> getDataBeforeTimestampGranularity(String crisisCode, String attributeCode, String labelCode,
																	Long timestamp, Long granularity) {
		Criteria criteria = getCurrentSession(em).createCriteria(this.getClass());
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisis_code", crisisCode))
				.add(Restrictions.eq("attribute_code", attributeCode))
				.add(Restrictions.eq("label_code", labelCode))
				.add(Restrictions.le("timestamp", timestamp))
				.add(Restrictions.eq("granularity", granularity));
				
		criteria.add(criterion); 		
		try {
			List<FrequencyData> objList = (List<FrequencyData>) criteria.list();
			return objList;
		} catch (HibernateException e) {
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	@Override
	public List<FrequencyData> getDataInInterval(String crisisCode, String attributeCode, String labelCode, Long timestamp1, Long timestamp2) {
		Criteria criteria = getCurrentSession(em).createCriteria(this.getClass());
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisis_code", crisisCode))
				.add(Restrictions.eq("attribute_code", attributeCode))
				.add(Restrictions.eq("label_code", labelCode))
				.add(Restrictions.ge("timestamp", timestamp1))
				.add(Restrictions.le("timestamp", timestamp2));
				
		criteria.add(criterion); 		
		try {
			List<FrequencyData> objList = (List<FrequencyData>) criteria.list();
			return objList;
		} catch (HibernateException e) {
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	@Override
	public List<FrequencyData> getDataInIntervalWithGranularity(String crisisCode, String attributeCode, String labelCode, 
																	Long timestamp1, Long timestamp2, Long granularity) {
		Criteria criteria = getCurrentSession(em).createCriteria(this.getClass());
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisis_code", crisisCode))
				.add(Restrictions.eq("attribute_code", attributeCode))
				.add(Restrictions.eq("label_code", labelCode))
				.add(Restrictions.eq("granularity", granularity))
				.add(Restrictions.ge("timestamp", timestamp1))
				.add(Restrictions.le("timestamp", timestamp2));
				
		criteria.add(criterion); 		
		try {
			List<FrequencyData> objList = (List<FrequencyData>) criteria.list();
			return objList;
		} catch (HibernateException e) {
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	
	////////////////////////////////////////////////
	// 				Bin related
	///////////////////////////////////////////////
	
	@Override
	public List<FrequencyData> getDataByCrisisWithBin(String crisisCode, Integer bin) {
		Criteria criteria = getCurrentSession(em).createCriteria(this.getClass());
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisis_code", crisisCode))
				.add(Restrictions.ge("bin", bin));
		criteria.add(criterion); 
		try {
			List<FrequencyData> objList = (List<FrequencyData>) criteria.list();
			return objList;
		} catch (HibernateException e) {
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	@Override
	public List<FrequencyData> getDataByCrisisAttributeLabelWithBin(String crisisCode, String attributeCode, String labelCode,
																		Integer bin) {
		Criteria criteria = getCurrentSession(em).createCriteria(this.getClass());
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisis_code", crisisCode))
				.add(Restrictions.eq("attribute_code", attributeCode))
				.add(Restrictions.eq("label_code", labelCode))
				.add(Restrictions.ge("bin", bin));
		criteria.add(criterion); 		
		try {
			List<FrequencyData> objList = (List<FrequencyData>) criteria.list();
			return objList;
		} catch (HibernateException e) {
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	@Override
	public List<FrequencyData> getDataByCrisisAttributeLabelGranularityWithBin(String crisisCode, String attributeCode, String labelCode,
																		Long granularity, Integer bin) {
		Criteria criteria = getCurrentSession(em).createCriteria(this.getClass());
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisis_code", crisisCode))
				.add(Restrictions.eq("attribute_code", attributeCode))
				.add(Restrictions.eq("label_code", labelCode))
				.add(Restrictions.eq("granularity", granularity))
				.add(Restrictions.ge("bin", bin));
		criteria.add(criterion); 		
		try {
			List<FrequencyData> objList = (List<FrequencyData>) criteria.list();
			return objList;
		} catch (HibernateException e) {
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	@Override
	public List<FrequencyData> getDataAfterTimestampWithBin(String crisisCode, String attributeCode, String labelCode, 
																		Long timestamp, Integer bin) {
		Criteria criteria = getCurrentSession(em).createCriteria(this.getClass());
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisis_code", crisisCode))
				.add(Restrictions.eq("attribute_code", attributeCode))
				.add(Restrictions.eq("label_code", labelCode))
				.add(Restrictions.ge("timestamp", timestamp))
				.add(Restrictions.ge("bin", bin));
				
		criteria.add(criterion); 		
		try {
			List<FrequencyData> objList = (List<FrequencyData>) criteria.list();
			return objList;
		} catch (HibernateException e) {
			logger.error(elog.toStringException(e));
		}
		return null;	
	}

	@Override
	public List<FrequencyData> getDataAfterTimestampGranularityWithBin(String crisisCode, String attributeCode, String labelCode,
																		Long timestamp, Long granularity, Integer bin) {
		Criteria criteria = getCurrentSession(em).createCriteria(this.getClass());
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisis_code", crisisCode))
				.add(Restrictions.eq("attribute_code", attributeCode))
				.add(Restrictions.eq("label_code", labelCode))
				.add(Restrictions.ge("timestamp", timestamp))
				.add(Restrictions.eq("granularity", granularity))
				.add(Restrictions.ge("bin", bin));
				
		criteria.add(criterion); 		
		try {
			List<FrequencyData> objList = (List<FrequencyData>) criteria.list();
			return objList;
		} catch (HibernateException e) {
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	@Override
	public List<FrequencyData> getDataBeforeTimestampWithBin(String crisisCode, String attributeCode, String labelCode, 
																		Long timestamp, Integer bin) {
		Criteria criteria = getCurrentSession(em).createCriteria(this.getClass());
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisis_code", crisisCode))
				.add(Restrictions.eq("attribute_code", attributeCode))
				.add(Restrictions.eq("label_code", labelCode))
				.add(Restrictions.le("timestamp", timestamp))
				.add(Restrictions.ge("bin", bin));
				
		criteria.add(criterion); 		
		try {
			List<FrequencyData> objList = (List<FrequencyData>) criteria.list();
			return objList;
		} catch (HibernateException e) {
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	@Override
	public List<FrequencyData> getDataBeforeTimestampGranularityWithBin(String crisisCode, String attributeCode, String labelCode,
																		Long timestamp, Long granularity, Integer bin) {
		Criteria criteria = getCurrentSession(em).createCriteria(this.getClass());
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisis_code", crisisCode))
				.add(Restrictions.eq("attribute_code", attributeCode))
				.add(Restrictions.eq("label_code", labelCode))
				.add(Restrictions.le("timestamp", timestamp))
				.add(Restrictions.eq("granularity", granularity))
				.add(Restrictions.ge("bin", bin));
				
		criteria.add(criterion); 		
		try {
			List<FrequencyData> objList = (List<FrequencyData>) criteria.list();
			return objList;
		} catch (HibernateException e) {
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	@Override
	public List<FrequencyData> getDataInIntervalWithBin(String crisisCode, String attributeCode, String labelCode, Long timestamp1,
																		Long timestamp2, Integer bin) {
		Criteria criteria = getCurrentSession(em).createCriteria(this.getClass());
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisis_code", crisisCode))
				.add(Restrictions.eq("attribute_code", attributeCode))
				.add(Restrictions.eq("label_code", labelCode))
				.add(Restrictions.ge("timestamp", timestamp1))
				.add(Restrictions.le("timestamp", timestamp2))
				.add(Restrictions.ge("bin", bin));
				
		criteria.add(criterion); 		
		try {
			List<FrequencyData> objList = (List<FrequencyData>) criteria.list();
			return objList;
		} catch (HibernateException e) {
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	@Override
	public List<FrequencyData> getDataInIntervalWithGranularityWithBin(String crisisCode, String attributeCode, String labelCode,
																		Long timestamp1, Long timestamp2, Long granularity, Integer bin) {
		Criteria criteria = getCurrentSession(em).createCriteria(this.getClass());
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisis_code", crisisCode))
				.add(Restrictions.eq("attribute_code", attributeCode))
				.add(Restrictions.eq("label_code", labelCode))
				.add(Restrictions.eq("granularity", granularity))
				.add(Restrictions.ge("timestamp", timestamp1))
				.add(Restrictions.le("timestamp", timestamp2))
				.add(Restrictions.ge("bin", bin));
				
		criteria.add(criterion); 		
		try {
			List<FrequencyData> objList = (List<FrequencyData>) criteria.list();
			return objList;
		} catch (HibernateException e) {
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	@Override
	public List<FrequencyData> getDataByCrisisInBin(String crisisCode, Integer bin) {
		Criteria criteria = getCurrentSession(em).createCriteria(this.getClass());
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisis_code", crisisCode))
				.add(Restrictions.eq("bin", bin));
		criteria.add(criterion); 
		try {
			List<FrequencyData> objList = (List<FrequencyData>) criteria.list();
			return objList;
		} catch (HibernateException e) {
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	@Override
	public List<FrequencyData> getDataByCrisisAttributeLabelInBin(String crisisCode, String attributeCode, String labelCode,
																		Integer bin) {
		Criteria criteria = getCurrentSession(em).createCriteria(this.getClass());
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisis_code", crisisCode))
				.add(Restrictions.eq("attribute_code", attributeCode))
				.add(Restrictions.eq("label_code", labelCode))
				.add(Restrictions.eq("bin", bin));
		criteria.add(criterion); 		
		try {
			List<FrequencyData> objList = (List<FrequencyData>) criteria.list();
			return objList;
		} catch (HibernateException e) {
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	@Override
	public List<FrequencyData> getDataByCrisisAttributeLabelGranularityInBin(String crisisCode, String attributeCode, String labelCode,
																		Long granularity, Integer bin) {
		Criteria criteria = getCurrentSession(em).createCriteria(this.getClass());
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisis_code", crisisCode))
				.add(Restrictions.eq("attribute_code", attributeCode))
				.add(Restrictions.eq("label_code", labelCode))
				.add(Restrictions.eq("granularity", granularity))
				.add(Restrictions.eq("bin", bin));
		criteria.add(criterion); 		
		try {
			List<FrequencyData> objList = (List<FrequencyData>) criteria.list();
			return objList;
		} catch (HibernateException e) {
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	@Override
	public List<FrequencyData> getDataAfterTimestampInBin(String crisisCode, String attributeCode, String labelCode, 
																		Long timestamp, Integer bin) {
		Criteria criteria = getCurrentSession(em).createCriteria(this.getClass());
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisis_code", crisisCode))
				.add(Restrictions.eq("attribute_code", attributeCode))
				.add(Restrictions.eq("label_code", labelCode))
				.add(Restrictions.ge("timestamp", timestamp))
				.add(Restrictions.eq("bin", bin));
				
		criteria.add(criterion); 		
		try {
			List<FrequencyData> objList = (List<FrequencyData>) criteria.list();
			return objList;
		} catch (HibernateException e) {
			logger.error(elog.toStringException(e));
		}
		return null;	
	}

	@Override
	public List<FrequencyData> getDataAfterTimestampGranularityInBin(String crisisCode, String attributeCode, String labelCode,
																		Long timestamp, Long granularity, Integer bin) {
		Criteria criteria = getCurrentSession(em).createCriteria(this.getClass());
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisis_code", crisisCode))
				.add(Restrictions.eq("attribute_code", attributeCode))
				.add(Restrictions.eq("label_code", labelCode))
				.add(Restrictions.ge("timestamp", timestamp))
				.add(Restrictions.eq("granularity", granularity))
				.add(Restrictions.eq("bin", bin));
				
		criteria.add(criterion); 		
		try {
			List<FrequencyData> objList = (List<FrequencyData>) criteria.list();
			return objList;
		} catch (HibernateException e) {
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	@Override
	public List<FrequencyData> getDataBeforeTimestampInBin(String crisisCode, String attributeCode, String labelCode, 
																		Long timestamp, Integer bin) {
		Criteria criteria = getCurrentSession(em).createCriteria(this.getClass());
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisis_code", crisisCode))
				.add(Restrictions.eq("attribute_code", attributeCode))
				.add(Restrictions.eq("label_code", labelCode))
				.add(Restrictions.le("timestamp", timestamp))
				.add(Restrictions.eq("bin", bin));
				
		criteria.add(criterion); 		
		try {
			List<FrequencyData> objList = (List<FrequencyData>) criteria.list();
			return objList;
		} catch (HibernateException e) {
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	@Override
	public List<FrequencyData> getDataBeforeTimestampGranularityInBin(String crisisCode, String attributeCode, String labelCode,
																		Long timestamp, Long granularity, Integer bin) {
		Criteria criteria = getCurrentSession(em).createCriteria(this.getClass());
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisis_code", crisisCode))
				.add(Restrictions.eq("attribute_code", attributeCode))
				.add(Restrictions.eq("label_code", labelCode))
				.add(Restrictions.le("timestamp", timestamp))
				.add(Restrictions.eq("granularity", granularity))
				.add(Restrictions.eq("bin", bin));
				
		criteria.add(criterion); 		
		try {
			List<FrequencyData> objList = (List<FrequencyData>) criteria.list();
			return objList;
		} catch (HibernateException e) {
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	@Override
	public List<FrequencyData> getDataInIntervalInBin(String crisisCode, String attributeCode, String labelCode, Long timestamp1,
																		Long timestamp2, Integer bin) {
		Criteria criteria = getCurrentSession(em).createCriteria(this.getClass());
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisis_code", crisisCode))
				.add(Restrictions.eq("attribute_code", attributeCode))
				.add(Restrictions.eq("label_code", labelCode))
				.add(Restrictions.ge("timestamp", timestamp1))
				.add(Restrictions.le("timestamp", timestamp2))
				.add(Restrictions.eq("bin", bin));
				
		criteria.add(criterion); 		
		try {
			List<FrequencyData> objList = (List<FrequencyData>) criteria.list();
			return objList;
		} catch (HibernateException e) {
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	@Override
	public List<FrequencyData> getDataInIntervalWithGranularityInBin(String crisisCode, String attributeCode, String labelCode,
																		Long timestamp1, Long timestamp2, Long granularity, Integer bin) {
		Criteria criteria = getCurrentSession(em).createCriteria(this.getClass());
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisis_code", crisisCode))
				.add(Restrictions.eq("attribute_code", attributeCode))
				.add(Restrictions.eq("label_code", labelCode))
				.add(Restrictions.eq("granularity", granularity))
				.add(Restrictions.ge("timestamp", timestamp1))
				.add(Restrictions.le("timestamp", timestamp2))
				.add(Restrictions.eq("bin", bin));
				
		criteria.add(criterion); 		
		try {
			List<FrequencyData> objList = (List<FrequencyData>) criteria.list();
			return objList;
		} catch (HibernateException e) {
			logger.error(elog.toStringException(e));
		}
		return null;
	}
}
