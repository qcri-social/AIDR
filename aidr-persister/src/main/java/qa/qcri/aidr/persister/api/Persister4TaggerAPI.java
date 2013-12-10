package qa.qcri.aidr.persister.api;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import com.sun.jersey.api.json.JSONWithPadding;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang.StringUtils;
import qa.qcri.aidr.persister.collector.RedisCollectorPersister;
import qa.qcri.aidr.persister.tagger.RedisTaggerPersister;
import qa.qcri.aidr.utils.ClassifiedTweet;
import qa.qcri.aidr.utils.Config;
import qa.qcri.aidr.utils.GenericCache;
import qa.qcri.aidr.utils.JsonDeserializer;

/**
 * REST Web Service
 *
 * @author Imran
 */
@Path("taggerPersister")
public class Persister4TaggerAPI {

    @Context
    private UriInfo context;

    public Persister4TaggerAPI() {
    }

    @GET
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/start")
    public Response startPersister(@QueryParam("file") String fileLocation, @QueryParam("collectionCode") String collectionCode) {
        String response = "";
        try {
            fileLocation = Config.DEFAULT_PERSISTER_FILE_PATH; //OVERRIDING PATH RECEIVED FROM EXTERNAL REQUEST
            if (StringUtils.isNotEmpty(fileLocation) && StringUtils.isNotEmpty(collectionCode)) {
                if (GenericCache.getInstance().getTaggerPersisterMap(collectionCode) != null) {
                    response = "A persister is already running for this collection code [" + collectionCode + "]";
                    return Response.ok(response).build();
                }

                RedisTaggerPersister p = new RedisTaggerPersister(fileLocation, collectionCode);
                p.startMe();
                GenericCache.getInstance().setTaggerPersisterMap(collectionCode, p);
                response = "Started tagger persisting to " + fileLocation;
                return Response.ok(response).build();
            }
        } catch (Exception ex) {
            Logger.getLogger(Persister4TaggerAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.ok(response).build();
    }

    @GET
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/stop")
    public Response stopPersister(@QueryParam("collectionCode") String collectionCode) {
        String response;
        RedisCollectorPersister p = (RedisCollectorPersister) GenericCache.getInstance().getPersisterObject(collectionCode);
        if (p != null) {
            try {
                System.out.println("Aborting persister...");
                GenericCache.getInstance().delPersisterObject(collectionCode);
                p.suspendMe();
                System.out.println("Aborting done for " + collectionCode);
                response = "Persistance of [" + collectionCode + "] has been stopped.";
                return Response.ok(response).build();
            } catch (InterruptedException ex) {
                Logger.getLogger(Persister4TaggerAPI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        response = "Unable to locate a running persister with the given collection code:[" + collectionCode + "]";
        return Response.ok(response).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/genCSV")
    public Response generateCSVFromLastestJSON(@QueryParam("collectionCode") String collectionCode, @QueryParam("exportLimit") int exportLimit) throws UnknownHostException {
        JsonDeserializer jsonD = new JsonDeserializer();
        String fileName = jsonD.taggerGenerateJSON2CSV_100K_BasedOnTweetCount(collectionCode, exportLimit);
        fileName = Config.SCD1_URL + collectionCode + "/" + fileName;
        return Response.ok(fileName).build();
    }

    @GET
    @Produces("application/json")
    @Path("/genTweetIds")
    public Response generateTweetsIDSCSVFromAllJSON(@QueryParam("collectionCode") String collectionCode) throws UnknownHostException {
        JsonDeserializer jsonD = new JsonDeserializer();
        String fileName = jsonD.generateClassifiedJson2TweetIdsCSV(collectionCode);
        fileName = Config.SCD1_URL + collectionCode + "/" + fileName;
        return Response.ok(fileName).build();
    }

    @GET
    @Produces({"application/javascript"})
    @Path("/getClassifiedTweets")
    public JSONWithPadding get_N_LatestClassifiedTweets(@QueryParam("collectionCode") String collectionCode, @QueryParam("exportLimit") int exportLimit, @QueryParam("callback") String callback) throws UnknownHostException {
        JsonDeserializer jsonD = new JsonDeserializer();
        List<ClassifiedTweet> tweets = jsonD.getNClassifiedTweetsJSON(collectionCode, exportLimit);
        //return Response.ok(tweets).build();
        return new JSONWithPadding(new GenericEntity<List<ClassifiedTweet>>(tweets) {
        }, callback);
    }

}
