package qa.qcri.aidr.manager.repository.impl;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import qa.qcri.aidr.manager.RoleType;
import qa.qcri.aidr.manager.hibernateEntities.Role;
import qa.qcri.aidr.manager.repository.RoleRepository;

@Repository
public class RoleRepositoryImpl extends GenericRepositoryImpl<Role,Serializable> implements RoleRepository {

	private Logger logger = Logger.getLogger(RoleRepositoryImpl.class);

	@Override
	public Role findByRoleType(RoleType roleType) {
		Role role = null;
		
		try {
			Criteria criteria = getHibernateTemplate().getSessionFactory()
					.getCurrentSession().createCriteria(Role.class);
			Criterion criterion = Restrictions.eq("roleType", roleType);
			criteria.add(criterion);
		    role = (Role) criteria.uniqueResult();
		} catch(Exception e) {
			logger.error("Error in fetching data by roleType : " +roleType, e);
		}
		
		return role;
	}
}
