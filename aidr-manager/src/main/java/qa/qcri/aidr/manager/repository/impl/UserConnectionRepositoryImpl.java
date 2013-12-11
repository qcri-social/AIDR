package qa.qcri.aidr.manager.repository.impl;

import java.io.Serializable;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import qa.qcri.aidr.manager.hibernateEntities.UserConnection;
import qa.qcri.aidr.manager.repository.UserConnectionRepository;

@Repository("userConnectionRepository")
public class UserConnectionRepositoryImpl  extends GenericRepositoryImpl<UserConnection,Serializable> implements UserConnectionRepository {

	@Override
	public UserConnection fetchbyUsername(String userName) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(UserConnection.class);
		criteria.add(Restrictions.eq("userId", userName));
		criteria.setMaxResults(1);
		return (UserConnection) criteria.uniqueResult();
	}

}
