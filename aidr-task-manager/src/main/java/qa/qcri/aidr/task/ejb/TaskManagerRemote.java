package qa.qcri.aidr.task.ejb;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;

import org.hibernate.criterion.Criterion;

import qa.qcri.aidr.task.entities.Document;


@Remote
public interface TaskManagerRemote<T, Serializable> {
	
	public Class<T> getClassType();
	
	public void insertNewTask(T task);
	public void insertNewTask(List<T> collection);
	
	public int deleteTask(T task, Criterion criterion);
	public int deleteTask(List<T> collection);
	
	public int updateTask(T task, Criterion criterion);
	public int updateTask(List<T> collection, Criterion criterion);
	
	public Document getNewTask(Long crisisID);
	public Document getNewTask(Long crisisID, Criterion criterion);
	public List<Document> getNewTaskCollection(Long crisisID, Criterion criterion);
	
	public Boolean isTaskAssigned(T task);
	public Boolean isTaskNew(T task);
	public Boolean isTaskDone(T task);
	public Boolean isExists(T task);
	
}
