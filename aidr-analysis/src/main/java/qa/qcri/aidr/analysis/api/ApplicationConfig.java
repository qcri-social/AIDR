/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.analysis.api;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;

import javax.ws.rs.core.Application;
import java.util.Set;

/**
 *
 * @author Imran
 */
@javax.ws.rs.ApplicationPath("rest")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<Class<?>>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically re-generated to populate
     * given list with all resources defined in the project.
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
    	resources.add(MoxyJsonFeature.class);
    	resources.add(JacksonFeature.class);
    	resources.add(qa.qcri.aidr.analysis.api.GetTagDataStatistics.class);
    	resources.add(qa.qcri.aidr.analysis.api.GetFrequencyStatistics.class);
    }
    
}
