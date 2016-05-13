package qa.qcri.aidr.collector.api;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import qa.qcri.aidr.collector.beans.CollectionTask;
import qa.qcri.aidr.collector.beans.CollectorStatus;
import qa.qcri.aidr.collector.utils.CollectorConfigurationProperty;
import qa.qcri.aidr.collector.utils.CollectorConfigurator;
import qa.qcri.aidr.collector.utils.GenericCache;

import com.google.gson.Gson;

/**
 * @author Imran
 * Provides RESTFul APIs to various management-related services.
 * 
 */
@Controller
@RequestMapping("/manage")
public class CollectorManageResource {

	private static Logger logger = Logger.getLogger(CollectorManageResource.class.getName());
	private static CollectorConfigurator configProperties = CollectorConfigurator.getInstance();
	
    @RequestMapping("/persist")
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
        }

        return response;

    }

    @RequestMapping("/runPersisted")
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
            logger.error("Error in reading file.");	
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
            	logger.error("Error in reading file.");	
            }
        }

        response = "Running persisted collections completed.";
        return response;
 
    }

    @RequestMapping("/count")
    @ResponseBody
    public Map getCollectionCount() {
    	Map<String, Long> countMap = new HashMap<String, Long>();
    	countMap.put("count", GenericCache.getInstance().getTotalCountSinceLastRestart());
    	return countMap;
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
        	logger.error("Could not start collection"+collection.getCollectionCode());
        	return "Could not start collection";
        }
    }
    
    @RequestMapping("/ping")
    @ResponseBody
    public CollectorStatus ping() throws InterruptedException {
        
        int runningCollectionsCount=0;
         List<CollectionTask> collections = GenericCache.getInstance().getAllRunningCollectionTasks();
        if (!(collections == null || collections.isEmpty())) {
            runningCollectionsCount = collections.size();
        }
        String startDate = GenericCache.getInstance().getCollectorStatus().getStartDate();
        return new CollectorStatus(startDate, "RUNNING", runningCollectionsCount);
    }
    
}
