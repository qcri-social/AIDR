package qa.qcri.aidr.task.ejb;



import java.util.List;
import java.util.Map;

import qa.qcri.aidr.task.entities.Document;
import qa.qcri.aidr.task.entities.TaskAssignment;


public interface TaskAssignmentService extends AbstractTaskManagerService<TaskAssignment, String>  {

    void insertTaskAssignment(List<Document> taskBuffer, Long userID);
    void insertOneTaskAssignment(Long documentID, Long userID);
    void undoTaskAssignment(List<Document> taskBuffer, Long userID);
    void undoTaskAssignment(Map<Long, Long> taskMap);
    void undoTaskAssignment(Long documentID, Long userID);
    void undoTaskAssignmentByTimer();
    TaskAssignment findTaskAssignment(Long documentID, Long userID);
    List<TaskAssignment> findTaskAssignmentByID(Long documentID);
    Integer getPendingTaskCount(Long userID);
}
