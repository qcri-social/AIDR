package qa.qcri.aidr.trainer.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import qa.qcri.aidr.trainer.api.entity.Crisis;
import qa.qcri.aidr.trainer.api.service.CrisisService;
import qa.qcri.aidr.trainer.api.template.CrisisJsonModel;
import qa.qcri.aidr.trainer.api.template.CrisisNominalAttributeModel;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/11/13
 * Time: 2:49 PM
 * To change this template use File | Settings | File Templates.
 */
@Path("/crisis")
@Component
public class CrisisController {
    @Autowired
    private CrisisService crisisService;

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Path("/id/{crisisid}")
    public CrisisJsonModel getCrisisByID(@PathParam("crisisid") String crisisid){
        return  crisisService.findByOptimizedCrisisID(new Long(crisisid));
    }

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Path("/getallactive")
    public List<Crisis> getAllActiveCrisis(){
        return  crisisService.findAllActiveCrisis();
    }

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Path("/getnominalAttribute")
    public List<CrisisNominalAttributeModel> getAllActiveCrisisNominalAttribute(){
        return  crisisService.getAllActiveCrisisNominalAttribute();
    }

}
