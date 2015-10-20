package qa.qcri.aidr.manager.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import qa.qcri.aidr.manager.hibernateEntities.UserAccount;
import qa.qcri.aidr.manager.service.UserService;

public class BaseController {
	
	@Resource(name="userService")
	protected UserService userService;

    protected <T> Map<String, Object> getUIWrapper(Boolean success) {
        return getUIWrapper(null, success, null, null);
    }

    protected <T> Map<String, Object> getUIWrapper(List<T> entityList, Long total) {
        return getUIWrapper(entityList, true, total, null);
    }

	protected <T> Map<String, Object> getUIWrapper(Boolean success, String message) {
		return getUIWrapper(null, success, null, message);
	}

	protected <T> Map<String, Object> getUIWrapper(Object data, Boolean success) {
		return getUIWrapper(data, success, null, null);
	}
	
	protected <T> Map<String, Object> getUIWrapper(Object data, Boolean success, Long total, String message) {
		Map<String, Object> modelMap = new HashMap<String, Object>(4);
        modelMap.put("total", total);
		modelMap.put("success", success);
		modelMap.put("data", data);
		modelMap.put("message", getMessage(success,message));
		return modelMap;
	}

    private String getMessage(Boolean success, String message){
        if (message != null){
            return message;
        }
        if (success != null && success){
            return "Successful";
        } else {
            return "Failure";
        }
    }

	protected UserAccount getAuthenticatedUser() throws Exception{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication!=null){
			return userService.fetchByUserName(authentication.getName());
		}else{
			throw new Exception("No user logged in ");
		}
	}

	protected String getAuthenticatedUserName() throws Exception{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication != null){
			return authentication.getName();
		}else{
			throw new Exception("No user logged in ");
		}
	}

    protected String getPublicUserName() throws Exception{

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        if(authentication != null){
            return authentication.getName();
        }else{
            return null;
        }
    }

}
