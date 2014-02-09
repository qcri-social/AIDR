package qa.qcri.aidr.manager.service;

import qa.qcri.aidr.manager.hibernateEntities.UserEntity;

import java.util.List;

public interface UserService {

	public void save(UserEntity user);

    public UserEntity fetchByUserName(String username);

    public List<UserEntity> getUsers(String query, Integer start, Integer limit);

    public Long getUsersCount(String query);

}
