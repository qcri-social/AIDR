package qa.qcri.aidr.manager.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import qa.qcri.aidr.manager.dto.UITemplateRequest;
import qa.qcri.aidr.manager.exception.AidrException;
import qa.qcri.aidr.manager.service.CollectionService;
import qa.qcri.aidr.manager.service.UITemplateService;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 3/23/14
 * Time: 1:59 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("protected/uitemplate")
public class UITemplateController extends BaseController {

    private Logger logger = Logger.getLogger(UITemplateController.class);

    @Autowired
    private UITemplateService uiTemplateService;

    @Autowired
    private CollectionService collectionService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }


    @RequestMapping(value = "/updateTemplate.action", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> updateTemplate(UITemplateRequest uiTemplateRequest) {
        logger.info("Creating new label in uiTemplate");

        try {
            UITemplateRequest response = uiTemplateService.updateTemplate(uiTemplateRequest);
            if (response != null){
                return getUIWrapper(response, true);
            } else {
                return getUIWrapper(false, "Error while creating new ui template in uiTemplate");
            }
        } catch (Exception e) {
            logger.error("Error while updating UI Template", e);
            return getUIWrapper(false, e.getMessage());
        }
    }

    @RequestMapping(value = "/getTemplate.action", method = {RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> getTemplate(long crisisID) {
        logger.info("get Template for crisisID = " + crisisID);

        try {
            logger.info("crisisID :" + crisisID);
            String response = uiTemplateService.getTemplatesByCrisisID(crisisID);

            if (response != null){
                return getUIWrapper(response, true);
            } else {
                return getUIWrapper(false, "Error while getLandingPageTemplate in uiTemplate");
            }

        } catch (Exception e) {
            logger.error("Error while fetching templates by crisisID: "+crisisID, e);
            return getUIWrapper(false, e.getMessage());
        }
    }

    @RequestMapping(value = "/getCrisisChildren.action", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getCrisisChildren(@RequestParam Integer id) throws Exception {
        try {
            logger.info("Get Assignable Task is started with crisis id: " + id);

            getAuthenticatedUserName();
            String sVar = uiTemplateService.getCrisisChildrenElement(id);

            return getUIWrapper(sVar, true);
        } catch (AidrException e) {
        	logger.error("Error while getting crisis children elements for crisisID: "+id, e);
        	return getUIWrapper(e.getMessage(), false);
        }
    }

}
