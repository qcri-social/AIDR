package qa.qcri.aidr.manager.repository;

import java.io.Serializable;
import java.util.List;

import qa.qcri.aidr.manager.hibernateEntities.UserConnection;

public interface UserConnectionRepository extends GenericRepository<UserConnection, Serializable>{

	public UserConnection fetchbyUsername(String userName);

    public List<UserConnection> getByUserId(String userId);
}
