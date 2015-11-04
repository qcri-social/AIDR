package qa.qcri.aidr.predictui.facade.imp;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import qa.qcri.aidr.dbmanager.dto.CollectionDTO;
import qa.qcri.aidr.dbmanager.dto.DocumentDTO;
import qa.qcri.aidr.dbmanager.dto.ModelFamilyDTO;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.TaskManagerRemote;
import qa.qcri.aidr.dbmanager.entities.model.ModelFamily;
import qa.qcri.aidr.dbmanager.entities.task.Document;
import qa.qcri.aidr.predictui.api.CollectionManagementResource;
import qa.qcri.aidr.predictui.facade.CrisisManagementResourceFacade;


@Stateless
public class CrisisManagementResourceFacadeImp implements CrisisManagementResourceFacade {

	@EJB
	private qa.qcri.aidr.dbmanager.ejb.remote.facade.DocumentResourceFacade remoteDocumentEJB;

	@EJB
	private qa.qcri.aidr.dbmanager.ejb.remote.facade.CollectionResourceFacade remoteCrisisEJB;

	@EJB
	private qa.qcri.aidr.dbmanager.ejb.remote.facade.ModelFamilyResourceFacade remoteModelFamilyEJB;

	@EJB
	private TaskManagerRemote<Document, Long> taskManager;

	private static Logger logger = Logger.getLogger(CollectionManagementResource.class);

	@Override
	public String trashByCrisisCode(String crisisCode) {

		//TODO: 
		// 1. set aidr_predict.crisis.isTrashed = true
		// 2. set model_family.isActive = 0
		// 3. remove tasks for this crisisID from document table -->
		//    this will trigger deletion of documents for this crisisID from 
		//    the task_assignment table through DELETE CASCADE
		logger.info("Received request to trash collection from aidr_predict DB: " + crisisCode);

		try {
			CollectionDTO collection = remoteCrisisEJB.getCrisisByCode(crisisCode);
			logger.info("crisis found for crisis code " + crisisCode + ": " + collection);
			if (null == collection) {
				StringBuilder sb = new StringBuilder().append("{\"TRASHED\":").append(new Long(-1)).append("}");
				logger.info("No crisis exists in aidr_predict DB for: " + crisisCode);
				return sb.toString();
			}
		
			//Otherwise go ahead with trashing
			collection.setIsTrashed(true);
			
			List<ModelFamilyDTO> associatedModels = remoteModelFamilyEJB.getAllModelFamiliesByCrisis(collection.getCrisisID());
			
			remoteCrisisEJB.merge(collection.toEntity());
			
			if (associatedModels.isEmpty()) {
				StringBuilder sb = new StringBuilder().append("{\"TRASHED\":").append(collection.getCrisisID()).append("}");
				logger.info("Success in deleting crisis: " + crisisCode);
				return sb.toString();
			}
			List<ModelFamily> list = new ArrayList<ModelFamily>();
			for (ModelFamilyDTO model: associatedModels) {
				model.setIsActive(false);
				list.add(model.toEntity());
			}
			remoteModelFamilyEJB.merge(list);

			List<DocumentDTO> associatedDocumentsDTO = remoteDocumentEJB.findUnLabeledDocumentsByCrisisID(collection.getCrisisID());
			if (associatedDocumentsDTO.isEmpty()) {
				StringBuilder sb = new StringBuilder().append("{\"TRASHED\":").append(collection.getCrisisID()).append("}");
				return sb.toString();
			}			
			logger.info("Found for " + crisisCode + ", unlabeled docs to delete  = " + associatedDocumentsDTO.size());
			List<Document> associatedDocuments = new ArrayList<Document>();
			try {
				for (DocumentDTO documentDTO : associatedDocumentsDTO) {
					associatedDocuments.add(documentDTO.toEntity());
				}
				taskManager.deleteTask(associatedDocuments);
			} catch (Exception e) {
				logger.error("Error in deleting document set");
			}
			List<DocumentDTO> temp = remoteDocumentEJB.findUnLabeledDocumentsByCrisisID(collection.getCrisisID());
			logger.info("Post Trashing: found for " + crisisCode + ", unlabeled docs after delete = " + temp.size());		
			if (temp.isEmpty()) {
				StringBuilder sb = new StringBuilder().append("{\"TRASHED\":").append(collection.getCrisisID()).append("}");
				logger.info("Success in deleting crisis: " + crisisCode);
				return sb.toString();
			} else {
				StringBuilder sb = new StringBuilder().append("{\"status\": \"FAILED\"}");
				return sb.toString();
			}
		} catch (Exception e) {
			logger.error("Something went wrong in trashing attempt!");
			StringBuilder sb = new StringBuilder().append("{\"status\": \"FAILED\"}");
			return sb.toString();
		}

	}

	@Override
	public String untrashByCrisisCode(String crisisCode) {
		//TODO: 
		// 1. set aidr_predict.crisis.isTrashed = false
		// 2. set model_family.isActive = 1
		logger.info("Received request to untrash collection from aidr_predict DB: " + crisisCode);
		CollectionDTO crisis = null;

		StringBuilder sb = new StringBuilder();
		try {
			crisis = remoteCrisisEJB.getCrisisByCode(crisisCode);
			if (null == crisis) {
				logger.warn("crisis does not exist in aidr_predict DB for: " + crisisCode);
				return sb.append("{\"status\": \"UNTRASHED\"}").toString();
			}
		} catch (Exception e) {
			logger.error("Could not retrieve crisis to untrash: " + crisisCode);
			return sb.append("{\"status\": \"FAILED\"}").toString();
		}
		try {
			if (crisis != null) {
				crisis.setIsTrashed(false);
				
				List<ModelFamilyDTO> associatedModels = remoteModelFamilyEJB.getAllModelFamiliesByCrisis(crisis.getCrisisID());
				remoteCrisisEJB.merge(crisis.toEntity());
				
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
			return sb.append("{\"status\": \"FAILED\"}").toString();
		}
	}

}
