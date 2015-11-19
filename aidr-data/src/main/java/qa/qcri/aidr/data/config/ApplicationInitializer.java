/**
 * 
 */
package qa.qcri.aidr.data.config;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Responsible for initialising the application filters and other
 * configurations(Replacement of web.xml)
 * 
 * @author Latika
 * 
 */
public class ApplicationInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {

		/* Setting the configuration classes */
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.setConfigLocation("qa.qcri.aidr.data.config");

		/*Configuring error handler filter for errors out isde the controllers
		FilterRegistration.Dynamic errorHandlerFilter = servletContext.addFilter("errorHandlerFilter", new ErrorHandlerFilter());
		errorHandlerFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
		*/
		FilterRegistration.Dynamic encodingFilter = servletContext.addFilter("encodingFilter", new org.springframework.web.filter.CharacterEncodingFilter());
		encodingFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
		encodingFilter.setInitParameter("encoding", "UTF-8");
		encodingFilter.setInitParameter("forceEncoding", "true");
		
		/* Adding context listener */
		servletContext.addListener(new ContextLoaderListener(context));
		
		/* Adding request listener */
		servletContext.addListener(new RequestContextListener());

		/* Configuring dispatcher servlet for spring mvc */
		/*CustomDispatcherServlet servlet = new CustomDispatcherServlet(context); */
		ServletRegistration.Dynamic appServlet = servletContext.addServlet("dispatcher", new DispatcherServlet(context));
		appServlet.setLoadOnStartup(1);
		appServlet.addMapping("/*");
	}
}
