package qa.qcri.aidr.manager.social.configuration;

import org.socialsignin.springsocial.security.signin.SpringSocialSecurityConnectInterceptor;
import org.springframework.social.google.api.Google;
import org.springframework.stereotype.Component;

@Component
public class GoogleConnectInterceptor extends SpringSocialSecurityConnectInterceptor<Google> {

}
