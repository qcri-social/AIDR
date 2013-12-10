package qa.qcri.aidr.trainer.pybossa.service;

import qa.qcri.aidr.trainer.pybossa.entity.Client;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/19/13
 * Time: 11:39 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ClientService {

    Client findClientbyID(String columnName, Long id);
    Client findClientByCriteria(String columnName, String value);

}
