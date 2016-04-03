/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.api;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import qa.qcri.aidr.common.wrapper.CollectionBriefInfo;
import qa.qcri.aidr.dbmanager.entities.misc.Collection;
import qa.qcri.aidr.predictui.facade.CollectionResourceFacade;
import qa.qcri.aidr.predictui.util.ResponseWrapper;

/**
 * REST Web Service
 *
 * @author Imran
 */
@Path("/collection")
@Stateless
public class CollectionResource {

    @Context
    private UriInfo context;
    @EJB
    private CollectionResourceFacade collectionLocalEJB;

    public CollectionResource() {
    }

    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{userID}")
    public Response getAllCrisisByUserID(@PathParam("userID") int userID) {
        List<Collection> collections = collectionLocalEJB.getAllRunningCollectionsByUserID(userID);
        ResponseWrapper response = new ResponseWrapper();
        
        if (collections == null || collections.isEmpty()) {
            response.setMessage("No collection found");
            return Response.ok(response).build();
        }
        response.setCollections(collections);
        return Response.ok(response).build();
    }
    
    @GET
    @Produces("application/json")
    @Path("/trainingData")
    public Response getCrisisForNominalAttributeById(@QueryParam("id") Integer attributeID,@QueryParam("crisis_type") Integer crisis_type,@QueryParam("lang_filters") String lang_filters) {
     
		List<CollectionBriefInfo> crisisList = collectionLocalEJB.getCrisisForNominalAttributeById(attributeID,crisis_type,lang_filters);
        ResponseWrapper response = new ResponseWrapper();
        response.setMessage("SUCCESS");
        response.setDataObject(crisisList);
        return Response.ok(response).build();
    }
}
