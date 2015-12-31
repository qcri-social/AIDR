package qa.qcri.aidr.data.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import qa.qcri.aidr.data.RoleType;
import qa.qcri.aidr.data.persistence.entity.UserAccount;
import qa.qcri.aidr.data.service.PersisterService;
import qa.qcri.aidr.data.service.UserAcountActivityService;
import qa.qcri.aidr.data.service.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/persister")
public class PersisterController {

	protected static final Logger LOGGER = LoggerFactory.getLogger(PersisterController.class);

	@Autowired
	private PersisterService persisterService;

	@Resource(name = "userService")
	private UserService userService;
	
	@Autowired
	private UserAcountActivityService userAcountActivityService;

	@PreAuthorize("hasRole('ROLE_USER_SPRINGSOCIALSECURITY')")
	@RequestMapping(value = "/generateDownloadLink", method = RequestMethod.POST)
	@ResponseBody
	@SuppressWarnings("unchecked")
	public Map<String, Object> generateCSVLink(@RequestParam String code, @RequestParam Integer count,
			@RequestParam boolean removeRetweet,
			@RequestParam(value = "createdTimestamp", required = false) Long createdTimestamp,
			@RequestParam(value = "type", defaultValue = "CSV", required = false) String jsonType, String queryString)
			throws Exception {
		String response = null;
		try {

			// check user download limit
			UserAccount userAccount = getAuthenticatedUser();
			List<RoleType> userRoles = userService.getUserRoles(userAccount.getId());
			
			//checking if current user is admin
			boolean isAdmin = false;
			if (userRoles != null) {
				for (RoleType userRole : userRoles) {
					if(userRole.equals(RoleType.ADMIN)) {
						isAdmin = true;
					}
				}
			}

			String userName = "AIDR_Data_User";

			if (StringUtils.isEmpty(queryString)) {
				queryString = "{\"constraints\":[]}";
			}

			Date createdDate = new Date();
			if (createdTimestamp != null) {
				createdDate = new Date(createdTimestamp);
			}

			response = persisterService.generateDownloadLink(code, queryString, userName, count, removeRetweet,
					jsonType, createdDate);
			if (!StringUtils.isEmpty(response)) {
				Map<String, Object> result = new ObjectMapper().readValue(response, Map.class);
				
				// if user is not admin then check download limit for a user
				/*if(!isAdmin && result != null && result.containsKey("tweetCount")){
					DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					Date fromDate = formatter.parse(formatter.format(new Date()));
					
					Calendar c = Calendar.getInstance(); 
					c.setTime(fromDate); 
					c.add(Calendar.DATE, 1);
					Date toDate = c.getTime();				
					
					List<UserAccountActivity> activities = userAcountActivityService.findByAccountIdandActivityDate(userAccount.getId(), fromDate, toDate);
					
					if(activities != null && !activities.isEmpty()){
						UserAccountActivity userAccountActivity = activities.get(0);
						Integer downloadCount = userAccountActivity.getDownloadCount();
						if(downloadCount == null) {
						}
					}
					
					Integer tweetCount = (Integer) result.get("tweetCount");
					System.out.println(activities);
				}*/		
				
				if (result != null && result.containsKey("url")) {
					return getUIWrapper(result.get("url"), true);
				} else {
					return getUIWrapper(false, "Something wrong - no file generated!");
				}
			} else {
				return getUIWrapper(false, "Something wrong - no file generated!");
			}

		} catch (Exception e) {
			LOGGER.error("Exception in generating CSV download link for collection: " + code, e);
			return getUIWrapper(false,
					"System is down or under maintenance. For further inquiries please contact admin.");
		}
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
		modelMap.put("message", getMessage(success, message));
		return modelMap;
	}

	private String getMessage(Boolean success, String message) {
		if (message != null) {
			return message;
		}
		if (success != null && success) {
			return "Successful";
		} else {
			return "Failure";
		}
	}

	protected UserAccount getAuthenticatedUser() throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			return userService.fetchByUserName(authentication.getName());
		} else {
			throw new Exception("No user logged in ");
		}
	}

}
