package qa.qcri.aidr.trainer.pybossa.dao;

import qa.qcri.aidr.trainer.pybossa.entity.ClientAppDeployment;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 6/11/14
 * Time: 1:37 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ClientAppDeploymentDao extends AbstractDao<ClientAppDeployment, String> {

    ClientAppDeployment findClientAppDeploymentByStatus(Long clientAppID, int status);
    List<ClientAppDeployment> findClientAppDeploymentByClientAppID(Long clientAppID);
}
