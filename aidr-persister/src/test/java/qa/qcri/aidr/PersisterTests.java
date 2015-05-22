/*package qa.qcri.aidr;

import java.util.Map;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class PersisterTests {

	public static final String PERSISTER_URL = "http://localhost:8084/AIDRPersister/webresources";
    public static final String COLLECTION_CODE = "2014-03-mh370";

    public static void main(String args[]) throws Exception {
    	try {
			Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
			WebTarget webResource = client.target(PERSISTER_URL + "/taggerPersister/filter/genCSV?collectionCode=" + COLLECTION_CODE + "&exportLimit=100000");
			//Response clientResponse = webResource.request(MediaType.APPLICATION_JSON).get();
			
			String queryString = "{\"constraints\": []}";
			Response clientResponse = webResource.request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(queryString), Response.class);
			
			Map<String, Object> jsonResponse = clientResponse.readEntity(Map.class);
			System.out.println("received response: " + jsonResponse);
            
		} catch (Exception e) {
			throw new Exception("Error while generating CSV link in Persister", e);
		}
    }
}
*/