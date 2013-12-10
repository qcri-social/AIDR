package qa.qcri.aidr.trainer.pybossa.service;

import qa.qcri.aidr.trainer.pybossa.entity.ClientApp;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/19/13
 * Time: 11:59 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ClientAppService {

    void createClientApp(ClientApp clientApp);
    ClientApp findClientAppByID(String columnName, Long id);
    ClientApp findClientAppByCriteria(String columnName, String value);
    List<ClientApp> getAllCrisis();
    List<ClientApp> getAllClientAppByClientID(Long clientID);
    List<ClientApp> findClientAppByStatus(Integer status);
    List<ClientApp> getAllClientAppByCrisisID(Long crisisID);
    List<ClientApp> getAllClientAppByClientIDAndStatus(Long clientID, Integer status);

}
