package qa.qcri.aidr.trainer.api.dao;

import qa.qcri.aidr.trainer.api.entity.Crisis;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/11/13
 * Time: 7:57 AM
 * To change this template use File | Settings | File Templates.
 */
    public interface CrisisDao extends AbstractDao<Crisis, String> {

        Crisis findByCrisisID(Long id);
        List<Crisis> findByCriteria(String columnName, Long value);
        List<Crisis> findByCriteria(String columnName, String value);
        List<Crisis> findAllActiveCrisis();
}
