package qa.qcri.aidr.trainer.api.controller;

import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import qa.qcri.aidr.trainer.api.service.TaskAssignmentService;
import qa.qcri.aidr.trainer.api.store.CodeLookUp;
import qa.qcri.aidr.trainer.api.store.StatusCodeType;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/15/13
 * Time: 2:59 PM
 * To change this template use File | Settings | File Templates.
 */
@RequestMapping("/taskassignment")
@RestController
public class TaskAssignmentController {

    protected static Logger logger = Logger.getLogger(TaskAssignmentController.class);
    
    @Autowired
    private TaskAssignmentService taskAssignmentService;

    @RequestMapping("/get/searchByUserID/{userID}")
    public Integer getCrisisByID(@PathVariable("userID") String userID){
        return  taskAssignmentService.getPendingTaskCount(Long.parseLong(userID));
    }

    @RequestMapping("/revert/id/{userID}/{documentID}")
    public Response revertTaskAssignment(@PathVariable("userID") Long userID, @PathVariable("documentID") Long documentID){
        taskAssignmentService.revertTaskAssignment(documentID, userID);
        return Response.status(CodeLookUp.APP_STATUS_ALIVE).entity(StatusCodeType.POST_COMPLETED).build();
    }

    @RequestMapping("/revert/name/{userName}/{documentID}")
    public Response revertTaskAssignmentByUserName(@PathVariable("userName") String userName, @PathVariable("documentID") Long documentID){

        taskAssignmentService.revertTaskAssignmentByUserName(documentID, userName);

        return Response.status(CodeLookUp.APP_STATUS_ALIVE).entity(StatusCodeType.POST_COMPLETED).build();
    }

}
