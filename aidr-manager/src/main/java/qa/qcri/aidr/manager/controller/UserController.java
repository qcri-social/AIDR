package qa.qcri.aidr.manager.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import qa.qcri.aidr.manager.RoleType;
import qa.qcri.aidr.manager.hibernateEntities.AidrCollection;
import qa.qcri.aidr.manager.hibernateEntities.UserEntity;
import qa.qcri.aidr.manager.hibernateEntities.UserRole;
import qa.qcri.aidr.manager.service.CollectionService;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("protected/user")
public class UserController extends BaseController{

	private Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    private CollectionService collectionService;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
	@RequestMapping(value = "/getCurrentUserRoles.action", method={RequestMethod.GET})
	@ResponseBody
	public Map<String,Object> getCurrentUserRoles() throws Exception {
		logger.info("Get current user roles");
		try{
			UserEntity entity = getAuthenticatedUser();
			List<RoleType> roles = Collections.EMPTY_LIST;
			if(entity != null) {
				roles = userService.getUserRoles(entity.getId());
			}
			return getUIWrapper(roles, true);
		}catch(Exception e){
            String msg = "Error while getting current user ";
			logger.error(msg, e);
			return getUIWrapper(false, msg);
		}
	}

    @RequestMapping(value = "/getUsers.action", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getUsers(@RequestParam(value = "query", defaultValue = "") String query,
                                       @RequestParam(value = "start", defaultValue = "0") Integer start,
                                       @RequestParam(value = "limit", defaultValue = "10") Integer limit) throws Exception {
        logger.info("Get users list");
        try{
            Long total = userService.getUsersCount(query);
            List<UserEntity> users = Collections.emptyList();
            if (total > 0) {
                users = userService.getUsers(query, start, limit);
            }
            return getUIWrapper(users, total);
        }catch(Exception e){
            String msg = "Error while getting users list";
            logger.error(msg, e);
            return getUIWrapper(false, msg);
        }
    }

    @RequestMapping(value = "/addManagerToCollection.action", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> addManagerToCollection(@RequestParam String code, @RequestParam Integer userId) throws Exception {
        logger.info("Add manager to Collection");
        String msg = "Error while adding manager to collection managers list";
        try{
            if (code == null || code.trim().length() == 0 || userId == null){
                return getUIWrapper(false, msg);
            }
            UserEntity userEntity = userService.getById(userId);
            AidrCollection collection = collectionService.findByCode(code);

            if (userService.isUserInCollectionManagersList(userEntity, collection)){
                msg = "Selected user is already in managers list of this collection.";
                return getUIWrapper(false, msg);
            }

            List<UserEntity> managers = collection.getManagers();
            if (managers == null){
                managers = new ArrayList<UserEntity>();
            }

            managers.add(userEntity);
            collectionService.update(collection);

            return getUIWrapper(userEntity, true);
        }catch(Exception e){
            logger.error(msg, e);
            return getUIWrapper(false, msg);
        }
    }

    @RequestMapping(value = "/removeManagerFromCollection.action", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> removeManagerFromCollection(@RequestParam String code, @RequestParam Integer userId) throws Exception {
        logger.info("Add manager to Collection");
        String msg = "Error while removing user from collection managers list";
        try{
            if (code == null || code.trim().length() == 0 || userId == null){
                return getUIWrapper(false, msg);
            }
            AidrCollection collection = collectionService.findByCode(code);
            List<UserEntity> managers = collection.getManagers();
            if (managers == null){
                return getUIWrapper(false, msg);
            }

            for (UserEntity manager : managers){
                if (manager.getId().equals(userId)){
                    managers.remove(manager);
                    break;
                }
            }

            collectionService.update(collection);

            return getUIWrapper(managers, true);
        }catch(Exception e){
            logger.error(msg, e);
            return getUIWrapper(false, msg);
        }
    }
	
}
