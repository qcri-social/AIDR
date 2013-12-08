/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.collector.api;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import qa.qcri.aidr.collector.beans.CollectionTask;
import qa.qcri.aidr.collector.utils.Config;
import qa.qcri.aidr.collector.utils.GenericCache;

/**
 * REST Web Service
 *
 * @author Imran
 */
@Path("fetcher/manage")
public class FetcherManageResource {

    @Context
    private UriInfo context;
    private Client client = new Client();

    /**
     * Creates a new instance of FetcherManageResource
     */
    public FetcherManageResource() {
    }

    @GET
    @Path("/persist")
    @Produces(MediaType.TEXT_PLAIN)
    public String persistRunningCollections() {

        String response = "";
        List<CollectionTask> collections = GenericCache.getInstance().getAllRunningCollectionTasks();
        if (collections == null || collections.isEmpty()) {
            return "No running collection found to persist.";
        }
        
        System.out.println(collections.size() + " collections found to be persisted.");
        Gson gson = new Gson();
        try {
            FileWriter file = new FileWriter("fetcher_running_coll.json");
            for (CollectionTask collection : collections) {
                String json = gson.toJson(collection);
                response += "Persisting: " + collection.getCollectionCode() + "\n";
                file.write(json + "\n");
            }
            file.flush();
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;

    }

    @GET
    @Path("/runPersisted")
    @Produces(MediaType.TEXT_PLAIN)
    public String runPersistedCollections() {

        String response = "";
        BufferedReader br = null;
        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader("fetcher_running_coll.json"));
            Gson gson = new Gson();
            System.out.println("Strated reading from disk...");
            while ((sCurrentLine = br.readLine()) != null) {
                CollectionTask collection = gson.fromJson(sCurrentLine, CollectionTask.class);
                System.out.println("Retrieved from disk :" + gson.toJson(collection));
                runCollection(collection);

            }
            System.out.println("Done reading.");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        response = "Running persisted collections completed.";
        return response;

    }

    private String runCollection(CollectionTask collection) {

        try {
            WebResource webResource = client.resource(Config.FETCHER_REST_URI + "/twitter/start");
            Gson gson = new Gson();
            ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .post(ClientResponse.class, gson.toJson(collection));
            String jsonResponse = clientResponse.getEntity(String.class);
            System.out.println("Fetcher Response: " + jsonResponse);
            return jsonResponse;
        } catch (Exception e) {

            return "Could not start collection";
        }
    }
}
