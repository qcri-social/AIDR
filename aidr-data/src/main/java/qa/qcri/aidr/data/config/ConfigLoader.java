package qa.qcri.aidr.data.config;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Responsible for application configurations and defining required beans
 * 
 * @author Latika
 * 
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "qa.qcri.aidr.data" })
@EnableScheduling
public class ConfigLoader extends WebMvcConfigurerAdapter {

	@Autowired
	EntityManagerFactory entityManagerFactory;

	/* To load properties files */
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		PropertySourcesPlaceholderConfigurer ppc = new PropertySourcesPlaceholderConfigurer();
		ClassPathResource locations[] = {
				new ClassPathResource("/application.properties"),
				new ClassPathResource("/environment.properties")};
		ppc.setLocations(locations);
		return ppc;
	}

	/*
	 * Defining bean to add openEntityManagerInViewInterceptor as intercepter
	 * for enabling openEntityManagerInViewInterceptor pattern
	 */
	@Bean
	public OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor() {
		OpenEntityManagerInViewInterceptor oemiv = new OpenEntityManagerInViewInterceptor();
		oemiv.setEntityManagerFactory(entityManagerFactory);
		return oemiv;
	}
}
