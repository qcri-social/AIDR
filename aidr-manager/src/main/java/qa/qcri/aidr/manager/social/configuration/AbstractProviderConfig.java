package qa.qcri.aidr.manager.social.configuration;

import qa.qcri.aidr.manager.social.security.ConnectInterceptorList;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ConnectInterceptor;

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
