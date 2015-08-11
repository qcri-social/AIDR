package qa.qcri.aidr.persister.init;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;

import qa.qcri.aidr.utils.PersisterConfigurator;
import qa.qcri.aidr.utils.PersisterConfigurationProperty;

@WebListener
public class PersisterInitializationContextListener implements
		ServletContextListener {

	private static Logger LOGGER = Logger
			.getLogger(PersisterInitializationContextListener.class);

	@Override
	public void contextInitialized(ServletContextEvent event) {
		PersisterConfigurator.getInstance();
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		// code here
	}
}
