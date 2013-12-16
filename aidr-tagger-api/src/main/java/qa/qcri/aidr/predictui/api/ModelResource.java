/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.api;

import java.util.List;
import qa.qcri.aidr.predictui.util.ResponseWrapper;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import qa.qcri.aidr.predictui.entities.Model;
import qa.qcri.aidr.predictui.facade.ModelFacade;
import qa.qcri.aidr.predictui.util.Config;
import qa.qcri.aidr.predictui.dto.ModelWrapper;

/**
 * REST Web Service
 *
 * @author Imran
 */
@Path("/model")
@Stateless
public class ModelResource {

    @Context
    private UriInfo context;
    @EJB
    private ModelFacade modelLocalEJB;

    public ModelResource() {
    }
    
    @GET
    @Produces("application/json")
    @Path("/all")
    public Response getAllModels() {
        List<Model> modelList = modelLocalEJB.getAllModels();
        ResponseWrapper response = new ResponseWrapper();
        response.setMessage(Config.STATUS_CODE_SUCCESS);
        response.setModels(modelList);
        return Response.ok(response).build();
    }
    
   @GET
   @Produces(MediaType.APPLICATION_JSON)
   @Path("{id}")
   public Response getModelByID(@PathParam("id") int id){
       Model model = modelLocalEJB.getModelByID(id);
       return Response.ok(model).build();
   }
   
   
   @GET
   @Produces(MediaType.APPLICATION_JSON)
   @Path("crisis/{crisisID}")
   public Response getModelByCrisisID(@PathParam("crisisID") int crisisID){
       List<ModelWrapper> modelList = modelLocalEJB.getModelByCrisisID(crisisID);
       ResponseWrapper response = new ResponseWrapper();
       if (modelList == null){
           response.setMessage("No models found for the given crisis-id");
           return Response.ok(response).build();
       }
        response.setModelWrapper(modelList);
       return Response.ok(response).build();
   }
    
}
