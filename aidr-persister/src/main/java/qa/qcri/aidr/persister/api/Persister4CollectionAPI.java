package qa.qcri.aidr.persister.api;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import qa.qcri.aidr.logging.ErrorLog;
import qa.qcri.aidr.persister.collction.RedisCollectionPersister;
import qa.qcri.aidr.utils.Config;
import qa.qcri.aidr.utils.GenericCache;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author Imran
 */
@Path("collectionPersister")
public class Persister4CollectionAPI {

    private static Logger logger = Logger.getLogger(Persister4CollectionAPI.class.getName());
    private static ErrorLog elog = new ErrorLog();

    @GET
    @Path("/start")
    @Produces(MediaType.APPLICATION_JSON)
    public Response startPersister(@QueryParam("channel_provider") String provider, @QueryParam("collection_code") String code) {
        String response = "";
        try {
            String channel = StringUtils.defaultIfBlank(provider, "") + "." + StringUtils.defaultIfBlank(code, "");
            if (StringUtils.isNotEmpty(code)) {
                if (GenericCache.getInstance().getCollectionPersisterObject(code) != null) {
                    response = "A persister is already running for this channel [" + channel + "]";
                    return Response.ok(response).build();
                }

                RedisCollectionPersister p = new RedisCollectionPersister(Config.DEFAULT_PERSISTER_FILE_PATH, channel, code);
                p.startMe();
                GenericCache.getInstance().setCollectionPersisterMap(code, p);
                response = "Started persisting to " + Config.DEFAULT_PERSISTER_FILE_PATH;
                return Response.ok(response).build();
            }
        } catch (Exception ex) {
            logger.error(code + ": Failed to start persister");
            logger.error(elog.toStringException(ex));
        }
        return Response.ok(response).build();
    }

    @GET
    @Path("/stop")
    @Produces(MediaType.APPLICATION_JSON)
    public Response stopPersister(@QueryParam("collection_code") String code) {
        String response;
        try {
            logger.debug(code + "Aborting persister...");
            RedisCollectionPersister persister = GenericCache.getInstance().delCollectionPersisterMap(code);
            if(persister != null)
                persister.suspendMe();
            logger.info("Aborting done for " + code);
            response = "Persistance of [" + code + "] has been stopped.";
            return Response.ok(response).build();
        } catch (InterruptedException ex) {
            logger.error(code + ": Failed to stop persister");
            logger.error(elog.toStringException(ex));
        }
        response = "Unable to locate a running persister with the given collection code:[" + code + "]";
        return Response.ok(response).build();
    }
}
