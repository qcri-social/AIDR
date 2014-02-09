package qa.qcri.aidr.manager.repository.impl;

import java.io.Serializable;
import java.net.URLDecoder;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.ScrollableResults;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import org.springframework.util.StringUtils;
import qa.qcri.aidr.manager.hibernateEntities.AidrCollection;
import qa.qcri.aidr.manager.hibernateEntities.UserEntity;
import qa.qcri.aidr.manager.repository.UserRepository;
import qa.qcri.aidr.manager.util.CollectionStatus;

@Repository("userRepository")
public class UserRepositoryImpl extends GenericRepositoryImpl<UserEntity,Serializable> implements UserRepository {

	@Override
	public UserEntity fetchByUsername(String username) {
		
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(UserEntity.class);
		criteria.add(Restrictions.eq("userName", username));
		return (UserEntity) criteria.uniqueResult();
	}

	@Override
    public List<UserEntity> getUsers(String query, Integer start, Integer limit) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(UserEntity.class);

        if (StringUtils.hasText(query)){
            String wildcard ='%'+ URLDecoder.decode(query.trim())+'%';
            criteria.add(Restrictions.ilike("userName", wildcard));
        }

        if (start != null) {
            criteria.setFirstResult(start);
        }
        if (limit != null) {
            criteria.setMaxResults(limit);
        }

        return (List<UserEntity>) criteria.list();
	}

	@Override
    public Long getUsersCount(String query) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(UserEntity.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        if (StringUtils.hasText(query)){
            String wildcard ='%'+ URLDecoder.decode(query.trim())+'%';
            criteria.add(Restrictions.ilike("userName", wildcard));
        }

        ScrollableResults scroll = criteria.scroll();
        int i = scroll.last() ? scroll.getRowNumber() + 1 : 0;
        return Long.valueOf(i);
	}

}
