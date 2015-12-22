/**
 * 
 */
package qa.qcri.aidr.data.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import qa.qcri.aidr.data.persistence.entity.UserAccountRole;

/**
 * @author Latika
 *
 */
public interface UserAccountRoleRepository extends CrudRepository<UserAccountRole, Long>{
	
	public List<UserAccountRole> findByAccountId(Long userId);
	
}
