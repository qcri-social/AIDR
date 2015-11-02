package qa.qcri.aidr.manager.service;

import java.util.List;

import org.json.simple.JSONArray;

import qa.qcri.aidr.manager.RoleType;
import qa.qcri.aidr.manager.hibernateEntities.AidrCollection;
import qa.qcri.aidr.manager.hibernateEntities.UserAccount;

public interface UserService {

	public void save(UserAccount user);

    public UserAccount fetchByUserName(String username);

    public UserAccount getById(Long id);

    public List<UserAccount> getUsers(String query, Integer start, Integer limit);

    public Long getUsersCount(String query);

    public boolean isUserInCollectionManagersList(UserAccount user, AidrCollection collection);

    public boolean isUserAdmin(UserAccount user);

    public UserAccount getAvailableUser(JSONArray users);
    
    public List<RoleType> getUserRoles(Long userId);

	void update(UserAccount user) throws Exception;

}
