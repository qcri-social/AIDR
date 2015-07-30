package qa.qcri.aidr.task.ejb.bean;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.CrisisDTO;
import qa.qcri.aidr.dbmanager.dto.DocumentDTO;
import qa.qcri.aidr.dbmanager.dto.DocumentNominalLabelDTO;
import qa.qcri.aidr.dbmanager.dto.HumanLabeledDocumentDTO;
import qa.qcri.aidr.dbmanager.dto.NominalLabelDTO;
import qa.qcri.aidr.dbmanager.dto.TaskAnswerDTO;
import qa.qcri.aidr.dbmanager.dto.TaskAssignmentDTO;
import qa.qcri.aidr.dbmanager.dto.UsersDTO;
import qa.qcri.aidr.dbmanager.entities.misc.Crisis;
import qa.qcri.aidr.dbmanager.entities.misc.Users;
import qa.qcri.aidr.dbmanager.entities.task.Document;
import qa.qcri.aidr.dbmanager.entities.task.DocumentNominalLabel;
import qa.qcri.aidr.dbmanager.entities.task.TaskAnswer;
import qa.qcri.aidr.dbmanager.entities.task.TaskAssignment;
import qa.qcri.aidr.task.common.TrainingDataFetchType;
import qa.qcri.aidr.task.ejb.TaskManagerRemote;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class implements the TaskManagerRemote interface, providing the business logic for operations 
 * on the document, document_nominal_label, task_answer and task_assignment table - logically grouped as the the 'task related operations'.
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

	@EJB
	private qa.qcri.aidr.dbmanager.ejb.remote.facade.NominalLabelResourceFacade remoteNominalLabelEJB;

	protected static Logger logger = Logger.getLogger(TaskManagerBean.class);

	private static Object lockObject = new Object();
	private static Integer inCS = 0;

	private Class<T> entityType;
	
	public TaskManagerBean()  {
		this.entityType = getClassType();
		//lock = new ReentrantLock();
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
		}
		return -1;
	}

	@Override
	public Long saveNewTask(T task, Long crisisID) {
		if (task == null) {
			logger.error("Attempting to insert NULL");
			return -1L;
		}
		try {
			DocumentDTO doc = (DocumentDTO) task;
			CrisisDTO crisisDTO = remoteCrisisEJB.findCrisisByID(crisisID);
			doc.setCrisisDTO(crisisDTO);
			doc.setHasHumanLabels(false);
			DocumentDTO savedDoc = remoteDocumentEJB.addDocument(doc);
			//System.out.println("Saved to DB document: " + savedDoc.getDocumentID() + ", for crisis = " + savedDoc.getCrisisDTO().getCode());
			//logger.info("Saved to DB document: " + savedDoc.getDocumentID() + ", for crisis = " + savedDoc.getCrisisDTO().getCode());
			return savedDoc.getDocumentID();
		} catch (Exception e) {
			logger.error("Error in insertion");
			e.printStackTrace();
		}
		return -1L;
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
			}
		} else {
			logger.warn("Attempting to insert NULL");
		}
	}

	@Override
	public List<Long> saveNewTasks(List<T> collection, Long crisisID) {
		List<Long> newList = new ArrayList<Long>();
		if (collection != null) {
			try {
				for (T doc: collection) {
					CrisisDTO crisisDTO = remoteCrisisEJB.findCrisisByID(crisisID);
					((DocumentDTO) doc).setCrisisDTO(crisisDTO);
					((DocumentDTO) doc).setHasHumanLabels(false);
					DocumentDTO savedDoc = remoteDocumentEJB.addDocument((DocumentDTO) doc);
					if (savedDoc != null) { 
						newList.add(savedDoc.getDocumentID());
					}
				}
			} catch (Exception e) {
				logger.error("Error in collection insertion");
			}
		} else {
			logger.warn("Attempting to insert NULL");
		}
		return newList;
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
			List<Long> idList = new ArrayList<Long>();
			for (DocumentDTO dto: (List<DocumentDTO>) collection) {
				idList.add(dto.getDocumentID());
			}
			try {
				return remoteDocumentEJB.deleteUnassignedDocumentCollection(idList);
			} catch (Exception e) {
				logger.error("Error in collection deletion");
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
			return 0;
		}
	}

	@Override
	public int truncateLabelingTaskBufferForCrisis(final long crisisID, final int maxLength, final int ERROR_MARGIN) {
		List<Document> docList = null;
		try {
			String aliasTable = "taskAssignments";
			String order = "ASC";
			String aliasTableKey = "taskAssignments.id.documentId";
			String[] orderBy = {"valueAsTrainingSample", "documentId"};
			Criterion criterion = Restrictions.conjunction()
					.add(Restrictions.eq("crisis.crisisId",crisisID))
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
			return 0;
		}

		// Next trim the document table for the given crisisID to the 
		// Config.LABELING_TASK_BUFFER_MAX_LENGTH size
		if (docList != null) {
			int docsToDelete = docList.size() - maxLength;
			if (docsToDelete > ERROR_MARGIN) {		// if less than this, then skip delete
				List<Long> documentIDList = new ArrayList<Long>();
				for (int i = 0;i < docsToDelete;i++) {
					Document d = docList.get(i);
					documentIDList.add(d.getDocumentId());
					logger.debug("To delete document: {" + d.getCrisis().getCrisisId() + ", " + d.getDocumentId() + ", " + d.isHasHumanLabels() + "}");
				}
				try {
					// Delete the lowest confidence documents from document table
					int deleteCount = remoteDocumentEJB.deleteUnassignedDocumentCollection(documentIDList);
					logger.info("Number of documents actually deleted = " + deleteCount);
					return deleteCount;
				} catch (Exception e) {
					logger.error("Exception when attempting to batch delete for trimming the document table");
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
			logger.error("Error in fetching new task.", e);
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
						.add(Restrictions.eq("crisis.crisisId",crisisID))
						.add(Restrictions.eq("hasHumanLabels",false));
			} else {
				newCriterion = Restrictions.conjunction()
						.add(Restrictions.eq("crisis.crisisId",crisisID))
						.add(Restrictions.eq("hasHumanLabels",false));
			}

			Document document = remoteDocumentEJB.getByCriteria(newCriterion);
			logger.debug("New task: " + document);
			if (document != null && !isTaskAssigned(document)) {
				logger.info("New task: " + document.getDocumentId());
				return new DocumentDTO(document);
			} else {
				logger.info("[getNewTask] New task: " + document);
			}
		} catch (Exception e) {
			logger.error("Error in getting new Task for crisisID: " + crisisID);
		}
		return null;
	}


	@Override
	public List<DocumentDTO> getNewTaskCollection(Long crisisID, Integer count, String order, Criterion criterion) {
		logger.info("Received request for crisisID = " + crisisID + ", count = " + count);
		String aliasTable = "taskAssignments";
		String aliasTableKey = "taskAssignments.id.documentId";
		String[] orderBy = {"valueAsTrainingSample","documentId"};
		Criterion newCriterion = criterion;
		if (criterion != null) {
			newCriterion = Restrictions.conjunction()
					.add(criterion)
					.add(Restrictions.eq("crisis.crisisId",crisisID))
					.add(Restrictions.eq("hasHumanLabels",false));
		} else {
			newCriterion = Restrictions.conjunction()
					.add(Restrictions.eq("crisis.crisisId",crisisID))
					.add(Restrictions.eq("hasHumanLabels",false));
		}
		Criterion aliasCriterion =  (Restrictions.isNull(aliasTableKey));
		try {
			List<Document> docList = remoteDocumentEJB.getByCriteriaWithAliasByOrder(newCriterion, order, orderBy, count, aliasTable, aliasCriterion);
			//System.out.println("[getNewTaskCollection] docList = " + docList);
			if (docList != null) {
				System.out.println("[getNewTaskCollection] Fetched size = " + docList.size());
				return createDocumentDTOList(docList);
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error("Error in getting new Task collection for crisisID: " + crisisID);
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
						.add(Restrictions.eq("crisis.crisisId", crisisID));
				Document document = remoteDocumentEJB.getByCriteria(newCriterion);

				return new DocumentDTO(document);
			}
		} catch (Exception e) {
			logger.error("Error in finding task");
		}
		return null;
	}

	@Override
	public List<DocumentDTO> getTaskCollectionByCriterion(Long crisisID, Integer count, Criterion criterion) {
		try {
			if (criterion != null) {
				Criterion newCriterion = Restrictions.conjunction()
						.add(criterion)
						.add(Restrictions.eq("crisis.crisisId", crisisID));
				List<Document> docList =  remoteDocumentEJB.getByCriteriaWithLimit(newCriterion, count);
				return createDocumentDTOList(docList);
			}
		} catch (Exception e) {
			logger.error("Error in finding task");
		}
		return null;
	}

	@Override
	public List<DocumentDTO> getNominalLabelDocumentCollection(Long nominalLabelID) {
		logger.info("Received fetch document request with nominal label = " + nominalLabelID);
		try {
			List<DocumentDTO> docList = remoteDocumentEJB.getDocumentCollectionWithNominalLabelData(nominalLabelID);
			logger.debug("docList = " + docList);
			if (docList != null) {
				logger.info("Fetched size = " + docList.size());
				return docList;
			} 
		} catch (Exception e) {
			logger.error("Error in getting new document collection for nominal Label ID: " + nominalLabelID);
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
		}
		return null;
	}

	////////////////////////////////////////////////////////////
	// Trainer API Task Assignment related APIs
	////////////////////////////////////////////////////////////
	@Override
	public List<DocumentDTO> getDocumentsForTagging(final Long crisisID, int count, final String userName, final int remainingCount, final TrainingDataFetchType fetchType) {
		UsersDTO users = null;
		try {
			users = remoteUsersEJB.getUserByName(userName);
		} catch (Exception e) {
			System.err.println("[getDocumentsForTagging] Exception in finding user userName = " + userName + ". Aborting...");
			logger.error("Exception", e);
			e.printStackTrace();
			return null;
		}
		if (users != null) {
			int fetchedSize = 0;
			Integer fetchCount = count;
			if (TrainingDataFetchType.BATCH_FETCH.equals(fetchType)) {
				fetchCount = null;		// fetch all available tasks
			}
			//while (!lock.tryLock()) {}// spin-loop wait
			synchronized(lockObject) {
				++inCS;
				try {
					List<DocumentDTO> dtoList = getNewTaskCollection(crisisID, fetchCount, "DESC", null);
					System.out.println("[getDocumentsForTagging] inCS = " + inCS + ". For crisisID = " + crisisID + ", user = " + userName 
							+ ", documents available: " + (dtoList != null ? dtoList.size() : "empty list"));
					if (dtoList != null) {
						fetchedSize = dtoList.size();
					}
					int availableRequestSize = fetchedSize - remainingCount;
					System.out.println("available = " + availableRequestSize + ", total = " + fetchedSize + ", remainingCount = " + remainingCount);
					if (availableRequestSize > 0) {
						count = Math.min(count, availableRequestSize);
						if (!dtoList.isEmpty() && count > 0) {
							System.out.println("[getDocumentsForTagging] Going to insert task list of size = " + count + ", for userID: " + users.getUserID());
							List<DocumentDTO> assignList = new ArrayList<DocumentDTO>();
							assignList.addAll(dtoList.subList(0, count));
							assignNewTaskToUser(assignList, users.getUserID());
							//lock.unlock();
							--inCS;
							return assignList;
						}
					} else {
						//lock.unlock();
						--inCS;
						return null;
					}
				} catch (Exception e) {
					logger.error("Exception", e);
					e.printStackTrace();
					//lock.unlock();
					return null;
				}
			}
		} else {
			System.err.println("[getDocumentsForTagging] No user found with userName = " + userName + ". Aborting...");
		}
		return null;
	}



	@Override
	public void assignNewTaskToUser(Long id, Long userId) throws Exception {
		try {
			int retVal = remoteTaskAssignmentEJB.insertOneTaskAssignment(id, userId);
			if (retVal <= 0) {
				logger.error("unable to undo task assignment");
				throw new Exception("[assignNewTaskToUser] Couldn't do task assignment");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void assignNewTaskToUser(List<DocumentDTO> collection, Long userId) throws Exception {
		int retVal = remoteTaskAssignmentEJB.insertTaskAssignment(collection, userId);
		if (retVal <= 0) {
			throw new Exception("[assignNewTaskToUser] Couldn't do task assignment");
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
			}
		}
		return null;
	}

	/**
	 * Takes as input a map consisting of the setter methods and their corresponding parameters
	 * of the entity to be modified. Returns the modified entity.
	 */
	@Override
	public <E> Object setTaskParameter(Class<E> entityType, Long id, Map<String, String> paramMap) {
		//logger.info("Received request for task ID = " + id + ", param Map = " + paramMap);
		Object doc = null;
		try {
			if (entityType.equals(qa.qcri.aidr.dbmanager.entities.task.Document.class)) {
				//logger.info("Detected of type Document.class, id = " + id);
				doc = (Document) remoteDocumentEJB.getById(id);
			} 
			if (entityType.equals(qa.qcri.aidr.dbmanager.entities.task.TaskAssignment.class)) {
				//logger.info("Detected of type TaskAssignment.class");
				doc = (TaskAssignment) remoteTaskAssignmentEJB.getById(id);
			}
			if (entityType.equals(qa.qcri.aidr.dbmanager.entities.task.TaskAnswer.class)) {
				//logger.info("Detected of type TaskAnswer.class");
				List<TaskAnswerDTO> docList = remoteTaskAnswerEJB.getTaskAnswer(id);
				if (docList != null) 
					doc = docList.get(0).toEntity();			
			}
			if (entityType.equals(qa.qcri.aidr.dbmanager.entities.misc.Users.class)) {
				//logger.info("Detected of type Users.class");
				doc = (Users) remoteUsersEJB.getById(id);
			}
			if (entityType.equals(qa.qcri.aidr.dbmanager.entities.task.DocumentNominalLabel.class)) {
				//logger.info("Detected of type DocumentNominalLabel.class");
				doc = (DocumentNominalLabel) remoteDocumentNominalLabelEJB.getById(id);
			}
			if (entityType.equals(qa.qcri.aidr.dbmanager.entities.misc.Crisis.class)) {
				//logger.info("Detected of type Crisis.class");
				doc = (Crisis) remoteCrisisEJB.getById(id);
			}
			//logger.info("Fetched task of type: " + doc.getClass());
			//logger.info("received map: " + paramMap);
		} catch (Exception e) {
			logger.error("Error in detecting Class Type");
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
					//logger.info("discovered method: " + methods[i].getName());
				}
			} catch (Exception e) {
				logger.error("Error in instantiating method class");
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
						//logger.info(methods[pointer].getName() + ": discovered parameter types: " + paramTypes[j].getName());
					}
					// Convert parameter to paramType
					if (paramTypes[0].getName().toLowerCase().contains("long")) {
						methods[pointer].invoke(doc, Long.parseLong(paramMap.get(name)));
						//logger.info("Invoking with Long parameter type");
					}
					if (paramTypes[0].getName().toLowerCase().contains("int")) {
						methods[pointer].invoke(doc, Integer.parseInt(paramMap.get(name)));
						//logger.info("Invoking with Integer parameter type");
					}
					if (paramTypes[0].getName().toLowerCase().equals("boolean")) {
						methods[pointer].invoke(doc, Boolean.parseBoolean(paramMap.get(name)));
						//logger.info("Invoking with Boolean parameter type");
					}
					if (paramTypes[0].getName().equals("String")) {
						methods[pointer].invoke(doc, paramMap.get(name));
						//logger.info("Invoking with String parameter type");
					}
					//logger.info("[setTaskParameter] Invoked method: " + methods[pointer].getName() + " with parameter: " + paramMap.get(name));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error("Error in invoking method via reflection: ");
					return null;
				} 
			}	
		}

		try {
			logger.info("Will attempt to merge update with document ID = " + id);
			if (entityType.equals(qa.qcri.aidr.dbmanager.entities.task.Document.class)) {
				//logger.info("Detected of type Document.class, id = " + id);
				remoteDocumentEJB.merge((Document) doc); 
				logger.info("Merge update successful for task id = " + id);
				//return serializeTask((qa.qcri.aidr.task.entities.Document) doc);
				return new DocumentDTO((Document) doc);
			} 
			if (entityType.equals(qa.qcri.aidr.dbmanager.entities.task.TaskAssignment.class)) {
				//logger.info("Detected of type TaskAssignment.class");
				remoteTaskAssignmentEJB.merge((TaskAssignment) doc);
				logger.info("Merge update successful for task id = " + id);
				//return serializeTask((qa.qcri.aidr.task.entities.TaskAssignment) doc);
				return new TaskAssignmentDTO((TaskAssignment) doc);
			}
			if (entityType.equals(qa.qcri.aidr.dbmanager.entities.task.TaskAnswer.class)) {
				//logger.info("Detected of type TaskAnswer.class");
				remoteTaskAnswerEJB.merge((TaskAnswer) doc);
				logger.info("Merge update successful for task id = " + id);
				//return serializeTask((qa.qcri.aidr.task.entities.TaskAnswer) doc);
				return new TaskAnswerDTO((TaskAnswer) doc);
			}
			if (entityType.equals(qa.qcri.aidr.dbmanager.entities.misc.Users.class)) {
				//logger.info("Detected of type Users.class");
				remoteUsersEJB.merge((Users) doc);
				logger.info("Merge update successful for task id = " + id);
				//return serializeTask((qa.qcri.aidr.task.entities.Users) doc);
				return new UsersDTO((Users) doc);
			}
			if (entityType.equals(qa.qcri.aidr.dbmanager.entities.task.DocumentNominalLabel.class)) {
				//logger.info("Detected of type DocumentNominalLabel.class");
				remoteDocumentNominalLabelEJB.merge((DocumentNominalLabel) doc);
				logger.info("Merge update successful for task id = " + id);
				//return serializeTask((qa.qcri.aidr.task.entities.DocumentNominalLabel) doc);
				return new DocumentNominalLabelDTO((DocumentNominalLabel) doc);
			}
			if (entityType.equals(qa.qcri.aidr.dbmanager.entities.misc.Crisis.class)) {
				//logger.info("Detected of type Crisis.class");
				remoteCrisisEJB.merge((Crisis) doc);
				logger.info("Merge update successful for task id = " + id);
				//return serializeTask((qa.qcri.aidr.task.entities.Crisis) doc);
				return new CrisisDTO((Crisis) doc);
			}
		} catch (Exception e) {
			logger.error("Error in updating entity on DB");
		}
		return null;
	}

	@Override
	public void insertTaskAnswer(TaskAnswerDTO taskAnswer) {
		try {
			TaskAnswerDTO t = remoteTaskAnswerEJB.insertTaskAnswer(taskAnswer);
			if (t != null) {
				System.out.println("Successfully inserted taskAnswer: taskID = " + t.getTaskID() + ", documentID = " + t.getDocumentID() + "answer = " + t.getAnswer() + ", from user = " + t.getUserID());
			}
		} catch (Exception e) {
			logger.error("Error in saving task answer : " + taskAnswer.getDocumentID() + ", " + taskAnswer.getAnswer() + ", " + taskAnswer.getUserID());
		}
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


	////////////////////////??////////////////////
	// DocumentNominalLabel service related APIs
	//////////////////////////??//////////////////

	@Override
	public void saveDocumentNominalLabel(DocumentNominalLabelDTO documentNominalLabel) {
		try {
			DocumentNominalLabelDTO dto = remoteDocumentNominalLabelEJB.addDocument(documentNominalLabel);
			System.out.println("Saved to DB document nominal label: " + dto.getIdDTO().getDocumentId() + ", with nominal label = " + dto.getIdDTO().getNominalLabelId());
			logger.info("Saved to DB document nominal label: " + dto.getIdDTO().getDocumentId() + ", with nominal label = " + dto.getIdDTO().getNominalLabelId());
		} catch (Exception e) {
			logger.error("Error in saving document nominal label : " + documentNominalLabel, e);
		}
	}

	@Override
	public boolean foundDuplicateDocumentNominalLabel(DocumentNominalLabelDTO documentNominalLabel) {
		Map<String, Long> attMap = new HashMap<String, Long>();
		try {
			attMap.put("id.documentId", documentNominalLabel.getIdDTO().getDocumentId());
			attMap.put("id.nominalLabelId", documentNominalLabel.getIdDTO().getNominalLabelId());
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
		}
		return null;
	}

	@Override
	public String pingRemoteEJB() {
		StringBuilder sb = new StringBuilder("{\"status\": \"RUNNING\"}");
		return sb.toString();
	}

	@Override
	public List<HumanLabeledDocumentDTO> getHumanLabeledDocumentsByCrisisID(Long crisisID, Integer count) throws Exception {
		if (null == crisisID) {
			logger.error("crisisID can't be null");
			throw new PropertyNotSetException("crisisID can't be null");
		}
		logger.info("Received request for crisisID = " + crisisID + ", count = " + count);
		List <HumanLabeledDocumentDTO> labeledDocList = null;

		String aliasTable = "documentNominalLabels";
		String aliasTableKeyField = "documentNominalLabels.id.nominalLabelId";
		String[] orderBy = {"documentId"};

		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisis.crisisId",crisisID))
				.add(Restrictions.eq("hasHumanLabels", true));
		Criterion aliasCriterion =  Restrictions.isNotNull(aliasTableKeyField);
		try {
			List<Document> docList = remoteDocumentEJB.getByCriteriaWithInnerJoinByOrder(criterion, "DESC", orderBy, count, aliasTable, aliasCriterion);
			if (docList != null) {
				System.out.println("Fetched size = " + docList.size());
				Set<Document> docSet = new TreeSet<Document>(new DocumentComparator());
				docSet.addAll(docList);
				System.out.println("Sizeof document collection set = " + docSet.size());

				// First get all labels for the fetched documents
				labeledDocList = new ArrayList<HumanLabeledDocumentDTO>();
				for (Document doc: docSet) {
					List<DocumentNominalLabelDTO> labeledDataDTO = remoteDocumentNominalLabelEJB.findLabeledDocumentListByID(doc.getDocumentId());
					if (labeledDataDTO != null) {
						for (DocumentNominalLabelDTO dto: labeledDataDTO) {
							NominalLabelDTO nominalLabel = remoteNominalLabelEJB.getNominalLabelWithAllFieldsByID(dto.getIdDTO().getNominalLabelId());
							if (nominalLabel != null) {
								nominalLabel.setDocumentNominalLabelsDTO(null);
								nominalLabel.setModelNominalLabelsDTO(null);
							}
							dto.setNominalLabelDTO(nominalLabel);
							dto.setDocumentDTO(null);
						}
						labeledDocList.add(new HumanLabeledDocumentDTO(new DocumentDTO(doc), labeledDataDTO));
					}
				}
				return labeledDocList;
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error("Error in getting human labeled documents collection for crisisID: " + crisisID);
			logger.error("exception", e);
		}
		return null;
	}

	@Override
	public List<HumanLabeledDocumentDTO> getHumanLabeledDocumentsByCrisisCode(String crisisCode, Integer count) throws Exception{
		if (null == crisisCode) {
			logger.error("crisis code can't be null");
			throw new PropertyNotSetException("crisis code can't be null");
		}
		CrisisDTO crisis = remoteCrisisEJB.getCrisisByCode(crisisCode);
		if (null == crisis) {
			logger.error("crisis code is invalid");
			throw new PropertyNotSetException("crisis code is invalid");
		}
		logger.info("Received request for crisis code = " + crisisCode + ", count = " + count);
		return this.getHumanLabeledDocumentsByCrisisID(crisis.getCrisisID(), count);
	}

	@Override
	public List<HumanLabeledDocumentDTO> getHumanLabeledDocumentsByCrisisIDUserID(Long crisisID, Long userID, Integer count) throws Exception {
		if (null == crisisID || null == userID) {
			logger.error("crisis ID or userID can't be null");
			throw new PropertyNotSetException("crisis ID or userID can't be null");
		}
		logger.info("Received request for crisisID = " + crisisID + ", userID = " + userID + ", count = " + count);
		List <HumanLabeledDocumentDTO> labeledDocList = null;

		String aliasTable = "documentNominalLabels";
		String aliasTableKeyField = "documentNominalLabels.id.nominalLabelId";
		String[] orderBy = {"documentId"};

		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisis.crisisId",crisisID))
				.add(Restrictions.eq("hasHumanLabels", true));
		Criterion aliasCriterion =  Restrictions.conjunction()
				.add(Restrictions.isNotNull(aliasTableKeyField))
				.add(Restrictions.eq("documentNominalLabels.id.userId", userID));
		try {
			List<Document> docList = remoteDocumentEJB.getByCriteriaWithInnerJoinByOrder(criterion, "DESC", orderBy, count, aliasTable, aliasCriterion);
			System.out.println("[getHumanLabeledDocumentsByCrisisIDUserID] docList = " + docList);
			if (docList != null) {
				System.out.println("Fetched size = " + docList.size());
				Set<Document> docSet = new TreeSet<Document>(new DocumentComparator());
				docSet.addAll(docList);
				System.out.println("Sizeof document collection set = " + docSet.size());

				// First get all labels for the fetched documents
				labeledDocList = new ArrayList<HumanLabeledDocumentDTO>();
				for (Document doc: docSet) {
					List<DocumentNominalLabelDTO> labeledDataDTO = remoteDocumentNominalLabelEJB.findLabeledDocumentListByID(doc.getDocumentId());
					if (labeledDataDTO != null) {
						for (DocumentNominalLabelDTO dto: labeledDataDTO) {
							NominalLabelDTO nominalLabel = remoteNominalLabelEJB.getNominalLabelWithAllFieldsByID(dto.getIdDTO().getNominalLabelId());
							if (nominalLabel != null) {
								nominalLabel.setDocumentNominalLabelsDTO(null);
								nominalLabel.setModelNominalLabelsDTO(null);
							}
							dto.setNominalLabelDTO(nominalLabel);
							dto.setDocumentDTO(null);
						}
						labeledDocList.add(new HumanLabeledDocumentDTO(new DocumentDTO(doc), labeledDataDTO));
					}
				}
				return labeledDocList;
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error("Error in getting human labeled documents collection for crisisID: " + crisisID);
			logger.error("exception", e);
		}
		return null;

	}

	@Override
	public List<HumanLabeledDocumentDTO> getHumanLabeledDocumentsByCrisisIDUserName(Long crisisID, String userName, Integer count) throws Exception {
		if (null == crisisID || null == userName) {
			logger.error("crisis ID or userName can't be null");
			throw new PropertyNotSetException("crisis ID or userName can't be null");
		}
		UsersDTO user = remoteUsersEJB.getUserByName(userName);
		if (null == user) {
			logger.error("User name is invalid");
			throw new PropertyNotSetException("User name is invalid");
		}
		logger.info("Received request for crisisID = " + crisisID + ", userName = " + userName + ", count = " + count);
		return this.getHumanLabeledDocumentsByCrisisIDUserID(crisisID, user.getUserID(), count);
	}

	@Override
	public boolean deleteTask(Long crisisID, Long userID) {

		boolean success;
		try {
			List<DocumentDTO> documentDTOs = remoteDocumentEJB.findDocumentsByCrisisID(crisisID);
			
			if(documentDTOs != null && userID != null) {
				for(DocumentDTO documentDTO : documentDTOs) {
					remoteTaskAssignmentEJB.undoTaskAssignment(documentDTO.getDocumentID(), userID);
					remoteTaskAnswerEJB.deleteTaskAnswer(documentDTO.getDocumentID());
				}
			}
			remoteDocumentEJB.deleteDocuments(documentDTOs);
			success = true;
			
			logger.info("Successful deletion for task data.");
		} catch (Exception e) {
			logger.error("Unable to delete task for crisidID : " + crisisID + " and userID : " + userID);
			success = false;
		}
		
		return success;
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

	private class DocumentComparator implements Comparator<Document> {

		@Override
		public int compare(Document d1, Document d2) {
			return d1.getDocumentId().compareTo(d2.getDocumentId());
		}

	}
}
