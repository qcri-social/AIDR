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

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import qa.qcri.aidr.task.ejb.CrisisService;
import qa.qcri.aidr.task.ejb.DocumentNominalLabelService;
import qa.qcri.aidr.task.ejb.DocumentService;
import qa.qcri.aidr.task.ejb.TaskAnswerService;
import qa.qcri.aidr.task.ejb.TaskAssignmentService;
import qa.qcri.aidr.task.ejb.TaskManagerRemote;
import qa.qcri.aidr.task.ejb.UsersService;
import qa.qcri.aidr.task.entities.Document;
import qa.qcri.aidr.task.entities.DocumentNominalLabel;
import qa.qcri.aidr.task.entities.TaskAnswer;
import qa.qcri.aidr.task.entities.TaskAssignment;
import qa.qcri.aidr.task.entities.Users;


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

	public TaskManagerBean()  {
		this.entityType = getClassType();
	}  

	private static final Object monitor = new Object();

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
	public void insertNewTask(T task) {
		if (task != null) {
			((Document) task).setHasHumanLabels(false);
			try {
				documentLocalEJB.save((Document) task);
			} catch (Exception e) {
				System.out.println("[insertNewTask] Error in insertion");
			}
		} else {
			System.err.println("[insertNewTask] Attempting to insert NULL");
		}
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
				System.out.println("[insertNewTask] Error in collection insertion");
			}
		} else {
			System.err.println("[insertNewTask] Attempting to insert NULL");
		}
	}


	@Override
	public int deleteTaskById(Long id) {
		Document document = (Document) documentLocalEJB.getById(id);
		System.out.println("[deleteTaskById] gotById document: " + document);
		try {
			if (document != null) 
				return documentLocalEJB.deleteNoLabelDocument(document);
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("[deleteTaskById] Error in deletion");
		}
		return 0;
	}

	@Override
	public int deleteTask(T task) {
		if (task != null) {
			try {
				System.out.println("[deleteTask] Going for deletion of fetched doc = " + ((Document) task).getDocumentID());
				return documentLocalEJB.deleteNoLabelDocument((Document) task);
			} catch (Exception e) {
				System.out.println("[deleteTask] Error in deletion");
				return 0;
			}
		} else {
			System.err.println("[deleteTask] Attempting to delete NULL");
		}
		return 0;
	}


	@Override
	public int deleteTask(List<T> collection) {
		if (collection != null) {
			try {
				return documentLocalEJB.deleteNoLabelDocument((List<Document>) collection);
			} catch (Exception e) {
				System.out.println("[deleteTask] Error in collection deletion");
				return 0;
			}
		} else {
			System.err.println("[deleteTask] Attempting to delete NULL");
		}
		return 0;
	}

	@Override
	public int deleteUnassignedTask(T task) {
		if (task != null) {
			try {
				return documentLocalEJB.deleteUnassignedDocument((Document) task);
			} catch (Exception e) {
				System.out.println("[deleteTask] Error in deletion");
				return 0;
			}
		} else {
			System.err.println("[deleteUnassignedTask] Attempting to delete NULL");
		}
		return 0;
	}


	@Override
	public int deleteUnassignedTaskCollection(List<T> collection) {
		if (collection != null) {
			try {
				return documentLocalEJB
						.deleteUnassignedDocumentCollection((List<Document>) collection);
			} catch (Exception e) {
				System.out.println("[deleteTask] Error in collection deletion");
				return 0;
			}
		} else {
			System.err.println("[deleteUnassignedTask] Attempting to delete NULL");
		}
		return 0;
	}


	@Override
	public int deleteStaleTasks(String joinType, String joinTable, String joinColumn,
			String sortOrder, String[] orderBy,
			final String maxTaskAge, final String scanInterval) {
		System.out.println("[deleteStaleTasks] received request: " + joinType + ", " + joinTable + ", " 
				+ joinColumn + ", " + maxTaskAge + ", " + scanInterval);
		try {
			int docDeleteCount = documentLocalEJB
					.deleteStaleDocuments(joinType, joinTable, joinColumn, 
							sortOrder, orderBy,
							maxTaskAge, scanInterval);
		} catch (Exception e) {
			System.out.println("[deleteStaleTasks] Error in deletion");
			return 0;
		}
		return 0;
	}


	@Override
	public void updateTask(T task) {
		try {
			// NOTE: can't use update() since serialization+deserialization
			// of persisted entity will throw an exception. Use merge() instead.
			//documentLocalEJB.update((Document) task);
			documentLocalEJB.merge((Document) task); 
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("[updateTask] failed update");
		}
	}


	@Override
	public void updateTask(List<T> collection) {
		try { 
			// NOTE: can't use update() since serialization+deserialization
			// of persisted entity will throw an exception. Use merge() instead.
			//documentLocalEJB.update((List<Document>) collection);
			documentLocalEJB.merge((List<Document>) collection);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("[updateTask] failed collection update");
		}
	}

	/**
	 * Gets a new task from the task repository
	 * @param <entityClass>
	 */
	@Override
	public String getNewTask(Long crisisID) {
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
	public String getNewTask(Long crisisID, Criterion criterion) {
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
			System.out.println("[getNewTask] New task: " + document);
			if (document != null && !isTaskAssigned(document)) {
				System.out.println("[getNewTask] New task: " + document.getDocumentID());
			} else {
				System.out.println("[getNewTask] New task: " + document);
			}
			String jsonString = serializeTask(document);
			return jsonString;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public String getNewTaskCollection(Long crisisID, Integer count, String order, Criterion criterion) {
		System.out.println("[getNewTaskCollection] recied request for crisisID = " + crisisID + ", count = " + count);
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
		List<Document> docList = documentLocalEJB.getByCriteriaWithAliasByOrder(newCriterion, order, orderBy, count, aliasTable, aliasCriterion);
		System.out.println("[getNewTaskCollection] docList = " + docList);
		if (docList != null) {
			System.out.println("[getNewTaskCollection] Fetched size = " + docList.size());
			String jsonString = serializeTask(docList);
			return jsonString;
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
				e.printStackTrace();
				System.err.println("[isTaskAssigned] Error in finding Task");
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
				if ((document != null) && ((Document) document).isHasHumanLabels()) {
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("[isTaskDone] Error in finding document");
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
				e.printStackTrace();
				System.err.println("[isExists] Error in finding document");
			}
		}
		return false;
	}

	@Override
	public String getTaskByCriterion(Long crisisID, Criterion criterion) {
		try {
			if (criterion != null) {
				Criterion newCriterion = Restrictions.conjunction()
						.add(criterion)
						.add(Restrictions.eq("crisisID", crisisID));
				Document document = documentLocalEJB.getByCriteria(newCriterion);
				String jsonString = serializeTask(document);
				return jsonString;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("[getTaskByCriterion] Error in finding task");
		}
		return null;
	}

	@Override
	public String getTaskCollectionByCriterion(Long crisisID, Integer count, Criterion criterion) {
		try {
			if (criterion != null) {
				Criterion newCriterion = Restrictions.conjunction()
						.add(criterion)
						.add(Restrictions.eq("crisisID", crisisID));
				List<Document> docList =  documentLocalEJB.getByCriteriaWithLimit(newCriterion, count);
				String jsonString = serializeTask(docList);
				return jsonString;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("[getTaskCollectionByCriterion] Error in finding task");
		}
		return null;
	}

	@Override
	public void taskUpdate(Criterion criterion, String joinType, String joinTable,
			String joinColumn, String sortOrder, String[] orderBy) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	@Override
	public String getTaskById(Long id) {
		try {
			Document document = documentLocalEJB.getById(id);
			System.out.println("[getTaskById] Fetched document: " + document);
			String jsonString = serializeTask(document);
			return jsonString;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("[getTaskById] Error in finding task");
		}
		return null;
	}

	@Override
	public String getAllTasks() {
		try {
			List<Document> docList =  documentLocalEJB.getAll();
			System.out.println("[getAllTasks] Fetched documents count: " + docList.size());

			String jsonString = serializeTask(docList);
			return jsonString;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("[getAllTasks] Error in finding task");
		}
		return null;
	}

	@Override
	public <E> String serializeTask(E task) {
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = null;
		try {
			if (task != null) jsonString = mapper.writeValueAsString(task);
		} catch (IOException e) {
			System.err.println("JSON serialization exception");
			e.printStackTrace();
		}
		return jsonString;
	}

	/**
	 * Example method call: deSerializeList(jsonString2, new TypeReference<List<Document>>() {}) 
	 */
	@Override
	public <E> E deSerializeList(String jsonString, TypeReference<E> type) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			if (jsonString != null) {
				E docList = mapper.readValue(jsonString, type);
				return docList;
			}	
		} catch (IOException e) {
			System.err.println("JSON deserialization exception");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Example method call: deSerialize(jsonString, Document.class)
	 */
	@Override
	public <E> E deSerialize(String jsonString, Class<E> entityType) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			if (jsonString != null) {
				E entity = mapper.readValue(jsonString, entityType);
				return entity;
			}	
		} catch (IOException e) {
			System.err.println("JSON deserialization exception");
			e.printStackTrace();
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
			throw new Exception("[assignNewTaskToUser] Couldn't undo task assignment");
		}
	}

	@Override
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
			throw new Exception("[undoTaskAssignment] Couldn't undo task assignment");
		}
	}

	@Override
	public void undoTaskAssignment(Long id, Long userId) throws Exception {
		int retVal = taskAssignmentEJB.undoTaskAssignment(id, userId);
		if (retVal <= 0) {
			throw new Exception("[undoTaskAssignment] Couldn't undo task assignment");
		}
	}

	@Override
	public Integer getPendingTaskCountByUser(Long userId) {
		return taskAssignmentEJB.getPendingTaskCount(userId);
	}

	@Override
	public String getAssignedTasksById(Long id) {
		List<TaskAssignment> docList = taskAssignmentEJB.findTaskAssignmentByID(id);
		if (docList != null || !docList.isEmpty()) {
			try {
				String jsonString = serializeTask(docList);
				return jsonString;
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("[getAssignedTasksById] Error in serializing collection");
			}
		}
		return null;
	}

	@Override
	public String getAssignedTaskByUserId(Long id, Long userId) {
		TaskAssignment assignedUserTask = taskAssignmentEJB.findTaskAssignment(id, userId);
		if (assignedUserTask != null) {
			try {
				String jsonString = serializeTask(assignedUserTask);
				return jsonString;
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("[getAssignedTaskByUserId] Error in serializing collection");
			}
		}
		return null;
	}

	/**
	 * Takes as input a map consisting of the setter methods and their corresponding parameters
	 * of the entity to be modified. Returns the modified entity.
	 */
	@Override
	public <T> T setTaskParameter(Class<T> entityType, Long id, Map<String, String> paramMap) {
		Object doc = null;
		try {
			if (entityType.getName().contains("qa.qcri.aidr.task.entities.Document")) {
				System.out.println("Detected of type Document.class, id = " + id);
				doc = (qa.qcri.aidr.task.entities.Document) documentLocalEJB.getById(id);
			} 
			if (entityType.getName().contains("qa.qcri.aidr.task.entities.TaskAssignment")) {
				System.out.println("Detected of type TaskAssignment.class");
				doc = (qa.qcri.aidr.task.entities.TaskAssignment) taskAssignmentEJB.getById(id);
			}
			if (entityType.getName().contains("qa.qcri.aidr.task.entities.TaskAnswer")) {
				System.out.println("Detected of type TaskAnswer.class");
				List<qa.qcri.aidr.task.entities.TaskAnswer> docList = taskAnswerEJB.getTaskAnswer(id);
				if (docList != null) 
					doc = docList.get(0);			
			}
			if (entityType.getName().contains("qa.qcri.aidr.task.entities.Users")) {
				System.out.println("Detected of type Users.class");
				doc = (qa.qcri.aidr.task.entities.Users) usersLocalEJB.getById(id);
			}
			if (entityType.getName().contains("qa.qcri.aidr.task.entities.DocumentNominalLabel")) {
				System.out.println("Detected of type DocumentNominalLabel.class");
				doc = (qa.qcri.aidr.task.entities.DocumentNominalLabel) documentNominalLabelEJB.getById(id);
			}
			if (entityType.getName().contains("qa.qcri.aidr.task.entities.Crisis")) {
				System.out.println("Detected of type Crisis.class");
				doc = (qa.qcri.aidr.task.entities.Crisis) crisisEJB.getById(id);
			}
		} catch (Exception e) {
			System.err.println("Error in detecting Class Type");
			e.printStackTrace();
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
					System.out.println("discovered method: " + methods[i].getName());
				}
			} catch (Exception e) {
				System.err.println("[setTaskParameter] Error in instantiating method class");
				e.printStackTrace();
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
						System.out.println(methods[pointer].getName() + ": discovered parameter types: " + paramTypes[j].getName());
					}
					// Convert parameter to paramType
					if (paramTypes[0].getName().toLowerCase().contains("long")) {
						methods[pointer].invoke(doc, Long.parseLong(paramMap.get(name)));
						System.out.println("Invoking with Long parameter type");
					}
					if (paramTypes[0].getName().toLowerCase().contains("int")) {
						methods[pointer].invoke(doc, Integer.parseInt(paramMap.get(name)));
						System.out.println("Invoking with Integer parameter type");
					}
					if (paramTypes[0].getName().toLowerCase().contains("boolean")) {
						methods[pointer].invoke(doc, Boolean.parseBoolean(paramMap.get(name)));
						System.out.println("Invoking with Boolean parameter type");
					}
					if (paramTypes[0].getName().contains("String")) {
						methods[pointer].invoke(doc, paramMap.get(name));
						System.out.println("Invoking with String parameter type");
					}
					System.out.println("[setTaskParameter] Invoked method: " + methods[pointer].getName() + " with parameter: " + paramMap.get(name));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.err.println("[setTaskParameter] Error in invoking method via reflection: ");
					e.printStackTrace();
					return null;
				} 
			}	
		}

		try {
			if (entityType.getName().contains("qa.qcri.aidr.task.entities.Document")) {
				System.out.println("Detected of type Document.class, id = " + id);
				documentLocalEJB.update((qa.qcri.aidr.task.entities.Document) doc); 
			} 
			if (entityType.getName().contains("qa.qcri.aidr.task.entities.TaskAssignment")) {
				System.out.println("Detected of type TaskAssignment.class");
				taskAssignmentEJB.update((qa.qcri.aidr.task.entities.TaskAssignment) doc);
			}
			if (entityType.getName().contains("qa.qcri.aidr.task.entities.TaskAnswer")) {
				System.out.println("Detected of type TaskAnswer.class");
				taskAnswerEJB.update((qa.qcri.aidr.task.entities.TaskAnswer) doc);		
			}
			if (entityType.getName().contains("qa.qcri.aidr.task.entities.Users")) {
				System.out.println("Detected of type Users.class");
				usersLocalEJB.update((qa.qcri.aidr.task.entities.Users) doc);
			}
			if (entityType.getName().contains("qa.qcri.aidr.task.entities.DocumentNominalLabel")) {
				System.out.println("Detected of type DocumentNominalLabel.class");
				documentNominalLabelEJB.update((qa.qcri.aidr.task.entities.DocumentNominalLabel) doc);
			}
			if (entityType.getName().contains("qa.qcri.aidr.task.entities.Crisis")) {
				System.out.println("Detected of type Crisis.class");
				crisisEJB.update((qa.qcri.aidr.task.entities.Crisis) doc);
			}
		} catch (Exception e) {
			System.err.println("Error in updating entity on DB");
			e.printStackTrace();
			return null;
		}
		return (T) doc;
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
	public String getUserByName(String name) {
		Users user = usersLocalEJB.findUserByName(name);
		return serializeTask(user);
	}

	@Override
	public String getUserById(Long id) {
		Users user = usersLocalEJB.findUserByID(id);
		return serializeTask(user);
	}

	@Override
	public String getAllUserByName(String name) {
		List<Users> userList = usersLocalEJB.findAllUsersByName(name);
		return serializeTask(userList);
	}

	@Override
	public void insertTaskAnswer(TaskAnswer taskAnswer) {
		try {
			taskAnswerEJB.save(taskAnswer);
		} catch (Exception e) {
			System.err.println("[insertTaskAnswer] Error in saving task answer : " + taskAnswer);
			e.printStackTrace();
		}
	}


	@Override
	public void saveDocumentNominalLabel(DocumentNominalLabel documentNominalLabel) {
		try {
			documentNominalLabelEJB.save(documentNominalLabel);
		} catch (Exception e) {
			System.err.println("[saveDocumentNominalLabel] Error in saving document nominal label : " + documentNominalLabel);
			e.printStackTrace();
		}
	}

	@Override
	public boolean foundDuplicateDocumentNominalLabel(DocumentNominalLabel documentNominalLabel) {
		Map<String, Long> attMap = new HashMap<String, Long>();
		attMap.put("documentID", documentNominalLabel.getDocumentID());
		attMap.put("nominalLabelID", documentNominalLabel.getNominalLabelID());

		DocumentNominalLabel obj =  documentNominalLabelEJB.getByCriterionID(Restrictions.allEq(attMap));

		if(obj != null)
			return true;

		return false;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
