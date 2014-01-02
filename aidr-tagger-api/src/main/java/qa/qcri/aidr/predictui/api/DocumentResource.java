package qa.qcri.aidr.predictui.api;

import java.util.List;
import qa.qcri.aidr.predictui.util.ResponseWrapper;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import qa.qcri.aidr.predictui.entities.Document;
import qa.qcri.aidr.predictui.facade.DocumentFacade;
import qa.qcri.aidr.predictui.util.Config;

/**
 * REST Web Service
 *
 * @author Imran
 */
@Path("/document")
@Stateless
public class DocumentResource {

    @Context
    private UriInfo context;
    @EJB
    private DocumentFacade documentLocalEJB;

    public DocumentResource() {
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/all")
    public Response getAllDocuments() {
        List<Document> documentList = documentLocalEJB.getAllDocuments();
        ResponseWrapper response = new ResponseWrapper();
        response.setMessage("SUCCESS");
        response.setDocuments(documentList);
        return Response.ok(response).build();
    }
    
   @GET
   @Produces(MediaType.APPLICATION_JSON)
   @Path("{id}")
   public Response getDocumentByID(@PathParam("id") long id){
       Document document = documentLocalEJB.getDocumentByID(id);
       return Response.ok(document).build();
   }
   
   @GET
   @Produces(MediaType.APPLICATION_JSON)
   @Path("/{crisisID}/{attributeID}/labeled/all")
   public Response getAllLabeledDocumentByCrisisID(@PathParam("crisisID") long crisisID, @PathParam("attributeID") long attributeID){
       List<Document> documentList = documentLocalEJB.getAllLabeledDocumentbyCrisisID(crisisID, attributeID);
       ResponseWrapper response = new ResponseWrapper(Config.STATUS_CODE_SUCCESS);
       response.setDocuments(documentList);
       return Response.ok(response).build();
   }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteDocument(@PathParam("id") Long id) {
        try {
            documentLocalEJB.deleteDocument(id);
        } catch (RuntimeException e) {
            return Response.ok(
                    new ResponseWrapper(Config.STATUS_CODE_FAILED,
                            "Error while deleting Document.")).build();
        }
        return Response.ok(new ResponseWrapper(Config.STATUS_CODE_SUCCESS)).build();
    }

    @DELETE
    @Path("/removeTrainingExample/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeTrainingExample(@PathParam("id") Long id) {
        try {
            documentLocalEJB.removeTrainingExample(id);
        } catch (RuntimeException e) {
            return Response.ok(
                    new ResponseWrapper(Config.STATUS_CODE_FAILED,
                            "Error while removing Training Example.")).build();
        }
        return Response.ok(new ResponseWrapper(Config.STATUS_CODE_SUCCESS)).build();
    }
    
}
