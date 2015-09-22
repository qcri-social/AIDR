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
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import qa.qcri.aidr.manager.RoleType;
import qa.qcri.aidr.manager.hibernateEntities.UserRole;
import qa.qcri.aidr.manager.repository.UserRoleRepository;

@Repository
public class UserRoleRepositoryImpl extends GenericRepositoryImpl<UserRole, Long> implements UserRoleRepository {

	private Logger logger = Logger.getLogger(UserRoleRepositoryImpl.class);
	
	@Override
	public List<RoleType> fetchByUserId(Integer userId) {
		
		List<RoleType> roles = Collections.EMPTY_LIST;
		
		try {
			Criteria criteria = getHibernateTemplate().getSessionFactory()
					.getCurrentSession().createCriteria(UserRole.class);
	        criteria.add(Restrictions.eq("user.id", userId));
	        criteria.setProjection(Projections.property("roleType"));
	        roles = (List<RoleType>) criteria.list();
		} catch (Exception e) {
			logger.error("Error in fetching roles by userID : " + userId, e);
		}
		return roles;
	}

	@Override
	public UserRole fetchByUserIdAndRoleType(Integer userId,
			RoleType roleType) {
		UserRole userRole = new UserRole();
		
		try {
			Criteria criteria = getHibernateTemplate().getSessionFactory()
					.getCurrentSession().createCriteria(UserRole.class);
			Criterion criterion = Restrictions.conjunction()
					.add(Restrictions.eq("user.id", userId))
					.add(Restrictions.eq("roleType", roleType));
			criteria.add(criterion);
		    userRole = (UserRole) criteria.uniqueResult();
		} catch(Exception e) {
			logger.error("Error in fetching data by userId : " + userId + " and roleType : " + roleType, e);
		}
		
		return userRole;
	}
}

