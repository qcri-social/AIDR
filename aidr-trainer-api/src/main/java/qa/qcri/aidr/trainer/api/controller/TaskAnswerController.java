package qa.qcri.aidr.trainer.api.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import qa.qcri.aidr.trainer.api.service.TaskAnswerService;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/15/13
 * Time: 8:37 AM
 * To change this template use File | Settings | File Templates.
 */
@Path("/taskanswer")
@Component
public class TaskAnswerController {

    protected static Logger logger = Logger.getLogger(TaskAnswerController.class);
    
    @Autowired
    private TaskAnswerService taskAnswerService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/save")
    public void saveTaskAnswer(String data){
        //logger.info("saveTaskAnswer start: " + new Date());
        //logger.info("saveTaskAnswer..: " + data);
        //System.out.println("saveTaskAnswer start: " + new Date());
        //System.out.println("saveTaskAnswer..: " + data);
        // place into transaction block
        try{
            taskAnswerService.processTaskAnswer(data);
        }
        catch(Exception e){
            logger.error("saveTaskAnswer got exception on: " + data);
        }

        // below is non transaction block
       /**
        try{
            TaskAnswerResponse taskAnswerResponse =  taskAnswerService.getTaskAnswerResponseData(data);
            taskAnswerService.markOnHasHumanTag(taskAnswerResponse.getDocumentID());
            taskAnswerService.addToTaskAnswer( taskAnswerResponse);
            taskAnswerService.addToDocumentNominalLabel(taskAnswerResponse);
            taskAnswerService.removeTaskAssignment(taskAnswerResponse);
            taskAnswerService.pushTaskAnswerToJedis(taskAnswerResponse);

        }
        catch(Exception e){
            System.out.println("saveTaskAnswer : " +  e);
        }  **/

    }
}
