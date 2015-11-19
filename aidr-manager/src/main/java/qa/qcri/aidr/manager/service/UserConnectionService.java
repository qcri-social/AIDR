package qa.qcri.aidr.manager.service;

import qa.qcri.aidr.manager.persistence.entities.UserConnection;

import java.util.List;

public interface UserConnectionService {
	public void register (UserConnection userConnection);

    public List<UserConnection> getByUserId (String userId);

    public void update (UserConnection userConnection);
}
