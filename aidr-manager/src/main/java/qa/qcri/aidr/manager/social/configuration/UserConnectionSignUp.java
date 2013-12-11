package qa.qcri.aidr.manager.social.configuration;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.support.OAuth1Connection;
import org.springframework.stereotype.Component;

import qa.qcri.aidr.manager.hibernateEntities.UserEntity;
import qa.qcri.aidr.manager.hibernateEntities.UserConnection;
import qa.qcri.aidr.manager.service.UserConnectionService;
import qa.qcri.aidr.manager.service.UserService;

@Component("userConnectionSignUp")
public class UserConnectionSignUp implements ConnectionSignUp {

	@Inject
	private UserConnectionService userConnectionService;
	
	@Resource(name="userService")
	private UserService userService;
	
	@Override
    public String execute(Connection<?> connection) {
		if(connection instanceof OAuth1Connection){
			OAuth1Connection<?> oauthConnection = (OAuth1Connection<?>) connection;
			ConnectionData data = oauthConnection.createData();
	        UserProfile profile = oauthConnection.fetchUserProfile();
	        UserConnection userConnection = new UserConnection();
	        userConnection.setUserId(profile.getUsername());
	        userConnection.setImageUrl(data.getImageUrl());
	        userConnection.setProfileUrl(data.getProfileUrl());
	        userConnection.setAccessToken(data.getAccessToken());
	        userConnection.setSecret(data.getSecret());
	        userConnection.setRefreshToken(data.getRefreshToken());
	        userConnection.setProviderUserId(profile.getUsername());
	        /**
	         * This is custom SignUp and for it providerID should be springSecurity
	         */
	        userConnection.setProviderId("springSocialSecurity");
	        userConnection.setDisplayName(data.getDisplayName());
	        userConnection.setRank(1);
	        userConnectionService.register(userConnection);
	        UserEntity user = new UserEntity();
	        user.setProvider(data.getProviderId());
	        user.setUserName(profile.getUsername());
	        userService.save(user);
	        return profile.getUsername();
		}
		
		return connection.fetchUserProfile().getUsername();
    }

}
