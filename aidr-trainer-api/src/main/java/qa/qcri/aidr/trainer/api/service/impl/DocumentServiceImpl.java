package qa.qcri.aidr.trainer.api.service.impl;

//import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import qa.qcri.aidr.common.util.TrainingDataFetchType;
import qa.qcri.aidr.dbmanager.dto.DocumentDTO;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.TaskManagerRemote;
import qa.qcri.aidr.dbmanager.entities.task.Document;
import qa.qcri.aidr.trainer.api.entity.Collection;
//import qa.qcri.aidr.trainer.api.entity.Document;
import qa.qcri.aidr.trainer.api.entity.Users;
import qa.qcri.aidr.trainer.api.service.CrisisService;
import qa.qcri.aidr.trainer.api.service.DocumentService;
import qa.qcri.aidr.trainer.api.service.TaskAssignmentService;
import qa.qcri.aidr.trainer.api.service.UsersService;
import qa.qcri.aidr.trainer.api.store.CodeLookUp;
import qa.qcri.aidr.trainer.api.template.CrisisJsonModel;
import qa.qcri.aidr.trainer.api.template.CrisisJsonOutput;
import qa.qcri.aidr.trainer.api.template.NominalAttributeJsonModel;
import qa.qcri.aidr.trainer.api.template.TaskBufferJsonModel;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 10/1/13
 * Time: 12:49 PM
 * To change this template use File | Settings | File Templates.
 */
@Service("documentService")
@Transactional(readOnly = true)
public class DocumentServiceImpl implements DocumentService {

	protected static Logger logger = Logger.getLogger(DocumentServiceImpl.class);
	
	//@Autowired
	//private DocumentDao documentDao;

	@Autowired
	private TaskAssignmentService taskAssignmentService;

	@Autowired
	private UsersService usersService;

	@Autowired
	private CrisisService crisisService;

	@Autowired TaskManagerRemote<DocumentDTO, Long> taskManager;

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updateHasHumanLabel(Long documentID, boolean value) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("setHasHumanLabels", new Boolean(value).toString());
		//logger.info("Will use the merge attempt");
		try {
			DocumentDTO dto = (DocumentDTO) taskManager.setTaskParameter(Document.class, documentID, paramMap);
			//logger.info("Update of hasHumanLabels successful for document " + dto.getDocumentID() + ", crisisId = " + dto.getCrisisDTO().getCrisisID());
		} catch (Exception e) {
			logger.error("Update unsuccessful for documentID = " + documentID, e);
		}
	}

	@Override
	public DocumentDTO findDocument(Long documentID) {
		//return documentDao.findDocument(documentID);  //To change body of implemented methods use File | Settings | File Templates.
		DocumentDTO doc = taskManager.getTaskById(documentID);
		return (doc != null) ? doc : null;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
	public List<DocumentDTO> getDocumentForTask(Long crisisID, int count, String userName) {
		/*
		List<DocumentDTO> documents = null;
		Users users = usersService.findUserByName(userName);
		if(users != null){
			int availableRequestSize = this.getAvailableDocumentCount(crisisID) - CodeLookUp.DOCUMENT_REMAINING_COUNT;
			if(availableRequestSize > 0){
				if(availableRequestSize < count){
					count = availableRequestSize;
				}
				documents =  this.getAvailableDocument(crisisID, count) ;
				if(documents != null && documents.size() > 0){
					taskAssignmentService.addToTaskAssignment(documents, users.getUserID());
				}
			}
		}
		*/
		List<DocumentDTO> documents = taskManager.getDocumentsForTagging(crisisID, count, userName, CodeLookUp.DOCUMENT_REMAINING_COUNT, TrainingDataFetchType.BATCH_FETCH);
		logger.info("For crisisID = " + crisisID + ", user = " + userName + ", documents available for tagging: " + (documents != null ? documents.size() : "empty list"));
		return documents;  
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
	public List<DocumentDTO> getDocumentForOneTask(Long crisisID, int count, String userName) {
		//logger.info("getDocumentForOneTask is called");
		/*
		List<DocumentDTO> documents = null;
		Users users = usersService.findUserByName(userName);
		if(users != null){
			documents =  this.getAvailableDocument(crisisID, count) ;
			System.out.println("For crisisID = " + crisisID + ", user = " + userName + ", documents available: " + (documents != null ? documents.size() : "empty list"));
			if(documents != null && documents.size() > 0){
				taskAssignmentService.addToTaskAssignment(documents, users.getUserID());
				System.out.println("Added to task_assignment table: " + documents.size() + "docID = " + documents.get(0).getDocumentID());
			}
		}
		*/
		List<DocumentDTO> documents = taskManager.getDocumentsForTagging(crisisID, count, userName, 0, TrainingDataFetchType.INTERNAL_TRAINING);
		logger.info("For crisisID = " + crisisID + ", user = " + userName + ", documents available for tagging: " + (documents != null ? documents.size() : "empty list"));
		return documents;  //To change body of implemented methods use File | Settings | File Templates.
	}


	@Override
	public List<TaskBufferJsonModel> findOneDocumentForTaskByCririsID(DocumentDTO document, Long crisisID) {
		List<TaskBufferJsonModel> jsonModelList = new ArrayList<TaskBufferJsonModel>();
		if(document != null){
			jsonModelList = getJsonModeForTask(crisisID, document);
		}
		return  jsonModelList;
	}


	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
	public void addToOneTaskAssignment(Long documentID, Long userID){
		// addToOneTaskAssignment(documentID, userID);
		taskAssignmentService.addToOneTaskAssignment(documentID, userID);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
	public void addToOneTaskAssignmentWithUserName(Long documentID, String userName){
		Users users = usersService.findUserByName(userName);
		taskAssignmentService.addToOneTaskAssignment(documentID, users.getUserID());
	}


	private List<TaskBufferJsonModel> getJsonModeForTask(long crisisID, DocumentDTO document){
		List<TaskBufferJsonModel> jsonModelList = new ArrayList<TaskBufferJsonModel>();
		Collection crisis =  crisisService.findByCrisisID(crisisID) ;
		CrisisJsonModel jsonOutput = new CrisisJsonOutput().crisisJsonModelGenerator(crisis);
		Set<NominalAttributeJsonModel> attributeJsonModelSet = jsonOutput.getNominalAttributeJsonModelSet() ;

		TaskBufferJsonModel jsonModel = new TaskBufferJsonModel(document.getDocumentID(),document.getCrisisDTO().getCrisisID(),attributeJsonModelSet,document.getLanguage(), document.getDoctype(), document.getData(), document.getValueAsTrainingSample(),0);
		jsonModelList.add(jsonModel);

		return jsonModelList;
	}

	@Override
	@Transactional(readOnly = true)
	public  List<DocumentDTO> getAvailableDocument(Long crisisID, Integer maxresult){
		//return documentDao.findDocumentForTask(crisisID, maxresult);
		List<DocumentDTO> dtoList = taskManager.getNewTaskCollection(crisisID, maxresult, "DESC", null);
		logger.info("Fetched from DB manager, documents list size = " + dtoList.size());
		return dtoList;
	}

	private int getAvailableDocumentCount(Long crisisID){
		//return documentDao.getAvailableTaskDocumentCount(crisisID);
		List<DocumentDTO> docList = getAvailableDocument(crisisID, null);
		return (docList != null) ? docList.size() : 0;

	}

}