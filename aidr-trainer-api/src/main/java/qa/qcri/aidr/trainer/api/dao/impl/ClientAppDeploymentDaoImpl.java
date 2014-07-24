package qa.qcri.aidr.trainer.api.dao.impl;


import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import qa.qcri.aidr.trainer.api.dao.ClientAppDeploymentDao;
import qa.qcri.aidr.trainer.api.entity.ClientAppDeployment;
import qa.qcri.aidr.trainer.api.store.StatusCodeType;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 6/11/14
 * Time: 1:49 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class ClientAppDeploymentDaoImpl extends AbstractDaoImpl<ClientAppDeployment, String> implements ClientAppDeploymentDao {

    protected ClientAppDeploymentDaoImpl(){
        super( ClientAppDeployment.class );
    }

    @Override
    public ClientAppDeployment findClientAppDeploymentByStatus(Long clientAppID, int status) {

        List<ClientAppDeployment> clientAppDeployments =
                findByCriteria(Restrictions.conjunction()
                        .add(Restrictions.eq("clientAppID",clientAppID))
                        .add(Restrictions.eq("status", status)));

        if(clientAppDeployments.size() > 0){
            return clientAppDeployments.get(0);
        }

        return null ;
    }

    @Override
    public List<ClientAppDeployment> findClientAppDeploymentByClientAppID(Long clientAppID) {
        return findByCriteria(Restrictions.eq("clientAppID", clientAppID));
    }

    @Override
    public List<ClientAppDeployment> findClientAppDeploymentByAppType(int appType) {

        return findByCriteria(Restrictions.conjunction()
                        .add(Restrictions.eq("appType",appType))
                        .add(Restrictions.eq("status", StatusCodeType.DEPLOYMENT_ACTIVE)));
    }

    @Override
    public void updateClientAppDeploymentStatus(Long deploymentID, int status) {
        List<ClientAppDeployment> c = findByCriteria(Restrictions.eq("deploymentID", deploymentID));
        if(c.size() > 0){
            ClientAppDeployment ca = c.get(0);
            ca.setStatus(status);
            saveOrUpdate(ca);
        }
    }

    @Override
    public List<ClientAppDeployment> findClientAppDeploymentByStatus(int status) {
        return findByCriteria(Restrictions.eq("status", status));
    }

    @Override
    public List<ClientAppDeployment> findActiveClientAppDeployment() {

        return findByCriteria(Restrictions.disjunction()
                .add(Restrictions.eq("status",StatusCodeType.DEPLOYMENT_MOBILE))
                .add(Restrictions.eq("status", StatusCodeType.DEPLOYMENT_ACTIVE)));
    }
}
