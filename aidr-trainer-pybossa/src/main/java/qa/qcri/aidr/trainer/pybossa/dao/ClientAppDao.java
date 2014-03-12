package qa.qcri.aidr.trainer.pybossa.dao;

import qa.qcri.aidr.trainer.pybossa.entity.ClientApp;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/18/13
 * Time: 6:00 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ClientAppDao extends AbstractDao<ClientApp, String>  {

    void createClientApp(ClientApp clientApp);
    void updateClientApp(ClientApp clientApp);
    ClientApp findClientAppByID(String columnName, Long id);
    ClientApp findClientAppByCriteria(String columnName, String value);
    List<ClientApp> findAllClientApp(Long clientID);
    List<ClientApp> findAllClientAppByCrisisID(Long crisisID);
    List<ClientApp> findAllClientAppByStatus(Integer status);
    List<ClientApp> getAllCrisisID();
    List<ClientApp> getAllClientAppByClientIDAndStatus(Long clientID, Integer status);
    List<ClientApp> getAllClientAppByCrisisIDAndStatus(Long clientID, Integer status);
}
