package qa.qcri.aidr.trainer.api.controller;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import qa.qcri.aidr.trainer.api.service.ClientAppSourceService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 6/12/14
 * Time: 9:28 AM
 * To change this template use File | Settings | File Templates.
 */
@Path("/source")
@Component
public class ClientAppSourceController {

    protected static Logger logger = Logger.getLogger("ClientAppSourceController");

    @Autowired
    private ClientAppSourceService clientAppSourceService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/save")
    public void saveAppSource(String data){
        try{
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject)parser.parse(data);
            String fileURL = (String)obj.get("url");
            String appType = (String)obj.get("appType");

           // clientAppSourceService.addExternalDataSouceWithClientAppID();
        }
        catch(Exception e){
            logger.error("saveAppSource got exception : " + e);
        }

    }
}