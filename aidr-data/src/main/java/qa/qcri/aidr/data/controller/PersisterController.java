package qa.qcri.aidr.data.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import qa.qcri.aidr.data.util.ActivityType;
import qa.qcri.aidr.data.util.CommonUtil;
import qa.qcri.aidr.data.persistence.entity.UserAccount;
import qa.qcri.aidr.data.persistence.entity.UserAccountActivity;
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
	
	@Autowired
	private CommonUtil commonUtil;

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
			UserAccount userAccount = commonUtil.getAuthenticatedUser();
			boolean isAdmin = commonUtil.isCurrentUserIsAdmin();			

			String userName = "AIDR_Data_User";

			if (StringUtils.isEmpty(queryString)) {
				queryString = "{\"constraints\":[]}";
			}

			Date createdDate = new Date();
			if (createdTimestamp != null) {
				createdDate = new Date(createdTimestamp);
			}
			
			UserAccountActivity userAccountActivity = null;
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date fromDate = formatter.parse(formatter.format(new Date()));
			// if user is not admin then check download limit for a user
			if(!isAdmin){		
				
				Calendar c = Calendar.getInstance(); 
				c.setTime(fromDate); 
				c.add(Calendar.DATE, 1);
				Date toDate = c.getTime();				
				
				List<UserAccountActivity> activities = userAcountActivityService.findByAccountIdandActivityDate(userAccount.getId(), fromDate, toDate);
				
				if(activities != null && !activities.isEmpty()){
					userAccountActivity = activities.get(0);
					if(userAccountActivity.getDownloadCount() == null) {
						userAccountActivity.setDownloadCount(0);
					}
					Integer downloadCount = userAccountActivity.getDownloadCount();
					count = count - downloadCount;
				}
				
				if(count <= 0){
					return getUIWrapper(false, "You have reached at your daily download limit. Please try tomorrow to download!");
				}
				
				//Integer tweetCount = (Integer) result.get("tweetCount");
				System.out.println(activities);
			}

			response = persisterService.generateDownloadLink(code, queryString, userName, count, removeRetweet,
					jsonType, createdDate);
			if (!StringUtils.isEmpty(response)) {
				Map<String, Object> result = new ObjectMapper().readValue(response, Map.class);

				if (result != null && result.containsKey("url")) {
					
					// update download count of userAccountActivity
					if(result.containsKey("tweetCount")){
						Object countObject = result.get("tweetCount");						
						if(userAccountActivity == null){
							userAccountActivity = new UserAccountActivity(userAccount, fromDate, 0, ActivityType.DOWNLOAD);
						}
						Integer downloadedTweets = (Integer)countObject;
						userAccountActivity.setDownloadCount(downloadedTweets + userAccountActivity.getDownloadCount());
						userAcountActivityService.save(userAccountActivity);
					}
					
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

}
