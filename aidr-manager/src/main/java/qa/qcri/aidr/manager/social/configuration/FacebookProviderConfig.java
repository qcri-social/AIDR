package qa.qcri.aidr.manager.social.configuration;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.web.ConnectInterceptor;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.stereotype.Component;

@Component
public class FacebookProviderConfig extends AbstractProviderConfig<Facebook> {
	
	@Inject
	private FacebookConnectInterceptor facebookConnectInterceptor;

	@Value("${facebook.consumerKey}")
	private String facebookConsumerKey;

	@Value("${facebook.consumerSecret}")
	private String facebookConsumerSecret;

	@Override
	protected ConnectionFactory<Facebook> createConnectionFactory() {
		FacebookConnectionFactory facebookConnectionFactory = new FacebookConnectionFactory(facebookConsumerKey, facebookConsumerSecret);
		facebookConnectionFactory.setScope("email");
		return facebookConnectionFactory;
	}

	@Override
	protected ConnectInterceptor<Facebook> getConnectInterceptor() {	
       if( facebookConnectInterceptor == null ) {
                facebookConnectInterceptor = new FacebookConnectInterceptor();
        }
        return facebookConnectInterceptor;
	}	
	
}
