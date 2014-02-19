/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.collector.client;

//import com.sun.jersey.api.client.Client;
//import com.sun.jersey.api.client.ClientResponse;
//import com.sun.jersey.api.client.UniformInterfaceException;
//import com.sun.jersey.api.client.WebResource;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
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
    //private WebResource webResource;		// gf 3 way
	private WebTarget webResource;
	private Client client;

    private static final String BASE_URI = "http://localhost:8080/AIDRFetcher/webresources";

    public AIDRFetcherClient() {
        //com.sun.jersey.api.client.config.ClientConfig config = new com.sun.jersey.api.client.config.DefaultClientConfig();
        //org.glassfish.jersey.client.ClientConfig config = new org.glassfish.jersey.client.ClientConfig();
        
        //client = Client.create(config);
        client = ClientBuilder.newClient();
        
        //webResource = client.resource(BASE_URI).path("fetcher/twitter");
        webResource = client.target(BASE_URI).path("fetcher/twitter");
    }

    //public <T> T getStatus(Class<T> responseType, String id) throws UniformInterfaceException {
    public <T> T getStatus(Class<T> responseType, String id) { 
    	//WebResource resource = webResource;
    	WebTarget resource = webResource;
        if (id != null) {
            resource = resource.queryParam("id", id);
        }
        resource = resource.path("status");
        //return resource.accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    //public <T> T getStatusAll(Class<T> responseType) throws UniformInterfaceException {
    public <T> T getStatusAll(Class<T> responseType) {
    	//WebResource resource = webResource;
    	WebTarget resource = webResource;
        resource = resource.path("status/all");
        
        //return resource.accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    //public <T> T stopTask(Class<T> responseType, String id) throws UniformInterfaceException {
    public <T> T stopTask(Class<T> responseType, String id) {
    	//WebResource resource = webResource;
    	WebTarget resource = webResource;
        if (id != null) {
            resource = resource.queryParam("id", id);
        }
        resource = resource.path("stop");
        
        //return resource.accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }
    
    //public ClientResponse startTask(Object requestEntity) throws UniformInterfaceException {
    public Response startTask(Object requestEntity) {
        //return webResource.path("start").type(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(ClientResponse.class, requestEntity);
    	return webResource.path("start").request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(Entity.json(requestEntity));
    }

    public void close() {
        //client.destroy();
    	client.close();
    }
    
}
