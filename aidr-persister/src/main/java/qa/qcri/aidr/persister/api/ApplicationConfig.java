/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.persister.api;

import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.jackson.JacksonFeature;

/**
 *
 * @author Imran
 */
//@javax.ws.rs.ApplicationPath("webresources")
@ApplicationPath("/webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<Class<?>>();
        resources.add(JacksonFeature.class);		// gf 3 way modified
        // following code can be used to customize Jersey 1.x JSON provider:
        /*
        try {
            Class jacksonProvider = Class.forName("org.codehaus.jackson.jaxrs.JacksonJsonProvider");
            resources.add(jacksonProvider);
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        */
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(qa.qcri.aidr.persister.api.Persister4CollectorAPI.class);
        resources.add(qa.qcri.aidr.persister.api.Persister4TaggerAPI.class);
    }
    
}
