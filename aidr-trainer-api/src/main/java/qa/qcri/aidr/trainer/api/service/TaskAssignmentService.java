package qa.qcri.aidr.trainer.api.service;


import qa.qcri.aidr.dbmanager.dto.DocumentDTO;
import java.util.List;


public interface TaskAssignmentService {
    void revertTaskAssignment(Long documentID, Long userID);
    void revertTaskAssignmentByUserName(Long documentID, String userName);
    Integer getPendingTaskCount(Long userID);
    void addToOneTaskAssignment(Long documentID, Long userID);
    void addToTaskAssignment(List<DocumentDTO> documents, Long userID);
}
