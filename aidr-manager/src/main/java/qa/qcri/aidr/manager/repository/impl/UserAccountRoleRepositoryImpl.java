/**
 * 
 */
package qa.qcri.aidr.manager.repository.impl;

/**
 * @author Latika
 *
 */
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import qa.qcri.aidr.manager.persistence.entities.UserAccountRole;
import qa.qcri.aidr.manager.repository.UserAccountRoleRepository;

@Repository
public class UserAccountRoleRepositoryImpl extends GenericRepositoryImpl<UserAccountRole, Long> implements UserAccountRoleRepository {

	private Logger logger = Logger.getLogger(UserAccountRoleRepositoryImpl.class);
	
	@Override
	public List<UserAccountRole> fetchByAccountId(Long userId) {
		
		List<UserAccountRole> roles = Collections.EMPTY_LIST;
		
		try {
			Criteria criteria = getHibernateTemplate().getSessionFactory()
					.getCurrentSession().createCriteria(UserAccountRole.class);
	        criteria.add(Restrictions.eq("account.id", userId));
	        roles = (List<UserAccountRole>) criteria.list();
		} catch (Exception e) {
			logger.error("Error in fetching roles by userID : " + userId, e);
		}
		return roles;
	}

	@Override
	public UserAccountRole fetchByUserIdAndRole(Long userId, Long roleId) {
		UserAccountRole userRole = null;
		
		try {
			Criteria criteria = getHibernateTemplate().getSessionFactory()
					.getCurrentSession().createCriteria(UserAccountRole.class);
			Criterion criterion = Restrictions.conjunction()
					.add(Restrictions.eq("account.id", userId))
					.add(Restrictions.eq("role.id", roleId));
			criteria.add(criterion);
		    userRole = (UserAccountRole) criteria.uniqueResult();
		} catch(Exception e) {
			logger.error("Error in fetching data by userId : " + userId + " and roleId : " + roleId, e);
		}
		
		return userRole;
	}
}

