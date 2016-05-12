package qa.qcri.aidr.manager.social.configuration;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.web.ConnectInterceptor;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.stereotype.Component;

@Component
public class GoogleProviderConfig extends AbstractProviderConfig<Google> {
	
	@Inject
	private GoogleConnectInterceptor googleConnectInterceptor;

	@Value("${google.consumerKey}")
	private String googleConsumerKey;

	@Value("${google.consumerSecret}")
	private String googleConsumerSecret;

	@Override
	protected ConnectionFactory<Google> createConnectionFactory() {
		GoogleConnectionFactory googleConnectionFactory = new GoogleConnectionFactory(googleConsumerKey, googleConsumerSecret);
		googleConnectionFactory.setScope("email");
		return googleConnectionFactory;
	}

	@Override
	protected ConnectInterceptor<Google> getConnectInterceptor() {	
       if( googleConnectInterceptor == null ) {
                googleConnectInterceptor = new GoogleConnectInterceptor();
        }
        return googleConnectInterceptor;
	}	
	
}
