package qa.qcri.aidr.manager.repository;

import java.io.Serializable;
import java.util.List;

import qa.qcri.aidr.manager.hibernateEntities.UserEntity;

public interface UserRepository extends GenericRepository<UserEntity, Serializable>{

	public UserEntity fetchByUsername(String username);

    public List<UserEntity> getUsers(String query, Integer start, Integer limit);

    public Long getUsersCount(String query);
}
