package qa.qcri.aidr.manager.service.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qa.qcri.aidr.manager.controller.PublicController;
import qa.qcri.aidr.manager.hibernateEntities.AidrCollection;
import qa.qcri.aidr.manager.hibernateEntities.Role;
import qa.qcri.aidr.manager.hibernateEntities.UserEntity;
import qa.qcri.aidr.manager.repository.CollectionRepository;
import qa.qcri.aidr.manager.repository.UserRepository;
import qa.qcri.aidr.manager.service.CollectionService;
import qa.qcri.aidr.manager.service.UserService;
import qa.qcri.aidr.manager.util.CollectionStatus;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

@Service("userService")
public class UserServiceImpl implements UserService{
    private Logger logger = Logger.getLogger(getClass());

	@Resource(name="userRepository")
	private UserRepository userRepository;

    @Autowired
    private CollectionRepository collectionRepository;


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

    @Override
    @Transactional(readOnly=false)
    public UserEntity getAvailableUser(JSONArray users) {
        SortedMap map = new TreeMap();
        UserEntity userEntity = null;
        AidrCollection aidrCollection;
        UserEntity unUsedUserEntity = null;

        for(Object user : users){
            String userName = (String) user;
            userEntity = fetchByUserName(userName);

            if(userEntity != null){
                List<AidrCollection> aidrCollections = collectionRepository.getAllCollectionByUser(userEntity.getId());
                boolean addedOnMap = false;
                for(int i=0; i < aidrCollections.size(); i++ ){
                    if(aidrCollections.get(i).getStatus().equals(CollectionStatus.INITIALIZING) || aidrCollections.get(i).getStatus().equals(CollectionStatus.RUNNING)){
                        map.put(aidrCollections.get(i).getCreatedDate(), userEntity);
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
            return (UserEntity)map.get(map.firstKey());
        }

        return userEntity;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
