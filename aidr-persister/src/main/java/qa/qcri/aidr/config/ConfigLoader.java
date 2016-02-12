package qa.qcri.aidr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 * Responsible for application configurations and defining required beans
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "qa.qcri.aidr" })
@EnableScheduling
public class ConfigLoader extends WebMvcConfigurerAdapter {

	/*@Autowired
	EntityManagerFactory entityManagerFactory;*/

	/* To load properties files */
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		PropertySourcesPlaceholderConfigurer ppc = new PropertySourcesPlaceholderConfigurer();
		ClassPathResource locations[] = {
				new ClassPathResource("/config.properties"),
				new ClassPathResource("/database.properties")};
		ppc.setLocations(locations);
		return ppc;
	}

	@Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
	
	/*
	 * Defining bean to add openEntityManagerInViewInterceptor as intercepter
	 * for enabling openEntityManagerInViewInterceptor pattern
	 */
	/*@Bean
	public OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor() {
		OpenEntityManagerInViewInterceptor oemiv = new OpenEntityManagerInViewInterceptor();
		oemiv.setEntityManagerFactory(entityManagerFactory);
		return oemiv;
	}*/
	
	@Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".html");
 
        return viewResolver;
    }
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
	    registry.addViewController("/").setViewName("/login");
	}
	
}
