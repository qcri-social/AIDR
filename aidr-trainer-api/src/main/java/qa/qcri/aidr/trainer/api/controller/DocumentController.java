package qa.qcri.aidr.trainer.api.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import qa.qcri.aidr.trainer.api.entity.Document;
import qa.qcri.aidr.trainer.api.service.DocumentService;
import qa.qcri.aidr.trainer.api.store.StatusCodeType;
import qa.qcri.aidr.trainer.api.template.TaskBufferJsonModel;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 3/4/14
 * Time: 9:17 PM
 * To change this template use File | Settings | File Templates.
 */
@Path("/document")
@Component
public class DocumentController {
    protected static Logger logger = Logger.getLogger("service");

    @Autowired
    private DocumentService documentService;

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Path("/getbatchtaskbuffer/{userName}/{crisisID}/{maxresult}")
    public List<Document> getAllTaskBufferToAssign(
            @PathParam("userName") String userName,
            @PathParam("crisisID") String crisisID,
            @PathParam("maxresult") String maxresult){

        return documentService.getDocumentForTask(new Long(crisisID), Integer.valueOf(maxresult), userName);
        //  return  taskBufferService.findAssignableTaskBuffer("assignedCount", 0, Integer.valueOf(maxresult));

    }

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Path("/getassignabletask/{userName}/{crisisID}/{maxresult}")
    public List<TaskBufferJsonModel> getOneTaskBufferToAssign(@PathParam("crisisID") String crisisID,
                                                              @PathParam("userName") String userName,
                                                              @PathParam("maxresult") String maxresult){

        logger.debug("getOneTaskBufferToAssign..: " + crisisID + " -" +  userName + " -" +  maxresult );
        return documentService.findOneDocumentForTaskByCririsID(new Long(crisisID),userName,Integer.valueOf(maxresult));
        //  return  taskBufferService.findAssignableTaskBuffer("assignedCount", 0, Integer.valueOf(maxresult));
    }
}
