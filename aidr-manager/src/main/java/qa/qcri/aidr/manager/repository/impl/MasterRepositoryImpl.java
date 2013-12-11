package qa.qcri.aidr.manager.repository.impl;

import java.io.Serializable;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import qa.qcri.aidr.manager.hibernateEntities.AidrMaster;
import qa.qcri.aidr.manager.repository.MasterRepository;

@Repository("masterRepository")
public class MasterRepositoryImpl extends GenericRepositoryImpl<AidrMaster,Serializable>  implements MasterRepository{

	@Override
	public AidrMaster findByKey(String key) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(AidrMaster.class);
		criteria.add(Restrictions.eq("key", key));
		return (AidrMaster) criteria.uniqueResult();
	}

}
