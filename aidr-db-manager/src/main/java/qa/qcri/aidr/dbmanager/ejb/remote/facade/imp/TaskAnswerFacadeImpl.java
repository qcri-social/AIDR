package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

import qa.qcri.aidr.dbmanager.dto.TaskAnswerDTO;
import qa.qcri.aidr.dbmanager.ejb.local.facade.TaskAnswerService;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.TaskAnswerFacadeRemote;
import qa.qcri.aidr.dbmanager.exception.TaskManagerException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 11/27/14
 * Time: 9:53 AM
 * To change this template use File | Settings | File Templates.
 */
@Stateless
public class TaskAnswerFacadeImpl<T, I> implements TaskAnswerFacadeRemote<T, Serializable> {

    private Class<T> entityType;
    @EJB
    private TaskAnswerService taskAnswerEJB;

    public TaskAnswerFacadeImpl()  {
        this.entityType = getClassType();
    }

    @Override
    public Class getClassType() {
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
    public void insertTaskAnswer(TaskAnswerDTO taskAnswerDTO) throws TaskManagerException {
        taskAnswerEJB.insertTaskAnswer(taskAnswerDTO.toEntity(taskAnswerDTO));
    }
}
