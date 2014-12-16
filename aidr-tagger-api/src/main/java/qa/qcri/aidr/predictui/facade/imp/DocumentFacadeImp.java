package qa.qcri.aidr.predictui.facade.imp;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.dbmanager.dto.DocumentDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import qa.qcri.aidr.predictui.entities.Crisis;
import qa.qcri.aidr.predictui.entities.Document;
import qa.qcri.aidr.predictui.facade.DocumentFacade;
import qa.qcri.aidr.task.ejb.TaskManagerRemote;

/**
 *
 * @author koushik
 */
@Stateless
public class DocumentFacadeImp implements DocumentFacade{

	//@PersistenceContext(unitName = "qa.qcri.aidr.predictui-EJBS")
	//private EntityManager em;

	@EJB
	private TaskManagerRemote<DocumentDTO, Long> taskManager;
	
	
	protected static Logger logger = LoggerFactory.getLogger(DocumentFacadeImp.class);
	private ErrorLog elog = new ErrorLog();

	public List<Document> getAllDocuments() {
		List<Document> docList = null;
		List<DocumentDTO> fetchedList = taskManager.getAllTasks();

		if (fetchedList != null) {
			try {
				docList = Document.toLocalDocumentList(fetchedList); 

				logger.info("retrieved doc count = " + docList.size());
				System.out.println("retrieved doc count = " + docList.size());
			} catch (Exception e) {
				logger.error("Error in converting to local document enetity type");
				logger.error(elog.toStringException(e));
				e.printStackTrace();
			}
		}
		return docList;   
	}

	public Document getDocumentByID(long id) {
		qa.qcri.aidr.dbmanager.dto.DocumentDTO fetchedDoc  = taskManager.getTaskById(id);
		Document document = null;
		try {
			document = Document.toLocalDocument(fetchedDoc);
		} catch (PropertyNotSetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return document;
	}

	public List<Document> getAllLabeledDocumentbyCrisisID(long crisisID, long attributeID) {

		Criterion criterion = Restrictions.eq("hasHumanLabels", true);
		List<qa.qcri.aidr.dbmanager.dto.DocumentDTO> fetchedList = taskManager.getTaskCollectionByCriterion(crisisID, null, criterion);
		if (fetchedList != null) {
			try {
				List<Document> documentList = Document.toLocalDocumentList(fetchedList);
				return documentList;
			} catch (Exception e) {
				logger.error("Error in converting to local document enetity type");
				logger.error(elog.toStringException(e));
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public int deleteDocument(Long documentID) {
		return taskManager.deleteTaskById(documentID);
	}

	@Override
	public void removeTrainingExample(Long documentID) {
		// Alternative way of doing the same update
		//qa.qcri.aidr.task.dto.DocumentDTO fetchedDoc  = taskManager.getTaskById(id);
		//fetchedDoc.setHasHumanLabels(false);
		//fetchedDoc.setNominalLabelCollection(null);
		//taskManager.updateTask(fetchedDoc);
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("setHasHumanLabels", new Boolean(false).toString());
		paramMap.put("setNominalLabelCollection", null);
		DocumentDTO newDoc = (DocumentDTO) taskManager.setTaskParameter(qa.qcri.aidr.dbmanager.entities.task.Document.class, documentID, paramMap);
	
		try {
			logger.info("Removed training example: " + newDoc.getDocumentID() + ", for crisisID = " + newDoc.getCrisisDTO().getCrisisID());
		} catch (PropertyNotSetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public List<Document> getAllUnlabeledDocumentbyCrisisID(Crisis crisis) {
		Criterion criterion = Restrictions.eq("hasHumanLabels", false);
		List<DocumentDTO> fetchedList = taskManager.getTaskCollectionByCriterion(crisis.getCrisisID(), null, criterion);
		if (fetchedList != null) {
			try {
				List<Document> documentList = Document.toLocalDocumentList(fetchedList);
				return documentList;
			} catch (Exception e) {
				logger.error("Error in converting to local document enetity type");
				logger.error(elog.toStringException(e));
				e.printStackTrace();
			}
		}
		return null;
	}

}
