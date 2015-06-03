package qa.qcri.aidr.predictui.facade.imp;

import qa.qcri.aidr.dbmanager.dto.DocumentDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import qa.qcri.aidr.predictui.facade.DocumentFacade;
import qa.qcri.aidr.predictui.util.ResponseWrapper;
import qa.qcri.aidr.predictui.util.TaggerAPIConfigurationProperty;
import qa.qcri.aidr.predictui.util.TaggerAPIConfigurator;
import qa.qcri.aidr.task.ejb.TaskManagerRemote;

/**
 *
 * @author koushik
 */
@Stateless
public class DocumentFacadeImp implements DocumentFacade {

	@EJB
	private TaskManagerRemote<DocumentDTO, Long> remoteTaskManager;

	@EJB
	private qa.qcri.aidr.dbmanager.ejb.remote.facade.DocumentResourceFacade remoteDocument;

	@EJB
	private qa.qcri.aidr.dbmanager.ejb.remote.facade.DocumentNominalLabelResourceFacade remoteDocumentNominalLabel;

	protected static Logger logger = Logger.getLogger("aidr-tagger-api");

	public List<DocumentDTO> getAllDocuments() {
		List<DocumentDTO> fetchedList = remoteTaskManager.getAllTasks();
		return fetchedList;   
	}

	public DocumentDTO getDocumentByID(Long id) {
		DocumentDTO fetchedDoc  = remoteTaskManager.getTaskById(id);
		return fetchedDoc;
	}

	public List<DocumentDTO> getAllLabeledDocumentbyCrisisID(Long crisisID, Long attributeID) {

		Criterion criterion = Restrictions.eq("hasHumanLabels", true);
		List<DocumentDTO> fetchedList = remoteTaskManager.getTaskCollectionByCriterion(crisisID, null, criterion);

		return fetchedList;
	}

	@Override
	public int deleteDocument(Long documentID) {
		return remoteTaskManager.deleteTaskById(documentID);
	}

	@Override
	public ResponseWrapper removeTrainingExample(Long documentID) {
		// Alternative way of doing the same update
		//qa.qcri.aidr.task.dto.DocumentDTO fetchedDoc  = remoteTaskManager.getTaskById(id);
		//fetchedDoc.setHasHumanLabels(false);
		//fetchedDoc.setNominalLabelCollection(null);
		//taskManager.updateTask(fetchedDoc);

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("setHasHumanLabels", new Boolean(false).toString());
		try {
			DocumentDTO newDoc = (DocumentDTO) remoteTaskManager.setTaskParameter(qa.qcri.aidr.dbmanager.entities.task.Document.class, documentID, paramMap);
			if (newDoc != null) {
				Criterion criterion = Restrictions.eq("id.documentId", documentID);
				remoteDocumentNominalLabel.deleteByCriteria(criterion);
				if (!remoteDocumentNominalLabel.isDocumentExists(documentID)) {
					logger.info("Removed training example: " + newDoc.getDocumentID() + ", for crisisID = " + newDoc.getCrisisDTO().getCrisisID());
					return new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_SUCCESS),
							"Deleted training example id " + documentID);
				} else {
					logger.error("Could NOT remove document from document nominal label table! id = " + documentID + "hasHumanLabels = " + remoteDocument.findDocumentByID(documentID).getHasHumanLabels());
					return new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_FAILED),
							"Error while deleting training example id " + documentID);
				}
			} else {
				logger.error("Could NOT remove document from document nominal label table! id = " + documentID + "hasHumanLabels = " + remoteDocument.findDocumentByID(documentID).getHasHumanLabels());
				return new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_FAILED),
						"Error while deleting training example id " + documentID);
			}
		} catch (Exception e) {
			logger.error("exception", e);
			return new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_FAILED),
					"Error while deleting training example id " + documentID);
		}
	}

	@Override
	public List<DocumentDTO> getAllUnlabeledDocumentbyCrisisID(Long crisisID) {
		Criterion criterion = Restrictions.eq("hasHumanLabels", false);
		List<DocumentDTO> fetchedList = remoteTaskManager.getTaskCollectionByCriterion(crisisID, null, criterion);

		return fetchedList;
	}

}
