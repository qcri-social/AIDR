package qa.qcri.aidr.trainer.pybossa.dao.impl;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import qa.qcri.aidr.trainer.pybossa.dao.ClientAppDao;
import qa.qcri.aidr.trainer.pybossa.entity.ClientApp;

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
    public void createClientApp(ClientApp clientApp) {
        try{
            saveOrUpdate(clientApp);
        }
        catch(Exception e){
            System.out.println(e);
        }

    }

    @Override
    public void updateClientApp(ClientApp clientApp) {
        saveOrUpdate(clientApp);
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
    public List<ClientApp> getAllCrisisID() {
        return findAll();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<ClientApp> getAllClientAppByClientIDAndStatus(Long clientID, Integer status) {
        return findByCriteria(Restrictions.conjunction()
                .add(Restrictions.eq("clientID",clientID))
                .add(Restrictions.eq("status", status)));
                 //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<ClientApp> getAllClientAppByCrisisIDAndStatus(Long crisisID, Integer status) {

        return findByCriteria(Restrictions.conjunction()
                .add(Restrictions.eq("crisisID",crisisID))
                .add(Restrictions.eq("status", status)));
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
