package qa.qcri.aidr.data.config;

import static org.hibernate.jpa.AvailableSettings.NAMING_STRATEGY;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Reponsible for jpa configurations
 *
 * @author Latika
 *
 */
@Profile("default")
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "qa.qcri.aidr.data.repository")
public class PersistenceConfig {

	@Value("${dataSource.driverClassName}")
	private String driver;

	@Value("${dataSource.url}")
	private String url;

	@Value("${dataSource.username}")
	private String username;

	@Value("${dataSource.password}")
	private String password;

	@Value("${hibernate.dialect}")
	private String dialect;

	@Value("${dataSource.persistentUnitName}")
	private String persistentUnitName;

	@Value("${dataSource.jdbc.interceptors}")
	private String jdbcInterceptors;

	@Bean
	public DataSource dataSource() {
		
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setUrl(url);
		dataSource.setDriverClassName(driver);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		return dataSource;
	}

	/* configuring jpa vendor adapter */
	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setDatabase(Database.MYSQL);
		vendorAdapter.setDatabasePlatform(dialect);
		vendorAdapter.setShowSql(true);
		vendorAdapter.setGenerateDdl(true);
		return vendorAdapter;
	}

	protected Properties getJpaProperties() {
		Properties properties = new Properties();
		properties.setProperty(NAMING_STRATEGY,	RespectfulImprovedNamingStrategy.class.getName());
		return properties;
	}

	/* Configuring Entity manager factory */
	@Bean
	public EntityManagerFactory entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactory.setDataSource(dataSource());
		entityManagerFactory
				.setPackagesToScan("qa.qcri.aidr.data.persistence.entity");
		entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter());
		entityManagerFactory.setJpaProperties(getJpaProperties());
		entityManagerFactory.setPersistenceUnitName(persistentUnitName);
		entityManagerFactory.afterPropertiesSet();
		return entityManagerFactory.getObject();
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		return new JpaTransactionManager(entityManagerFactory());
	}

	@Bean
	public PersistenceExceptionTranslator hibernateExceptionTranslator() {
		return new HibernateExceptionTranslator();
	}
}
