/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.api;

import java.util.Set;
import javax.ws.rs.core.Application;

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
        resources.add(qa.qcri.aidr.predictui.api.CollectionResource.class);
        resources.add(qa.qcri.aidr.predictui.api.CrisisResource.class);
        resources.add(qa.qcri.aidr.predictui.api.CrisisTypeResource.class);
        resources.add(qa.qcri.aidr.predictui.api.DocumentResource.class);
        resources.add(qa.qcri.aidr.predictui.api.MiscResource.class);
        resources.add(qa.qcri.aidr.predictui.api.ModelFamilyResource.class);
        resources.add(qa.qcri.aidr.predictui.api.ModelNominalLabelResource.class);
        resources.add(qa.qcri.aidr.predictui.api.ModelResource.class);
        resources.add(qa.qcri.aidr.predictui.api.NominalAttributeResource.class);
        resources.add(qa.qcri.aidr.predictui.api.NominalLabelResource.class);
        resources.add(qa.qcri.aidr.predictui.api.TrainingDataResource.class);
        resources.add(qa.qcri.aidr.predictui.api.UserResource.class);
        resources.add(qa.qcri.aidr.predictui.util.JAXBContextResolver.class);
        resources.add(qa.qcri.aidr.predictui.api.TaskBufferScanner.class);
        
    }
    
}
