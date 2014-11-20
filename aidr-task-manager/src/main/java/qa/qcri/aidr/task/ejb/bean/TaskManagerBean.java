package qa.qcri.aidr.task.ejb.bean;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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

import qa.qcri.aidr.common.logging.ErrorLog;
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


/**
 * 
 * @author Koushik
 *
 */

@Stateless
public class TaskManagerBean<T, I> implements TaskManagerRemote<T, Serializable> {

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

		Document doc = (Document) task;
		doc.setHasHumanLabels(false);
		try {
			documentLocalEJB.save(doc);
			//System.out.println("saved document " + doc);
			//if (doc != null) System.out.println("docID = " + doc.getDocumentID());

			// Now check if also need to insert into document_nominal_label
			if (doc.getHasHumanLabels() && doc.getNominalLabelCollection() != null) {
				//System.out.println("Attempting saving to document_nominal_label table: " + doc);
				for (NominalLabel label: doc.getNominalLabelCollection()) {
					DocumentNominalLabel labeledDoc = new DocumentNominalLabel(doc.getDocumentID(), 
							new Long(label.getNominalLabelID()), 32L);
					//System.out.println("To write nominal label:  {" + labeledDoc.getDocumentID() + ", " + labeledDoc.getNominalLabelID() + ", " + labeledDoc.getTimestamp() + "}");
					documentNominalLabelEJB.save(labeledDoc);
					//System.out.println("Wrote nominal label:  {" + labeledDoc.getDocumentID() + ", " + labeledDoc.getNominalLabelID() + ", " + labeledDoc.getTimestamp() + "}");
				}
			}
			return doc.getDocumentID();
		} catch (Exception e) {
			logger.error("Error in insertion");
			logger.error(elog.toStringException(e));
		}
		return -1;
	}


	@Override
	public void insertNewTask(List<T> collection) {
		if (collection != null) {
			for (T doc: collection) {
				((Document) doc).setHasHumanLabels(false);
			}
			try {
				documentLocalEJB.save((List<Document>) collection);
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
		Document document = (Document) documentLocalEJB.getById(id);
		//System.out.println("[deleteTaskById] gotById document: " + document);
		try {
			if (document != null) 
				return documentLocalEJB.deleteNoLabelDocument(document);
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
				return documentLocalEJB.deleteNoLabelDocument((Document) task);
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


	@SuppressWarnings("unchecked")
	@Override
	public int deleteTask(List<T> collection) {
		if (collection != null) {
			try {
				return documentLocalEJB.deleteNoLabelDocument((List<Document>) collection);
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
				return documentLocalEJB.deleteUnassignedDocument((Document) task);
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
				return documentLocalEJB
						.deleteUnassignedDocumentCollection((List<Document>) collection);
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
			int docDeleteCount = documentLocalEJB
					.deleteStaleDocuments(joinType, joinTable, joinColumn, 
							sortOrder, orderBy,
							maxTaskAge, scanInterval);
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
			docList = documentLocalEJB.getByCriteriaWithAliasByOrder(criterion, order, orderBy, null, aliasTable, aliasCriterion);
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
					int deleteCount = documentLocalEJB.deleteUnassignedDocumentCollection(docList);
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
			documentLocalEJB.merge(((Document) task)); 
		} catch (Exception e) {
			logger.error("failed update");
			logger.error(elog.toStringException(e));
		}
	}



	@Override
	public void updateTaskList(List<T> collection) {
		try { 
			// NOTE: can't use update() since serialization+deserialization
			// of persisted entity will throw an exception. Use merge() instead.
			//documentLocalEJB.update((List<Document>) collection);
			documentLocalEJB.merge((List<Document>) collection);
		} catch (Exception e) {
			logger.error("failed collection update");
			logger.error(elog.toStringException(e));
		}
	}

	@Override
	public void updateTask(qa.qcri.aidr.task.dto.DocumentDTO dto) {
		try {
			// NOTE: can't use update() since serialization+deserialization
			// of persisted entity will throw an exception. Use merge() instead.
			//documentLocalEJB.update((Document) task);
			documentLocalEJB.merge(DocumentDTOHelper.toDocument(dto)); 
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
	public qa.qcri.aidr.task.dto.DocumentDTO getNewTask(Long crisisID) {
		//Document document = (Document) getNewTask(crisisID, null);
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
	public qa.qcri.aidr.task.dto.DocumentDTO getNewTask(Long crisisID, Criterion criterion) {
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

			Document document = documentLocalEJB.getByCriteria(newCriterion);
			logger.debug("New task: " + document);
			if (document != null && !isTaskAssigned(document)) {
				logger.info("New task: " + document.getDocumentID());
			} else {
				logger.info("[getNewTask] New task: " + document);
			}
			//String jsonString = serializeTask(document);
			//return jsonString;
			return DocumentDTOHelper.toDocumentDTO(document);
		} catch (Exception e) {
			logger.error("Error in getting new Task for crisisID: " + crisisID);
			logger.error(elog.toStringException(e));
		}
		return null;
	}


	@Override
	public List<qa.qcri.aidr.task.dto.DocumentDTO> getNewTaskCollection(Long crisisID, Integer count, String order, Criterion criterion) {
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
			List<Document> docList = documentLocalEJB.getByCriteriaWithAliasByOrder(newCriterion, order, orderBy, count, aliasTable, aliasCriterion);
			logger.debug("[getNewTaskCollection] docList = " + docList);
			if (docList != null) {
				logger.info("Fetched size = " + docList.size());
				//String jsonString = serializeTask(docList);
				//return jsonString;
				return DocumentDTOHelper.toDocumentDTOList(docList);
			}
			return null;
		} catch (Exception e) {
			logger.error("Error in getting new Task collection for crisisID: " + crisisID);
			logger.error(elog.toStringException(e));
		}
		return null;
	}


	@Override
	public <E> Boolean isTaskAssigned(E task) {
		List<TaskAssignment> fetchedList = null;
		if (task != null) {
			try {
				Document document = (Document) task;
				fetchedList= taskAssignmentEJB.findTaskAssignmentByID(document.getDocumentID());
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
				Document document = documentLocalEJB.getById(((Document) task).getDocumentID());
				if ((document != null) && ((Document) document).getHasHumanLabels()) {
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
			Document document = (Document) task;
			try {
				if (documentLocalEJB.getById(document.getDocumentID()) != null) {
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
	public qa.qcri.aidr.task.dto.DocumentDTO getTaskByCriterion(Long crisisID, Criterion criterion) {
		try {
			if (criterion != null) {
				Criterion newCriterion = Restrictions.conjunction()
						.add(criterion)
						.add(Restrictions.eq("crisisID", crisisID));
				Document document = documentLocalEJB.getByCriteria(newCriterion);
				//String jsonString = serializeTask(document);
				//return jsonString;
				return DocumentDTOHelper.toDocumentDTO(document);
			}
		} catch (Exception e) {
			logger.error("Error in finding task");
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	@Override
	public List<qa.qcri.aidr.task.dto.DocumentDTO> getTaskCollectionByCriterion(Long crisisID, Integer count, Criterion criterion) {
		try {
			if (criterion != null) {
				Criterion newCriterion = Restrictions.conjunction()
						.add(criterion)
						.add(Restrictions.eq("crisisID", crisisID));
				List<Document> docList =  documentLocalEJB.getByCriteriaWithLimit(newCriterion, count);
				//String jsonString = serializeTask(docList);
				//return jsonString;
				return DocumentDTOHelper.toDocumentDTOList(docList);
			}
		} catch (Exception e) {
			logger.error("Error in finding task");
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
	public qa.qcri.aidr.task.dto.DocumentDTO getTaskById(Long id) {
		try {
			Document document = documentLocalEJB.getById(id);
			logger.info("Fetched document: " + document);
			//String jsonString = serializeTask(document);
			//return jsonString;
			return DocumentDTOHelper.toDocumentDTO(document);
		} catch (Exception e) {
			logger.error("Error in finding task");
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	@Override
	public List<qa.qcri.aidr.task.dto.DocumentDTO> getAllTasks() {
		try {
			List<Document> docList =  documentLocalEJB.getAll();
			logger.info("Fetched documents count: " + docList.size());

			//String jsonString = serializeTask(docList);
			//return jsonString;
			return DocumentDTOHelper.toDocumentDTOList(docList);
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
		int retVal = taskAssignmentEJB.insertOneTaskAssignment(id, userId);
		if (retVal <= 0) {
			logger.error("unable to undo task assignment");
			throw new Exception("[assignNewTaskToUser] Couldn't undo task assignment");
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void assignNewTaskToUser(List<T> collection, Long userId) throws Exception {
		int retVal = taskAssignmentEJB.insertTaskAssignment((List<Document>) collection, userId);
		if (retVal <= 0) {
			throw new Exception("[assignNewTaskToUser] Couldn't undo task assignment");
		}
	}

	@Override
	public void undoTaskAssignment(Map<Long, Long> taskMap) throws Exception {
		int retVal = taskAssignmentEJB.undoTaskAssignment(taskMap);
		if (retVal <= 0) {
			logger.error("Unable to undo task assignment");
			throw new Exception("[undoTaskAssignment] Couldn't undo task assignment");
		}
	}

	@Override
	public void undoTaskAssignment(Long id, Long userId) throws Exception {
		int retVal = taskAssignmentEJB.undoTaskAssignment(id, userId);
		if (retVal <= 0) {
			logger.error("Unable to undo task assignment");
			throw new Exception("[undoTaskAssignment] Couldn't undo task assignment");
		}
	}

	@Override
	public Integer getPendingTaskCountByUser(Long userId) {
		return taskAssignmentEJB.getPendingTaskCount(userId);
	}

	@Override
	public List<qa.qcri.aidr.task.dto.TaskAssignmentDTO> getAssignedTasksById(Long id) {
		List<TaskAssignment> docList = taskAssignmentEJB.findTaskAssignmentByID(id);
		if (docList != null || !docList.isEmpty()) {
			try {
				//String jsonString = serializeTask(docList);
				//return jsonString;
				return TaskAssignmentDTOHelper.toTaskAssignmentDTOList(docList);
			} catch (Exception e) {
				logger.error("Error in serializing collection");
				logger.error(elog.toStringException(e));
			}
		}
		return null;
	}

	@Override
	public qa.qcri.aidr.task.dto.TaskAssignmentDTO getAssignedTaskByUserId(Long id, Long userId) {
		TaskAssignment assignedUserTask = taskAssignmentEJB.findTaskAssignment(id, userId);
		if (assignedUserTask != null) {
			try {
				//String jsonString = serializeTask(assignedUserTask);
				//return jsonString;
				return TaskAssignmentDTOHelper.toTaskAssignmentDTO(assignedUserTask);
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
	public Object setTaskParameter(Class<T> entityType, Long id, Map<String, String> paramMap) {
		logger.info("Received request for task ID = " + id + ", param Map = " + paramMap);
		Object doc = null;
		try {
			if (entityType.getName().contains("qa.qcri.aidr.task.entities.Document")) {
				logger.info("Detected of type Document.class, id = " + id);
				doc = (qa.qcri.aidr.task.entities.Document) documentLocalEJB.getById(id);
			} 
			if (entityType.getName().contains("qa.qcri.aidr.task.entities.TaskAssignment")) {
				logger.info("Detected of type TaskAssignment.class");
				doc = (qa.qcri.aidr.task.entities.TaskAssignment) taskAssignmentEJB.getById(id);
			}
			if (entityType.getName().contains("qa.qcri.aidr.task.entities.TaskAnswer")) {
				logger.info("Detected of type TaskAnswer.class");
				List<qa.qcri.aidr.task.entities.TaskAnswer> docList = taskAnswerEJB.getTaskAnswer(id);
				if (docList != null) 
					doc = docList.get(0);			
			}
			if (entityType.getName().contains("qa.qcri.aidr.task.entities.Users")) {
				logger.info("Detected of type Users.class");
				doc = (qa.qcri.aidr.task.entities.Users) usersLocalEJB.getById(id);
			}
			if (entityType.getName().contains("qa.qcri.aidr.task.entities.DocumentNominalLabel")) {
				logger.info("Detected of type DocumentNominalLabel.class");
				doc = (qa.qcri.aidr.task.entities.DocumentNominalLabel) documentNominalLabelEJB.getById(id);
			}
			if (entityType.getName().contains("qa.qcri.aidr.task.entities.Crisis")) {
				logger.info("Detected of type Crisis.class");
				doc = (qa.qcri.aidr.task.entities.Crisis) crisisEJB.getById(id);
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
			if (entityType.getName().contains("qa.qcri.aidr.task.entities.Document")) {
				logger.info("Detected of type Document.class, id = " + id);
				documentLocalEJB.merge((qa.qcri.aidr.task.entities.Document) doc); 
				logger.info("Merge update successful for task id = " + id);
				//return serializeTask((qa.qcri.aidr.task.entities.Document) doc);
				return DocumentDTOHelper.toDocumentDTO((qa.qcri.aidr.task.entities.Document) doc);
			} 
			if (entityType.getName().contains("qa.qcri.aidr.task.entities.TaskAssignment")) {
				logger.info("Detected of type TaskAssignment.class");
				taskAssignmentEJB.merge((qa.qcri.aidr.task.entities.TaskAssignment) doc);
				logger.info("Merge update successful for task id = " + id);
				//return serializeTask((qa.qcri.aidr.task.entities.TaskAssignment) doc);
				return TaskAssignmentDTOHelper.toTaskAssignmentDTO((qa.qcri.aidr.task.entities.TaskAssignment) doc);
			}
			if (entityType.getName().contains("qa.qcri.aidr.task.entities.TaskAnswer")) {
				logger.info("Detected of type TaskAnswer.class");
				taskAnswerEJB.merge((qa.qcri.aidr.task.entities.TaskAnswer) doc);
				logger.info("Merge update successful for task id = " + id);
				//return serializeTask((qa.qcri.aidr.task.entities.TaskAnswer) doc);
				return TaskAnswerDTOHelper.toTaskAnswerDTO((qa.qcri.aidr.task.entities.TaskAnswer) doc);
			}
			if (entityType.getName().contains("qa.qcri.aidr.task.entities.Users")) {
				logger.info("Detected of type Users.class");
				usersLocalEJB.merge((qa.qcri.aidr.task.entities.Users) doc);
				logger.info("Merge update successful for task id = " + id);
				//return serializeTask((qa.qcri.aidr.task.entities.Users) doc);
				return UsersDTOHelper.toUsersDTO((qa.qcri.aidr.task.entities.Users) doc);
			}
			if (entityType.getName().contains("qa.qcri.aidr.task.entities.DocumentNominalLabel")) {
				logger.info("Detected of type DocumentNominalLabel.class");
				documentNominalLabelEJB.merge((qa.qcri.aidr.task.entities.DocumentNominalLabel) doc);
				logger.info("Merge update successful for task id = " + id);
				//return serializeTask((qa.qcri.aidr.task.entities.DocumentNominalLabel) doc);
				return DocumentNominalLabelDTOHelper.toDocumentNominalLabelDTO((qa.qcri.aidr.task.entities.DocumentNominalLabel) doc);
			}
			if (entityType.getName().contains("qa.qcri.aidr.task.entities.Crisis")) {
				logger.info("Detected of type Crisis.class");
				crisisEJB.merge((qa.qcri.aidr.task.entities.Crisis) doc);
				logger.info("Merge update successful for task id = " + id);
				//return serializeTask((qa.qcri.aidr.task.entities.Crisis) doc);
				return CrisisDTOHelper.toCrisisDTO((qa.qcri.aidr.task.entities.Crisis) doc);
			}
		} catch (Exception e) {
			logger.error("Error in updating entity on DB");
			logger.error(elog.toStringException(e));
		}
		return null;
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

	////////////////////////////////////////////
	// User service related APIs
	////////////////////////////////////////////
	@Override
	public qa.qcri.aidr.task.dto.UsersDTO getUserByName(String name) {
		Users user = usersLocalEJB.findUserByName(name);
		//return serializeTask(user);
		return UsersDTOHelper.toUsersDTO(user);
	}

	@Override
	public qa.qcri.aidr.task.dto.UsersDTO getUserById(Long id) {
		Users user = usersLocalEJB.findUserByID(id);
		//return serializeTask(user);
		return UsersDTOHelper.toUsersDTO(user);
	}

	@Override
	public List<qa.qcri.aidr.task.dto.UsersDTO> getAllUserByName(String name) {
		List<Users> userList = usersLocalEJB.findAllUsersByName(name);
		//return serializeTask(userList);
		return UsersDTOHelper.toUsersDTOList(userList);
	}

	@Override
	public void insertTaskAnswer(qa.qcri.aidr.task.entities.TaskAnswer taskAnswer) {
		try {
			taskAnswerEJB.save(taskAnswer);
		} catch (Exception e) {
			logger.error("Error in saving task answer : " + taskAnswer);
			logger.error(elog.toStringException(e));
		}
	}


	@Override
	public void saveDocumentNominalLabel(qa.qcri.aidr.task.entities.DocumentNominalLabel documentNominalLabel) {
		try {
			documentNominalLabelEJB.save(documentNominalLabel);
		} catch (Exception e) {
			logger.error("Error in saving document nominal label : " + documentNominalLabel);
			logger.error(elog.toStringException(e));
		}
	}

	@Override
	public boolean foundDuplicateDocumentNominalLabel(qa.qcri.aidr.task.entities.DocumentNominalLabel documentNominalLabel) {
		Map<String, Long> attMap = new HashMap<String, Long>();
		attMap.put("documentID", documentNominalLabel.getDocumentID());
		attMap.put("nominalLabelID", documentNominalLabel.getNominalLabelID());

		DocumentNominalLabel obj =  documentNominalLabelEJB.getByCriterionID(Restrictions.allEq(attMap));

		if(obj != null)
			return true;

		return false;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public qa.qcri.aidr.task.dto.DocumentDTO getDocumentById(Long id) {
		try {
			qa.qcri.aidr.task.entities.Document document = documentLocalEJB.getById(id);
			logger.info("Fetched document: " + document);

			return DocumentDTOHelper.toDocumentDTO(document);
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

}
