package qa.qcri.aidr.manager.repository.impl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import qa.qcri.aidr.manager.persistence.entities.UserConnection;
import qa.qcri.aidr.manager.repository.UserConnectionRepository;

@Repository("userConnectionRepository")
public class UserConnectionRepositoryImpl  extends GenericRepositoryImpl<UserConnection,Serializable> implements UserConnectionRepository {

	@Override
	public List<UserConnection> getByUserIdAndProviderUserId(String userId,
			String providerUserId) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(UserConnection.class);
        criteria.add(Restrictions.eq("userId", userId));
        criteria.add(Restrictions.eq("providerUserId", providerUserId));
        return criteria.list();
	}

	@Override
	public UserConnection getByUserIdAndProviderId(String userId,
			String providerId) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(UserConnection.class);
        criteria.add(Restrictions.eq("userId", userId));
        criteria.add(Restrictions.eq("providerId", providerId));
        criteria.setMaxResults(1);
        return (UserConnection) criteria.uniqueResult();
	}
}
