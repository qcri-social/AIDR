package qa.qcri.aidr.trainer.api.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import qa.qcri.aidr.trainer.api.service.TaskAnswerService;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/15/13
 * Time: 8:37 AM
 * To change this template use File | Settings | File Templates.
 */
@RequestMapping("/taskanswer")
@RestController
public class TaskAnswerController {

    protected static Logger logger = Logger.getLogger(TaskAnswerController.class);
    
    @Autowired
    private TaskAnswerService taskAnswerService;

    @RequestMapping(value = "/save", method={RequestMethod.POST})
    public void saveTaskAnswer(@RequestBody String data){
        //logger.info("saveTaskAnswer start: " + new Date());
        //logger.info("saveTaskAnswer..: " + data);
        //System.out.println("saveTaskAnswer start: " + new Date());
        //System.out.println("saveTaskAnswer..: " + data);
        // place into transaction block
        try{
            taskAnswerService.processTaskAnswer(data);
        }
        catch(Exception e){
            logger.error("Error while saving Task Answer",e);
        }
    }
}
