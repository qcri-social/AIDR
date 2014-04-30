package qa.qcri.aidr.task.ejb.bean;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import qa.qcri.aidr.task.ejb.DocumentService;
import qa.qcri.aidr.task.ejb.TaskManagerRemote;
import qa.qcri.aidr.task.entities.Document;

@Stateless
public class TaskManagerBean<T, Serializable> implements TaskManagerRemote<T, Serializable>{

	@EJB
	private DocumentService documentLocalEJB;

	private Class<T> entityType;

	public TaskManagerBean()  {
		this.entityType = getClassType();
	}  
	
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
		
	}


	@Override
	public void insertNewTask(List<T> collection) {
		// TODO Auto-generated method stub

	}


	@Override
	public int deleteTask(T task, Criterion criterion) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int deleteTask(List<T> collection) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int updateTask(T task, Criterion criterion) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int updateTask(List<T> collection, Criterion criterion) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Gets a new task from the task repository
	 * @param <entityClass>
	 */
	@Override
	public Document getNewTask(Long crisisID) {
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisisID", crisisID))
				.add(Restrictions.eq("hasHumanLabels", false));
		Document document = documentLocalEJB.getByCriteria(criterion);
		System.out.println("[getNewTask] New task: " + document.getDocumentID());
		return document;
	}

	/**
	 * Gets a new task from the task repository
	 * based on specified criterion
	 */
	@Override
	public Document getNewTask(Long crisisID, Criterion criterion) {
		if (criterion != null) {
			Document document = documentLocalEJB.getByCriteria(criterion);
			System.out.println("[getNewTask] New task with criterion: " + document.getDocumentID());
			return document;
		} else {
			return getNewTask(crisisID);
		}
	}


	@Override
	public List<Document> getNewTaskCollection(Long crisisID,
			Criterion criterion) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Boolean isTaskAssigned(T task) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public Boolean isTaskNew(T task) {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public Boolean isTaskDone(T task) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public Boolean isExists(T task) {
		// TODO Auto-generated method stub
		return false;
	}

}
