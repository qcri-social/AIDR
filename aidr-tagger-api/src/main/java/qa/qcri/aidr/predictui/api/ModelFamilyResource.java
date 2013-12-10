/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.api;

import java.util.List;

import qa.qcri.aidr.predictui.dto.ModelFamilyDTO;
import qa.qcri.aidr.predictui.util.ResponseWrapper;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import qa.qcri.aidr.predictui.entities.ModelFamily;
import qa.qcri.aidr.predictui.facade.ModelFamilyFacade;
import qa.qcri.aidr.predictui.util.Config;

/**
 * REST Web Service
 *
 * @author Imran
 */
@Path("/modelfamily")
@Stateless
public class ModelFamilyResource {

    @Context
    private UriInfo context;
    @EJB
    private ModelFamilyFacade modelFamilyLocalEJB;

    public ModelFamilyResource() {
    }
    
    @GET
    @Produces("application/json")
    @Path("/all")
    public Response getAllModelFamilies() {
        List<ModelFamily> modelFamilyList = modelFamilyLocalEJB.getAllModelFamilies();
        ResponseWrapper response = new ResponseWrapper();
        response.setMessage("SUCCESS");
        response.setModelFamilies(modelFamilyList);
        return Response.ok(response).build();
    }
    
    @GET
    @Produces("application/json")
    @Path("/crisis/{id}")
    public Response getAllModelFamiliesByCrisisID(@PathParam("id") int crisisID) {
        List<ModelFamily> modelFamilyList = modelFamilyLocalEJB.getAllModelFamiliesByCrisis(crisisID);
        ResponseWrapper response = new ResponseWrapper();
        response.setMessage("SUCCESS");
        response.setModelFamilies(modelFamilyList);
        return Response.ok(response).build();
    }
    
   @GET
   @Produces(MediaType.APPLICATION_JSON)
   @Path("{id}")
   public Response getModelByID(@PathParam("id") int id){
       ModelFamily modelFamily = modelFamilyLocalEJB.getModelFamilyByID(id);
       return Response.ok(modelFamily).build();
   }
   
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCrisisAttribute(ModelFamilyDTO modelFamilyDTO) {
//      because ModelFamily has @XmlTransient annotation for crises and crisis was always null
        ModelFamily modelFamily = transformModelFamilyDTO(modelFamilyDTO);
        try {
            modelFamily = modelFamilyLocalEJB.addCrisisAttribute(modelFamily);
        } catch (RuntimeException e) {
            return Response.ok("Error while adding Crisis attribute. Possible causes could be duplication of primary key, incomplete data, incompatible data format.").build();
        }

        return Response.ok(modelFamily).build();

    }

    private ModelFamily transformModelFamilyDTO(ModelFamilyDTO modelFamilyDTO){
        ModelFamily modelFamily = new ModelFamily();
        modelFamily.setCrisis(modelFamilyDTO.getCrisis());
        modelFamily.setNominalAttribute(modelFamilyDTO.getNominalAttribute());
        modelFamily.setIsActive(modelFamilyDTO.getIsActive());
        return modelFamily;
    }
    
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAttribute(@PathParam("id") int id) {
        try {
            modelFamilyLocalEJB.deleteModelFamily(id);
        } catch (RuntimeException e) {
            return Response.ok(
                    new ResponseWrapper(Config.STATUS_CODE_FAILED,
                    "Error while deleting Classifier.")).build();
        }
        return Response.ok(new ResponseWrapper(Config.STATUS_CODE_SUCCESS)).build();
    }
    
}
