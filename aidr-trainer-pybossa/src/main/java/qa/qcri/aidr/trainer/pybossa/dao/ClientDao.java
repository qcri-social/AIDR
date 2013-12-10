package qa.qcri.aidr.trainer.pybossa.dao;


import qa.qcri.aidr.trainer.pybossa.entity.Client;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/18/13
 * Time: 5:49 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ClientDao extends AbstractDao<Client, String> {

    void createClient(Client client);
    Client findClientByID(String columnName,Long id);
    Client findClientByCriteria(String columnName, String value);

}
