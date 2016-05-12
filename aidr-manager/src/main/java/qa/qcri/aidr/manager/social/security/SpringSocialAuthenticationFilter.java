package qa.qcri.aidr.manager.social.security;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.socialsignin.springsocial.security.signin.AuthenticatedUserIdHolder;
import org.socialsignin.springsocial.security.signin.SpringSocialSecurityAuthenticationFactory;
import org.socialsignin.springsocial.security.signin.SpringSocialSecuritySignInDetails;
import org.socialsignin.springsocial.security.signin.SpringSocialSecuritySignInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.social.connect.ConnectionData;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import qa.qcri.aidr.manager.persistence.entities.UserConnection;
import qa.qcri.aidr.manager.service.UserConnectionService;
import qa.qcri.aidr.manager.util.ConstantUtils;

@Component
@Qualifier("springSocialAuthenticationFilter")
public class SpringSocialAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	
	@Value("${socialsignin.defaultAuthenticationSuccessUrl:}")
	private String defaultAuthenticationSuccessUrl;
	
	public final static String DEFAULT_AUTHENTICATION_URL = "/authenticate";
	
	private boolean removeSignInDetailsFromSessionOnSuccessfulAuthentication = true;

	private boolean allowRepeatedAuthenticationAttempts = false;

	public void setAllowRepeatedAuthenticationAttempts(
			boolean allowRepeatedAuthenticationAttempts) {
		this.allowRepeatedAuthenticationAttempts = allowRepeatedAuthenticationAttempts;
	}

	@Override
	@Autowired(required = false)
	public void setRememberMeServices(RememberMeServices rememberMeServices) {
		super.setRememberMeServices(rememberMeServices);
	}

	@Autowired
	@Qualifier("springSocialUserDetailsService")
	private UserDetailsService userDetailsService;

    @Inject
    private UserConnectionService userConnectionService;

    @Autowired
	private SpringSocialSecurityAuthenticationFactory authenticationFactory;

	public void setRemoveSignInDetailsFromSessionOnSuccessfulAuthentication(
			boolean removeSignInDetailsFromSessionOnSuccessfulAuthentication) {
		this.removeSignInDetailsFromSessionOnSuccessfulAuthentication = removeSignInDetailsFromSessionOnSuccessfulAuthentication;
	}

	@Autowired
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		super.setAuthenticationManager(authenticationManager);
	}

	public SpringSocialAuthenticationFilter() {
		super(DEFAULT_AUTHENTICATION_URL);
	}
	
	@PostConstruct
	public void init(){
		if (defaultAuthenticationSuccessUrl != null && !defaultAuthenticationSuccessUrl.isEmpty()){
			SavedRequestAwareAuthenticationSuccessHandler savedRequestAwareAuthenticationSuccessHandler = new SavedRequestAwareAuthenticationSuccessHandler();
			savedRequestAwareAuthenticationSuccessHandler.setDefaultTargetUrl(defaultAuthenticationSuccessUrl);		
			setAuthenticationSuccessHandler(savedRequestAwareAuthenticationSuccessHandler);
		}
	}

	protected SpringSocialAuthenticationFilter(String authenticationUrl) {
		super(authenticationUrl);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException,
			IOException, ServletException {

		SpringSocialSecuritySignInDetails signInDetails = (SpringSocialSecuritySignInDetails) request.getSession().getAttribute(
						SpringSocialSecuritySignInService.SIGN_IN_DETAILS_SESSION_ATTRIBUTE_NAME);
		String alreadyAuthenticatedUserId = AuthenticatedUserIdHolder.getAuthenticatedUserId();

		if (signInDetails != null) {
			UserDetails user = userDetailsService.loadUserByUsername(signInDetails.getConnectionData().getProviderId() + ConstantUtils.USER_NAME_SPLITTER + signInDetails.getUserId());
			if (removeSignInDetailsFromSessionOnSuccessfulAuthentication) {
				request.getSession().removeAttribute(SpringSocialSecuritySignInService.SIGN_IN_DETAILS_SESSION_ATTRIBUTE_NAME);
			}
            updateUserKeys(signInDetails.getConnectionData(), signInDetails.getUserId());
            //Setting social login provider for current session
            //request.getSession().setAttribute("provider", signInDetails.getConnectionData().getProviderId());
            
			return authenticationFactory.createAuthenticationFromUserDetails(user);
		} else if (allowRepeatedAuthenticationAttempts && alreadyAuthenticatedUserId != null) {
			return SecurityContextHolder.getContext().getAuthentication();
		} else {
			logger.info("SpringSocialSecurity sign in details not found in session");
			throw new InsufficientAuthenticationException(
					"SpringSocialSecurity sign in details not found in session");
		}
	}

    private void updateUserKeys(ConnectionData connectionData, String userId){
        List<UserConnection> userConnections = userConnectionService.getByUserIdAndProviderUserId(userId, connectionData.getProviderUserId());
        if(!CollectionUtils.isFull(userConnections)){
           for(UserConnection uc : userConnections){
               uc.setAccessToken(connectionData.getAccessToken());
               uc.setSecret(connectionData.getSecret());
               userConnectionService.update(uc);
           }
        }
    }

}
