package qa.qcri.aidr.manager.repository;

import java.io.Serializable;
import java.util.List;

import qa.qcri.aidr.manager.persistence.entities.UserConnection;

public interface UserConnectionRepository extends GenericRepository<UserConnection, Serializable>{

    public List<UserConnection> getByUserIdAndProviderUserId(String userId, String providerUserId);
    
    public UserConnection getByUserIdAndProviderId(String userId, String providerId);
}
