package qa.qcri.aidr.trainer.api.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import qa.qcri.aidr.dbmanager.dto.CustomUiTemplateDTO;
import qa.qcri.aidr.dbmanager.entities.misc.CustomUiTemplate;
import qa.qcri.aidr.trainer.api.service.CustomUITemplateService;
import qa.qcri.aidr.trainer.api.store.CodeLookUp;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 4/4/14
 * Time: 5:24 PM
 * To change this template use File | Settings | File Templates.
 */
@RequestMapping("/customUI")
@RestController
public class CustomUITemplateController {

    protected static Logger logger = Logger.getLogger(CustomUITemplateController.class);

    @Autowired
    private CustomUITemplateService customUITemplateService;

    @RequestMapping("/get/LandingPage/{crisisID}")
    public List<CustomUiTemplate> getLandingUIByID(@PathVariable("crisisID") Long crisisID){
        return  customUITemplateService.getCustomTemplateForLandingPage(crisisID);
    }

    @RequestMapping("/get/customUI/{crisisID}")
    public List<CustomUiTemplate> getCustomUIByID(@PathVariable("crisisID") Long crisisID){
        logger.info("[getCustomUIByID] Received request for crisisID = " + crisisID);
    	return  customUITemplateService.getCustomTemplateForLandingPage(crisisID);
    }

    @RequestMapping(value = "/welcome/update", method={RequestMethod.POST})
    public void updateWelcomePage(@RequestBody String data){
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
            logger.debug("Exception while updating welcome page " + data,e);
        }
    }


    @RequestMapping(value = "/tutorial/update", method={RequestMethod.POST})
    public void updateTutorial(@RequestBody String data){
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
            List<CustomUiTemplateDTO> templates = customUITemplateService.getCustomTemplateSkinType(crisisID);

            if(templates.size() > 0){
            	CustomUiTemplateDTO c = templates.get(0);
                skinType = Integer.parseInt(c.getTemplateValue());
            }

            customUITemplateService.updateCustomTemplateByAttribute(crisisID,attributeID,customUIType,skinType);

        }
        catch(Exception e){
            logger.debug("Exception while updating tutorial " + data,e);
        }

        //updateCustomTemplateByAttribute(Long crisisID, Long attributeID, int customUIType, int skinType)

    }

    @RequestMapping(value = "/skin/update", method={RequestMethod.POST})
    public void updateSkin(@RequestBody String data){
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
            List<CustomUiTemplateDTO> templates = customUITemplateService.getCustomTemplateSkinType(crisisID);

            if(templates.size() > 0){
            	CustomUiTemplateDTO c = templates.get(0);
                skinType = Integer.parseInt(c.getTemplateValue());
            }

            customUITemplateService.updateCustomTemplateByAttribute(crisisID,attributeID,customUIType,skinType);

        }
        catch(Exception e){
            logger.debug("Exception while updating skin " + data,e);
        }
    }
}
