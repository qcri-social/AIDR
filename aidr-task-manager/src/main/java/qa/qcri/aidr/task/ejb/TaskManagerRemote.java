package qa.qcri.aidr.task.ejb;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Remote;

import org.codehaus.jackson.type.TypeReference;
import org.hibernate.criterion.Criterion;

import qa.qcri.aidr.task.entities.Document;
import qa.qcri.aidr.task.entities.DocumentNominalLabel;
import qa.qcri.aidr.task.entities.TaskAnswer;



@Remote
public interface TaskManagerRemote<T, Serializable> {
	
	public Class<T> getClassType();
	
	public String getAllTasks();
	
	public void insertNewTask(T task);
	public void insertNewTask(List<T> collection);
	
	public void assignNewTaskToUser(Long id, Long userId) throws Exception;
	public void assignNewTaskToUser(List<T> collection, Long userId) throws Exception;
	
	public void undoTaskAssignment(Map<Long, Long> taskMap) throws Exception;
	public void undoTaskAssignment(Long documentID, Long userID) throws Exception;
	
	public int deleteTaskById(Long id);
	public int deleteTask(T task);
	public int deleteTask(List<T> collection);
	public int deleteUnassignedTaskCollection(List<T> collection);
	public int deleteUnassignedTask(T task);
	public int deleteStaleTasks(String joinType, String joinTable, String joinColumn,  
						  	    String sortOrder, String[] orderBy,
						  	    final String maxTaskAge, final String scanInterval);
	
	public void updateTask(T task);
	public void updateTask(List<T> collection);
	public void taskUpdate(Criterion criterion, String joinType, String joinTable, 
			  			   String joinColumn, String sortOrder, String[] orderBy);
	
	public String getNewTask(Long crisisID);
	public String getNewTask(Long crisisID, Criterion criterion);
	public String getNewTaskCollection(Long crisisID, Integer count, String order, Criterion criterion);
	public Integer getPendingTaskCountByUser(Long userId);
	
	public String getTaskById(Long id);
	public String getAssignedTasksById(Long id);
	public String getAssignedTaskByUserId(Long id, Long userId);
	
	public qa.qcri.aidr.task.entities.Document getDocumentById(Long id);
	
	public String getUserByName(String name);
	public String getUserById(Long id);
	public String getAllUserByName(String name);
	
	public void insertTaskAnswer(TaskAnswer taskAnswer);
	
	public void saveDocumentNominalLabel(DocumentNominalLabel documentNominalLabel);
	public boolean foundDuplicateDocumentNominalLabel(DocumentNominalLabel documentNominalLabel);
	
	public String getTaskByCriterion(Long crisisID, Criterion criterion);
	public String getTaskCollectionByCriterion(Long crisisID, Integer count, Criterion criterion);
	
	public qa.qcri.aidr.task.entities.Document getNewDocumentByCriterion(Long id, Criterion criterion);
	public qa.qcri.aidr.task.entities.Document getNewDocumentByCrisisId(Long crisisID);
	
	public <T> T setTaskParameter(Class<T> entityType, Long id, Map<String, String> paramMap);
	
	public <E> Boolean isTaskAssigned(E task);
	public <E> Boolean isTaskNew(E task);
	public <E> Boolean isTaskDone(E task);
	public <E> Boolean isExists(E task);

	public <E> E deSerialize(String jsonString, Class<E> entityType);
	public <E> E deSerializeList(String jsonString, TypeReference<E> type);

	public <E> String serializeTask(E task);
	
	// for testing purpose
	public String pingRemoteEJB();
	public String getNewDefaultTask();
}
