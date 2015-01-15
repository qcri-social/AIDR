package qa.qcri.aidr.predictui.facade.imp;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.dbmanager.dto.CrisisDTO;
import qa.qcri.aidr.dbmanager.dto.DocumentDTO;
import qa.qcri.aidr.dbmanager.dto.ModelFamilyDTO;
import qa.qcri.aidr.dbmanager.entities.model.ModelFamily;
import qa.qcri.aidr.predictui.api.CrisisManagementResource;
import qa.qcri.aidr.predictui.facade.CrisisManagementResourceFacade;
import qa.qcri.aidr.predictui.facade.CrisisResourceFacade;
import qa.qcri.aidr.predictui.facade.ModelFamilyFacade;
import qa.qcri.aidr.task.ejb.TaskManagerRemote;


@Stateless
public class CrisisManagementResourceFacadeImp implements CrisisManagementResourceFacade {

	@EJB
	private qa.qcri.aidr.dbmanager.ejb.remote.facade.DocumentResourceFacade remoteDocumentEJB;

	@EJB
	private qa.qcri.aidr.dbmanager.ejb.remote.facade.CrisisResourceFacade remoteCrisisEJB;

	@EJB
	private qa.qcri.aidr.dbmanager.ejb.remote.facade.ModelFamilyResourceFacade remoteModelFamilyEJB;

	@EJB
	private TaskManagerRemote<qa.qcri.aidr.dbmanager.dto.DocumentDTO, Long> taskManager;

	private static Logger logger = LoggerFactory.getLogger(CrisisManagementResource.class);
	private static ErrorLog elog = new ErrorLog();

	@Override
	public String trashByCrisisCode(String crisisCode) {

		//TODO: 
		// 1. set aidr_predict.crisis.isTrashed = true
		// 2. set model_family.isActive = 0
		// 3. remove tasks for this crisisID from document table -->
		//    this will trigger deletion of documents for this crisisID from 
		//    the task_assignment table through DELETE CASCADE
		System.out.println("[trashByCrisisCode] Received request to trash collection from aidr_predict DB: " + crisisCode);
		logger.info("Received request to trash collection from aidr_predict DB: " + crisisCode);

		try {
			CrisisDTO crisis = remoteCrisisEJB.getCrisisByCode(crisisCode);
			logger.info("crisis found for crisis code " + crisisCode + ": " + crisis);
			if (null == crisis) {
				StringBuilder sb = new StringBuilder().append("{\"TRASHED\":").append(new Long(-1)).append("}");
				logger.info("No crisis exists in aidr_predict DB for: " + crisisCode);
				return sb.toString();
			}
			//Otherwise go ahead with trashing
			crisis.setIsTrashed(true);
			remoteCrisisEJB.merge(crisis.toEntity());

			List<ModelFamilyDTO> associatedModels = remoteModelFamilyEJB.getAllModelFamiliesByCrisis(crisis.getCrisisID());
			if (associatedModels.isEmpty()) {
				StringBuilder sb = new StringBuilder().append("{\"TRASHED\":").append(crisis.getCrisisID()).append("}");
				logger.info("Success in deleting crisis: " + crisisCode);
				return sb.toString();
			}
			List<ModelFamily> list = new ArrayList<ModelFamily>();
			for (ModelFamilyDTO model: associatedModels) {
				model.setIsActive(false);
				list.add(model.toEntity());
			}
			remoteModelFamilyEJB.merge(list);

			List<DocumentDTO> associatedDocs = remoteDocumentEJB.findUnLabeledDocumentsByCrisisID(crisis.getCrisisID());
			if (associatedDocs.isEmpty()) {
				StringBuilder sb = new StringBuilder().append("{\"TRASHED\":").append(crisis.getCrisisID()).append("}");
				logger.info("Success in deleting crisis: " + crisisCode);
				return sb.toString();
			}			
			logger.info("Found for " + crisisCode + ", unlabeled docs to delete  = " + associatedDocs.size());
			try {
				taskManager.deleteTask(associatedDocs);
			} catch (Exception e) {
				logger.error("Error in deleting document set");
				logger.error(elog.toStringException(e));
			}
			List<DocumentDTO> temp = remoteDocumentEJB.findUnLabeledDocumentsByCrisisID(crisis.getCrisisID());
			logger.info("Post Trashing: found for " + crisisCode + ", unlabeled docs after delete = " + temp.size());		
			if (temp.isEmpty()) {
				StringBuilder sb = new StringBuilder().append("{\"TRASHED\":").append(crisis.getCrisisID()).append("}");
				logger.info("Success in deleting crisis: " + crisisCode);
				return sb.toString();
			} else {
				StringBuilder sb = new StringBuilder().append("{\"status\": \"FAILED\"}");
				return sb.toString();
			}
		} catch (Exception e) {
			logger.error("Something went wrong in trashing attempt!");
			logger.error(elog.toStringException(e));
			StringBuilder sb = new StringBuilder().append("{\"status\": \"FAILED\"}");
			return sb.toString();
		}

	}

	@Override
	public String untrashByCrisisCode(String crisisCode) {
		//TODO: 
		// 1. set aidr_predict.crisis.isTrashed = false
		// 2. set model_family.isActive = 1
		System.out.println("Received request to untrash collection from aidr_predict DB: " + crisisCode);
		logger.info("Received request to untrash collection from aidr_predict DB: " + crisisCode);
		CrisisDTO crisis = null;

		StringBuilder sb = new StringBuilder();
		try {
			crisis = remoteCrisisEJB.getCrisisByCode(crisisCode);
			if (null == crisis) {
				logger.warn("crisis does not exist in aidr_predict DB for: " + crisisCode);
				return sb.append("{\"status\": \"UNTRASHED\"}").toString();
			}
		} catch (Exception e) {
			logger.error("Could not retrieve crisis to untrash: " + crisisCode);
			logger.error(elog.toStringException(e));

			return sb.append("{\"status\": \"FAILED\"}").toString();
		}
		try {
			if (crisis != null) {
				crisis.setIsTrashed(false);
				remoteCrisisEJB.merge(crisis.toEntity());

				List<ModelFamilyDTO> associatedModels = remoteModelFamilyEJB.getAllModelFamiliesByCrisis(crisis.getCrisisID());
				if (associatedModels != null) {
					List<ModelFamily> list = new ArrayList<ModelFamily>();
					for (ModelFamilyDTO model: associatedModels) {
						model.setIsActive(true);
						list.add(model.toEntity());
					}
					remoteModelFamilyEJB.merge(list);
					logger.info("Found for " + crisisCode + ", model families  = " + associatedModels.size());
				}
			}
			logger.info("Untrashed crisis: " + crisisCode);
			return sb.append("{\"status\": \"UNTRASHED\"}").toString();
		} catch (Exception e) {
			logger.error("Something went wrong in untrashing attempt!");
			logger.error(elog.toStringException(e));
			return sb.append("{\"status\": \"FAILED\"}").toString();
		}
	}

}
