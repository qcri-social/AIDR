package qa.qcri.aidr.trainer.api.controller;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import qa.qcri.aidr.trainer.api.entity.CustomUITemplate;
import qa.qcri.aidr.trainer.api.service.CustomUITemplateService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 4/4/14
 * Time: 5:24 PM
 * To change this template use File | Settings | File Templates.
 */
@Path("/customUI")
@Component
public class CustomUITemplateController {

    protected static Logger logger = Logger.getLogger("service");

    @Autowired
    private CustomUITemplateService customUITemplateService;

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Path("/get/LandingPage/{crisisID}")
    public List<CustomUITemplate> getLandingUIByID(@PathParam("crisisID") Long crisisID){
        return  customUITemplateService.getCustomTemplateForLandingPage(crisisID);
    }

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Path("/get/customUI/{crisisID}")
    public List<CustomUITemplate> getCustomUIByID(@PathParam("crisisID") Long crisisID){
        return  customUITemplateService.getCustomTemplateForLandingPage(crisisID);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/welcome/update")
    public void updateWelcomePage(String data){
        logger.debug("updateWelcomePage start: " + new Date());
        logger.debug("updateWelcomePage..: " + data);
        //updateCustomTemplateByAttribute(Long crisisID, Long attributeID, int customUIType, int skinType)
        try{
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(data);

            JSONObject jsonObject = (JSONObject)obj;

            long crisisID = (Long)jsonObject.get("crisisID");
            long attributeID = (Long)jsonObject.get("attributeID");
            int customUIType = (Integer)jsonObject.get("customUIType");

            customUITemplateService.updateCustomTemplateByAttribute(crisisID,attributeID,customUIType,0);

        }
        catch(Exception e){

        }
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/tutorial/update")
    public void updateTutorial(String data){
        logger.debug("updateTutorial start: " + new Date());
        logger.debug("updateTutorial..: " + data);

        try{
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(data);

            JSONObject jsonObject = (JSONObject)obj;

            long crisisID = (Long)jsonObject.get("crisisID");
            long attributeID = (Long)jsonObject.get("attributeID");
            int customUIType = (Integer)jsonObject.get("customUIType");
            int skinType = (Integer)jsonObject.get("skinType");

            customUITemplateService.updateCustomTemplateByAttribute(crisisID,attributeID,customUIType,skinType);


        }
        catch(Exception e){

        }

        //updateCustomTemplateByAttribute(Long crisisID, Long attributeID, int customUIType, int skinType)

    }
    // update landing page
    // update tutorial
    // update long description
}
