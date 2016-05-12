package qa.qcri.aidr.manager.social.security;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.socialsignin.springsocial.security.api.SpringSocialProfile;
import org.socialsignin.springsocial.security.signin.SpringSocialSecurityAuthenticationFactory;
import org.socialsignin.springsocial.security.signup.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import qa.qcri.aidr.manager.RoleType;
import qa.qcri.aidr.manager.persistence.entities.UserAccount;
import qa.qcri.aidr.manager.persistence.entities.UserConnection;
import qa.qcri.aidr.manager.service.UserConnectionService;
import qa.qcri.aidr.manager.service.UserService;
import qa.qcri.aidr.manager.util.ConstantUtils;

@Repository
@Service
@Qualifier("springSocialUserDetailsService")
public class SpringSocialUserDetailService implements UserDetailsService {

	private static Logger logger = Logger.getLogger(SpringSocialUserDetailService.class);
	@Autowired
	private UsersConnectionRepository usersConnectionRepository;

	@Autowired
	private SpringSocialSecurityAuthenticationFactory authenticationFactory;
	
	@Autowired
	private UserConnectionService userConnectionService;
	
	@Autowired
	private SignUpService<?> signUpService;
	
	@Resource(name="userService")
	private UserService userService;
	
	@Override
	public UserDetails loadUserByUsername(String combinedUserName)throws UsernameNotFoundException {
		String provider = combinedUserName.substring(0, combinedUserName.indexOf(ConstantUtils.USER_NAME_SPLITTER));
		String userName = combinedUserName.substring(combinedUserName.indexOf(ConstantUtils.USER_NAME_SPLITTER)+1);
		
		ConnectionRepository connectionRepository = usersConnectionRepository.createConnectionRepository(userName);
		SpringSocialProfile springSocialProfile = null;
		//Fetching user with provider id springSecurity
		try{
			springSocialProfile = signUpService.getUserProfile(userName);
		}catch(Exception e){
			logger.error("Multiple accounts exist with same userName: "+userName,e);
		}
		
		List<Connection<?>> allConnections = getConnections(connectionRepository);
		
		if (allConnections.size() > 0) {
			Authentication authentication = null;
			if(springSocialProfile == null){
				UserConnection userProfile = userConnectionService.getByUserIdAndProviderId(userName, provider);
				authentication = authenticationFactory.createAuthenticationForAllConnections(combinedUserName,
						userProfile.getAccessToken(),allConnections);
			}else{
				authentication = authenticationFactory.createAuthenticationForAllConnections(combinedUserName,
						springSocialProfile.getPassword(),allConnections);
			}
				
			UserAccount user =  userService.fetchByUserName(combinedUserName);
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			authorities.addAll(authentication.getAuthorities());
			
			List<RoleType> roles = userService.getUserRoles(user.getId());
			if(roles != null && !roles.isEmpty() ){
				for(RoleType role : roles){
				   GrantedAuthority authority = new SimpleGrantedAuthority(role.name());
				   authorities.add(authority);
				}
			}
			return new User(combinedUserName, authentication.getCredentials().toString(), true, true, true, true,authorities);
		
		} else {
			logger.info("UsernameNotFoundException for user: "+combinedUserName);
			throw new UsernameNotFoundException(combinedUserName);
		}
	}
	
	private List<Connection<?>> getConnections(ConnectionRepository connectionRepository) {
		MultiValueMap<String, Connection<?>> connections = connectionRepository.findAllConnections();
		List<Connection<?>> allConnections = new ArrayList<Connection<?>>();
		if (connections.size() > 0) {
			for (List<Connection<?>> connectionList : connections.values()) {
				for (Connection<?> connection : connectionList) {
					allConnections.add(connection);
				}
			}
		}
		return allConnections;
	}

}
