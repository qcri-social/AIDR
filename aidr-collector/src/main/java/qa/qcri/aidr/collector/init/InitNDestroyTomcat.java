/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.collector.init;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 *
 * @author Imran
 */
@WebListener
public class InitNDestroyTomcat implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        //code here
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        //code here
    }
}
