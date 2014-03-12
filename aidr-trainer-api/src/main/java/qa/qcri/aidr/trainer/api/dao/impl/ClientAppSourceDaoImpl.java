package qa.qcri.aidr.trainer.api.dao.impl;

import org.springframework.stereotype.Repository;
import qa.qcri.aidr.trainer.api.dao.ClientAppSourceDao;
import qa.qcri.aidr.trainer.api.entity.ClientAppSource;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 3/4/14
 * Time: 12:17 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class ClientAppSourceDaoImpl extends AbstractDaoImpl<ClientAppSource, String> implements ClientAppSourceDao {

    protected ClientAppSourceDaoImpl(){
        super(ClientAppSource.class);
    }

    @Override
    public List<ClientAppSource> findActiveSourcePerClient(Long clientAppID) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void acceptSource(String fileURL, String clientAppName) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void acceptSource(String fileURL, Long clientAppID) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
