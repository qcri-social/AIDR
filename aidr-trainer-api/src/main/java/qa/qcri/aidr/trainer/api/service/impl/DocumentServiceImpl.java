package qa.qcri.aidr.trainer.api.service.impl;

//import org.apache.log4j.Logger;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.task.ejb.TaskManagerRemote;
import qa.qcri.aidr.trainer.api.dao.CrisisDao;
import qa.qcri.aidr.trainer.api.dao.DocumentDao;
import qa.qcri.aidr.trainer.api.dao.TaskAssignmentDao;
import qa.qcri.aidr.trainer.api.dao.UsersDao;
import qa.qcri.aidr.trainer.api.entity.Crisis;
import qa.qcri.aidr.trainer.api.entity.Document;
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
import qa.qcri.aidr.trainer.api.util.TaskManagerEntityMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

	protected static Logger logger = LoggerFactory.getLogger(DocumentServiceImpl.class);
	private ErrorLog elog = new ErrorLog();

	//@Autowired
	//private DocumentDao documentDao;

	@Autowired
	private TaskAssignmentService taskAssignmentService;

	@Autowired
	private UsersService usersService;

	@Autowired
	private CrisisService crisisService;

	@Autowired TaskManagerRemote<qa.qcri.aidr.task.entities.Document, Long> taskManager;

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updateHasHumanLabel(Long documentID, boolean value) {
		/*
		logger.debug("documentID : " + documentID) ;
		Document document = new Document(documentID, true);
		logger.debug("document : " + document) ;
		if(document != null ) {
			documentDao.updateHasHumanLabel(document);
		}
		 */
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("setHasHumanLabels", new Boolean(value).toString());
		logger.info("Will use the merge attempt");
		try {
			qa.qcri.aidr.task.dto.DocumentDTO dto = (qa.qcri.aidr.task.dto.DocumentDTO) taskManager.setTaskParameter(qa.qcri.aidr.task.entities.Document.class, documentID, paramMap);
			Document doc = Document.toLocalDocument(dto);
			logger.info("Update of hasHumanLabels successful for document " + doc.getDocumentID() + ", crisisId = " + doc.getCrisisID());
		} catch (Exception e) {
			logger.error("Update unsuccessful for documentID = " + documentID);
			logger.error(elog.toStringException(e));
		}

		// Alternative method of doing the same
		/*
		Document doc = findDocument(documentID);
		logger.info("Found document : " + doc);
		if (doc != null) {
			try {
				doc.setHasHumanLabels(true);
				TaskManagerEntityMapper mapper = new TaskManagerEntityMapper();
				qa.qcri.aidr.task.entities.Document document = mapper.reverseTransformDocument(doc);
				document.setHasHumanLabels(true);
				taskManager.updateTask(document); 
				logger.info("Update of hasHumanLabels for documentID = " + documentID + " for crisisID " + document.getCrisisID() + " successful.");
			} catch (Exception e) {
				logger.info("Failed to update hasHumanLabels field for documentID = " + documentID + ", crisisID = " + doc.getCrisisID());
				logger.error(elog.toStringException(e));
			}
		} else {
			logger.info("Document with ID = " + documentID + " does not exist in DB. Can't update hasHumanLabels field!");
		}*/
	}

	@Override
	public Document findDocument(Long documentID) {
		//return documentDao.findDocument(documentID);  //To change body of implemented methods use File | Settings | File Templates.

		Document doc = Document.toLocalDocument(taskManager.getTaskById(documentID));
		return (doc != null) ? doc : null;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
	public List<Document> getDocumentForTask(Long crisisID, int count, String userName) {

		List<Document> documents = null;
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

		return documents;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
	public List<Document> getDocumentForOneTask(Long crisisID, int count, String userName) {
		//logger.info("getDocumentForOneTask is called");
		List<Document> documents = null;
		Users users = usersService.findUserByName(userName);

		if(users != null){
			documents =  this.getAvailableDocument(crisisID, count) ;
			//logger.info("documents : " + documents.size());
			if(documents != null && documents.size() > 0){
				taskAssignmentService.addToTaskAssignment(documents, users.getUserID());
			}

		}

		return documents;  //To change body of implemented methods use File | Settings | File Templates.
	}


	@Override
	public List<TaskBufferJsonModel> findOneDocumentForTaskByCririsID(Document document, Long crisisID) {
		List<TaskBufferJsonModel> jsonModelList = new ArrayList<TaskBufferJsonModel>();
		if(document != null){
			jsonModelList = getJsonModeForTask(crisisID, document);
		}
		return  jsonModelList;
	}


	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
	public void addToOneTaskAssignment(long documentID, long userID){
		// addToOneTaskAssignment(documentID, userID);
		taskAssignmentService.addToOneTaskAssignment(documentID, userID);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
	public void addToOneTaskAssignmentWithUserName(long documentID, String userName){
		Users users = usersService.findUserByName(userName);
		taskAssignmentService.addToOneTaskAssignment(documentID, users.getUserID());
	}


	private List<TaskBufferJsonModel> getJsonModeForTask(long crisisID, Document document){
		List<TaskBufferJsonModel> jsonModelList = new ArrayList<TaskBufferJsonModel>();
		Crisis crisis =  crisisService.findByCrisisID(crisisID) ;
		CrisisJsonModel jsonOutput = new CrisisJsonOutput().crisisJsonModelGenerator(crisis);
		Set<NominalAttributeJsonModel> attributeJsonModelSet = jsonOutput.getNominalAttributeJsonModelSet() ;

		TaskBufferJsonModel jsonModel = new TaskBufferJsonModel(document.getDocumentID(),document.getCrisisID(),attributeJsonModelSet,document.getLanguage(), document.getDoctype(), document.getData(), document.getValueAsTrainingSample(),0);
		jsonModelList.add(jsonModel);

		return jsonModelList;
	}

	@Override
	@Transactional(readOnly = true)
	//public  List<Document> getAvailableDocument(long crisisID, int maxresult){
	public  List<Document> getAvailableDocument(long crisisID, Integer maxresult){
		//return documentDao.findDocumentForTask(crisisID, maxresult);

		List<qa.qcri.aidr.task.dto.DocumentDTO> dtoList = taskManager.getNewTaskCollection(crisisID, maxresult, "DESC", null);
		List<Document> docList = Document.toLocalDocumentList(dtoList);
		return docList;
	}

	private int getAvailableDocumentCount(long crisisID){
		//return documentDao.getAvailableTaskDocumentCount(crisisID);
		List<Document> docList = getAvailableDocument(crisisID, null);
		return (docList != null) ? docList.size() : 0;

	}

}
