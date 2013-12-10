package qa.qcri.aidr.trainer.pybossa.service;

import qa.qcri.aidr.trainer.pybossa.entity.ClientAppSource;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 10/18/13
 * Time: 2:30 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ClientAppSourceService {
    List<ClientAppSource> getClientAppSourceByStatus(Long clientAppID, int status);
    void updateClientAppSourceStatus(Long clientAppSourceID, int status);
    void insertNewClientAppSource(ClientAppSource clientAppSource);
}
