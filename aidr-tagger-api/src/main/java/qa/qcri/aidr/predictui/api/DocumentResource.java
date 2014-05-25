package qa.qcri.aidr.predictui.api;

import java.io.IOException;
import java.util.List;

import qa.qcri.aidr.predictui.util.ResponseWrapper;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
				System.out.println("[getAllDocuments] received doc count = " + docList.size());
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
		System.out.println("[getDocumentByID] received request for : " + id);

		String jsonString = taskManager.getTaskById(id);
		ObjectMapper mapper = new ObjectMapper();

		Document doc = null;
		try {
			if (jsonString != null) {
				doc = mapper.readValue(jsonString, Document.class);
				System.out.println("Converted doc id: " + doc.getDocumentID());
			} else {
				System.out.println("doc id: null");
			}
		} catch (IOException e) {
			System.err.println("JSON deserialization parse error!");
			e.printStackTrace();
		}
		return Response.ok(doc).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{crisisID}/{attributeID}/labeled/all")
	public Response getAllLabeledDocumentByCrisisID(@PathParam("crisisID") int crisisID, @PathParam("attributeID") long attributeID){

		//List<Document> documentList = documentLocalEJB.getAllLabeledDocumentbyCrisisID(crisisID, attributeID);
		
		Criterion criterion = Restrictions.eq("hasHumanLabels", true);
		String jsonString = taskManager.getTaskCollectionByCriterion((long) crisisID, criterion);
		ObjectMapper mapper = new ObjectMapper();
		List<Document> docList = null;
		if (jsonString != null) {
			try {
				docList = mapper.readValue(jsonString, new TypeReference<List<Document>>() {});
				System.out.println("[getAllLabeledDocumentByCrisisID] received doc count = " + docList.size());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
			System.out.println("[deleteDocument] deleted count = " + result);
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
					System.out.println("Converted doc id: " + document.getDocumentID());
				} catch (IOException e) {
					System.err.println("JSON deserialization parse error!");
					e.printStackTrace();
				}
				document.setHasHumanLabels(false);
				document.setNominalLabelCollection(null);
				taskManager.updateTask(document);
			} else {
				System.out.println("doc id: null");
			}
		} catch (RuntimeException e) {
			return Response.ok(
					new ResponseWrapper(Config.STATUS_CODE_FAILED,
							"Error while removing Training Example.")).build();
		}
		return Response.ok(new ResponseWrapper(Config.STATUS_CODE_SUCCESS)).build();
	}

	/*
	public qa.qcri.aidr.predictui.entities.Document transformTaskToDocument(qa.qcri.aidr.task.entities.Document document) {
		qa.qcri.aidr.predictui.entities.Document doc = new qa.qcri.aidr.predictui.entities.Document();
		doc.setDocumentID(document.getDocumentID());
		doc.setCrisisID(document.getCrisisID());
		doc.setDoctype(document.getDoctype());
		doc.setData(document.getData());
		doc.setEvaluationSet(document.isEvaluationSet());
		doc.setGeoFeatures(document.getGeoFeatures());
		doc.setLanguage(document.getLanguage());
		doc.setHasHumanLabels(document.isHasHumanLabels());
		//doc.setNominalLabelCollection((Collection<NominalLabel>) document.getNominalLabelCollection());
		doc.setReceivedAt(document.getReceivedAt());
		doc.setSourceIP(document.getSourceIP());
		doc.setWordFeatures(document.getWordFeatures());
		doc.setValueAsTrainingSample(document.getValueAsTrainingSample());
		//doc.setTaskAssignment((List<TaskAssignment>) document.getTaskAssignment());

		return doc;

	}

	public List<qa.qcri.aidr.predictui.entities.Document> transformTaskToDocument(List<qa.qcri.aidr.task.entities.Document> documentList) {

		List<qa.qcri.aidr.predictui.entities.Document> docList = new ArrayList<qa.qcri.aidr.predictui.entities.Document>();

		for (qa.qcri.aidr.task.entities.Document document: documentList) {
			qa.qcri.aidr.predictui.entities.Document doc = new qa.qcri.aidr.predictui.entities.Document();
			doc.setDocumentID(document.getDocumentID());
			doc.setCrisisID(document.getCrisisID());
			doc.setDoctype(document.getDoctype());
			doc.setData(document.getData());
			doc.setEvaluationSet(document.isEvaluationSet());
			doc.setGeoFeatures(document.getGeoFeatures());
			doc.setLanguage(document.getLanguage());
			doc.setHasHumanLabels(document.isHasHumanLabels());
			//doc.setNominalLabelCollection(transformNominalLabelCollection(document.getNominalLabelCollection()));
			doc.setReceivedAt(document.getReceivedAt());
			doc.setSourceIP(document.getSourceIP());
			doc.setWordFeatures(document.getWordFeatures());
			doc.setValueAsTrainingSample(document.getValueAsTrainingSample());
			//doc.setTaskAssignment((List<TaskAssignment>) document.getTaskAssignment());
			docList.add(doc);
		}
		return docList;
	}
	 */
}
