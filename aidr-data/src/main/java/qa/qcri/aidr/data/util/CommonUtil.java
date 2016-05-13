package qa.qcri.aidr.data.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import qa.qcri.aidr.data.persistence.entity.UserAccount;
import qa.qcri.aidr.data.service.UserService;

@Component
public class CommonUtil {
	
	@Autowired
	private UserService userService;
	
	public boolean isCurrentUserIsAdmin() throws Exception{
		UserAccount userAccount = getAuthenticatedUser();
		List<RoleType> userRoles = userService.getUserRoles(userAccount.getId());
		
		if (userRoles != null) {
			for (RoleType userRole : userRoles) {
				if(userRole.equals(RoleType.ADMIN)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public UserAccount getAuthenticatedUser() throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			return userService.fetchByUserName(authentication.getName());
		} else {
			throw new Exception("No user logged in ");
		}
	}
	
	public String getSplittedUserName(String combinedUserName) throws Exception {
		String userName = combinedUserName;
		
		if(combinedUserName.contains(ConstantUtils.USER_NAME_SPLITTER)){
			String provider = combinedUserName.substring(0,combinedUserName.indexOf(ConstantUtils.USER_NAME_SPLITTER));
			
			if(provider.equalsIgnoreCase(SocialSignInProvider.TWITTER) || provider.equalsIgnoreCase(SocialSignInProvider.GOOGLE) || provider.equalsIgnoreCase(SocialSignInProvider.FACEBOOK) ){
				userName = combinedUserName.substring(combinedUserName.indexOf(ConstantUtils.USER_NAME_SPLITTER) + 1);
			}
		}
		return userName;
	}
	
}
