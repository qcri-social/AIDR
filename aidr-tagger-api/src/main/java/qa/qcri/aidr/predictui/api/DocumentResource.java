package qa.qcri.aidr.predictui.api;

import java.io.IOException;
import java.util.List;

import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.predictui.util.ResponseWrapper;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import qa.qcri.aidr.predictui.entities.Document;
import qa.qcri.aidr.predictui.facade.DocumentFacade;
import qa.qcri.aidr.predictui.util.Config;
import qa.qcri.aidr.task.ejb.TaskManagerRemote;

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

	@EJB
	private TaskManagerRemote<qa.qcri.aidr.task.entities.Document, Long> taskManager;

	private static Logger logger = Logger.getLogger(DocumentResource.class);
	private static ErrorLog elog = new ErrorLog();
	
	public DocumentResource() {
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/all")
	public Response getAllDocuments() {
		//List<Document> documentList = documentLocalEJB.getAllDocuments();
		String jsonString = taskManager.getAllTasks();
		ObjectMapper mapper = new ObjectMapper();
		List<Document> docList = null;
		if (jsonString != null) {
			try {
				docList = mapper.readValue(jsonString,  
						new TypeReference<List<Document>>() {});
				logger.info("retrieved doc count = " + docList.size());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ResponseWrapper response = new ResponseWrapper();
		response.setMessage("SUCCESS");
		response.setDocuments(docList);
		return Response.ok(response).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Response getDocumentByID(@PathParam("id") long id){
		//Document document = documentLocalEJB.getDocumentByID(id);
		logger.info("received request for : " + id);

		String jsonString = taskManager.getTaskById(id);
		ObjectMapper mapper = new ObjectMapper();

		Document doc = null;
		try {
			if (jsonString != null) {
				doc = mapper.readValue(jsonString, Document.class);
				logger.info("Converted doc id: " + doc.getDocumentID());
			} else {
				logger.warn("doc id: null");
			}
		} catch (IOException e) {
			logger.error("JSON deserialization parse error!");
			logger.error(elog.toStringException(e));
		}
		return Response.ok(doc).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{crisisID}/{attributeID}/labeled/all")
	public Response getAllLabeledDocumentByCrisisID(@PathParam("crisisID") int crisisID, @PathParam("attributeID") long attributeID){

		//List<Document> documentList = documentLocalEJB.getAllLabeledDocumentbyCrisisID(crisisID, attributeID);
		
		Criterion criterion = Restrictions.eq("hasHumanLabels", true);
		String jsonString = taskManager.getTaskCollectionByCriterion(new Long(crisisID), null, criterion);
		ObjectMapper mapper = new ObjectMapper();
		List<Document> docList = null;
		if (jsonString != null) {
			try {
				docList = mapper.readValue(jsonString, new TypeReference<List<Document>>() {});
				logger.info("retrieved doc count = " + docList.size());
			} catch (IOException e) {
				logger.error("Error in JSON deserialization to local List<Document> type");
				logger.error(elog.toStringException(e));
			}
		}
		ResponseWrapper response = new ResponseWrapper(Config.STATUS_CODE_SUCCESS);
		response.setDocuments(docList);
		return Response.ok(response).build();
	}

	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteDocument(@PathParam("id") Long id) {
		try {
			//documentLocalEJB.deleteDocument(id);
			int result = taskManager.deleteTaskById(id);
			logger.info("deleted count = " + result);
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
			//documentLocalEJB.removeTrainingExample(id);
			String jsonString = taskManager.getTaskById(id);

			ObjectMapper mapper = new ObjectMapper();
			qa.qcri.aidr.task.entities.Document document = null;
			if (jsonString != null) {
				try {

					document = mapper.readValue(jsonString, qa.qcri.aidr.task.entities.Document.class);
					logger.info("Converted doc id: " + document.getDocumentID());
				} catch (IOException e) {
					logger.error("JSON deserialization parse error!");
					logger.error(elog.toStringException(e));
				}
				document.setHasHumanLabels(false);
				document.setNominalLabelCollection(null);
				taskManager.updateTask(document);
			} else {
				logger.warn("doc id: null");
			}
		} catch (RuntimeException e) {
			return Response.ok(
					new ResponseWrapper(Config.STATUS_CODE_FAILED,
							"Error while removing Training Example.")).build();
		}
		return Response.ok(new ResponseWrapper(Config.STATUS_CODE_SUCCESS)).build();
	}

	
}
