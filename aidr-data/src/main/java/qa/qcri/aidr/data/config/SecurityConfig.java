package qa.qcri.aidr.data.config;


/*@Configuration
@ImportResource({"classpath:spring-security.xml"})
public class SecurityConfig {
	
    @Bean
    public DelegatingFilterProxy springSecurityFilterChain() {
    	DelegatingFilterProxy filterProxy =  new DelegatingFilterProxy();
        return filterProxy;
    }	
   
}


*/



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import qa.qcri.aidr.data.social.security.SpringSocialAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
 
	@Autowired
    SpringSocialAuthenticationFilter springSocialAuthenticationFilter;
	
    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                //Spring Security ignores request to static resources such as CSS or JS files.
                .ignoring()
                    .antMatchers("/static/**");
    }
 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //Configures form login
                .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/login/authenticate")
                    .failureUrl("/login?error=bad_credentials")                    
                //Configures the logout function
                .and()
                    .logout()
                        .deleteCookies("JSESSIONID")
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                //Configures url based authorization
                .and()
                    .authorizeRequests()
                        //Anyone can access the urls
                        .antMatchers(
                                "/resources/**",
                                "/login"
                                //"/signin/**",
                                //"/user/register/**"
                        ).permitAll()
                        //The rest of the our application is protected.
                        //.antMatchers("/index").hasRole("ADMIN")
                //Adds the SocialAuthenticationFilter to Spring Security's filter chain.
                //.and().apply(new SpringSocialConfigurer())
                 .and()
                 	.addFilterBefore(springSocialAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                 .csrf().disable();
    }
 
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());
    }   
    
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
    
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
 
   /* @Bean
    public DelegatingFilterProxy springSecurityFilterChain() {
    	DelegatingFilterProxy filterProxy =  new DelegatingFilterProxy();
        return filterProxy;
    }	 */
    
}