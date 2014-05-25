package qa.qcri.aidr.task.ejb.bean;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import qa.qcri.aidr.task.ejb.DocumentService;
import qa.qcri.aidr.task.ejb.TaskAssignmentService;
import qa.qcri.aidr.task.ejb.TaskManagerRemote;
import qa.qcri.aidr.task.entities.Document;
import qa.qcri.aidr.task.entities.TaskAssignment;

@Stateless
public class TaskManagerBean<T, I> implements TaskManagerRemote<T, Serializable> {

	@EJB
	private DocumentService documentLocalEJB;

	@EJB
	private TaskAssignmentService taskAssignmentEJB;

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
	public String getNewTaskCollection(Long crisisID, Criterion criterion) {
		String aliasTable = "taskAssignment";
		String aliasTableKey = "taskAssignment.documentID";
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
		List<Document> docList = documentLocalEJB.getByCriteriaWithAliasByOrder(newCriterion, "ASC", null, null, aliasTable, aliasCriterion);
		String jsonString = serializeTask(docList);
		return jsonString;
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
	public String getTaskCollectionByCriterion(Long crisisID, Criterion criterion) {
		try {
			if (criterion != null) {
				Criterion newCriterion = Restrictions.conjunction()
						.add(criterion)
						.add(Restrictions.eq("crisisID", crisisID));
				List<Document> docList =  documentLocalEJB.getAllByCriteria(newCriterion);
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

			ObjectMapper mapper = new ObjectMapper();
			String jsonString = serializeTask(docList);
			return jsonString;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("[getAllTasks] Error in finding task");
		}
		return null;
	}

	private <E> String serializeTask(E task) {
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
}
