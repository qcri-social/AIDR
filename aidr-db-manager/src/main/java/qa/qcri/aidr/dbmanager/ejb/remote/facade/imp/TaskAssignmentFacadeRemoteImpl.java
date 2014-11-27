package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

import qa.qcri.aidr.dbmanager.dto.TaskAssignmentDTO;
import qa.qcri.aidr.dbmanager.ejb.local.facade.TaskAssignmentService;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.TaskAssignmentFacadeRemote;
import qa.qcri.aidr.dbmanager.entities.task.TaskAssignment;
import qa.qcri.aidr.dbmanager.exception.TaskManagerException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 11/27/14
 * Time: 11:35 AM
 * To change this template use File | Settings | File Templates.
 */
@Stateless
public class TaskAssignmentFacadeRemoteImpl<T, I> implements TaskAssignmentFacadeRemote<T, Serializable> {

    private Class<T> entityType;

    @EJB
    private TaskAssignmentService taskAssignmentService;

    @Override
    public Class<T> getClassType() {
        Class<? extends Object> thisClass = getClass();
        Type genericSuperclass = thisClass.getGenericSuperclass();

        if( genericSuperclass instanceof ParameterizedType) {
            Type[] argumentTypes = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
            Class<T> entityBeanType = (Class<T>)argumentTypes[0];
            return entityBeanType;
        } else {
            return null;
        }
    }

    @Override
    public void insertTaskAssignment(TaskAssignmentDTO taskAssignmentDTO) throws TaskManagerException {
        taskAssignmentService.insertOneTaskAssignment(taskAssignmentDTO.getDocumentID(), taskAssignmentDTO.getUserID());
    }

    @Override
    public void undoTaskAssignment(TaskAssignmentDTO taskAssignmentDTO) throws TaskManagerException {
        taskAssignmentService.undoTaskAssignment(taskAssignmentDTO.getDocumentID(), taskAssignmentDTO.getUserID()) ;
    }

    @Override
    public TaskAssignmentDTO findTaskAssignment(Long documentID, Long userID) throws TaskManagerException {
        return new TaskAssignmentDTO(taskAssignmentService.findTaskAssignment(documentID,userID));
    }

    @Override
    public List<TaskAssignmentDTO> findTaskAssignmentByID(Long documentID) throws TaskManagerException {
        List<TaskAssignmentDTO> assignmentDTOs = new ArrayList<TaskAssignmentDTO>();

        List<TaskAssignment> taskAssignments =   taskAssignmentService.findTaskAssignmentByID(documentID) ;

        for(TaskAssignment assignment : taskAssignments){
            assignmentDTOs.add(new TaskAssignmentDTO(assignment))  ;
        }

        return assignmentDTOs;
    }

    @Override
    public Integer getPendingTaskCount(Long userID) throws TaskManagerException {
        return taskAssignmentService.getPendingTaskCount(userID);  //To change body of implemented methods use File | Settings | File Templates.
    }
}
