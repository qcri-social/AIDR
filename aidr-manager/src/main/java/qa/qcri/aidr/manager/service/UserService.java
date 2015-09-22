package qa.qcri.aidr.manager.service;

import java.util.List;

import org.json.simple.JSONArray;

import qa.qcri.aidr.manager.RoleType;
import qa.qcri.aidr.manager.hibernateEntities.AidrCollection;
import qa.qcri.aidr.manager.hibernateEntities.UserEntity;

public interface UserService {

	public void save(UserEntity user);

    public UserEntity fetchByUserName(String username);

    public UserEntity getById(Integer id);

    public List<UserEntity> getUsers(String query, Integer start, Integer limit);

    public Long getUsersCount(String query);

    public boolean isUserInCollectionManagersList(UserEntity user, AidrCollection collection);

    public boolean isUserAdmin(UserEntity user);

    public UserEntity getAvailableUser(JSONArray users);
    
    public List<RoleType> getUserRoles(Integer userId);

}
