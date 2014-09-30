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
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import qa.qcri.aidr.predictui.dto.CrisisTypeDTO;
import qa.qcri.aidr.predictui.entities.CrisisType;
import qa.qcri.aidr.predictui.facade.CrisisTypeResourceFacade;
import qa.qcri.aidr.predictui.util.Config;

import static qa.qcri.aidr.predictui.util.ConfigProperties.getProperty;

/**
 * REST Web Service
 *
 * @author Imran
 */
@Path("/crisisType")
@Stateless
public class CrisisTypeResource {

    @Context
    private UriInfo context;
    @EJB
    private CrisisTypeResourceFacade crisisTypeLocal;

    public CrisisTypeResource() {
    }
    
    @GET
    @Produces("application/json")
    @Path("{id}")
    public Response getCrisisTypeByID(@PathParam("id") int id) {
        CrisisType crisistype = null;
        try {
            crisistype = crisisTypeLocal.getCrisisTypeByID(id);
        } catch (RuntimeException e) {
            return Response.ok(new ResponseWrapper(getProperty("STATUS_CODE_FAILED"), e.getCause().getCause().getMessage())).build();
        }
        return Response.ok(crisistype).build();
    }

    @GET
    @Produces("application/json")
    @Path("/all")
    public Response getCrisisTypes() {
        List<CrisisTypeDTO> crisisTypeList = crisisTypeLocal.getCrisisTypes();
        ResponseWrapper response = new ResponseWrapper();
        response.setCrisisTypes(crisisTypeList);
        return Response.ok(response).build();
    }

    @POST
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCrisisType(CrisisType crisis) {
        try {
            crisis = crisisTypeLocal.addCrisisType(crisis);
        } catch (RuntimeException e) {
            return Response.ok(new ResponseWrapper(getProperty("STATUS_CODE_FAILED"), e.getCause().getCause().getMessage())).build();
        }
        return Response.ok(crisis).build();
    }

    @PUT
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response editCrisisType(CrisisType crisis) {
        try {
            crisis = crisisTypeLocal.editCrisisType(crisis);
        } catch (RuntimeException e) {
            return Response.ok(new ResponseWrapper(getProperty("STATUS_CODE_FAILED"), e.getCause().getCause().getMessage())).build();
        }
        return Response.ok(crisis).build();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCrisisType(@PathParam("id") int id) {
        try {
            crisisTypeLocal.deleteCrisisType(id);
        } catch (RuntimeException e) {
            return Response.ok(
                    new ResponseWrapper(getProperty("STATUS_CODE_FAILED"),
                    "Error while deleting CrisisType. Possible cause(s): (1) Given crisis-type ID does not exist.")).build();
        }
        return Response.ok(new ResponseWrapper(getProperty("STATUS_CODE_SUCCESS"))).build();
    }
}
