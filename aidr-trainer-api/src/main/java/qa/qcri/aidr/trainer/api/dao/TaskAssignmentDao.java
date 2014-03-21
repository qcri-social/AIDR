package qa.qcri.aidr.trainer.api.dao;

import qa.qcri.aidr.trainer.api.entity.Document;
import qa.qcri.aidr.trainer.api.entity.TaskAssignment;

import java.util.List;
import java.util.Map;


public interface TaskAssignmentDao extends AbstractDao<TaskAssignment, String>  {

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
