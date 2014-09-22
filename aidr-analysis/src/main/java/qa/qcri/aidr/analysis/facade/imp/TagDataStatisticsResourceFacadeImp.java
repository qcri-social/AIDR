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

import qa.qcri.aidr.analysis.entity.TagData;
import qa.qcri.aidr.analysis.entity.TagDataPK;
import qa.qcri.aidr.analysis.facade.TagDataStatisticsResourceFacade;
import qa.qcri.aidr.analysis.utils.CommonOperations;
import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.common.values.ReturnCode;

@Stateless
public class TagDataStatisticsResourceFacadeImp extends CommonOperations implements TagDataStatisticsResourceFacade {

	private static Logger logger = LoggerFactory.getLogger(TagDataStatisticsResourceFacadeImp.class);
	private static ErrorLog elog = new ErrorLog();
	@PersistenceContext(unitName = "qa.qcri.aidr.analysis-EJBS")
	private EntityManager em;

	@Override
	public ReturnCode writeData(TagData tagData) {
		try {
			em.persist(tagData);
			return ReturnCode.SUCCESS;
		} catch (Exception e) {
			logger.info("Error in persisting data for: " + tagData.getCrisisCode() + ", " + tagData.getAttributeCode() + ", " + tagData.getLabelCode());
			logger.error(elog.toStringException(e));
			return ReturnCode.FAIL;
		}
	}

	@Override
	public TagData getDataByPK(TagDataPK tagDataPK) {
		Criteria criteria = getCurrentSession(em).createCriteria(this.getClass());
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisis_code", tagDataPK.getCrisisCode()))
				.add(Restrictions.eq("timestamp", tagDataPK.getTimestamp()))
				.add(Restrictions.eq("granularity", tagDataPK.getGranularity()))
				.add(Restrictions.eq("attribute_code", tagDataPK.getAttributeCode()))
				.add(Restrictions.eq("label_code", tagDataPK.getLabelCode()));
		criteria.add(criterion); 		
		try {
			TagData obj = (TagData) criteria.uniqueResult();
			return obj;
		} catch (HibernateException e) {
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	@Override
	public List<TagData> getDataByCrisisAttributeLabel(String crisisCode, String attributeCode, String labelCode) {
		Criteria criteria = getCurrentSession(em).createCriteria(this.getClass());
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisis_code", crisisCode))
				.add(Restrictions.eq("attribute_code", attributeCode))
				.add(Restrictions.eq("label_code", labelCode));
		criteria.add(criterion); 		
		try {
			List<TagData> objList = (List<TagData>) criteria.list();
			return objList;
		} catch (HibernateException e) {
			logger.error(elog.toStringException(e));
		}
		return null;
	}

}
