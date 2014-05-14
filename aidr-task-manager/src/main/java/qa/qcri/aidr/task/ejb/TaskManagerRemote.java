package qa.qcri.aidr.task.ejb;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Remote;

import org.hibernate.criterion.Criterion;

import qa.qcri.aidr.task.entities.Document;


@Remote
public interface TaskManagerRemote<T, Serializable> {
	
	public Class<T> getClassType();
	
	public void insertNewTask(T task);
	public void insertNewTask(List<T> collection);
	
	public int deleteTask(T task);
	public int deleteTask(List<T> collection);
	public int deleteUnassignedTaskCollection(List<T> collection);
	public int deleteUnassignedTask(T task);
	public int deleteStaleTasks(String joinType, String joinTable, String joinColumn,  
						  	    String sortOrder, String[] orderBy,
						  	    final String maxTaskAge, final String scanInterval);
	
	public void updateTask(T task, Criterion criterion);
	public void updateTask(List<T> collection, Criterion criterion);
	public void taskUpdate(Criterion criterion, String joinType, String joinTable, 
			  			   String joinColumn, String sortOrder, String[] orderBy);
	
	public Document getNewTask(Long crisisID);
	public Document getNewTask(Long crisisID, Criterion criterion);
	public List<Document> getNewTaskCollection(Long crisisID, Criterion criterion);
	
	public Document getTaskByCriterion(Long crisisID, Criterion criterion);
	public List<Document> getTaskCollectionByCriterion(Long crisisID, Criterion criterion);
	//public List<T> getByCriterion(Criterion criterion, String joinType, String[] joinTables, 
	//							  String joinColumn, String sortOrder, String[] orderBy);
	
	public Boolean isTaskAssigned(T task);
	public Boolean isTaskNew(T task);
	public Boolean isTaskDone(T task);
	public Boolean isExists(T task);
	
}
