/**
 * 
 */
package qa.qcri.aidr.manager.repository;

import java.util.List;

import qa.qcri.aidr.manager.RoleType;
import qa.qcri.aidr.manager.hibernateEntities.UserRole;

/**
 * @author Latika
 *
 */
public interface UserRoleRepository extends GenericRepository<UserRole, Long>{
	public List<RoleType> fetchByUserId(Integer userId);
	public UserRole fetchByUserIdAndRoleType(Integer userId, RoleType roleType);
}
