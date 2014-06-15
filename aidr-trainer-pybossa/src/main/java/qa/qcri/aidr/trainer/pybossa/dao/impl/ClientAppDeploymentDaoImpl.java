package qa.qcri.aidr.trainer.pybossa.dao.impl;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import qa.qcri.aidr.trainer.pybossa.dao.ClientAppDeploymentDao;
import qa.qcri.aidr.trainer.pybossa.entity.ClientApp;
import qa.qcri.aidr.trainer.pybossa.entity.ClientAppDeployment;

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
        return null ;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<ClientAppDeployment> findClientAppDeploymentByClientAppID(Long clientAppID) {
        return findByCriteria(Restrictions.eq("clientAppID", clientAppID));
         //To change body of implemented methods use File | Settings | File Templates.
    }
}
