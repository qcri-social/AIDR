package qa.qcri.aidr.dbmanager.ejb.remote.facade;

import qa.qcri.aidr.dbmanager.dto.DocumentDTO;
import qa.qcri.aidr.dbmanager.dto.TaskAssignmentDTO;
import qa.qcri.aidr.dbmanager.entities.task.TaskAssignment;
import qa.qcri.aidr.dbmanager.exception.TaskManagerException;

import javax.ejb.Remote;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 11/27/14
 * Time: 10:23 AM
 * To change this template use File | Settings | File Templates.
 */
@Remote
public interface TaskAssignmentFacadeRemote<T, Serializable> {

    public Class<T> getClassType();

    void insertTaskAssignment(TaskAssignmentDTO taskAssignmentDTO) throws TaskManagerException;

    void undoTaskAssignment(TaskAssignmentDTO taskAssignmentDTO) throws TaskManagerException;

    TaskAssignmentDTO findTaskAssignment(Long documentID, Long userID) throws TaskManagerException;

    List<TaskAssignmentDTO> findTaskAssignmentByID(Long documentID) throws TaskManagerException;

    Integer getPendingTaskCount(Long userID) throws TaskManagerException;
}
