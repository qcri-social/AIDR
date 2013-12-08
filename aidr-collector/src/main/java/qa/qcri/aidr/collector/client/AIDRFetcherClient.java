/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.collector.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

/**
 * Jersey REST client generated for REST resource:TwitterCollectorAPI
 * [fetcher/twitter/]<br>
 * USAGE:
 * <pre>
 *        AIDRFetcherClient client = new AIDRFetcherClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author Imran
 */
public class AIDRFetcherClient {
    private WebResource webResource;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/AIDRFetcher/webresources";

    public AIDRFetcherClient() {
        com.sun.jersey.api.client.config.ClientConfig config = new com.sun.jersey.api.client.config.DefaultClientConfig();
        client = Client.create(config);
        webResource = client.resource(BASE_URI).path("fetcher/twitter");
    }

    public <T> T getStatus(Class<T> responseType, String id) throws UniformInterfaceException {
        WebResource resource = webResource;
        if (id != null) {
            resource = resource.queryParam("id", id);
        }
        resource = resource.path("status");
        return resource.accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T getStatusAll(Class<T> responseType) throws UniformInterfaceException {
        WebResource resource = webResource;
        resource = resource.path("status/all");
        return resource.accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T stopTask(Class<T> responseType, String id) throws UniformInterfaceException {
        WebResource resource = webResource;
        if (id != null) {
            resource = resource.queryParam("id", id);
        }
        resource = resource.path("stop");
        return resource.accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public ClientResponse startTask(Object requestEntity) throws UniformInterfaceException {
        return webResource.path("start").type(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(ClientResponse.class, requestEntity);
    }

    public void close() {
        client.destroy();
    }
    
}
