/*
 * Interface for the task manager that covers the following tables: document, document_nominal_label, task_assignment and task_answer
 * of the aidr_predict table. 
 */
package qa.qcri.aidr.task.ejb;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;

import qa.qcri.aidr.dbmanager.dto.DocumentDTO;
import qa.qcri.aidr.dbmanager.dto.DocumentNominalLabelDTO;
import qa.qcri.aidr.dbmanager.dto.HumanLabeledDocumentDTO;
import qa.qcri.aidr.dbmanager.dto.TaskAnswerDTO;
import qa.qcri.aidr.dbmanager.dto.TaskAssignmentDTO;
import qa.qcri.aidr.dbmanager.dto.UsersDTO;
import qa.qcri.aidr.task.common.TrainingDataFetchType;

import com.fasterxml.jackson.core.type.TypeReference;

@Remote
public interface TaskManagerRemote<T, Serializable> {
	
	static Logger logger = Logger.getLogger(TaskManagerRemote.class);
	
	public Class<T> getClassType();
	
	//public String getAllTasks();
	public List<DocumentDTO> getAllTasks();
	
	public long insertNewTask(T task);
	public void insertNewTask(List<T> collection);
	
	public Long saveNewTask(T task, Long crisisID);
	public List<Long> saveNewTasks(List<T> collection, Long crisisID);
	
	public void assignNewTaskToUser(Long id, Long userId) throws Exception;
	public void assignNewTaskToUser(List<DocumentDTO> collection, Long userId) throws Exception;
	
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
	
	public void insertTaskAnswer(TaskAnswerDTO taskAnswer);
	
	public void saveDocumentNominalLabel(DocumentNominalLabelDTO documentNominalLabel);
	public boolean foundDuplicateDocumentNominalLabel(DocumentNominalLabelDTO documentNominalLabel);
	public List<DocumentDTO> getNominalLabelDocumentCollection(Long nominalLabelID);
	
	public DocumentDTO getTaskByCriterion(Long crisisID, Criterion criterion);
	public List<DocumentDTO> getTaskCollectionByCriterion(Long crisisID, Integer count, Criterion criterion);
	
	//public qa.qcri.aidr.task.entities.Document getNewDocumentByCriterion(Long id, Criterion criterion);
	//public qa.qcri.aidr.task.entities.Document getNewDocumentByCrisisId(Long crisisID);
	
	public <E> Object  setTaskParameter(Class<E> entityType, Long id, Map<String, String> paramMap);
	
	public <E> Boolean isTaskAssigned(E task);
	public <E> Boolean isTaskNew(E task);
	public <E> Boolean isTaskDone(E task);
	public <E> Boolean isExists(E task);

	public <E> E deSerialize(String jsonString, Class<E> entityType);
	public <E> E deSerializeList(String jsonString, TypeReference<E> type);

	public <E> String serializeTask(E task);
	
	public List<HumanLabeledDocumentDTO> getHumanLabeledDocumentsByCrisisID(Long crisisID, Integer count) throws Exception;
	public List<HumanLabeledDocumentDTO> getHumanLabeledDocumentsByCrisisCode(String crisisCode, Integer count) throws Exception;
	public List<HumanLabeledDocumentDTO> getHumanLabeledDocumentsByCrisisIDUserID(Long crisisID, Long userID, Integer count) throws Exception;
	public List<HumanLabeledDocumentDTO> getHumanLabeledDocumentsByCrisisIDUserName(Long crisisID, String userName, Integer count) throws Exception;

	public List<DocumentDTO> getDocumentsForTagging(final Long crisisID, final int count, final String userName, final int remainingCount,
													final TrainingDataFetchType fetchType);
	
	// for testing purpose
	public String pingRemoteEJB();

	public boolean deleteTask(Long crisisID, Long userID);
}
