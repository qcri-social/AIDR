package qa.qcri.aidr.trainer.pybossa.dao;

import qa.qcri.aidr.trainer.pybossa.entity.ClientAppSource;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 10/18/13
 * Time: 2:02 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ClientAppSourceDao extends AbstractDao<ClientAppSource, String>{

    List<ClientAppSource> getClientAppSource(Long clientAppID, int status);
    void updateClientAppSourceStatus(Long clientAppID, int status);
    void insertClientAppSource(ClientAppSource clientAppSource);
}
