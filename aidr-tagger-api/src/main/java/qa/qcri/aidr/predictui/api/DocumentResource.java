package qa.qcri.aidr.predictui.api;

import java.io.IOException;
import java.util.List;

import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.dbmanager.dto.DocumentDTO;
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

import org.apache.log4j.Logger;

import qa.qcri.aidr.predictui.facade.DocumentFacade;

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

	private static Logger logger = Logger.getLogger("aidr-tagger-api");

	public DocumentResource() {
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/all")
	public Response getAllDocuments() {
		List<DocumentDTO> docList = documentLocalEJB.getAllDocuments();

		ResponseWrapper response = new ResponseWrapper();
		response.setMessage("SUCCESS");
		response.setDocuments(docList);
		return Response.ok(response).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Response getDocumentByID(@PathParam("id") Long id){
		logger.info("received request for : " + id);

		DocumentDTO doc = documentLocalEJB.getDocumentByID(id);
		return Response.ok(doc).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{crisisID}/{attributeID}/labeled/all")
	public Response getAllLabeledDocumentByCrisisID(@PathParam("crisisID") Long crisisID, @PathParam("attributeID") Long attributeID){

		List<DocumentDTO> documentList = documentLocalEJB.getAllLabeledDocumentbyCrisisID(crisisID, attributeID);

		ResponseWrapper response = new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_SUCCESS));
		response.setDocuments(documentList);
		return Response.ok(response).build();
	}

	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteDocument(@PathParam("id") Long id) {
		try {
			int result = documentLocalEJB.deleteDocument(id);
			logger.info("deleted count = " + result);
		} catch (RuntimeException e) {
			return Response.ok(
					new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_FAILED), "Error while deleting Document.")).build();
		}
		return Response.ok(new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_SUCCESS))).build();
	}

	@DELETE
	@Path("/removeTrainingExample/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeTrainingExample(@PathParam("id") Long id) {
		try {
			if (documentLocalEJB.removeTrainingExample(id).getStatusCode().equalsIgnoreCase(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_SUCCESS))) {
				logger.info("Successfully removed document with id = " + id);
				return Response.ok(new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_SUCCESS))).build();
			} else {
				return Response.ok(
						new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_FAILED), "Error while removing Training Example.")).build();
			}
		} catch (RuntimeException e) {
			return Response.ok(
					new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_FAILED), "Error while removing Training Example.")).build();
		}
	}


}
