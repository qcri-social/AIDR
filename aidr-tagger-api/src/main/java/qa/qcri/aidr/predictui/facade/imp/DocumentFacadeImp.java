package qa.qcri.aidr.predictui.facade.imp;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
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
import qa.qcri.aidr.task.ejb.TaskManagerRemote;

/**
 *
 * @author koushik
 */
@Stateless
public class DocumentFacadeImp implements DocumentFacade{

	@EJB
	private TaskManagerRemote<DocumentDTO, Long> remoteTaskManager;

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
	public void removeTrainingExample(Long documentID) {
		// Alternative way of doing the same update
		//qa.qcri.aidr.task.dto.DocumentDTO fetchedDoc  = remoteTaskManager.getTaskById(id);
		//fetchedDoc.setHasHumanLabels(false);
		//fetchedDoc.setNominalLabelCollection(null);
		//taskManager.updateTask(fetchedDoc);

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("setHasHumanLabels", new Boolean(false).toString());
		paramMap.put("setNominalLabelCollection", null);
		try {
			DocumentDTO newDoc = (DocumentDTO) remoteTaskManager.setTaskParameter(qa.qcri.aidr.dbmanager.entities.task.Document.class, documentID, paramMap);
			if (newDoc != null) {
				logger.info("Removed training example: " + newDoc.getDocumentID() + ", for crisisID = " + newDoc.getCrisisDTO().getCrisisID());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<DocumentDTO> getAllUnlabeledDocumentbyCrisisID(Long crisisID) {
		Criterion criterion = Restrictions.eq("hasHumanLabels", false);
		List<DocumentDTO> fetchedList = remoteTaskManager.getTaskCollectionByCriterion(crisisID, null, criterion);

		return fetchedList;
	}

}
