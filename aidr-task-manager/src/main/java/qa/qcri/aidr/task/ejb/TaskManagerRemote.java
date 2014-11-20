package qa.qcri.aidr.task.ejb;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import org.hibernate.criterion.Criterion;

import com.fasterxml.jackson.core.type.TypeReference;

@Remote
public interface TaskManagerRemote<T, Serializable> {
	
	public Class<T> getClassType();
	
	//public String getAllTasks();
	public List<qa.qcri.aidr.task.dto.DocumentDTO> getAllTasks();
	
	public long insertNewTask(T task);
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
	public int truncateLabelingTaskBufferForCrisis(final long crisisID, final int maxLength, final int ERROR_MARGIN);
	
	public void updateTask(T task);
	public void updateTaskList(List<T> collection);
	public void updateTask(qa.qcri.aidr.task.dto.DocumentDTO dto);
	
	public void taskUpdate(Criterion criterion, String joinType, String joinTable, 
			  			   String joinColumn, String sortOrder, String[] orderBy);
	
	public qa.qcri.aidr.task.dto.DocumentDTO getNewTask(Long crisisID);
	public qa.qcri.aidr.task.dto.DocumentDTO getNewTask(Long crisisID, Criterion criterion);
	public List<qa.qcri.aidr.task.dto.DocumentDTO> getNewTaskCollection(Long crisisID, Integer count, String order, Criterion criterion);
	public Integer getPendingTaskCountByUser(Long userId);
	
	public qa.qcri.aidr.task.dto.DocumentDTO getTaskById(Long id);
	
	public List<qa.qcri.aidr.task.dto.TaskAssignmentDTO> getAssignedTasksById(Long id);
	public qa.qcri.aidr.task.dto.TaskAssignmentDTO getAssignedTaskByUserId(Long id, Long userId);
	
	public qa.qcri.aidr.task.dto.DocumentDTO getDocumentById(Long id);
	
	public qa.qcri.aidr.task.dto.UsersDTO getUserByName(String name);
	public qa.qcri.aidr.task.dto.UsersDTO getUserById(Long id);
	public List<qa.qcri.aidr.task.dto.UsersDTO> getAllUserByName(String name);
	
	public void insertTaskAnswer(qa.qcri.aidr.task.entities.TaskAnswer taskAnswer);
	
	public void saveDocumentNominalLabel(qa.qcri.aidr.task.entities.DocumentNominalLabel documentNominalLabel);
	public boolean foundDuplicateDocumentNominalLabel(qa.qcri.aidr.task.entities.DocumentNominalLabel documentNominalLabel);
	
	public qa.qcri.aidr.task.dto.DocumentDTO getTaskByCriterion(Long crisisID, Criterion criterion);
	public List<qa.qcri.aidr.task.dto.DocumentDTO> getTaskCollectionByCriterion(Long crisisID, Integer count, Criterion criterion);
	
	//public qa.qcri.aidr.task.entities.Document getNewDocumentByCriterion(Long id, Criterion criterion);
	//public qa.qcri.aidr.task.entities.Document getNewDocumentByCrisisId(Long crisisID);
	
	public Object setTaskParameter(Class<T> entityType, Long id, Map<String, String> paramMap);
	
	public <E> Boolean isTaskAssigned(E task);
	public <E> Boolean isTaskNew(E task);
	public <E> Boolean isTaskDone(E task);
	public <E> Boolean isExists(E task);

	public <E> E deSerialize(String jsonString, Class<E> entityType);
	public <E> E deSerializeList(String jsonString, TypeReference<E> type);

	public <E> String serializeTask(E task);
	
	// for testing purpose
	public String pingRemoteEJB();

}
