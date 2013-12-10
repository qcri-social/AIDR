package qa.qcri.aidr.trainer.pybossa.dao;

import qa.qcri.aidr.trainer.pybossa.entity.ClientAppAnswer;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 10/20/13
 * Time: 1:36 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ClientAppAnswerDao extends AbstractDao<ClientAppAnswer, String>  {

    ClientAppAnswer findClientAppAnswerByID(Long clientAppID);
}
