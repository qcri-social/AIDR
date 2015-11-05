package qa.qcri.aidr.manager.repository;

import java.io.Serializable;

import qa.qcri.aidr.manager.RoleType;
import qa.qcri.aidr.manager.persistence.entities.Role;


public interface RoleRepository extends GenericRepository<Role, Serializable>{

	public Role findByRoleType(RoleType roleType);
}
