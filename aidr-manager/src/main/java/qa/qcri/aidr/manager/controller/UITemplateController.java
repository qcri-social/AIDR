package qa.qcri.aidr.manager.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import qa.qcri.aidr.manager.dto.UITemplateRequest;
import qa.qcri.aidr.manager.exception.AidrException;
import qa.qcri.aidr.manager.service.CollectionService;
import qa.qcri.aidr.manager.service.TaggerService;
import qa.qcri.aidr.manager.service.UITemplateService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
            logger.error(e.getMessage(), e);
            return getUIWrapper(false, e.getMessage());
        }
    }

    @RequestMapping(value = "/getTemplate.action", method = {RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> getTemplate(long crisisID) {
        logger.info("get Template");

        try {
            System.out.println("crisisID :" + crisisID);
            String response = uiTemplateService.getTemplatesByCrisisID(crisisID);

            if (response != null){
                return getUIWrapper(response, true);
            } else {
                return getUIWrapper(false, "Error while getLandingPageTemplate in uiTemplate");
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return getUIWrapper(false, e.getMessage());
        }
    }

    @RequestMapping(value = "/getCrisisChildren.action", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getCrisisChildren(@RequestParam Integer id) throws Exception {
        try {
            logger.info("Get Assignable Task is started with crisis id: " + id);

            String userName = getAuthenticatedUserName();
            String sVar = uiTemplateService.getCrisisChildrenElement(id);

           // logger.info("sVar : " + sVar);
            return getUIWrapper(sVar, true);
        } catch (AidrException e) {
            e.printStackTrace();
            return getUIWrapper(e.getMessage(), false);
        }
    }

}
