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
    
}
