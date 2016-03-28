package qa.qcri.aidr.trainer.api.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import qa.qcri.aidr.trainer.api.service.TemplateService;
import qa.qcri.aidr.trainer.api.template.CrisisApplicationListModel;
import qa.qcri.aidr.trainer.api.template.CrisisLandingHtmlModel;
import qa.qcri.aidr.trainer.api.template.CrisisLandingStatusModel;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 10/27/13
 * Time: 3:05 PM
 * To change this template use File | Settings | File Templates.
 */
@RequestMapping("/template")
@RestController
public class TemplateController {
	
	protected static Logger logger = Logger.getLogger(TemplateController.class);

    @Autowired
    private TemplateService templateService;

    @RequestMapping("/app/id/{crisisid}")
    public List<CrisisApplicationListModel> getCrisisByID(@PathVariable("crisisid") Long crisisid){

       return templateService.getApplicationListHtmlByCrisisID(crisisid);
    }

    @RequestMapping("/app/code/{code}")
    public List<CrisisApplicationListModel> getCrisisByCode(@PathVariable("code") String code){

        return templateService.getApplicationListHtmlByCrisisCode(code);
    }

    @RequestMapping("/crisis/code/{code}")
    public CrisisLandingHtmlModel getCrisisTemplateByCode(@PathVariable("code") String code){

        return templateService.getCrisisLandingHtmlByCrisisCode(code);
    }

    @RequestMapping("/crisis/id/{crisisid}")
    public CrisisLandingHtmlModel getCrisisTemplateByID(@PathVariable("crisisid") Long crisisid){

        return templateService.getCrisisLandingHtmlByCrisisID(crisisid);
    }

    @RequestMapping("/status/crisis/code/{code}")
    public CrisisLandingStatusModel getCrisisTemplateByID(@PathVariable("code") String code){
    	logger.info("[getCrisisTemplateByID] received request for code = " + code);
    	return templateService.getCrisisLandingStatusByCrisisCode(code);
    }


    @RequestMapping("/JSONP/crisis/id/{crisisid}")
    public String getCrisisTemplateJSONPByID(@PathVariable("crisisid") Long crisisid){

        return templateService.getCrisisLandingJSONPByCrisisID(crisisid);
    }

    @RequestMapping("/JSONP/crisis/code/{crisisCode}")
    public String getCrisisTemplateJSONPByCode(@PathVariable("crisisCode") String crisisCode ){

        return templateService.getCrisisLandingJSONPByCrisisCode(crisisCode);
    }
}
