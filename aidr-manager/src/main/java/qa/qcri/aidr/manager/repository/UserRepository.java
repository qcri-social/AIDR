package qa.qcri.aidr.manager.repository;

import java.io.Serializable;

import qa.qcri.aidr.manager.hibernateEntities.UserEntity;

public interface UserRepository extends GenericRepository<UserEntity, Serializable>{

	public UserEntity fetchByUsername(String username);
	
}
