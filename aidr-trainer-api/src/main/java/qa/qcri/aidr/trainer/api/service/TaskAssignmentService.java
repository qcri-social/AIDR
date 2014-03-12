package qa.qcri.aidr.trainer.api.service;


import qa.qcri.aidr.trainer.api.entity.Document;
import qa.qcri.aidr.trainer.api.entity.TaskBuffer;
import java.util.List;
import java.util.Map;


public interface TaskAssignmentService {
    //List<TaskBuffer> findAssignableTaskBuffer(String columnName, Integer value);
    void revertTaskAssignment(List<Document> taskBuffer, Long userID);
    void revertTaskAssignmentByIDList(Map<Long, Long> taskMap);
    void revertTaskAssignment(Long documentID, Long userID);
    void revertTaskAssignment(Long documentID, String userName);
    Integer getPendingTaskCount(Long userID);
    void processTaskAssignmentCleanUp();

}
