package qa.qcri.aidr.dbmanager.ejb.remote.facade;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import org.hibernate.criterion.Criterion;

import qa.qcri.aidr.dbmanager.dto.DocumentDTO;
import qa.qcri.aidr.dbmanager.dto.TaskAssignmentDTO;
import qa.qcri.aidr.dbmanager.dto.UsersDTO;

import com.fasterxml.jackson.core.type.TypeReference;

@Remote
public interface TaskManagerRemote<T, Serializable> {
	
	public Class<T> getClassType();
	
	//public String getAllTasks();
	public List<DocumentDTO> getAllTasks();
	
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
	public void updateTask(DocumentDTO dto);
	
	public void taskUpdate(Criterion criterion, String joinType, String joinTable, 
			  			   String joinColumn, String sortOrder, String[] orderBy);
	
	public DocumentDTO getNewTask(Long crisisID);
	public DocumentDTO getNewTask(Long crisisID, Criterion criterion);
	public List<DocumentDTO> getNewTaskCollection(Long crisisID, Integer count, String order, Criterion criterion);
	public Integer getPendingTaskCountByUser(Long userId);
	
	public DocumentDTO getTaskById(Long id);
	
	public List<TaskAssignmentDTO> getAssignedTasksById(Long id);
	public TaskAssignmentDTO getAssignedTaskByUserId(Long id, Long userId);
	
	public DocumentDTO getDocumentById(Long id);
	
	public UsersDTO getUserByName(String name);
	public UsersDTO getUserById(Long id);
	public List<UsersDTO> getAllUserByName(String name);
	
	public void insertTaskAnswer(TaskAnswer taskAnswer);
	
	public void saveDocumentNominalLabel(DocumentNominalLabel documentNominalLabel);
	public boolean foundDuplicateDocumentNominalLabel(DocumentNominalLabel documentNominalLabel);
	public List<DocumentDTO> getNominalLabelDocumentCollection(Integer nominalLabelID);
	
	public DocumentDTO getTaskByCriterion(Long crisisID, Criterion criterion);
	public List<DocumentDTO> getTaskCollectionByCriterion(Long crisisID, Integer count, Criterion criterion);
	
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
