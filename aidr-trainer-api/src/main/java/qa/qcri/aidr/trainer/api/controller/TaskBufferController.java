package qa.qcri.aidr.trainer.api.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import qa.qcri.aidr.trainer.api.entity.TaskBuffer;
import qa.qcri.aidr.trainer.api.service.TaskBufferService;

import org.springframework.beans.factory.annotation.Autowired;
import qa.qcri.aidr.trainer.api.store.StatusCodeType;
import qa.qcri.aidr.trainer.api.template.TaskBufferJsonModel;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/taskbuffer")
@Component
public class TaskBufferController {
    protected static Logger logger = Logger.getLogger("service");

    @Autowired
    private TaskBufferService taskBufferService;

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Path("/getbatchtaskbuffer/{userName}/{crisisID}/{maxresult}")
    public List<TaskBuffer> getAllTaskBufferToAssign(
                                                     @PathParam("userName") String userName,
                                                     @PathParam("crisisID") String crisisID,
                                                     @PathParam("maxresult") String maxresult){
        return taskBufferService.findAvailableaskBufferByCririsID(new Long(crisisID),userName, StatusCodeType.TASK_BUFFER_STATUS_AVAILABLE,Integer.valueOf(maxresult));
      //  return  taskBufferService.findAssignableTaskBuffer("assignedCount", 0, Integer.valueOf(maxresult));

    }

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Path("/getassignabletask/{userName}/{crisisID}/{maxresult}")
    public List<TaskBufferJsonModel> getOneTaskBufferToAssign(@PathParam("crisisID") String crisisID,
                                                     @PathParam("userName") String userName,
                                                     @PathParam("maxresult") String maxresult){

        logger.debug("getOneTaskBufferToAssign..: " + crisisID + " -" +  userName + " -" +  maxresult );
        return taskBufferService.findOneTaskBufferByCririsID(new Long(crisisID),userName, StatusCodeType.TASK_BUFFER_STATUS_AVAILABLE,Integer.valueOf(maxresult));
        //  return  taskBufferService.findAssignableTaskBuffer("assignedCount", 0, Integer.valueOf(maxresult));
    }
}
