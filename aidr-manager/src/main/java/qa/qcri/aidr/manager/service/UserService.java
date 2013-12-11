package qa.qcri.aidr.manager.service;

import qa.qcri.aidr.manager.hibernateEntities.UserEntity;

public interface UserService {

	public void save(UserEntity user);
	public UserEntity fetchByUserName(String username);
	
}
