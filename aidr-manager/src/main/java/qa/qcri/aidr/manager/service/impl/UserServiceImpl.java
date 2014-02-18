package qa.qcri.aidr.manager.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qa.qcri.aidr.manager.hibernateEntities.AidrCollection;
import qa.qcri.aidr.manager.hibernateEntities.Role;
import qa.qcri.aidr.manager.hibernateEntities.UserEntity;
import qa.qcri.aidr.manager.repository.UserRepository;
import qa.qcri.aidr.manager.service.UserService;

import java.util.List;

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

	@Override
	@Transactional(readOnly=true)
	public UserEntity getById(Integer id) {
		return userRepository.getById(id);
	}

	@Override
	@Transactional(readOnly=true)
    public List<UserEntity> getUsers(String query, Integer start, Integer limit) {
		return userRepository.getUsers(query, start, limit);
	}

	@Override
	@Transactional(readOnly=true)
    public Long getUsersCount(String query) {
		return userRepository.getUsersCount(query);
	}

    public boolean isUserInCollectionManagersList(UserEntity user, AidrCollection collection) {
        if (collection.getManagers() == null) {
            return false;
        }
        for (UserEntity manager : collection.getManagers()){
            if (manager.getId().equals(user.getId())){
                return true;
            }
        }
        return false;
    }

    public boolean isUserAdmin(UserEntity user) {
        List<Role> roles = user.getRoles();
        if(roles == null){
            return false;
        }
        for(Role role : roles) {
            String roleName = role.getName().toLowerCase();
            if("admin".equals(roleName)){
                return true;
            }
        }
        return false;
    }

}
