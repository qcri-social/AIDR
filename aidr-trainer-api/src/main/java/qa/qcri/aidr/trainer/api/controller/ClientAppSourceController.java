package qa.qcri.aidr.trainer.api.controller;

import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/source")
@RestController
public class ClientAppSourceController {

    protected static Logger logger = Logger.getLogger(ClientAppSourceController.class);

    @Autowired
    private ClientAppSourceService clientAppSourceService;

    @RequestMapping(value = "/save", method={RequestMethod.POST})
    public Response saveAppSource(@RequestBody String data){
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