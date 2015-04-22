package qa.qcri.aidr.trainer.pybossa.dao;

import qa.qcri.aidr.trainer.pybossa.entity.ClientAppGroup;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 4/16/15
 * Time: 5:16 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ClientAppGroupDao extends AbstractDao<ClientAppGroup, String>  {

    List<ClientAppGroup> findGroup(Long groupID);
    List<ClientAppGroup> findGroupByCrisisID(Long crisisID);

}
