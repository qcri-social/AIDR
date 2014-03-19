package qa.qcri.aidr.trainer.api.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import qa.qcri.aidr.trainer.api.service.TaskAssignmentService;
import qa.qcri.aidr.trainer.api.store.StatusCodeType;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/15/13
 * Time: 2:59 PM
 * To change this template use File | Settings | File Templates.
 */
@Path("/taskassignment")
@Component
public class TaskAssignmentController {

    protected static Logger logger = Logger.getLogger("service");

    @Autowired
    private TaskAssignmentService taskAssignmentService;

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Path("/get/searchByUserID/{userID}")
    public Integer getCrisisByID(@PathParam("userID") String userID){
        return  taskAssignmentService.getPendingTaskCount(new Long(userID));
    }

}
