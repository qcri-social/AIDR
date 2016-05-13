package qa.qcri.aidr.data.service;

import java.util.List;

import qa.qcri.aidr.data.persistence.entity.UserConnection;

public interface UserConnectionService {
	public void register (UserConnection userConnection);
	
    public UserConnection getByProviderIdAndUserId (String providerId , String userId);

    public List<UserConnection> getByProviderUserIdAndUserId (String providerUserId , String userId);
    
    public void update (UserConnection userConnection);

}
