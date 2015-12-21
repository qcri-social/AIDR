package qa.qcri.aidr.data.social.configuration;

import javax.sql.DataSource;

import org.socialsignin.springsocial.security.signin.SpringSocialSecuritySignInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.connect.web.ProviderSignInController;

@Configuration
public class SocialConfig {
	
	@Value("${callBackURL}")
	private String callbackURL;
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired 
	private Environment env;
	
	@Autowired
	UserConnectionSignUp userConnectionSignUp;        

    @Autowired
	private ConnectionFactoryRegistry connectionFactoryLocator;
    
    @Bean
    public TextEncryptor textEncryptor() {
        return Encryptors.noOpText();
    }
    
   
    
    @Bean
    public UsersConnectionRepository usersConnectionRepository() {    	
    	JdbcUsersConnectionRepository jdbcUsersConnectionRepository = new JdbcUsersConnectionRepository(dataSource, 
        		connectionFactoryLocator, 
        		textEncryptor());
    	jdbcUsersConnectionRepository.setConnectionSignUp(userConnectionSignUp);
    	return jdbcUsersConnectionRepository;
    }
    
    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    public ConnectionRepository connectionRepository() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new IllegalStateException("No User Signed in");
        }
        return usersConnectionRepository().createConnectionRepository(auth.getName());
    } 
    	
    @Bean
    public ConnectController connectController() {
    	ConnectController connectController = new ConnectController(connectionFactoryLocator, connectionRepository());
        connectController.setApplicationUrl(callbackURL);
        return connectController;
    }
    
    @Bean
    public ProviderSignInController providerSignInController() {
    	
    	ProviderSignInController providerSignInController = new ProviderSignInController(connectionFactoryLocator, usersConnectionRepository(), 
    			new SpringSocialSecuritySignInService());
    	providerSignInController.setSignInUrl("/login");
    	providerSignInController.setSignUpUrl("/signup");
    	providerSignInController.setPostSignInUrl("/authenticate");
    	providerSignInController.setApplicationUrl(callbackURL);
    	
        return providerSignInController;
    }
    
}
