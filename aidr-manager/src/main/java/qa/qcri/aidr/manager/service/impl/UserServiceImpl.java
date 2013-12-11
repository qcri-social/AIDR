package qa.qcri.aidr.manager.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qa.qcri.aidr.manager.hibernateEntities.UserEntity;
import qa.qcri.aidr.manager.repository.UserRepository;
import qa.qcri.aidr.manager.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService{

	@Resource(name="userRepository")
	private UserRepository userRepository;
	
	@Override
	@Transactional(readOnly=false)
	public void save(UserEntity user) {
		userRepository.save(user);
	}

	@Override
	@Transactional(readOnly=true)
	public UserEntity fetchByUserName(String username) {
		return userRepository.fetchByUsername(username);
	}

}
