package qa.qcri.aidr.trainer.api.service;


import qa.qcri.aidr.trainer.api.entity.Document;
import java.util.List;
import java.util.Map;


public interface TaskAssignmentService {
    void revertTaskAssignment(Long documentID, Long userID);
    void revertTaskAssignment(Long documentID, String userName);
    Integer getPendingTaskCount(Long userID);
    void addToOneTaskAssignment(long documentID, long userID);
    void addToTaskAssignment(List<Document> documents, long userID);
}
