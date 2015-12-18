/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.data.social.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.stereotype.Component;

/**
 *
 * @author Imran
 */
@Component
public class UtilConnectionRegistry {
    
        @Bean
        public ConnectionFactoryRegistry connectionFactoryRegistry() {
                return new ConnectionFactoryRegistry();
        }
    
}
