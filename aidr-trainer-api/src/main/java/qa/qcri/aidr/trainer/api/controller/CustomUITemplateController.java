package qa.qcri.aidr.trainer.api.controller;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.trainer.api.entity.CustomUITemplate;
import qa.qcri.aidr.trainer.api.service.CustomUITemplateService;
import qa.qcri.aidr.trainer.api.store.CodeLookUp;

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

    protected static Logger logger = Logger.getLogger(CustomUITemplateController.class);
    private static ErrorLog elog = new ErrorLog();

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
        //logger.debug("updateWelcomePage start: " + new Date());
        //logger.debug("updateWelcomePage..: " + data);
        //updateCustomTemplateByAttribute(Long crisisID, Long attributeID, int customUIType, int skinType)
        try{
            //logger.debug("updateWelcomePage. before parse: " + data);
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(data);

            JSONObject jsonObject = (JSONObject)obj;

            Long crisisID = (Long)jsonObject.get("crisisID");


            Long attributeID = (Long)jsonObject.get("nominalAttributeID");


            int customUIType = ((Long)jsonObject.get("templateType")).intValue();


            //logger.debug("crisisID..: " + crisisID);
            //logger.debug("attributeID..: " + attributeID);
            //logger.debug("customUIType..: " + customUIType);

            customUITemplateService.updateCustomTemplateByAttribute(crisisID,attributeID,customUIType,0);

        }
        catch(Exception e){
            logger.debug("updateWelcomePage. Exception: " + data);
            logger.error(elog.toStringException(e));
        }
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/tutorial/update")
    public void updateTutorial(String data){
        //logger.debug("updateTutorial start: " + new Date());
        //logger.debug("updateTutorial..: " + data);

        try{
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(data);

            JSONObject jsonObject = (JSONObject)obj;

            long crisisID = (Long)jsonObject.get("crisisID");
            long attributeID = (Long)jsonObject.get("nominalAttributeID");
            int customUIType = ((Long)jsonObject.get("templateType")).intValue();
            int skinType = CodeLookUp.DEFAULT_SKIN;
            List<CustomUITemplate> templates = customUITemplateService.getCustomTemplateSkinType(crisisID);

            if(templates.size() > 0){
                CustomUITemplate c = templates.get(0);
                skinType = Integer.parseInt(c.getTemplateValue());
            }

            customUITemplateService.updateCustomTemplateByAttribute(crisisID,attributeID,customUIType,skinType);

        }
        catch(Exception e){
            logger.debug("updateTutorial. Exception: " + data);
            logger.error(elog.toStringException(e));

        }

        //updateCustomTemplateByAttribute(Long crisisID, Long attributeID, int customUIType, int skinType)

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/skin/update")
    public void updateSkin(String data){
        //logger.debug("updateSkin start: " + new Date());
        //logger.debug("updateSkin..: " + data);

        try{
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(data);

            JSONObject jsonObject = (JSONObject)obj;

            long crisisID = (Long)jsonObject.get("crisisID");
            long attributeID = 0;
            int customUIType = ((Long)jsonObject.get("templateType")).intValue();
            int skinType = CodeLookUp.DEFAULT_SKIN;
            List<CustomUITemplate> templates = customUITemplateService.getCustomTemplateSkinType(crisisID);

            if(templates.size() > 0){
                CustomUITemplate c = templates.get(0);
                skinType = Integer.parseInt(c.getTemplateValue());
            }

            customUITemplateService.updateCustomTemplateByAttribute(crisisID,attributeID,customUIType,skinType);

        }
        catch(Exception e){
            logger.debug("updateTutorial. Exception: " + data);
            logger.error(elog.toStringException(e));

        }

        //updateCustomTemplateByAttribute(Long crisisID, Long attributeID, int customUIType, int skinType)

    }


    // update landing page
    // update tutorial
    // update long description
}
