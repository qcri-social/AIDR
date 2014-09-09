package qa.qcri.aidr.predictui.api;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.predictui.entities.Crisis;
import qa.qcri.aidr.predictui.entities.Document;
import qa.qcri.aidr.predictui.entities.ModelFamily;
import qa.qcri.aidr.predictui.facade.CrisisResourceFacade;
import qa.qcri.aidr.predictui.facade.DocumentFacade;
import qa.qcri.aidr.predictui.facade.ModelFamilyFacade;
import qa.qcri.aidr.predictui.util.TaskManagerEntityMapper;
import qa.qcri.aidr.task.ejb.TaskManagerRemote;

@Path("/manage/collection")
@Stateless
public class CrisisManagementResource {

	@Context
	private UriInfo context;
	@EJB
	private DocumentFacade documentLocalEJB;

	@EJB
	private CrisisResourceFacade crisisLocalEJB;

	@EJB
	private ModelFamilyFacade modelFamilyLocalEJB;

	@PersistenceContext(unitName = "qa.qcri.aidr.predictui-EJBS")
	private EntityManager em;

	@EJB
	private TaskManagerRemote<qa.qcri.aidr.task.entities.Document, Long> taskManager;

	private static Logger logger = Logger.getLogger(CrisisManagementResource.class);
	private static ErrorLog elog = new ErrorLog();
			
	public CrisisManagementResource() {
	}

	@GET
	@Path("/trash/crisis/{crisisCode}")
	@Produces({"application/json"})
	public Response trashByCrisisCode(@PathParam("crisisCode") String crisisCode) {
		//TODO: 
		// 1. set aidr_predict.crisis.isTrashed = true
		// 2. set model_family.isActive = 0
		// 3. remove tasks for this crisisID from document table -->
		//    this will trigger deletion of documents for this crisisID from 
		//    the task_assignment table through DELETE CASCADE
		System.out.println("[trashByCrisisCode] Received request to trash collection from aidr_predict DB: " + crisisCode);
		logger.info("Received request to trash collection from aidr_predict DB: " + crisisCode);
		try {
			Crisis crisis = crisisLocalEJB.getCrisisByCode(crisisCode);
			logger.info("crisis found for crisis code " + crisisCode + ": " + crisis);
			if (null == crisis) {
				StringBuilder sb = new StringBuilder().append("{\"TRASHED\":").append(new Long(-1)).append("}");
				logger.info("No crisis exists in aidr_predict DB for: " + crisisCode);
				return Response.ok(sb.toString()).build();
			}
			//Otherwise go ahead with trashing
			crisis.setIsTrashed(true);
			em.merge(crisis);

			List<ModelFamily> associatedModels = modelFamilyLocalEJB.getAllModelFamiliesByCrisis(crisis.getCrisisID());
			if (null == associatedModels) {
				StringBuilder sb = new StringBuilder().append("{\"TRASHED\":").append(crisis.getCrisisID()).append("}");
				logger.info("Success in deleting crisis: " + crisisCode);
				return Response.ok(sb.toString()).build();
			}
			for (ModelFamily model: associatedModels) {
				model.setIsActive(false);
				em.merge(model);
			}

			List<Document> associatedDocs = documentLocalEJB.getAllUnlabeledDocumentbyCrisisID(crisis);
			if (null == associatedDocs) {
				StringBuilder sb = new StringBuilder().append("{\"TRASHED\":").append(crisis.getCrisisID()).append("}");
				logger.info("Success in deleting crisis: " + crisisCode);
				return Response.ok(sb.toString()).build();
			}			
			logger.info("Found for " + crisisCode + ", unlabeled docs to delete  = " + associatedDocs.size());
			TaskManagerEntityMapper mapper = new TaskManagerEntityMapper();		
			for (Document document: associatedDocs) {
				//em.remove(document);
				try {
					qa.qcri.aidr.task.entities.Document doc = mapper.reverseTransformDocument(document);
					taskManager.deleteTask(doc);
				} catch (Exception e) {
					logger.error("Error in deleting document: " + document);
					logger.error(elog.toStringException(e));
				}
			}
			List<Document> temp = documentLocalEJB.getAllUnlabeledDocumentbyCrisisID(crisis);
			logger.info("Post Trashing: found for " + crisisCode + ", unlabeled docs after delete = " + temp.size());		

			if (temp.isEmpty()) {
				StringBuilder sb = new StringBuilder().append("{\"TRASHED\":").append(crisis.getCrisisID()).append("}");
				logger.info("Success in deleting crisis: " + crisisCode);
				return Response.ok(sb.toString()).build();
			} else {
				return Response.ok("{\"status\": \"FAILED\"}").build();
			}
		} catch (Exception e) {
			logger.error("Something went wrong in trashing attempt!");
			logger.error(elog.toStringException(e));
			return Response.ok("{\"status\": \"FAILED\"}").build();
		}
	}

	@GET
	@Path("/untrash/crisis/{crisisCode}")
	@Produces({"application/json"})
	public Response untrashByCrisisCode(@PathParam("crisisCode") String crisisCode) {
		//TODO: 
		// 1. set aidr_predict.crisis.isTrashed = false
		// 2. set model_family.isActive = 1
		System.out.println("Received request to untrash collection from aidr_predict DB: " + crisisCode);
		logger.info("Received request to untrash collection from aidr_predict DB: " + crisisCode);
		Crisis crisis = null;
		try {
			crisis = crisisLocalEJB.getCrisisByCode(crisisCode);
			if (null == crisis) {
				logger.warn("crisis does not exist in aidr_predict DB for: " + crisisCode);
				return Response.ok("{\"status\": \"UNTRASHED\"}").build();
			}
		} catch (Exception e) {
			logger.error("Could not retrieve crisis to untrash: " + crisisCode);
			logger.error(elog.toStringException(e));
			return Response.ok("{\"status\": \"FAILED\"}").build();
		}
		try {
			if (crisis != null) {
				crisis.setIsTrashed(false);
				em.merge(crisis);
				
				List<ModelFamily> associatedModels = modelFamilyLocalEJB.getAllModelFamiliesByCrisis(crisis.getCrisisID());
				if (associatedModels != null) {
					for (ModelFamily model: associatedModels) {
						model.setIsActive(true);
						em.merge(model);
					}
					logger.info("Found for " + crisisCode + ", model families  = " + associatedModels.size());
				}
			}
			logger.info("Untrashed crisis: " + crisisCode);
			return Response.ok("{\"status\": \"UNTRASHED\"}").build();
		} catch (Exception e) {
			logger.error("Something went wrong in untrashing attempt!");
			logger.error(elog.toStringException(e));
			return Response.ok("{\"status\": \"FAILED\"}").build();
		}
	}

}
