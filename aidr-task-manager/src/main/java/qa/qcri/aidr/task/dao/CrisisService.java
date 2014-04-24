package qa.qcri.aidr.task.dao;


import java.util.List;

import qa.qcri.aidr.task.entities.Crisis;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/11/13
 * Time: 7:57 AM
 * To change this template use File | Settings | File Templates.
 */
    public interface CrisisService extends AbstractTaskManagerService<Crisis, String> {

        Crisis findByCrisisID(Long id);
        List<Crisis> findByCriteria(String columnName, Long value);
        List<Crisis> findByCriteria(String columnName, String value);
        //List<Crisis> findAllActiveCrisis();
}
