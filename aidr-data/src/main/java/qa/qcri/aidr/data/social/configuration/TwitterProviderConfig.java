package qa.qcri.aidr.data.social.configuration;


import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.web.ConnectInterceptor;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;
import org.springframework.stereotype.Component;

@Component
public class TwitterProviderConfig extends AbstractProviderConfig<Twitter> {
	
	@Inject
	private TwitterConnectInterceptor twitterConnectInterceptor;

	@Value("${twitter.consumerKey}")
	private String twitterConsumerKey;

	@Value("${twitter.consumerSecret}")
	private String twitterConsumerSecret;

	@Override
	protected ConnectionFactory<Twitter> createConnectionFactory() {
		return new TwitterConnectionFactory(
				twitterConsumerKey, twitterConsumerSecret);
	}

	@Override
	protected ConnectInterceptor<Twitter> getConnectInterceptor() {
	
               if( twitterConnectInterceptor == null ) {
                        twitterConnectInterceptor = new TwitterConnectInterceptor();
                }
                return twitterConnectInterceptor;
            //return twitterConnectInterceptor;
	}
	
}
