package qa.qcri.aidr.collector.api;

import com.google.gson.Gson;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

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
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.glassfish.jersey.jackson.JacksonFeature;

import qa.qcri.aidr.collector.beans.CollectionTask;
import qa.qcri.aidr.collector.beans.CollectorStatus;
import qa.qcri.aidr.collector.utils.CollectorConfigurator;
import qa.qcri.aidr.collector.utils.CollectorConfigurationProperty;
import qa.qcri.aidr.collector.utils.GenericCache;
import qa.qcri.aidr.common.logging.ErrorLog;

/**
 * REST Web Service
 *
 * @author Imran
 */
@Path("/manage")
public class CollectorManageResource {

	private static Logger logger = Logger.getLogger(CollectorManageResource.class.getName());
	private static ErrorLog elog = new ErrorLog();
	private static CollectorConfigurator configProperties = CollectorConfigurator.getInstance();
	
    @Context
    private UriInfo context;
    
    /**
     * Creates a new instance of FetcherManageResource
     */
    public CollectorManageResource() {
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
        
        logger.info(collections.size() + " collections found to be persisted.");
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
            logger.error("Error in persisting running collections");
            logger.error(elog.toStringException(e));	
        }

        return response;

    }

    @GET
    @Path("/runPersisted")
    @Produces(MediaType.TEXT_PLAIN)
    public String runPersistedCollections() throws InterruptedException {

        String response = "";
        BufferedReader br = null;
        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader("fetcher_running_coll.json"));
            Gson gson = new Gson();
            logger.info("Started reading from disk...");
            while ((sCurrentLine = br.readLine()) != null) {
                CollectionTask collection = gson.fromJson(sCurrentLine, CollectionTask.class);
                System.out.println("Retrieved from disk :" + gson.toJson(collection));
                runCollection(collection);
                Thread.sleep(2000); // starting up collections in a polite way to avoid blocking problem from Twitter

            }
            logger.info("Done reading.");

        } catch (IOException e) {
            logger.error(elog.toStringException(e));	
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                logger.error(elog.toStringException(ex));	
            }
        }

        response = "Running persisted collections completed.";
        return response;
 
    }

    private String runCollection(CollectionTask collection) {
    	Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
        try {
            
        	WebTarget webResource = client.target(configProperties.getProperty(CollectorConfigurationProperty.COLLECTOR_REST_URI) + "/twitter/start");
            Gson gson = new Gson();
            Response clientResponse = webResource.request(MediaType.APPLICATION_JSON)
            							.post(Entity.json(gson.toJson(collection)), Response.class);
            
            String jsonResponse = clientResponse.readEntity(String.class);
            
            logger.info("Fetcher Response: " + jsonResponse);
            return jsonResponse;
        } catch (Exception e) {
        	logger.error("Could not start collection");
            logger.error(elog.toStringException(e));	
        	return "Could not start collection";
        }
    }
    
    @GET
    @Path("/ping")
    @Produces(MediaType.APPLICATION_JSON)
    public Response ping() throws InterruptedException {
        
        int runningCollectionsCount=0;
         List<CollectionTask> collections = GenericCache.getInstance().getAllRunningCollectionTasks();
        if (!(collections == null || collections.isEmpty())) {
            runningCollectionsCount = collections.size();
        }
        String startDate = GenericCache.getInstance().getCollectorStatus().getStartDate();
        return Response.ok(new CollectorStatus(startDate, "RUNNING", runningCollectionsCount)).build();
    }
    
}
