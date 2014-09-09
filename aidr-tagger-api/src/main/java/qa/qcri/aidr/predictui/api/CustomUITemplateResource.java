package qa.qcri.aidr.predictui.api;

import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.predictui.entities.CustomUITemplate;
import qa.qcri.aidr.predictui.facade.CustomUITemplateFacade;
import qa.qcri.aidr.predictui.util.Config;
import qa.qcri.aidr.predictui.util.ResponseWrapper;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 3/20/14
 * Time: 2:27 PM
 * To change this template use File | Settings | File Templates.
 */
@Path("/customuitemplate")
@Stateless
public class CustomUITemplateResource {

    @Context
    private UriInfo context;
    @EJB
    private CustomUITemplateFacade customUITemplateFacade;

    private static Logger logger = Logger.getLogger(CustomUITemplateResource.class);
    private static ErrorLog elog = new ErrorLog();
    
    public CustomUITemplateResource(){

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addNewTemplate(CustomUITemplate customUITemplate) {
        System.out.println("*************************************************************");
        boolean isUpdate = false;
        CustomUITemplate dbTemplate = null;
        int type = customUITemplate.getTemplateType();
        if(!isAttributeInfoRequired(type)){
            List<CustomUITemplate> templates = customUITemplateFacade.getCustomUITemplateBasedOnTypeByCrisisID(customUITemplate.getCrisisID(), customUITemplate.getTemplateType());
            logger.info("templates size : " + templates.size());
            if(templates.size() > 0){
                isUpdate = true;
                dbTemplate = templates.get(0);
            }
        }
        else{
            List<CustomUITemplate> templates = customUITemplateFacade.getCustomUITemplateBasedOnTypeByCrisisIDAndAttributeID(customUITemplate.getCrisisID(),customUITemplate.getNominalAttributeID(), customUITemplate.getTemplateType());
            logger.info("templates size : " + templates.size());
            if(templates.size() > 0){
                isUpdate = true;
                dbTemplate = templates.get(0);
            }
        }

        CustomUITemplate addedTemplate = null;
        logger.info("isUpdate : " + isUpdate);

        if(isUpdate) {
            addedTemplate = customUITemplateFacade.updateCustomUITemplate(dbTemplate, customUITemplate) ;
            logger.info(addedTemplate.getCrisisID());
            return Response.ok(addedTemplate).build();
        }
        else{
            addedTemplate = customUITemplateFacade.addCustomUITemplate(customUITemplate);
        }

        return Response.ok(addedTemplate).build();

    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/crisisID/{crisisID}")
    public Response getCrisisByCode(@PathParam("crisisID") int crisisID) {

        List<CustomUITemplate> customUITemplate = null;

        try {
            customUITemplate = customUITemplateFacade.getAllCustomUITemplateByCrisisID((long) crisisID);
        } catch (RuntimeException e) {
            return Response.ok(new ResponseWrapper(Config.STATUS_CODE_FAILED, e.getCause().getCause().getMessage())).build();
        }

        return Response.ok(customUITemplate).build();
    }


    @GET
    @Produces("application/json")
    @Path("/tester")
    public Response ping() {
        String response = "{\"application\":\"aidr-tagger-api\", \"status\":\"RUNNING\"}";
        return Response.ok(response).build();

        ///landingpage/crisisID/
    }

    private boolean isAttributeInfoRequired(int type){
        boolean returnValue = false;

        if(type == Config.CLASSIFIER_DESCRIPTION_PAGE){
            returnValue = true;
        }

        if(type == Config.CLASSIFIER_TUTORIAL_ONE){
            returnValue = true;
        }

        if(type == Config.CLASSIFIER_TUTORIAL_TWO){
            returnValue = true;
        }

        return returnValue;
    }

}
