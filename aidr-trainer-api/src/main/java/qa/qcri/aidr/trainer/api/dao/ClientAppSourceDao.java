package qa.qcri.aidr.trainer.api.dao;

import qa.qcri.aidr.trainer.api.entity.ClientAppSource;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 3/4/14
 * Time: 11:56 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ClientAppSourceDao extends AbstractDao<ClientAppSource, String>  {
    List<ClientAppSource> findActiveSourcePerClient(Long clientAppID);
    void acceptSource(String fileURL, Long clientAppID);
    void createNewSource(ClientAppSource clientAppSource);
    boolean findDuplicateSource(String fileURL, Long clientAppID);
}
