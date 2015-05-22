/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.collector.init;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import qa.qcri.aidr.collector.utils.CollectorConfigurationProperty;
import qa.qcri.aidr.collector.utils.CollectorConfigurator;

/**
 *
 * @author Imran
 */
@WebListener
public class InitNDestroyTomcat implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent event) {
		// Initializing the configuration properties.
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		// code here
	}
}
