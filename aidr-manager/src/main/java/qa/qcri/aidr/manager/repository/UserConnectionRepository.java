package qa.qcri.aidr.manager.repository;

import java.io.Serializable;
import qa.qcri.aidr.manager.hibernateEntities.UserConnection;

public interface UserConnectionRepository extends GenericRepository<UserConnection, Serializable>{

	public UserConnection fetchbyUsername(String userName);
}
