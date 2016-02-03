package qa.qcri.aidr.data.controller;

import java.util.List;

import javax.annotation.Resource;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import qa.qcri.aidr.data.model.CollectionSummaryInfo;
import qa.qcri.aidr.data.persistence.entity.UserAccount;
import qa.qcri.aidr.data.persistence.entity.UserConnection;
import qa.qcri.aidr.data.service.CollectionSummaryService;
import qa.qcri.aidr.data.service.UserConnectionService;
import qa.qcri.aidr.data.service.UserService;

@Controller
public class HomeController {

	@Autowired
    protected CollectionSummaryService collectionSummaryService;
    
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(HomeController.class);
    
    @Resource(name="userService")
	protected UserService userService;
    
    @Autowired
    private UserConnectionService userConnectionService;
    
    @RequestMapping(value = "/dashboard/list")    
    @PreAuthorize("hasRole('ROLE_USER_SPRINGSOCIALSECURITY')")
    @ResponseBody
    public List<CollectionSummaryInfo> list(){
    	List<CollectionSummaryInfo> list = collectionSummaryService.fetchAllCollections();
        return list;
    }
    
    @ResponseBody 
    @PreAuthorize("hasRole('ROLE_USER_SPRINGSOCIALSECURITY')")
    @RequestMapping(value = "/dashboard")
    public ModelAndView index(){
    	ModelAndView view = new ModelAndView();  
        view.setViewName("index"); //name of the jsp-file in the "page" folder  
          
        String str = "MVC Spring is here!";  
        view.addObject("message", str); //adding of str object as "message" parameter  
          
        return view;  
    }   
    
    @ResponseBody 
    @RequestMapping(value = "/newui")
    public ModelAndView newui(){
    	ModelAndView view = new ModelAndView();  
        view.setViewName("newui"); //name of the jsp-file in the "page" folder  
        return view;  
    }
    
    @ResponseBody
    @RequestMapping(value = "/login")
    public ModelAndView login(){
    	ModelAndView view = new ModelAndView();  
        view.setViewName("login"); //name of the jsp-file in the "page" folder  
          
        return view;  
    }
    
    protected UserAccount getAuthenticatedUser() throws Exception{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication!=null){
			return userService.fetchByUserName(authentication.getName());
		}else{
			throw new Exception("No user logged in ");
		}
	}
    
    @PreAuthorize("hasRole('ROLE_USER_SPRINGSOCIALSECURITY')")
    @RequestMapping(value = "/current-user", method={RequestMethod.GET})
  	@ResponseBody
  	public JSONObject getCurrentUser() throws Exception {
	
		UserAccount userAccount = getAuthenticatedUser();
		UserConnection userConnection = null;
		
		List<UserConnection> userConnections = userConnectionService.getByUserId(userAccount.getUserName());
		if (userConnections != null && !userConnections.isEmpty()) {
			userConnection = userConnections.get(0);
		}
		JSONObject jsonObject = getUserProfile(userAccount, userConnection);	
		return jsonObject;
  		
  	}

	@SuppressWarnings("unchecked")
	private JSONObject getUserProfile(UserAccount userAccount, UserConnection userConnection) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("name", userConnection.getDisplayName());
		jsonObject.put("email", userAccount.getEmail());
		jsonObject.put("profile_pic", userConnection.getImageUrl());
		return jsonObject;
	}
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('ROLE_USER_SPRINGSOCIALSECURITY')")
    @RequestMapping(value = "/update-profile", method={RequestMethod.POST})
  	@ResponseBody
  	public JSONObject updateProfile(@RequestBody JSONObject jsonObject) throws Exception {
		JSONObject result = new JSONObject();
		if(jsonObject != null && jsonObject.get("email") != null){
			String email = (String) jsonObject.get("email");
			
			UserAccount userAccount = getAuthenticatedUser();
			userAccount.setEmail(email);
			userService.save(userAccount);
			result.put("updated", true);
		}else{
			result.put("updated", false);
		}
		return result;
  		
  	}
    
}
