/**
 * 
 */
package qa.qcri.aidr.manager.repository.impl;

/**
 * @author Latika
 *
 */
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import qa.qcri.aidr.manager.RoleType;
import qa.qcri.aidr.manager.hibernateEntities.UserRole;
import qa.qcri.aidr.manager.repository.UserRoleRepository;

@Repository
public class UserRoleRepositoryImpl extends GenericRepositoryImpl<UserRole, Long> implements UserRoleRepository {

	@Override
	public UserRole findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RoleType> fetchByUserId(Integer userId) {
		Criteria criteria = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createCriteria(UserRole.class);
        criteria.add(Restrictions.eq("user.id", userId));
        criteria.setProjection(Projections.property("roleType"));
        return (List<RoleType>) criteria.list();
	}

	@Override
	public UserRole fetchByUserIdAndRoleType(Integer userId,
			RoleType roleType) {
		Criteria criteria = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createCriteria(UserRole.class);
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("user.id", userId))
				.add(Restrictions.eq("roleType", roleType.name()));
        return (UserRole) criteria.uniqueResult();
	}
}

