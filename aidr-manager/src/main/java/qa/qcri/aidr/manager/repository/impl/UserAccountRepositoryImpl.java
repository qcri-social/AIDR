package qa.qcri.aidr.manager.repository.impl;

import java.io.Serializable;
import java.net.URLDecoder;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.ScrollableResults;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import qa.qcri.aidr.manager.persistence.entities.UserAccount;
import qa.qcri.aidr.manager.repository.UserAccountRepository;

@Repository("userRepository")
public class UserAccountRepositoryImpl extends GenericRepositoryImpl<UserAccount,Serializable> implements UserAccountRepository {

	@Override
	public UserAccount fetchByUsername(String username) {
		
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(UserAccount.class);
		criteria.add(Restrictions.eq("userName", username));
		return (UserAccount) criteria.uniqueResult();
	}

    @Override
	public UserAccount getById(Long id) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(UserAccount.class);
		criteria.add(Restrictions.eq("id", id));
		return (UserAccount) criteria.uniqueResult();
	}

	@Override
    public List<UserAccount> getUsers(String query, Integer start, Integer limit) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(UserAccount.class);

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

        return (List<UserAccount>) criteria.list();
	}

	@Override
    public Long getUsersCount(String query) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(UserAccount.class);
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
