package qa.qcri.aidr.manager.init;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import qa.qcri.aidr.manager.util.ManagerConfigurator;
import qa.qcri.aidr.manager.util.ManagerConfigurationProperty;

@WebListener
public class InitializationContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent event) {
		// Initializing the configuration properties.
		ManagerConfigurator.getInstance().initProperties(
				ManagerConfigurator.configLoadFileName,
				ManagerConfigurationProperty.values());
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		// code here
	}
}
