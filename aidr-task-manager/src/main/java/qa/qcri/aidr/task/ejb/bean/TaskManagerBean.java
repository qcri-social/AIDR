package qa.qcri.aidr.task.ejb.bean;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;




//import org.apache.log4j.Logger;
//import org.codehaus.jackson.map.ObjectMapper;
//import org.codehaus.jackson.type.TypeReference;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.common.logging.ErrorLog;
/*
import qa.qcri.aidr.task.dto.DocumentDTO;
import qa.qcri.aidr.task.dto.util.CrisisDTOHelper;
import qa.qcri.aidr.task.dto.util.DocumentDTOHelper;
import qa.qcri.aidr.task.dto.util.DocumentNominalLabelDTOHelper;
import qa.qcri.aidr.task.dto.util.TaskAnswerDTOHelper;
import qa.qcri.aidr.task.dto.util.TaskAssignmentDTOHelper;
import qa.qcri.aidr.task.dto.util.UsersDTOHelper;
import qa.qcri.aidr.task.ejb.CrisisService;
import qa.qcri.aidr.task.ejb.DocumentNominalLabelService;
import qa.qcri.aidr.task.ejb.DocumentService;
import qa.qcri.aidr.task.ejb.TaskAnswerService;
import qa.qcri.aidr.task.ejb.TaskAssignmentService;
import qa.qcri.aidr.task.ejb.TaskManagerRemote;
import qa.qcri.aidr.task.ejb.UsersService;
import qa.qcri.aidr.task.entities.Document;
import qa.qcri.aidr.task.entities.DocumentNominalLabel;
import qa.qcri.aidr.task.entities.NominalLabel;
import qa.qcri.aidr.task.entities.TaskAnswer;
import qa.qcri.aidr.task.entities.TaskAssignment;
import qa.qcri.aidr.task.entities.Users;
 */

import qa.qcri.aidr.dbmanager.dto.CrisisDTO;
import qa.qcri.aidr.dbmanager.dto.DocumentDTO;
import qa.qcri.aidr.dbmanager.dto.DocumentNominalLabelDTO;
import qa.qcri.aidr.dbmanager.dto.TaskAnswerDTO;
import qa.qcri.aidr.dbmanager.dto.TaskAssignmentDTO;
import qa.qcri.aidr.dbmanager.dto.UsersDTO;
import qa.qcri.aidr.dbmanager.entities.task.*;
import qa.qcri.aidr.dbmanager.entities.misc.Crisis;
import qa.qcri.aidr.dbmanager.entities.misc.Users;
import qa.qcri.aidr.task.ejb.TaskManagerRemote;


/**
 * 
 * @author Koushik
 *
 */

@Stateless
public class TaskManagerBean<T, I> implements TaskManagerRemote<T, Serializable> {

	/*
	@EJB
	private DocumentService documentLocalEJB;

	@EJB
	private TaskAssignmentService taskAssignmentEJB;

	@EJB
	private TaskAnswerService taskAnswerEJB;

	@EJB
	private UsersService usersLocalEJB;

	@EJB
	private DocumentNominalLabelService documentNominalLabelEJB;

	@EJB
	private CrisisService crisisEJB;
	 */

	@EJB
	private qa.qcri.aidr.dbmanager.ejb.remote.facade.CrisisResourceFacade remoteCrisisEJB;

	@EJB
	private qa.qcri.aidr.dbmanager.ejb.remote.facade.DocumentResourceFacade remoteDocumentEJB;

	@EJB
	private qa.qcri.aidr.dbmanager.ejb.remote.facade.UsersResourceFacade remoteUsersEJB;

	@EJB
	private qa.qcri.aidr.dbmanager.ejb.remote.facade.DocumentNominalLabelResourceFacade remoteDocumentNominalLabelEJB;		

	@EJB
	private qa.qcri.aidr.dbmanager.ejb.remote.facade.CrisisTypeResourceFacade remoteCrisisTypeEJB;

	@EJB
	private qa.qcri.aidr.dbmanager.ejb.remote.facade.TaskAnswerResourceFacade remoteTaskAnswerEJB;

	@EJB
	private qa.qcri.aidr.dbmanager.ejb.remote.facade.TaskAssignmentResourceFacade remoteTaskAssignmentEJB;


	private Class<T> entityType;

	private Logger logger = LoggerFactory.getLogger(TaskManagerBean.class);
	private ErrorLog elog = new ErrorLog();

	public TaskManagerBean()  {
		this.entityType = getClassType();
	}  

	//private static final Object monitor = new Object();

	@SuppressWarnings("unchecked")
	public Class<T> getClassType() {
		Class<? extends Object> thisClass = getClass();
		Type genericSuperclass = thisClass.getGenericSuperclass();
		if( genericSuperclass instanceof ParameterizedType ) {
			Type[] argumentTypes = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
			Class<T> entityBeanType = (Class<T>)argumentTypes[0];
			return entityBeanType;
		} else {
			return null;
		}
	}

	@Override
	public long insertNewTask(T task) {
		if (task == null) {
			logger.error("Attempting to insert NULL");
			return -1;
		}
		DocumentDTO doc = (DocumentDTO) task;
		doc.setHasHumanLabels(false);
		try {
			//documentLocalEJB.save(doc);
			DocumentDTO savedDoc = remoteDocumentEJB.addDocument(doc);
			return savedDoc.getDocumentID();
		} catch (Exception e) {
			logger.error("Error in insertion");
			logger.error(elog.toStringException(e));
		}
		return -1;
	}


	@Override
	public void insertNewTask(List<T> collection) {
		if (collection != null) {
			try {
				for (T doc: collection) {
					((DocumentDTO) doc).setHasHumanLabels(false);
					remoteDocumentEJB.addDocument((DocumentDTO) doc);
				}
			} catch (Exception e) {
				logger.error("Error in collection insertion");
				logger.error(elog.toStringException(e));
			}
		} else {
			logger.warn("Attempting to insert NULL");
		}
	}


	@Override
	public int deleteTaskById(Long id) {
		try {
			DocumentDTO doc = remoteDocumentEJB.findDocumentByID(id);
			Integer result = remoteDocumentEJB.deleteNoLabelDocument(doc);
			//System.out.println("[deleteTaskById] gotById document: " + document);
			return result;
		} catch(Exception e) {			
			logger.error("Error in deletion");
			logger.error(elog.toStringException(e));
		}
		return 0;
	}

	@Override
	public int deleteTask(T task) {
		if (task != null) {
			try {
				//System.out.println("[deleteTask] Going for deletion of fetched doc = " + ((Document) task).getDocumentID());
				return remoteDocumentEJB.deleteNoLabelDocument((DocumentDTO) task);
				//documentLocalEJB.delete((Document) task);
			} catch (Exception e) {
				logger.error("Error in deletion of task");
				logger.error(elog.toStringException(e));
				return 0;
			}
		} else {
			logger.warn("Attempting to delete NULL");
		}
		return 0;
	}

	private List<DocumentDTO> createDocumentDTOList(List<Document> list) throws PropertyNotSetException {
		List<DocumentDTO> dtoList = new ArrayList<DocumentDTO>();
		if (list != null) {
			for (Document d : list) {
				dtoList.add(new DocumentDTO(d));
			}
		}
		return dtoList;
	}

	private List<Document> createDocumentEntityList(List<DocumentDTO> list) throws PropertyNotSetException {
		List<Document> eList = new ArrayList<Document>();
		if (list != null) {
			for (DocumentDTO d : list) {
				eList.add(d.toEntity());
			}
		}
		return eList;
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public int deleteTask(List<T> collection) {
		List<DocumentDTO> dtoList = null;
		if (collection != null) {
			try {
				dtoList = createDocumentDTOList((List<Document>) collection);
			} catch (PropertyNotSetException e) {
				logger.error("Unable to create DTO list.", e);
				e.printStackTrace();
			}

			try {
				return remoteDocumentEJB.deleteNoLabelDocument(dtoList);
			} catch (Exception e) {
				logger.error("Error in collection deletion of size: " + collection.size());
				logger.error(elog.toStringException(e));
				return 0;
			}
		} else {
			logger.warn("Attempting to delete a NULL collection");
		}
		return 0;
	}

	@Override
	public int deleteUnassignedTask(T task) {
		if (task != null) {
			try {
				return remoteDocumentEJB.deleteUnassignedDocument((DocumentDTO) task);
			} catch (Exception e) {
				logger.error("Error in deletion");
				logger.error(elog.toStringException(e));
				return 0;
			}
		} else {
			logger.warn("Attempting to delete NULL");
		}
		return 0;
	}


	@SuppressWarnings("unchecked")
	@Override
	public int deleteUnassignedTaskCollection(List<T> collection) {
		if (collection != null) {
			try {
				return remoteDocumentEJB.deleteUnassignedDocumentCollection((List<DocumentDTO>) collection);
			} catch (Exception e) {
				logger.error("Error in collection deletion");
				logger.error(elog.toStringException(e));
				return 0;
			}
		} else {
			logger.warn("Attempting to delete NULL collection");
		}
		return 0;
	}


	@Override
	public int deleteStaleTasks(String joinType, String joinTable, String joinColumn,
			String sortOrder, String[] orderBy,
			final String maxTaskAge, final String scanInterval) {
		logger.info("Received request: " + joinType + ", " + joinTable + ", " 
				+ joinColumn + ", " + maxTaskAge + ", " + scanInterval);
		try {
			int docDeleteCount = remoteDocumentEJB.deleteStaleDocuments(joinType, joinTable, joinColumn, 
					sortOrder, orderBy, maxTaskAge, scanInterval);
			return docDeleteCount;
		} catch (Exception e) {
			logger.error("Error in deletion");
			logger.error(elog.toStringException(e));
			return 0;
		}
	}

	@Override
	public int truncateLabelingTaskBufferForCrisis(final long crisisID, final int maxLength, final int ERROR_MARGIN) {
		List<Document> docList = null;
		try {
			String aliasTable = "taskAssignment";
			String order = "ASC";
			String aliasTableKey = "taskAssignment.documentID";
			String[] orderBy = {"valueAsTrainingSample", "documentID"};
			Criterion criterion = Restrictions.conjunction()
					.add(Restrictions.eq("crisisID",crisisID))
					.add(Restrictions.eq("hasHumanLabels",false));

			Criterion aliasCriterion =  (Restrictions.isNull(aliasTableKey));
			docList = remoteDocumentEJB.getByCriteriaWithAliasByOrder(criterion, order, orderBy, null, aliasTable, aliasCriterion);
			/*
			System.out.println("Fetched docList = " + docList);
			if (docList != null) {
				System.out.println("Fetched size = " + docList.size());
			}*/
		} catch (Exception e) {
			logger.error("Exception in fetching unassigned documents with hasHumaLabels=false");
			logger.error(elog.toStringException(e));
			return 0;
		}

		// Next trim the document table for the given crisisID to the 
		// Config.LABELING_TASK_BUFFER_MAX_LENGTH size
		if (docList != null) {
			int docsToDelete = docList.size() - maxLength;
			if (docsToDelete > ERROR_MARGIN) {		// if less than this, then skip delete
				try {
					// Delete the lowest confidence documents from document table
					int deleteCount = remoteDocumentEJB.deleteUnassignedDocumentCollection(createDocumentDTOList((List<Document>) docList));
					logger.info("Number of documents actually deleted = " + deleteCount);
					return deleteCount;
				} catch (Exception e) {
					logger.error("Exception when attempting to batch delete for trimming the document table");
					logger.error(elog.toStringException(e));
				} 
			} else {
				logger.info("No need for truncation: docListSize = " + docList.size() + ", max buffer size = " + maxLength);
			}
		}
		return 0;
	}



	@Override
	public void updateTask(T task) {
		try {
			// NOTE: can't use update() since serialization+deserialization
			// of persisted entity will throw an exception. Use merge() instead.
			//documentLocalEJB.update((Document) task);
			remoteDocumentEJB.merge(((DocumentDTO) task).toEntity()); 
		} catch (Exception e) {
			logger.error("failed update");
			logger.error(elog.toStringException(e));
		}
	}



	@SuppressWarnings("unchecked")
	@Override
	public void updateTaskList(List<T> collection) {
		try { 
			// NOTE: can't use update() since serialization+deserialization
			// of persisted entity will throw an exception. Use merge() instead.
			//documentLocalEJB.update((List<Document>) collection);
			remoteDocumentEJB.merge(createDocumentEntityList((List<DocumentDTO>)collection));
		} catch (Exception e) {
			logger.error("failed collection update");
			logger.error(elog.toStringException(e));
		}
	}

	@Override
	public void updateTask(DocumentDTO dto) {
		try {
			// NOTE: can't use update() since serialization+deserialization
			// of persisted entity will throw an exception. Use merge() instead.
			//documentLocalEJB.update((Document) task);
			remoteDocumentEJB.merge(dto.toEntity()); 
		} catch (Exception e) {
			logger.error("failed update");
			logger.error(elog.toStringException(e));
		}
	}


	/**
	 * Gets a new task from the task repository
	 * @param <entityClass>
	 */
	@Override
	public DocumentDTO getNewTask(Long crisisID) {
		try {
			return getNewTask(crisisID, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Gets a new task from the task repository
	 * based on specified criterion
	 */
	@Override
	public DocumentDTO getNewTask(Long crisisID, Criterion criterion) {
		Criterion newCriterion = null;
		try {
			if (criterion != null) {
				newCriterion = Restrictions.conjunction()
						.add(criterion)
						.add(Restrictions.eq("crisisID",crisisID))
						.add(Restrictions.eq("hasHumanLabels",false));
			} else {
				newCriterion = Restrictions.conjunction()
						.add(Restrictions.eq("crisisID",crisisID))
						.add(Restrictions.eq("hasHumanLabels",false));
			}

			Document document = remoteDocumentEJB.getByCriteria(newCriterion);
			logger.debug("New task: " + document);
			if (document != null && !isTaskAssigned(document)) {
				logger.info("New task: " + document.getDocumentId());
			} else {
				logger.info("[getNewTask] New task: " + document);
			}

			return new DocumentDTO(document);
		} catch (Exception e) {
			logger.error("Error in getting new Task for crisisID: " + crisisID);
			logger.error(elog.toStringException(e));
		}
		return null;
	}


	@Override
	public List<DocumentDTO> getNewTaskCollection(Long crisisID, Integer count, String order, Criterion criterion) {
		logger.info("Received request for crisisID = " + crisisID + ", count = " + count);
		String aliasTable = "taskAssignment";
		String aliasTableKey = "taskAssignment.documentID";
		String[] orderBy = {"valueAsTrainingSample","documentID"};
		Criterion newCriterion = null;
		if (criterion != null) {
			newCriterion = Restrictions.conjunction()
					.add(criterion)
					.add(Restrictions.eq("crisisID",crisisID))
					.add(Restrictions.eq("hasHumanLabels",false));
		} else {
			newCriterion = Restrictions.conjunction()
					.add(Restrictions.eq("crisisID",crisisID))
					.add(Restrictions.eq("hasHumanLabels",false));
		}
		Criterion aliasCriterion =  (Restrictions.isNull(aliasTableKey));
		try {
			List<Document> docList = remoteDocumentEJB.getByCriteriaWithAliasByOrder(newCriterion, order, orderBy, count, aliasTable, aliasCriterion);
			logger.debug("[getNewTaskCollection] docList = " + docList);
			if (docList != null) {
				logger.info("Fetched size = " + docList.size());

				return createDocumentDTOList(docList);
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error("Error in getting new Task collection for crisisID: " + crisisID);
			logger.error(elog.toStringException(e));
		}
		return null;
	}


	@Override
	public <E> Boolean isTaskAssigned(E task) {
		List<TaskAssignmentDTO> fetchedList = null;
		if (task != null) {
			try {
				DocumentDTO document = (DocumentDTO) task;
				fetchedList= remoteTaskAssignmentEJB.findTaskAssignmentByID(document.getDocumentID());
			} catch (Exception e) {
				logger.error("Error in finding Task");
				logger.error(elog.toStringException(e));
				return false;
			}
		}
		if (null == fetchedList || fetchedList.isEmpty()) {
			return false; 
		}
		return true;
	}


	@Override
	public <E> Boolean isTaskNew(E task) {
		if (task != null && !isTaskAssigned(task) && !isTaskDone(task)) {
			return true;
		} else {
			return false;
		}
	}


	@Override
	public <E> Boolean isTaskDone(E task) {
		if (task != null) {
			try {
				Document document = remoteDocumentEJB.getById(((DocumentDTO) task).getDocumentID());
				if ((document != null) && ((Document) document).isHasHumanLabels()) {
					return true;
				}
			} catch (Exception e) {
				logger.error("Error in finding document");
				logger.error(elog.toStringException(e));
				return false;
			}
		}
		return false;		// no entry for documentID in task_answer table
	}


	@Override
	public <E> Boolean isExists(E task) {
		if (task != null) {
			DocumentDTO document = (DocumentDTO) task;
			try {
				if (remoteDocumentEJB.getById(document.getDocumentID()) != null) {
					return true;
				}
			} catch (Exception e) {
				logger.error("Error in finding document");
				logger.error(elog.toStringException(e));
			}
		}
		return false;
	}

	@Override
	public DocumentDTO getTaskByCriterion(Long crisisID, Criterion criterion) {
		try {
			if (criterion != null) {
				Criterion newCriterion = Restrictions.conjunction()
						.add(criterion)
						.add(Restrictions.eq("crisisID", crisisID));
				Document document = remoteDocumentEJB.getByCriteria(newCriterion);

				return new DocumentDTO(document);
			}
		} catch (Exception e) {
			logger.error("Error in finding task");
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	@Override
	public List<DocumentDTO> getTaskCollectionByCriterion(Long crisisID, Integer count, Criterion criterion) {
		try {
			if (criterion != null) {
				Criterion newCriterion = Restrictions.conjunction()
						.add(criterion)
						.add(Restrictions.eq("crisisID", crisisID));
				List<Document> docList =  remoteDocumentEJB.getByCriteriaWithLimit(newCriterion, count);
				return createDocumentDTOList(docList);
			}
		} catch (Exception e) {
			logger.error("Error in finding task");
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	@Override
	public List<DocumentDTO> getNominalLabelDocumentCollection(Integer nominalLabelID) {
		Criterion criterion = Restrictions.eq("nominalLabelID", nominalLabelID);
		try {
			List<DocumentDTO> docList =  remoteDocumentEJB.getDocumentCollectionForNominalLabel(criterion);
			return docList;
		} catch (Exception e) {
			logger.error("Error in finding documents for given criterion: " + criterion.toString());
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	@Override
	public void taskUpdate(Criterion criterion, String joinType, String joinTable,
			String joinColumn, String sortOrder, String[] orderBy) {
		// TODO Auto-generated method stub
	}

	@Override
	public DocumentDTO getTaskById(Long id) {
		try {
			DocumentDTO document = remoteDocumentEJB.findDocumentByID(id);
			logger.info("Fetched document: " + document);

			return document;
		} catch (Exception e) {
			logger.error("Error in finding task");
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	@Override
	public List<DocumentDTO> getAllTasks() {
		try {
			List<Document> docList =  remoteDocumentEJB.getAll();
			logger.info("Fetched documents count: " + docList.size());

			return createDocumentDTOList(docList);
		} catch (Exception e) {
			logger.error("Error in finding task");
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	@Override
	public <E> String serializeTask(E task) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		String jsonString = null;
		try {
			if (task != null) jsonString = mapper.writeValueAsString(task);
		} catch (IOException e) {
			logger.error("JSON serialization exception");
			logger.error(elog.toStringException(e));
		}
		return jsonString;
	}

	/**
	 * Example method call: deSerializeList(jsonString2, new TypeReference<List<Document>>() {}) 
	 */
	@Override
	public <E> E deSerializeList(String jsonString, TypeReference<E> type) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			if (jsonString != null) {
				E docList = mapper.readValue(jsonString, type);
				return docList;
			}	
		} catch (IOException e) {
			logger.error("JSON deserialization exception");
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	/**
	 * Example method call: deSerialize(jsonString, Document.class)
	 */
	@Override
	public <E> E deSerialize(String jsonString, Class<E> entityType) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			if (jsonString != null) {
				E entity = mapper.readValue(jsonString, entityType);
				return entity;
			}	
		} catch (IOException e) {
			logger.error("JSON deserialization exception");
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	////////////////////////////////////////////////////////////
	// Trainer API Task Assignment related APIs
	////////////////////////////////////////////////////////////
	@Override
	public void assignNewTaskToUser(Long id, Long userId) throws Exception {
		int retVal = remoteTaskAssignmentEJB.insertOneTaskAssignment(id, userId);
		if (retVal <= 0) {
			logger.error("unable to undo task assignment");
			throw new Exception("[assignNewTaskToUser] Couldn't undo task assignment");
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void assignNewTaskToUser(List<T> collection, Long userId) throws Exception {
		int retVal = remoteTaskAssignmentEJB.insertTaskAssignment(createDocumentDTOList((List<Document>)collection), userId);
		if (retVal <= 0) {
			throw new Exception("[assignNewTaskToUser] Couldn't undo task assignment");
		}
	}

	@Override
	public void undoTaskAssignment(Map<Long, Long> taskMap) throws Exception {
		int retVal = remoteTaskAssignmentEJB.undoTaskAssignment(taskMap);
		if (retVal <= 0) {
			logger.error("Unable to undo task assignment");
			throw new Exception("[undoTaskAssignment] Couldn't undo task assignment");
		}
	}

	@Override
	public void undoTaskAssignment(Long id, Long userId) throws Exception {
		int retVal = remoteTaskAssignmentEJB.undoTaskAssignment(id, userId);
		if (retVal <= 0) {
			logger.error("Unable to undo task assignment");
			throw new Exception("[undoTaskAssignment] Couldn't undo task assignment");
		}
	}

	@Override
	public Integer getPendingTaskCountByUser(Long userId) {
		return remoteTaskAssignmentEJB.getPendingTaskCount(userId);
	}

	@Override
	public List<TaskAssignmentDTO> getAssignedTasksById(Long id) {
		List<TaskAssignmentDTO> docList = remoteTaskAssignmentEJB.findTaskAssignmentByID(id);
		if (docList != null) {
			try {
				return docList;
			} catch (Exception e) {
				logger.error("Error in serializing collection");
				logger.error(elog.toStringException(e));
			}
		}
		return null;
	}

	@Override
	public TaskAssignmentDTO getAssignedTaskByUserId(Long id, Long userId) {
		TaskAssignmentDTO assignedUserTask = remoteTaskAssignmentEJB.findTaskAssignment(id, userId);
		if (assignedUserTask != null) {
			try {
				return assignedUserTask;
			} catch (Exception e) {
				logger.error("Error in serializing collection");
				logger.error(elog.toStringException(e));
			}
		}
		return null;
	}

	/**
	 * Takes as input a map consisting of the setter methods and their corresponding parameters
	 * of the entity to be modified. Returns the modified entity.
	 */
	@Override
	public Object setTaskParameter(Class entityType, Long id, Map<String, String> paramMap) {
		logger.info("Received request for task ID = " + id + ", param Map = " + paramMap);
		Object doc = null;
		try {
			if (entityType.getName().contains("qa.qcri.aidr.dbmanager.entities.task.Document")) {
				logger.info("Detected of type Document.class, id = " + id);
				doc = (Document) remoteDocumentEJB.getById(id);
			} 
			if (entityType.getName().contains("qa.qcri.aidr.dbmanager.entities.task.TaskAssignment")) {
				logger.info("Detected of type TaskAssignment.class");
				doc = (TaskAssignment) remoteTaskAssignmentEJB.getById(id);
			}
			if (entityType.getName().contains("qa.qcri.aidr.dbmanager.entities.task.TaskAnswer")) {
				logger.info("Detected of type TaskAnswer.class");
				List<TaskAnswerDTO> docList = remoteTaskAnswerEJB.getTaskAnswer(id);
				if (docList != null) 
					doc = docList.get(0).toEntity();			
			}
			if (entityType.getName().contains("qa.qcri.aidr.dbmanager.entities.misc.Users")) {
				logger.info("Detected of type Users.class");
				doc = (Users) remoteUsersEJB.getById(id);
			}
			if (entityType.getName().contains("qa.qcri.aidr.dbmanager.entities.task.DocumentNominalLabel")) {
				logger.info("Detected of type DocumentNominalLabel.class");
				doc = (DocumentNominalLabel) remoteDocumentNominalLabelEJB.getById(id);
			}
			if (entityType.getName().contains("qa.qcri.aidr.dbmanager.entities.misc.Crisis")) {
				logger.info("Detected of type Crisis.class");
				doc = (Crisis) remoteCrisisEJB.getById(id);
			}
			logger.info("Fetched task of type: " + doc.getClass());
			logger.info("received map: " + paramMap);
		} catch (Exception e) {
			logger.error("Error in detecting Class Type");
			logger.error(elog.toStringException(e));
			return null;
		}

		if (doc != null) {
			Class docClass = null;
			//Object obj = null;
			Method[] methods = null;
			Class[] paramTypes = null;
			try {
				//docClass = Class.forName(className);
				docClass = entityType;
				//obj = docClass.newInstance();
				methods = docClass.getDeclaredMethods();
				for (int i = 0; i < methods.length;i++) {
					logger.info("discovered method: " + methods[i].getName());
				}
			} catch (Exception e) {
				logger.error("Error in instantiating method class");
				logger.error(elog.toStringException(e));
				return null;
			}

			Iterator<String> itr = paramMap.keySet().iterator();
			while (itr.hasNext()) {
				String name = itr.next();
				try {
					int pointer = -1;
					for (int j = 0;j < methods.length;j++) {
						if (methods[j].getName().equals(name)) {
							pointer = j;
							break;
						}
					}
					paramTypes = methods[pointer].getParameterTypes(); 
					for (int j = 0; j < paramTypes.length;j++) {
						logger.info(methods[pointer].getName() + ": discovered parameter types: " + paramTypes[j].getName());
					}
					// Convert parameter to paramType
					if (paramTypes[0].getName().toLowerCase().contains("long")) {
						methods[pointer].invoke(doc, Long.parseLong(paramMap.get(name)));
						logger.info("Invoking with Long parameter type");
					}
					if (paramTypes[0].getName().toLowerCase().contains("int")) {
						methods[pointer].invoke(doc, Integer.parseInt(paramMap.get(name)));
						logger.info("Invoking with Integer parameter type");
					}
					if (paramTypes[0].getName().toLowerCase().contains("boolean")) {
						methods[pointer].invoke(doc, Boolean.parseBoolean(paramMap.get(name)));
						logger.info("Invoking with Boolean parameter type");
					}
					if (paramTypes[0].getName().contains("String")) {
						methods[pointer].invoke(doc, paramMap.get(name));
						logger.info("Invoking with String parameter type");
					}
					logger.info("[setTaskParameter] Invoked method: " + methods[pointer].getName() + " with parameter: " + paramMap.get(name));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error("Error in invoking method via reflection: ");
					logger.error(elog.toStringException(e));
					return null;
				} 
			}	
		}

		try {
			logger.info("Will attempt to merge update with document ID = " + id);
			if (entityType.getName().contains("qa.qcri.aidr.dbmanager.entities.task.Document")) {
				logger.info("Detected of type Document.class, id = " + id);
				remoteDocumentEJB.merge((Document) doc); 
				logger.info("Merge update successful for task id = " + id);
				//return serializeTask((qa.qcri.aidr.task.entities.Document) doc);
				return new DocumentDTO((Document) doc);
			} 
			if (entityType.getName().contains("qa.qcri.aidr.dbmanager.entities.task.TaskAssignment")) {
				logger.info("Detected of type TaskAssignment.class");
				remoteTaskAssignmentEJB.merge((TaskAssignment) doc);
				logger.info("Merge update successful for task id = " + id);
				//return serializeTask((qa.qcri.aidr.task.entities.TaskAssignment) doc);
				return new TaskAssignmentDTO((TaskAssignment) doc);
			}
			if (entityType.getName().contains("qa.qcri.aidr.dbmanager.entities.task.TaskAnswer")) {
				logger.info("Detected of type TaskAnswer.class");
				remoteTaskAnswerEJB.merge((TaskAnswer) doc);
				logger.info("Merge update successful for task id = " + id);
				//return serializeTask((qa.qcri.aidr.task.entities.TaskAnswer) doc);
				return new TaskAnswerDTO((TaskAnswer) doc);
			}
			if (entityType.getName().contains("qa.qcri.aidr.dbmanager.entities.misc.Users")) {
				logger.info("Detected of type Users.class");
				remoteUsersEJB.merge((Users) doc);
				logger.info("Merge update successful for task id = " + id);
				//return serializeTask((qa.qcri.aidr.task.entities.Users) doc);
				return new UsersDTO((Users) doc);
			}
			if (entityType.getName().contains("qa.qcri.aidr.dbmanager.entities.task.DocumentNominalLabel")) {
				logger.info("Detected of type DocumentNominalLabel.class");
				remoteDocumentNominalLabelEJB.merge((DocumentNominalLabel) doc);
				logger.info("Merge update successful for task id = " + id);
				//return serializeTask((qa.qcri.aidr.task.entities.DocumentNominalLabel) doc);
				return new DocumentNominalLabelDTO((DocumentNominalLabel) doc);
			}
			if (entityType.getName().contains("qa.qcri.aidr.dbmanager.entities.misc.Crisis")) {
				logger.info("Detected of type Crisis.class");
				remoteCrisisEJB.merge((Crisis) doc);
				logger.info("Merge update successful for task id = " + id);
				//return serializeTask((qa.qcri.aidr.task.entities.Crisis) doc);
				return new CrisisDTO((Crisis) doc);
			}
		} catch (Exception e) {
			logger.error("Error in updating entity on DB");
			logger.error(elog.toStringException(e));
		}
		return null;
	}



	////////////////////////////////////////////
	// User service related APIs
	////////////////////////////////////////////
	@Override
	public UsersDTO getUserByName(String name) {
		UsersDTO user = null;
		try {
			user = remoteUsersEJB.getUserByName(name);
		} catch (PropertyNotSetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public UsersDTO getUserById(Long id) {
		UsersDTO user = null;
		try {
			user = remoteUsersEJB.getUserById(id);
		} catch (PropertyNotSetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//return serializeTask(user);
		return user;
	}

	@Override
	public List<UsersDTO> getAllUserByName(String name) {
		List<UsersDTO> userList = new ArrayList<UsersDTO>();
		try {
			userList = remoteUsersEJB.getAllUsersByName(name);
		} catch (PropertyNotSetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userList;
	}

	@Override
	public void insertTaskAnswer(TaskAnswerDTO taskAnswer) {
		try {
			remoteTaskAnswerEJB.save(taskAnswer.toEntity());
		} catch (Exception e) {
			logger.error("Error in saving task answer : " + taskAnswer);
			logger.error(elog.toStringException(e));
		}
	}


	@Override
	public void saveDocumentNominalLabel(DocumentNominalLabelDTO documentNominalLabel) {
		try {
			remoteDocumentNominalLabelEJB.save(documentNominalLabel.toEntity());
		} catch (Exception e) {
			logger.error("Error in saving document nominal label : " + documentNominalLabel);
			logger.error(elog.toStringException(e));
		}
	}

	@Override
	public boolean foundDuplicateDocumentNominalLabel(DocumentNominalLabelDTO documentNominalLabel) {
		Map<String, Long> attMap = new HashMap<String, Long>();
		try {
			attMap.put("documentID", documentNominalLabel.getIdDTO().getDocumentId());
			attMap.put("nominalLabelID", documentNominalLabel.getIdDTO().getNominalLabelId());
		} catch (PropertyNotSetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DocumentNominalLabel obj =  remoteDocumentNominalLabelEJB.getByCriterionID(Restrictions.allEq(attMap));

		if(obj != null) {
			return true;
		} else {
			return false;  //To change body of implemented methods use File | Settings | File Templates.
		}
	}

	@Override
	public DocumentDTO getDocumentById(Long id) {
		try {
			Document document = remoteDocumentEJB.getById(id);
			logger.info("Fetched document: " + document);

			return new DocumentDTO(document);
		} catch (Exception e) {
			logger.error("Error in finding task");
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	@Override
	public String pingRemoteEJB() {
		StringBuilder sb = new StringBuilder("{\"status\": \"RUNNING\"}");
		return sb.toString();
	}


	/*
	public static void main(String args[]) {
		TaskManagerRemote<Document, Serializable> tm = new TaskManagerBean<Document, Long>();
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("setHasHumanLabels", new Boolean(false).toString());
		paramMap.put("setCrisisID", new Long(117L).toString());
		qa.qcri.aidr.task.entities.Document newDoc = tm.setTaskParameter(qa.qcri.aidr.task.entities.Document.class, 4579275L, paramMap);
		System.out.println("newDoc = " + newDoc.getDocumentID() + ": " + newDoc.isHasHumanLabels());
	}
	 */

}
