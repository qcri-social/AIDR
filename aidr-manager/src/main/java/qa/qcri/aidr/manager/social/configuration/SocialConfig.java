package qa.qcri.aidr.manager.social.configuration;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.stereotype.Component;

@Component
public class SocialConfig {
	
	@Value("${twitter.callBackURL}")
	private String callbackURL;
    @Inject
    private ConnectionRepository connectionRepository;
    //@Resource(name = "connectionFactoryRegistry")
    private ConnectionFactoryLocator connectionFactoryLocator;
    @Resource(name="usersConnectionRepository")
    private UsersConnectionRepository usersConnectionRepository;
    
    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    public ConnectionRepository connectionRepository() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new IllegalStateException("No User Signed in");
        }
        return usersConnectionRepository.createConnectionRepository(auth.getName());
    }
 
    @Bean
    public ConnectController connectController() {
        ConnectController controller = new ConnectController(connectionFactoryLocator, connectionRepository);
        controller.setApplicationUrl(callbackURL);
        return controller;
    }
}
