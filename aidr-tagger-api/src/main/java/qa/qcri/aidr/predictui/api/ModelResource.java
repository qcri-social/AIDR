/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.api;

import java.util.ArrayList;
import java.util.List;

import qa.qcri.aidr.dbmanager.dto.ModelDTO;
import qa.qcri.aidr.dbmanager.dto.taggerapi.ModelHistoryWrapper;
import qa.qcri.aidr.dbmanager.dto.taggerapi.ModelWrapper;
import qa.qcri.aidr.predictui.util.ResponseWrapper;
import qa.qcri.aidr.predictui.util.TaggerAPIConfigurationProperty;
import qa.qcri.aidr.predictui.util.TaggerAPIConfigurator;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import qa.qcri.aidr.predictui.facade.ModelFacade;

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
        List<ModelDTO> modelList = modelLocalEJB.getAllModels();
        ResponseWrapper response = new ResponseWrapper();
        response.setMessage(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_SUCCESS));
        response.setModels(modelList);
        return Response.ok(response).build();
    }
    
   @GET
   @Produces(MediaType.APPLICATION_JSON)
   @Path("{id}")
   public Response getModelByID(@PathParam("id") Long id){
       ModelDTO model = modelLocalEJB.getModelByID(id);
       return Response.ok(model).build();
   }
   
   
   @GET
   @Produces(MediaType.APPLICATION_JSON)
   @Path("crisis/{crisisID}")
   public Response getModelByCrisisID(@PathParam("crisisID") Long crisisID){
       List<ModelWrapper> modelList = modelLocalEJB.getModelByCrisisID(crisisID);
       ResponseWrapper response = new ResponseWrapper();
       if (modelList == null){
           response.setMessage("No models found for the given crisis-id");
           return Response.ok(response).build();
       }
        response.setModelWrapper(modelList);
       return Response.ok(response).build();
   }

   @GET
   @Produces(MediaType.APPLICATION_JSON)
   @Path("modelFamily/{modelFamilyID}")
   public Response getModelsByModelFamilyID(@PathParam("modelFamilyID") Long modelFamilyID,
                                            @QueryParam("start") Integer start,
                                            @QueryParam("limit") Integer limit,
                                			@DefaultValue("") @QueryParam("sortColumn") String sortColumn,
                                			@DefaultValue("") @QueryParam("sortDirection") String sortDirection){
       start = (start != null) ? start : 0;
       limit = (limit != null) ? limit : 50;
       Integer total = modelLocalEJB.getModelCountByModelFamilyID(modelFamilyID);
       List<ModelHistoryWrapper> modelList = new ArrayList<ModelHistoryWrapper>(0);

       if (total > 0){
           modelList = modelLocalEJB.getModelByModelFamilyID(modelFamilyID, start, limit, sortColumn, sortDirection);
       }

       ResponseWrapper response = new ResponseWrapper();
       response.setModelHistoryWrapper(modelList);
       response.setTotal(total);
       return Response.ok(response).build();
   }

}
