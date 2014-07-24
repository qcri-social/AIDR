package qa.qcri.aidr.trainer.api.dao;

import qa.qcri.aidr.trainer.api.entity.ClientAppDeployment;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 6/11/14
 * Time: 4:39 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ClientAppDeploymentDao extends AbstractDao<ClientAppDeployment, String> {

    ClientAppDeployment findClientAppDeploymentByStatus(Long clientAppID, int status);
    List<ClientAppDeployment> findClientAppDeploymentByClientAppID(Long clientAppID);
    List<ClientAppDeployment> findClientAppDeploymentByAppType(int appType);
    void updateClientAppDeploymentStatus(Long clientAppDeploymentID, int status);
    List<ClientAppDeployment> findClientAppDeploymentByStatus(int status);
    List<ClientAppDeployment> findActiveClientAppDeployment();

}