package qa.qcri.aidr.trainer.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import qa.qcri.aidr.trainer.api.service.TemplateService;
import qa.qcri.aidr.trainer.api.template.CrisisApplicationListModel;
import qa.qcri.aidr.trainer.api.template.CrisisLandingHtmlModel;
import qa.qcri.aidr.trainer.api.template.CrisisLandingStatusModel;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 10/27/13
 * Time: 3:05 PM
 * To change this template use File | Settings | File Templates.
 */
@Path("/template")
@Component
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Path("/app/id/{crisisid}")
    public List<CrisisApplicationListModel> getCrisisByID(@PathParam("crisisid") Long crisisid){

       return templateService.getApplicationListHtmlByCrisisID(crisisid);
    }

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Path("/app/code/{code}")
    public List<CrisisApplicationListModel> getCrisisByCode(@PathParam("code") String code){

        return templateService.getApplicationListHtmlByCrisisCode(code);
    }

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Path("/crisis/code/{code}")
    public CrisisLandingHtmlModel getCrisisTemplateByCode(@PathParam("code") String code){

        return templateService.getCrisisLandingHtmlByCrisisCode(code);
    }

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Path("/crisis/id/{crisisid}")
    public CrisisLandingHtmlModel getCrisisTemplateByID(@PathParam("crisisid") Long crisisid){

        return templateService.getCrisisLandingHtmlByCrisisID(crisisid);
    }

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Path("/status/crisis/code/{code}")
    public CrisisLandingStatusModel getCrisisTemplateByID(@PathParam("code") String code){
        return templateService.getCrisisLandingStatusByCrisisCode(code);
    }


    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Path("/JSONP/crisis/id/{crisisid}")
    public String getCrisisTemplateJSONPByID(@PathParam("crisisid") Long crisisid){

        return templateService.getCrisisLandingJSONPByCrisisID(crisisid);
    }

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Path("/JSONP/crisis/code/{crisisCode}")
    public String getCrisisTemplateJSONPByCode(@PathParam("crisisCode") String crisisCode ){

        return templateService.getCrisisLandingJSONPByCrisisCode(crisisCode);
    }
}
