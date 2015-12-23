package qa.qcri.aidr.data.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import qa.qcri.aidr.data.RoleType;
import qa.qcri.aidr.data.persistence.entity.Role;
import qa.qcri.aidr.data.persistence.entity.UserAccount;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long>{

	public Role findByRoleType(RoleType roleType);

}
