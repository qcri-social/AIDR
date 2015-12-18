package qa.qcri.aidr.data.social.configuration;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ConnectInterceptor;

import qa.qcri.aidr.data.social.security.ConnectInterceptorList;

public abstract class AbstractProviderConfig<S> {

	@Autowired
	private ConnectionFactoryRegistry registry;
	
	@Autowired
	@Qualifier("connectInterceptorList")
	private ConnectInterceptorList connectInterceptorList;
	
	protected abstract ConnectionFactory<S> createConnectionFactory();
	protected abstract ConnectInterceptor<S> getConnectInterceptor();
	
	
	
	@PostConstruct
	public void register()
	{
		registry.addConnectionFactory(createConnectionFactory());
		connectInterceptorList.add(getConnectInterceptor());

	}
}
