package qa.qcri.aidr.trainer.api.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import qa.qcri.aidr.trainer.api.service.ClientAppSourceService;
import qa.qcri.aidr.trainer.api.store.CodeLookUp;
import qa.qcri.aidr.trainer.api.store.StatusCodeType;

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

    protected static Logger logger = Logger.getLogger(ClientAppSourceController.class);

    @Autowired
    private ClientAppSourceService clientAppSourceService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/save")
    public Response saveAppSource(String data){
        String returnValue = StatusCodeType.RETURN_SUCCESS;

        logger.info("saveAppSource : " + data );

        try{
            JSONParser parser = new JSONParser();
            JSONArray objs = (JSONArray)parser.parse(data);
            for(Object a : objs){
                JSONObject obj = (JSONObject)a;
                String fileURL = (String)obj.get("fileURL");
                Long appID = (Long)obj.get("appID");

                logger.info("fileURL : " + fileURL );
                logger.info("appID : " + appID );

                clientAppSourceService.addExternalDataSouceWithClientAppID(fileURL, appID);
            }
        }
        catch(Exception e){
            returnValue = StatusCodeType.RETURN_FAIL;
            logger.error("Exception while saving app source",e);
        }

        return Response.status(CodeLookUp.APP_REQUEST_SUCCESS).entity(returnValue).build();
    }

}