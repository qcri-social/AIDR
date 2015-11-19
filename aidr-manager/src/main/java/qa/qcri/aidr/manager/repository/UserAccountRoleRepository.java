/**
 * 
 */
package qa.qcri.aidr.manager.repository;

import java.util.List;

import qa.qcri.aidr.manager.persistence.entities.UserAccountRole;

/**
 * @author Latika
 *
 */
public interface UserAccountRoleRepository extends GenericRepository<UserAccountRole, Long>{
	public List<UserAccountRole> fetchByAccountId(Long userId);
	public UserAccountRole fetchByUserIdAndRole(Long userId, Long roleId);
}
