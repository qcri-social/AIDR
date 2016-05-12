package qa.qcri.aidr.manager.service;

import qa.qcri.aidr.manager.persistence.entities.UserConnection;

import java.util.List;

public interface UserConnectionService {
	public void register (UserConnection userConnection);

    public List<UserConnection> getByUserIdAndProviderUserId (String userId, String providerUserId);

    public UserConnection getByUserIdAndProviderId (String userId, String providerId);
    
    public void update (UserConnection userConnection);
    
    public UserConnection fetchByCombinedUserName (String userName);
    
}
