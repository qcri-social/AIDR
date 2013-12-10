/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.api;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import qa.qcri.aidr.predictui.dto.CrisisDTO;
import qa.qcri.aidr.predictui.dto.CrisisTypeDTO;
import qa.qcri.aidr.predictui.entities.Crisis;
import qa.qcri.aidr.predictui.facade.CrisisResourceFacade;
import qa.qcri.aidr.predictui.util.Config;
import qa.qcri.aidr.predictui.util.ResponseWrapper;

/**
 * REST Web Service
 *
 * @author Imran
 */
@Path("/train")
@Stateless
public class TrainingDataResource {

    @Context
    private UriInfo context;
    @EJB
    private CrisisResourceFacade crisisLocalEJB;

    public TrainingDataResource() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{crisisCode}/getItem")
    public Response getTweetToTag(@PathParam("crisisCode") int crisisCode) {
        Crisis crisis = null;
        try {
            crisis = crisisLocalEJB.getCrisisByID(crisisCode);
        } catch (RuntimeException e) {
            return Response.ok(new ResponseWrapper(Config.STATUS_CODE_FAILED, e.getCause().getCause().getMessage())).build();
        }
        return Response.ok(crisis).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/by-code/{code}")
    public Response getCrisisByCode(@PathParam("code") String crisisCode) {
        Crisis crisis = null;
        try {
            crisis = crisisLocalEJB.getCrisisByCode(crisisCode);
        } catch (RuntimeException e) {
            return Response.ok(new ResponseWrapper(Config.STATUS_CODE_FAILED, e.getCause().getCause().getMessage())).build();
        }
        CrisisDTO dto = transformCrisisToDto(crisis);
        return Response.ok(dto).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/code/{code}")
    public Response isCrisisExists(@PathParam("code") String crisisCode) {
        Integer crisisId = crisisLocalEJB.isCrisisExists(crisisCode);
//        null value can not be correct deserialized
        if (crisisId == null){
            crisisId = 0;
        }
        //TODO: Following way of creating JSON should be chagned through a proper and automatic way
        String response = "{\"crisisCode\":\"" + crisisCode + "\", \"crisisId\":\"" + crisisId + "\"}";
        return Response.ok(response).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/all")
    public Response getAllCrisis() {
        List<Crisis> crisisList = crisisLocalEJB.getAllCrisis();
        ResponseWrapper response = new ResponseWrapper();
        response.setMessage(Config.STATUS_CODE_SUCCESS);
        response.setCrisises(crisisList);
        return Response.ok(response).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseWrapper getAllCrisisByUserID(@QueryParam("userID") int userID) throws Exception {
        List<Crisis> crisisList = crisisLocalEJB.getAllCrisisByUserID(userID);
        ResponseWrapper response = new ResponseWrapper();
        if (crisisList == null) {
            response.setMessage("No crisis found associated with the given user id.");
            return response;
        }
        response.setCrisises(crisisList);
        return response;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCrisis(Crisis crisis) {
        try {
            crisisLocalEJB.addCrisis(crisis);
        } catch (RuntimeException e) {
            return Response.ok("Error while adding Crisis. Possible causes could be duplication of primary key, incomplete data, incompatible data format.").build();
        }

        return Response.ok(Config.STATUS_CODE_SUCCESS).build();

    }

    @PUT
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response editCrisis(Crisis crisis) {
        try {
            crisis = crisisLocalEJB.editCrisis(crisis);
        } catch (RuntimeException e) {
            return Response.ok(new ResponseWrapper(Config.STATUS_CODE_FAILED, e.getCause().getCause().getMessage())).build();
        }
        CrisisDTO dto = transformCrisisToDto(crisis);
        return Response.ok(dto).build();
    }

    private CrisisDTO transformCrisisToDto(Crisis c){
        CrisisTypeDTO typeDTO = null;
        if (c.getCrisisType() != null) {
            typeDTO = new CrisisTypeDTO(c.getCrisisType().getCrisisTypeID(), c.getCrisisType().getName());
        }
        CrisisDTO dto = new CrisisDTO();
        dto.setCode(c.getCode());
        dto.setName(c.getName());
        dto.setCrisisID(c.getCrisisID());
        dto.setCrisisType(typeDTO);
        return dto;
    }
}
