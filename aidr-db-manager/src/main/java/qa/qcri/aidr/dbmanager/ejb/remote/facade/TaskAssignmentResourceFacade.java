package qa.qcri.aidr.dbmanager.ejb.remote.facade;



import java.util.List;
import java.util.Map;

import javax.ejb.Remote;
import qa.qcri.aidr.dbmanager.dto.DocumentDTO;
import qa.qcri.aidr.dbmanager.dto.TaskAssignmentDTO;
import qa.qcri.aidr.dbmanager.ejb.local.facade.CoreDBServiceFacade;
import qa.qcri.aidr.dbmanager.entities.task.TaskAssignment;

@Remote
public interface TaskAssignmentResourceFacade extends CoreDBServiceFacade<TaskAssignment, Long>  {

    public int insertTaskAssignment(List<DocumentDTO> taskList, Long userID);
    public int insertOneTaskAssignment(Long documentID, Long userID);
    public int undoTaskAssignment(List<DocumentDTO> taskList, Long userID);
    public int undoTaskAssignment(Map<Long, Long> taskMap);
    public int undoTaskAssignment(Long documentID, Long userID);
    public void undoTaskAssignmentByTimer();
    public TaskAssignmentDTO findTaskAssignment(Long documentID, Long userID);
    public List<TaskAssignmentDTO> findTaskAssignmentByID(Long documentID);
    public Integer getPendingTaskCount(Long userID);
}
