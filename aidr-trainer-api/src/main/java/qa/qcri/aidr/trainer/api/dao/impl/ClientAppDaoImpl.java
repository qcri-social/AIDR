package qa.qcri.aidr.trainer.api.dao.impl;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import qa.qcri.aidr.trainer.api.dao.ClientAppDao;
import qa.qcri.aidr.trainer.api.entity.ClientApp;


import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/18/13
 * Time: 6:31 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class ClientAppDaoImpl extends AbstractDaoImpl<ClientApp, String> implements ClientAppDao {

    protected ClientAppDaoImpl(){
        super(ClientApp.class);
    }

    @Override
    public ClientApp findClientAppByID(String columnName, Long id) {
        ClientApp appCfg = findByCriterionID(Restrictions.eq(columnName, id));
        return appCfg;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ClientApp findClientAppByCriteria(String columnName, String value) {
        ClientApp appCfg = findByCriterionID(Restrictions.eq(columnName, value));
        return appCfg;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<ClientApp> findAllClientApp(Long clientID) {
        List<ClientApp> clientAppList =  findByCriteria(Restrictions.eq("clientID", clientID));

        return clientAppList;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<ClientApp> findAllClientAppByCrisisID(Long crisisID) {
        return findByCriteria(Restrictions.eq("crisisID", crisisID));  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<ClientApp> findAllClientAppByStatus(Integer status) {
        return findByCriteria(Restrictions.eq("status", status));  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<ClientApp> findClientAppByCrisisAndAttribute(Long crisisID, Long attributeID) {

        return findByCriteria(Restrictions.conjunction()
                .add(Restrictions.eq("crisisID",crisisID))
                .add(Restrictions.eq("nominalAttributeID",attributeID)));
    }


    @Override
    public List<ClientApp> findClientAppByAppType(String columnName, Integer typeID) {

        return findByCriteria(Restrictions.conjunction()
                .add(Restrictions.eq("appType",typeID))
                .add(Restrictions.not(Restrictions.eq("status",0))));
    }




}
