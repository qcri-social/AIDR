package qa.qcri.aidr.dbmanager.ejb.remote.facade;

import qa.qcri.aidr.dbmanager.dto.TaskAnswerDTO;
import qa.qcri.aidr.dbmanager.exception.TaskManagerException;

import javax.ejb.Remote;
import javax.ejb.Stateless;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 11/27/14
 * Time: 9:44 AM
 * To change this template use File | Settings | File Templates.
 */
@Remote
public interface TaskAnswerFacadeRemote<T, Serializable>  {

    public Class<T> getClassType();
    void insertTaskAnswer(TaskAnswerDTO taskAnswerDTO) throws TaskManagerException;
}
