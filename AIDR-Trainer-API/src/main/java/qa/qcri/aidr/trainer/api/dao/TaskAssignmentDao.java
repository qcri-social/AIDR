package qa.qcri.aidr.trainer.api.dao;

import qa.qcri.aidr.trainer.api.entity.TaskAssignment;
import qa.qcri.aidr.trainer.api.entity.TaskBuffer;

import java.util.List;
import java.util.Map;


public interface TaskAssignmentDao extends AbstractDao<TaskAssignment, String>  {

    void insertTaskAssignment(List<TaskBuffer> taskBuffer, Long userID);
    void undoTaskAssignment(List<TaskBuffer> taskBuffer, Long userID);
    void undoTaskAssignment(Map<Long, Long> taskMap);
    void undoTaskAssignment(Long documentID, Long userID);
    TaskAssignment findTaskAssignment(Long documentID, Long userID);
    Integer getPendingTaskCount(Long userID);
}
