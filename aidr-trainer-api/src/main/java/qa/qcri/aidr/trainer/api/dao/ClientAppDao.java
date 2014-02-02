package qa.qcri.aidr.trainer.api.dao;



import qa.qcri.aidr.trainer.api.entity.ClientApp;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/18/13
 * Time: 6:00 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ClientAppDao extends AbstractDao<ClientApp, String>  {

    ClientApp findClientAppByID(String columnName, Long id);
    ClientApp findClientAppByCriteria(String columnName, String value);
    List<ClientApp> findAllClientApp(Long clientID);
    List<ClientApp> findAllClientAppByCrisisID(Long crisisID);
    List<ClientApp> findAllClientAppByStatus(Integer status);
    List<ClientApp> findClientAppByAppType(String columnName, Integer typeID);


}
