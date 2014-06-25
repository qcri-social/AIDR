/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.collector.global;

import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.jackson.JacksonFeature;

/**
 *
 * @author Imran
 */
@ApplicationPath("/webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        System.out.println("In ApplicationConfig: registering REST APIs");
    	Set<Class<?>> resources = new java.util.HashSet<Class<?>>();
    	resources.add(JacksonFeature.class);
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(qa.qcri.aidr.collector.api.CollectorManageResource.class);
        resources.add(qa.qcri.aidr.collector.api.TwitterCollectorAPI.class);
        
    }
    
}
