package qa.qcri.aidr.trainer.api.dao;

import qa.qcri.aidr.trainer.api.entity.Collection;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/11/13
 * Time: 7:57 AM
 * To change this template use File | Settings | File Templates.
 */
    public interface CrisisDao extends AbstractDao<Collection, Long> {

        Collection findByCrisisID(Long id);
        List<Collection> findByCriteria(String columnName, Long value);
        List<Collection> findByCriteria(String columnName, String value);
        List<Collection> findAllActiveCrisis();
        List<Collection> findActiveCrisis();
}
