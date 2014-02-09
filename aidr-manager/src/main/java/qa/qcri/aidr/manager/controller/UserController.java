package qa.qcri.aidr.manager.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import qa.qcri.aidr.manager.hibernateEntities.UserEntity;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("protected/user")
public class UserController extends BaseController{

	private Logger logger = Logger.getLogger(UserController.class);

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
			return getUIWrapper(entity, true);
		}catch(Exception e){
            String msg = "Error while getting current user";
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
                for (UserEntity user : users) {
                    user.setRoles(null);
                }
            }
            return getUIWrapper(users, total);
        }catch(Exception e){
            String msg = "Error while getting users list";
            logger.error(msg, e);
            return getUIWrapper(false, msg);
        }
    }
	
}
