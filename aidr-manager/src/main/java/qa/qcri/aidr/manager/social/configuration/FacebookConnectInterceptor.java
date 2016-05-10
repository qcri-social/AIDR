package qa.qcri.aidr.manager.social.configuration;

import org.socialsignin.springsocial.security.signin.SpringSocialSecurityConnectInterceptor;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Component;

@Component
public class FacebookConnectInterceptor extends SpringSocialSecurityConnectInterceptor<Facebook> {

}
