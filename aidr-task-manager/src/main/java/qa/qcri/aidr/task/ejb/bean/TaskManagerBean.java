package qa.qcri.aidr.task.ejb.bean;

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

import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import qa.qcri.aidr.task.ejb.DocumentService;
import qa.qcri.aidr.task.ejb.TaskManagerRemote;
import qa.qcri.aidr.task.entities.Document;

@Stateless
public class TaskManagerBean<T, Serializable> implements TaskManagerRemote<T, Serializable> {

	@EJB
	private DocumentService documentLocalEJB;

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
		// TODO Auto-generated method stub
		((Document) task).setHasHumanLabels(false);
		documentLocalEJB.save((Document) task);
	}


	@Override
	public void insertNewTask(List<T> collection) {
		for (T doc: collection) {
			((Document) doc).setHasHumanLabels(false);
		}
		documentLocalEJB.save((List<Document>) collection);
	}


	@Override
	public int deleteTask(T task) {
		return documentLocalEJB.deleteDocument((Document) task);
	}


	@Override
	public int deleteTask(List<T> collection) {
		return documentLocalEJB.deleteDocument((List<Document>) collection);
	}
	
	@Override
	public int deleteUnassignedTask(T task) {
		return documentLocalEJB.deleteUnassignedDocument((Document) task);
	}
	
	
	@Override
	public int deleteUnassignedTaskCollection(List<T> collection) {
		return documentLocalEJB.deleteUnassignedDocumentCollection((List<Document>) collection);
	}


	@Override
	public int deleteStaleTasks(String joinType, String joinTable, String joinColumn,
								 String sortOrder, String[] orderBy,
								 final String maxTaskAge, final String scanInterval) {
		
		return documentLocalEJB.deleteStaleDocuments(joinType, joinTable, joinColumn, 
													 sortOrder, orderBy,
													 maxTaskAge, scanInterval);
	}


	@Override
	public void updateTask(T task, Criterion criterion) {
		// TODO Auto-generated method stub

	}


	@Override
	public void updateTask(List<T> collection, Criterion criterion) {
		// TODO Auto-generated method stub

	}

	/**
	 * Gets a new task from the task repository
	 * @param <entityClass>
	 */
	@Override
	public Document getNewTask(Long crisisID) {
		Document document = getNewTask(crisisID, null);
		if (document != null) {
			System.out.println("[getNewTask] New task: " + document.getDocumentID());
		} else {
			System.out.println("[getNewTask] New task: " + document);
		}
		return document;
	}

	/**
	 * Gets a new task from the task repository
	 * based on specified criterion
	 */
	@Override
	public Document getNewTask(Long crisisID, Criterion criterion) {
		String aliasTable = "taskAssignment";
		String aliasTableKey = "taskAssignment.documentID";

		Criterion newCriterion = Restrictions.conjunction()
				.add(criterion)
				.add(Restrictions.eq("crisisID",crisisID))
				.add(Restrictions.eq("hasHumanLabels",false));

		Criterion aliasCriterion =  (Restrictions.isNull(aliasTableKey));

		List<Document> fetchedList = (documentLocalEJB.getByCriteriaWithAliasByOrder(newCriterion, "ASC", null, 1, aliasTable, aliasCriterion));
		if (null == fetchedList || fetchedList.isEmpty()) {
			return null;
		} 
		return fetchedList.get(0);
	}


	@Override
	public List<Document> getNewTaskCollection(Long crisisID, Criterion criterion) {
		String aliasTable = "taskAssignment";
		String aliasTableKey = "taskAssignment.documentID";

		Criterion newCriterion = Restrictions.conjunction()
				.add(criterion)
				.add(Restrictions.eq("crisisID",crisisID))
				.add(Restrictions.eq("hasHumanLabels",false));

		Criterion aliasCriterion =  (Restrictions.isNull(aliasTableKey));
		return documentLocalEJB.getByCriteriaWithAliasByOrder(newCriterion, "ASC", null, null, aliasTable, aliasCriterion); 
	}


	@Override
	public Boolean isTaskAssigned(T task) {
		// If task is assigned then LEFT JOIN result will return null
		// for given taskID embedded in task
		Document document = (Document) task;
		String aliasTable = "taskAssignment";
		String aliasTableKey = "taskAssignment.documentID";

		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("documentID", document.getDocumentID()))
				.add(Restrictions.eq("hasHumanLabels",false));

		Criterion aliasCriterion =  (Restrictions.isNull(aliasTableKey));
		List<Document> fetchedList = documentLocalEJB.getByCriteriaWithAliasByOrder(criterion, "ASC", null, 1, aliasTable, aliasCriterion);
		if (null == fetchedList || fetchedList.isEmpty()) {
			return true; 
		}
		return false;
	}


	@Override
	public Boolean isTaskNew(T task) {
		if (!isTaskAssigned(task)) {
			return true;
		} else {
			return false;
		}
	}


	@Override
	public Boolean isTaskDone(T task) {
		Document document = documentLocalEJB.getById(((Document) task).getDocumentID());
		if ((document != null) && document.isHasHumanLabels()) {
			return true;
		}
		return false;		// no entry for documentID in task_answer table
	}


	@Override
	public Boolean isExists(T task) {
		Document document = (Document) task;
		if (documentLocalEJB.getById(document.getDocumentID()) != null) {
			return true;
		}
		return false;
	}

	@Override
	public Document getTaskByCriterion(Long crisisID, Criterion criterion) {
		Criterion newCriterion = Restrictions.conjunction()
				.add(criterion)
				.add(Restrictions.eq("crisisID", crisisID));
		return documentLocalEJB.getByCriteria(newCriterion);
	}

	@Override
	public List<Document> getTaskCollectionByCriterion(Long crisisID, Criterion criterion) {
		Criterion newCriterion = Restrictions.conjunction()
				.add(criterion)
				.add(Restrictions.eq("crisisID", crisisID));
		return documentLocalEJB.getAllByCriteria(newCriterion);
	}

	@Override
	public void taskUpdate(Criterion criterion, String joinType, String joinTable,
			String joinColumn, String sortOrder, String[] orderBy) {
		// TODO Auto-generated method stub

	}
}
