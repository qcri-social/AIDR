package qa.qcri.aidr.manager.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import qa.qcri.aidr.manager.RoleType;
import qa.qcri.aidr.manager.hibernateEntities.AidrCollection;
import qa.qcri.aidr.manager.hibernateEntities.UserAccount;
import qa.qcri.aidr.manager.service.CollectionService;

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
			UserAccount entity = getAuthenticatedUser();
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
            List<UserAccount> users = Collections.emptyList();
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
    public Map<String,Object> addManagerToCollection(@RequestParam String code, @RequestParam Long userId) throws Exception {
        logger.info("Add manager to Collection");
        String msg = "Error while adding manager to collection managers list";
        try{
            if (code == null || code.trim().length() == 0 || userId == null){
                return getUIWrapper(false, msg);
            }
            UserAccount userEntity = userService.getById(userId);
            AidrCollection collection = collectionService.findByCode(code);

            if (userService.isUserInCollectionManagersList(userEntity, collection)){
                msg = "Selected user is already in managers list of this collection.";
                return getUIWrapper(false, msg);
            }

            List<UserAccount> managers = collection.getManagers();
            if (managers == null){
                managers = new ArrayList<UserAccount>();
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
            List<UserAccount> managers = collection.getManagers();
            if (managers == null){
                return getUIWrapper(false, msg);
            }

            for (UserAccount manager : managers){
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
	
    @RequestMapping(value = "/getCurrentUser.action", method={RequestMethod.GET})
	@ResponseBody
	public Map<String,Object> getCurrentUser() throws Exception {
		logger.info("Get current user info");
		try{
			UserAccount entity = getAuthenticatedUser();
			return getUIWrapper(entity, true);
		}catch(Exception e){
            String msg = "Error while getting current user's info ";
			logger.error(msg, e);
			return getUIWrapper(false, msg);
		}
	}
    

    @RequestMapping(value = "/updateUserInfo.action", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> updateUserInfo(@RequestParam Long userId, @RequestParam (required=false) String email, @RequestParam (required=false) String locale) throws Exception {
        logger.info("Updating userInfo");
        String msg = "Error while updating userInfo";
        try{
            if (userId == null){
                return getUIWrapper(false, msg);
            }
            UserAccount currentUser = getAuthenticatedUser();
            if(currentUser.getId()==userId){
            	if(!StringUtils.isEmpty(locale)){
            		currentUser.setLocale(locale);
            	}
            	if(email!=null){
            		currentUser.setEmail(email);
            	}
            	
            	userService.update(currentUser);
            	currentUser = userService.getById(userId);
                return getUIWrapper(currentUser, true);
            }
            return getUIWrapper(false, "You can't update other user's info");
           
        }catch(Exception e){
            logger.error(msg, e);
            return getUIWrapper(false, msg);
        }
    }
}
