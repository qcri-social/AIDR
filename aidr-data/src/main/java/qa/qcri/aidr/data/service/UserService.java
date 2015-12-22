package qa.qcri.aidr.data.service;

import java.util.List;

import qa.qcri.aidr.data.RoleType;
import qa.qcri.aidr.data.persistence.entity.UserAccount;

public interface UserService {

	public void save(UserAccount user);
	
	public List<RoleType> getUserRoles(Long userId);
	
	public UserAccount fetchByUserName(String username);

}
