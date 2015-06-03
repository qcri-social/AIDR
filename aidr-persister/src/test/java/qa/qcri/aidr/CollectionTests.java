/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 
package qa.qcri.aidr;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.Assert;
import org.junit.Test;

import qa.qcri.aidr.utils.PersisterConfigurationProperty;
import qa.qcri.aidr.utils.PersisterConfigurator;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class CollectionTests {
    
	public static final String PERSISTER_URL = "http://localhost:8084/AIDRPersister/webresources";
    public static final String COLLECTION_CODE = "test";

    @Test
    public void start() {

        String uri = PERSISTER_URL + "/collectionPersister/start?channel_provider=provider&collection_code=" + COLLECTION_CODE;

        Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
        WebTarget webResource = client.target(uri);
        Response response = webResource.request(MediaType.APPLICATION_JSON).get();
        String message = response.readEntity(String.class);

        Assert.assertEquals("Start collection successfully", "Started persisting to " + PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH), message);
    }

    @Test
    public void stop() {
        String uri = PERSISTER_URL + "/collectionPersister/stop?collection_code=" + COLLECTION_CODE;

        Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
        WebTarget webResource = client.target(uri);
        Response response = webResource.request(MediaType.APPLICATION_JSON).get();
        String message = response.readEntity(String.class);

        Assert.assertEquals("Stop collection successfully", "Persistance of [" + COLLECTION_CODE + "] has been stopped.", message);
    }

}*/
