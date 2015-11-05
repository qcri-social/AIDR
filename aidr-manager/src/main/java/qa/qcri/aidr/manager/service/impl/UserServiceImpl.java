package qa.qcri.aidr.manager.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qa.qcri.aidr.manager.RoleType;
import qa.qcri.aidr.manager.persistence.entities.Collection;
import qa.qcri.aidr.manager.persistence.entities.Role;
import qa.qcri.aidr.manager.persistence.entities.UserAccount;
import qa.qcri.aidr.manager.persistence.entities.UserAccountRole;
import qa.qcri.aidr.manager.repository.CollectionRepository;
import qa.qcri.aidr.manager.repository.RoleRepository;
import qa.qcri.aidr.manager.repository.UserAccountRepository;
import qa.qcri.aidr.manager.repository.UserAccountRoleRepository;
import qa.qcri.aidr.manager.service.UserService;
import qa.qcri.aidr.manager.util.CollectionStatus;

@Service("userService")
public class UserServiceImpl implements UserService{
    private Logger logger = Logger.getLogger(getClass());

	@Resource(name="userRepository")
	private UserAccountRepository userRepository;

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private UserAccountRoleRepository userRoleRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
	@Override
	@Transactional(readOnly=false)
	public void save(UserAccount user) {
		userRepository.save(user);
	}

	@Override
	@Transactional(readOnly=true)
	public UserAccount fetchByUserName(String username) {
		return userRepository.fetchByUsername(username);
	}

	@Override
	@Transactional(readOnly=true)
	public UserAccount getById(Long id) {
		return userRepository.getById(id);
	}

	@Override
	@Transactional(readOnly=true)
    public List<UserAccount> getUsers(String query, Integer start, Integer limit) {
		return userRepository.getUsers(query, start, limit);
	}

	@Override
	@Transactional(readOnly=true)
    public Long getUsersCount(String query) {
		return userRepository.getUsersCount(query);
	}

    public boolean isUserInCollectionManagersList(UserAccount user, Collection collection) {
        /*if (collection.getManagers() == null) {
            return false;
        }
        for (UserAccount manager : collection.getManagers()){
            if (manager.getId().equals(user.getId())){
                return true;
            }
        }*/
        return false;
    }

    @Override
    @Transactional(readOnly=false)
    public boolean isUserAdmin(UserAccount user) {
    	
    	Role role = roleRepository.findByRoleType(RoleType.ADMIN);
        UserAccountRole userRole = userRoleRepository.fetchByUserIdAndRole(user.getId(), role.getId());
        return (userRole != null && userRole.getRole().getRoleType().equals(RoleType.ADMIN)) ? 
        		true : false;
    }

    @Override
    @Transactional(readOnly=false)
    public UserAccount getAvailableUser(JSONArray users) {
        SortedMap map = new TreeMap();
        UserAccount userEntity = null;
        Collection Collection;
        UserAccount unUsedUserEntity = null;

        for(Object user : users){
            String userName = (String) user;
            userEntity = fetchByUserName(userName);

            if(userEntity != null){
                List<Collection> collections = collectionRepository.getAllCollectionByUser(userEntity.getId());
                boolean addedOnMap = false;
                for(int i=0; i < collections.size(); i++ ){
                    if(collections.get(i).getStatus().equals(CollectionStatus.INITIALIZING) || collections.get(i).getStatus().equals(CollectionStatus.RUNNING)){
                        map.put(collections.get(i).getCreatedAt(), userEntity);
                        addedOnMap = true;
                    }
                }
                if(!addedOnMap){
                    unUsedUserEntity =  userEntity;
                }
            }
        }

        if(unUsedUserEntity != null){
            return unUsedUserEntity;
        }

        if(map.size() > 0 && unUsedUserEntity == null){
            logger.info("firstKey: " + userEntity.getUserName());
            return (UserAccount)map.get(map.firstKey());
        }

        return userEntity;  //To change body of implemented methods use File | Settings | File Templates.
    }

	@Override
	@Transactional(readOnly=true)
	public List<RoleType> getUserRoles(Long userId) {
		List<UserAccountRole> roles = userRoleRepository.fetchByAccountId(userId);
		
		List<RoleType> roleTypes = new ArrayList<RoleType>();
		if(roles != null) {
			for(UserAccountRole role : roles) {
				roleTypes.add(role.getRole().getRoleType());
			}
		}
		return roleTypes;
	}
	
	@Override
	@Transactional(readOnly = false)
	public void update(UserAccount user) throws Exception {
		userRepository.update(user);
	}

}
