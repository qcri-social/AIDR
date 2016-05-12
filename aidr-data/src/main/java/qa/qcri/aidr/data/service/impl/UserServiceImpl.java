package qa.qcri.aidr.data.service.impl;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qa.qcri.aidr.data.persistence.entity.UserAccount;
import qa.qcri.aidr.data.persistence.entity.UserAccountRole;
import qa.qcri.aidr.data.repository.UserAccountRepository;
import qa.qcri.aidr.data.repository.UserAccountRoleRepository;
import qa.qcri.aidr.data.service.UserService;
import qa.qcri.aidr.data.util.RoleType;


@Service("userService")
public class UserServiceImpl implements UserService{

	//@Resource(name="userRepository")
	@Autowired
	private UserAccountRepository userRepository;
	
	@Autowired
    private UserAccountRoleRepository userRoleRepository;
    
	@Override
	@Transactional(readOnly=false)
	public void save(UserAccount user) {
		userRepository.save(user);
	}
	
	@Override
	@Transactional(readOnly=true)
	public UserAccount fetchByUserName(String username) {
		return userRepository.findByUserName(username);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<RoleType> getUserRoles(Long userId) {
		List<UserAccountRole> roles = userRoleRepository.findByAccountId(userId);
		
		List<RoleType> roleTypes = new ArrayList<RoleType>();
		if(roles != null) {
			for(UserAccountRole role : roles) {
				roleTypes.add(role.getRole().getRoleType());
			}
		}
		return roleTypes;
	}
}
